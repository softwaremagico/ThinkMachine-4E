package com.softwaremagico.tm.qr;

/*-
 * #%L
 * Think Machine 4E (QR)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Reads a PNG file containing a QR code and decodes the embedded {@link CharacterPlayer}.
 *
 * <p><strong>Android-compatible.</strong> Uses only {@code com.google.zxing:core} and
 * pure-Java APIs ({@code java.util.zip.Inflater}, {@code java.nio.charset}).
 * No {@code java.awt}, {@code javax.imageio}, or {@code com.google.zxing:javase} dependency.
 *
 * <p>Supports 8-bit grayscale and 8-bit RGB PNG files as produced by
 * {@link CharacterQrPngWriter} or any standard QR-code PNG tool.
 *
 * <p>Usage:
 * <pre>{@code
 * CharacterPlayer player = CharacterQrPngReader.readPng(Path.of("/tmp/character.png"));
 * }</pre>
 */
public final class CharacterQrPngReader {

    // --- PNG structure ---
    private static final int PNG_SIGNATURE_LENGTH = 8;
    private static final int CHUNK_LENGTH_BYTES = 4;
    private static final int CHUNK_TYPE_BYTES = 4;
    private static final int CHUNK_CRC_BYTES = 4;
    private static final int IHDR_DATA_LENGTH = 13;
    private static final int IHDR_HEIGHT_OFFSET = 4;
    private static final int IHDR_COLOR_TYPE_OFFSET = 9;

    // --- PNG color types (8-bit depth) ---
    private static final int COLOR_GRAYSCALE = 0;
    private static final int COLOR_RGB = 2;
    private static final int COLOR_GRAYSCALE_ALPHA = 4;
    private static final int COLOR_RGBA = 6;

    // --- PNG filter types per scanline ---
    private static final int FILTER_NONE = 0;
    private static final int FILTER_SUB = 1;
    private static final int FILTER_UP = 2;
    private static final int FILTER_AVERAGE = 3;
    private static final int FILTER_PAETH = 4;

    // --- Byte utilities ---
    private static final int MASK_BYTE = 0xFF;
    private static final int SHIFT_24 = 24;
    private static final int SHIFT_16 = 16;
    private static final int SHIFT_8 = 8;
    private static final int INT_BYTE3_OFFSET = 3;
    private static final int AVERAGE_DIVISOR = 2;

    // --- BT.601 luminance weights (×1000 to avoid floating-point) ---
    private static final int LUMINANCE_RED = 299;
    private static final int LUMINANCE_GREEN = 587;
    private static final int LUMINANCE_BLUE = 114;
    private static final int LUMINANCE_DIVISOR = 1000;

    // --- Bytes-per-pixel per color type ---
    private static final int BPP_GRAYSCALE = 1;
    private static final int BPP_RGB = 3;
    private static final int BPP_GRAYSCALE_ALPHA = 2;
    private static final int BPP_RGBA = 4;

    private static final int BUFFER_SIZE = 8192;

    private CharacterQrPngReader() {
    }

    /**
     * Reads a PNG QR code from {@code path} and decodes the embedded character.
     *
     * @param path path to the PNG file
     * @return the decoded {@link CharacterPlayer}
     * @throws IOException                if the file cannot be read or is not a valid PNG
     * @throws NotFoundException          if no QR code is found in the image
     * @throws InvalidXmlElementException if the payload references unknown game elements
     */
    public static CharacterPlayer readPng(Path path)
            throws IOException, NotFoundException, InvalidXmlElementException {
        return readPng(Files.readAllBytes(path));
    }

    /**
     * Reads a PNG QR code from {@code in} and decodes the embedded character.
     * The caller is responsible for closing the stream.
     *
     * @param in the input stream
     * @return the decoded {@link CharacterPlayer}
     * @throws IOException                if reading fails or the stream is not a valid PNG
     * @throws NotFoundException          if no QR code is found in the image
     * @throws InvalidXmlElementException if the payload references unknown game elements
     */
    public static CharacterPlayer readPng(InputStream in)
            throws IOException, NotFoundException, InvalidXmlElementException {
        return readPng(readAllBytes(in));
    }

