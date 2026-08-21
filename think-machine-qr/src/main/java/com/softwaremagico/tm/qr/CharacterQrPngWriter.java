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

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.softwaremagico.tm.character.CharacterPlayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

/**
 * Generates a QR code PNG image from a {@link CharacterPlayer} and writes it to a file or stream.
 *
 * <p><strong>Android-compatible.</strong> Uses only pure-Java APIs available on Android:
 * {@code java.util.zip.Deflater} for PNG compression and {@code java.util.zip.CRC32} for
 * chunk checksums. No {@code java.awt}, {@code javax.imageio}, or
 * {@code com.google.zxing:javase} dependency.
 *
 * <p>The output is a valid 8-bit grayscale PNG readable by {@link CharacterQrPngReader}.
 *
 * <p>Usage:
 * <pre>{@code
 * CharacterQrPngWriter.writePng(player, Path.of("/tmp/character.png"));
 * }</pre>
 */
public final class CharacterQrPngWriter {

    // PNG signature (ISO 15948)
    static final byte[] PNG_SIGNATURE = {
        (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
    };

    private static final byte[] IHDR_TYPE = {'I', 'H', 'D', 'R'};
    private static final byte[] IDAT_TYPE = {'I', 'D', 'A', 'T'};
    private static final byte[] IEND_TYPE = {'I', 'E', 'N', 'D'};

    private static final int COLOR_TYPE_GRAYSCALE = 0;
    private static final int BIT_DEPTH_8 = 8;
    private static final int FILTER_NONE = 0;
    private static final int PIXEL_BLACK = 0;
    private static final int PIXEL_WHITE = 255;
    private static final int BUFFER_SIZE = 4096;
    private static final int IHDR_DATA_SIZE = 13;
    private static final int MASK_BYTE = 0xFF;
    private static final int SHIFT_24 = 24;
    private static final int SHIFT_16 = 16;
    private static final int SHIFT_8 = 8;

    private CharacterQrPngWriter() {
    }

    /**
     * Encodes the character and writes a PNG QR code image to {@code outputPath}.
     *
     * @param player     the character to encode
     * @param outputPath destination file path (will be created / overwritten)
     * @throws IOException     if writing fails
     * @throws WriterException if QR encoding fails
     */
    public static void writePng(CharacterPlayer player, Path outputPath)
            throws IOException, WriterException {
        writePng(player, outputPath, CharacterQrMatrix.DEFAULT_SIZE);
    }

    /**
     * Encodes the character and writes a PNG QR code image of the given pixel size.
     *
     * @param player     the character to encode
     * @param outputPath destination file path (will be created / overwritten)
     * @param size       width and height in pixels
     * @throws IOException     if writing fails
     * @throws WriterException if QR encoding fails
     */
    public static void writePng(CharacterPlayer player, Path outputPath, int size)
            throws IOException, WriterException {
        final String payload = CharacterQrCodec.encode(player);
        final BitMatrix matrix = CharacterQrMatrix.encode(payload, size);
        try (OutputStream os = Files.newOutputStream(outputPath)) {
            writePngToStream(matrix, os);
        }
    }

    /**
     * Encodes the character and writes a PNG QR code to an {@link OutputStream}.
     * The caller is responsible for closing the stream.
     *
     * @param player the character to encode
     * @param out    destination stream
     * @throws IOException     if writing fails
     * @throws WriterException if QR encoding fails
     */
    public static void writePng(CharacterPlayer player, OutputStream out)
            throws IOException, WriterException {
        writePng(player, out, CharacterQrMatrix.DEFAULT_SIZE);
    }

    /**
     * Encodes the character and writes a PNG QR code of the given size to an {@link OutputStream}.
     * The caller is responsible for closing the stream.
     *
     * @param player the character to encode
     * @param out    destination stream
     * @param size   width and height in pixels
     * @throws IOException     if writing fails
     * @throws WriterException if QR encoding fails
     */
    public static void writePng(CharacterPlayer player, OutputStream out, int size)
            throws IOException, WriterException {
        final String payload = CharacterQrCodec.encode(player);
        final BitMatrix matrix = CharacterQrMatrix.encode(payload, size);
        writePngToStream(matrix, out);
    }

    // ── PNG encoding (pure Java, Android-compatible) ──────────────────────────

    static void writePngToStream(BitMatrix matrix, OutputStream out) throws IOException {
        final int width = matrix.getWidth();
        final int height = matrix.getHeight();

        out.write(PNG_SIGNATURE);
        writeChunk(out, IHDR_TYPE, buildIhdrData(width, height));
        writeChunk(out, IDAT_TYPE, buildIdatData(matrix, width, height));
        writeChunk(out, IEND_TYPE, new byte[0]);
        out.flush();
    }

    private static byte[] buildIhdrData(int width, int height) throws IOException {
        final ByteArrayOutputStream buf = new ByteArrayOutputStream(IHDR_DATA_SIZE);
        writeInt(buf, width);
        writeInt(buf, height);
        buf.write(BIT_DEPTH_8);
        buf.write(COLOR_TYPE_GRAYSCALE);
        buf.write(0); // compression method: deflate
        buf.write(0); // filter method: adaptive
        buf.write(0); // interlace method: none
        return buf.toByteArray();
    }

    private static byte[] buildIdatData(BitMatrix matrix, int width, int height) {
        // Each scanline: 1 filter byte (0 = None) + width grayscale bytes
        final byte[] raw = new byte[(width + 1) * height];
        int pos = 0;
        for (int y = 0; y < height; y++) {
            raw[pos++] = (byte) FILTER_NONE;
            for (int x = 0; x < width; x++) {
                raw[pos++] = (byte) (matrix.get(x, y) ? PIXEL_BLACK : PIXEL_WHITE);
            }
        }

        final Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        try {
            deflater.setInput(raw);
            deflater.finish();
            final ByteArrayOutputStream compressed = new ByteArrayOutputStream(raw.length / 2);
            final byte[] buf = new byte[BUFFER_SIZE];
            while (!deflater.finished()) {
                final int n = deflater.deflate(buf);
                compressed.write(buf, 0, n);
            }
            return compressed.toByteArray();
        } finally {
            deflater.end();
        }
    }

    private static void writeChunk(OutputStream out, byte[] type, byte[] data) throws IOException {
        writeInt(out, data.length);
        out.write(type);
        out.write(data);
        final CRC32 crc = new CRC32();
        crc.update(type);
        crc.update(data);
        writeInt(out, (int) crc.getValue());
    }

    private static void writeInt(OutputStream out, int value) throws IOException {
        out.write((value >>> SHIFT_24) & MASK_BYTE);
        out.write((value >>> SHIFT_16) & MASK_BYTE);
        out.write((value >>> SHIFT_8) & MASK_BYTE);
        out.write(value & MASK_BYTE);
    }
}