    /**
     * Reads a PNG QR code from raw bytes and decodes the embedded character.
     *
     * @param pngBytes raw PNG image bytes
     * @return the decoded {@link CharacterPlayer}
     * @throws IOException                if the bytes are not a valid PNG
     * @throws NotFoundException          if no QR code is found in the image
     * @throws InvalidXmlElementException if the payload references unknown game elements
     */
    public static CharacterPlayer readPng(byte[] pngBytes)
            throws IOException, NotFoundException, InvalidXmlElementException {
        final PngImage image = decodePng(pngBytes);
        final LuminanceSource source = new GreyLuminanceSource(image.grey, image.width, image.height);
        final BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
        final Result result = new MultiFormatReader().decode(bitmap);
        return CharacterQrCodec.decode(result.getText());
    }

    // ── PNG decoding (pure Java) ──────────────────────────────────────────────

    private static PngImage decodePng(byte[] data) throws IOException {
        verifySignature(data);

        int offset = PNG_SIGNATURE_LENGTH;
        int width = 0;
        int height = 0;
        int colorType = 0;
        final ByteArrayOutputStream idatCollector = new ByteArrayOutputStream();
        boolean done = false;

        while (!done && offset + CHUNK_LENGTH_BYTES + CHUNK_TYPE_BYTES <= data.length) {
            final int chunkLength = readInt(data, offset);
            final String chunkType = new String(data, offset + CHUNK_LENGTH_BYTES, CHUNK_TYPE_BYTES,
                    StandardCharsets.ISO_8859_1);
            final int dataStart = offset + CHUNK_LENGTH_BYTES + CHUNK_TYPE_BYTES;
            offset = dataStart + chunkLength + CHUNK_CRC_BYTES;

            switch (chunkType) {
                case "IHDR":
                    if (chunkLength < IHDR_DATA_LENGTH) {
                        throw new IOException("Truncated IHDR chunk");
                    }
                    width = readInt(data, dataStart);
                    height = readInt(data, dataStart + IHDR_HEIGHT_OFFSET);
                    colorType = data[dataStart + IHDR_COLOR_TYPE_OFFSET] & MASK_BYTE;
                    break;
                case "IDAT":
                    idatCollector.write(data, dataStart, chunkLength);
                    break;
                case "IEND":
                    done = true;
                    break;
                default:
                    break; // ancillary chunks are silently skipped
            }
        }

        final byte[] grey = inflateAndFilter(idatCollector.toByteArray(), width, height, colorType);
        return new PngImage(width, height, grey);
    }

    private static void verifySignature(byte[] data) throws IOException {
        if (data.length < PNG_SIGNATURE_LENGTH) {
            throw new IOException("File too short to be a PNG");
        }
        for (int i = 0; i < PNG_SIGNATURE_LENGTH; i++) {
            if (data[i] != CharacterQrPngWriter.PNG_SIGNATURE[i]) {
                throw new IOException("Not a valid PNG file (bad signature at byte " + i + ")");
            }
        }
    }

    private static byte[] inflateAndFilter(byte[] compressed, int width, int height, int colorType)
            throws IOException {
        final int bpp = bytesPerPixel(colorType);
        final int stride = width * bpp;
        final byte[] inflated;
        try {
            inflated = inflate(compressed, (stride + 1) * height);
        } catch (DataFormatException e) {
            throw new IOException("PNG IDAT decompression failed", e);
        }

        final byte[] grey = new byte[width * height];
        final byte[] prevRow = new byte[stride];
        int inPos = 0;
        int outPos = 0;

        for (int y = 0; y < height; y++) {
            final int filterType = inflated[inPos++] & MASK_BYTE;
            final byte[] row = new byte[stride];
            System.arraycopy(inflated, inPos, row, 0, stride);
            inPos += stride;
            applyFilter(filterType, row, prevRow, bpp);
            System.arraycopy(row, 0, prevRow, 0, stride);
            for (int x = 0; x < width; x++) {
                grey[outPos++] = toGrey(row, x, colorType, bpp);
            }
        }

        return grey;
    }

    private static byte[] inflate(byte[] input, int expectedSize) throws DataFormatException {
        final Inflater inflater = new Inflater();
        try {
            inflater.setInput(input);
            final ByteArrayOutputStream out = new ByteArrayOutputStream(Math.max(expectedSize, BUFFER_SIZE));
            final byte[] buf = new byte[BUFFER_SIZE];
            while (!inflater.finished()) {
                final int n = inflater.inflate(buf);
                if (n > 0) {
                    out.write(buf, 0, n);
                } else if (inflater.needsInput()) {
                    break; // no more input available; stream truncated
                }
            }
            return out.toByteArray();
        } finally {
            inflater.end();
        }
    }

    private static void applyFilter(int filterType, byte[] row, byte[] prev, int bpp) {
        switch (filterType) {
            case FILTER_NONE:
                break; // row bytes are used as-is
            case FILTER_SUB:
                for (int i = bpp; i < row.length; i++) {
                    row[i] = (byte) ((row[i] & MASK_BYTE) + (row[i - bpp] & MASK_BYTE));
                }
                break;
            case FILTER_UP:
                for (int i = 0; i < row.length; i++) {
                    row[i] = (byte) ((row[i] & MASK_BYTE) + (prev[i] & MASK_BYTE));
                }
                break;
            case FILTER_AVERAGE:
                for (int i = 0; i < row.length; i++) {
                    final int left = i >= bpp ? row[i - bpp] & MASK_BYTE : 0;
                    final int above = prev[i] & MASK_BYTE;
                    row[i] = (byte) ((row[i] & MASK_BYTE) + (left + above) / AVERAGE_DIVISOR);
                }
                break;
            case FILTER_PAETH:
                for (int i = 0; i < row.length; i++) {
                    final int left = i >= bpp ? row[i - bpp] & MASK_BYTE : 0;
                    final int above = prev[i] & MASK_BYTE;
                    final int upLeft = i >= bpp ? prev[i - bpp] & MASK_BYTE : 0;
                    row[i] = (byte) ((row[i] & MASK_BYTE) + paethPredictor(left, above, upLeft));
                }
                break;
            default:
                break;
        }
    }

    private static int paethPredictor(int left, int above, int upLeft) {
        final int p = left + above - upLeft;
        final int pa = Math.abs(p - left);
        final int pb = Math.abs(p - above);
        final int pc = Math.abs(p - upLeft);
        if (pa <= pb && pa <= pc) {
            return left;
        } else if (pb <= pc) {
            return above;
        } else {
            return upLeft;
        }
    }

    private static byte toGrey(byte[] row, int x, int colorType, int bpp) {
        final int base = x * bpp;
        if (colorType == COLOR_GRAYSCALE || colorType == COLOR_GRAYSCALE_ALPHA) {
            return row[base];
        }
        if (colorType == COLOR_RGB || colorType == COLOR_RGBA) {
            final int r = row[base] & MASK_BYTE;
            final int g = row[base + 1] & MASK_BYTE;
            final int b = row[base + 2] & MASK_BYTE;
            return (byte) ((r * LUMINANCE_RED + g * LUMINANCE_GREEN + b * LUMINANCE_BLUE) / LUMINANCE_DIVISOR);
        }
        return row[base];
    }

    private static int bytesPerPixel(int colorType) {
        final int bpp;
        switch (colorType) {
            case COLOR_RGB:
                bpp = BPP_RGB;
                break;
            case COLOR_GRAYSCALE_ALPHA:
                bpp = BPP_GRAYSCALE_ALPHA;
                break;
            case COLOR_RGBA:
                bpp = BPP_RGBA;
                break;
            default:
                // COLOR_GRAYSCALE (0) and any unknown type
                bpp = BPP_GRAYSCALE;
                break;
        }
        return bpp;
    }

    private static int readInt(byte[] data, int offset) {
        return ((data[offset] & MASK_BYTE) << SHIFT_24)
                | ((data[offset + 1] & MASK_BYTE) << SHIFT_16)
                | ((data[offset + 2] & MASK_BYTE) << SHIFT_8)
                | (data[offset + INT_BYTE3_OFFSET] & MASK_BYTE);
    }

    private static byte[] readAllBytes(InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
        return out.toByteArray();
    }

    // ── Inner types ───────────────────────────────────────────────────────────

    private static final class PngImage {
        private final int width;
        private final int height;
        private final byte[] grey;

        PngImage(int width, int height, byte[] grey) {
            this.width = width;
            this.height = height;
            this.grey = grey;
        }
    }

    /**
     * {@link LuminanceSource} backed by a row-major grayscale byte array (0=black, 255=white).
     */
    private static final class GreyLuminanceSource extends LuminanceSource {
        private final byte[] grey;

        GreyLuminanceSource(byte[] grey, int width, int height) {
            super(width, height);
            this.grey = grey;
        }

        @Override
        public byte[] getRow(int y, byte[] row) {
            final int width = getWidth();
            if (row == null || row.length < width) {
                row = new byte[width];
            }
            System.arraycopy(grey, y * width, row, 0, width);
            return row;
        }

        @Override
        public byte[] getMatrix() {
            return grey.clone();
        }
    }
}
