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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.EnumMap;
import java.util.Map;

/**
 * Converts a character payload string to/from a ZXing {@link BitMatrix}.
 *
 * <p><b>Android-compatible:</b> this class only uses {@code com.google.zxing:core}
 * and never imports {@code java.awt} or {@code javax.imageio}.
 * Callers are responsible for rendering the {@link BitMatrix} to a visual image
 * using platform-specific APIs (Android {@code Canvas}, desktop {@code BufferedImage}, etc.).
 */
public final class CharacterQrMatrix {

    /** Luminance value for a light (white) QR module. */
    private static final byte LUMINANCE_LIGHT = (byte) 255;
    /** Luminance value for a dark (black) QR module. */
    private static final byte LUMINANCE_DARK = (byte) 0;

    /** Default QR size in pixels. */
    public static final int DEFAULT_SIZE = 512;

    private CharacterQrMatrix() {
    }

    /**
     * Encodes a payload string as a QR code {@link BitMatrix} of {@link #DEFAULT_SIZE}.
     */
    public static BitMatrix encode(String payload) throws WriterException {
        return encode(payload, DEFAULT_SIZE);
    }

    /**
     * Encodes a payload string as a QR code {@link BitMatrix}.
     *
     * @param payload the URL-safe Base64 string produced by {@link CharacterQrCodec#encode}
     * @param size    width and height in pixels
     * @return the QR {@link BitMatrix} — render with your platform's image API
     */
    public static BitMatrix encode(String payload, int size) throws WriterException {
        final Map<com.google.zxing.EncodeHintType, Object> hints = new EnumMap<>(com.google.zxing.EncodeHintType.class);
        hints.put(com.google.zxing.EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(com.google.zxing.EncodeHintType.MARGIN, 1);
        return new QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, size, size, hints);
    }

    /**
     * Decodes a {@link BitMatrix} back to the payload string.
     *
     * <p>This method works entirely within {@code zxing:core} via a
     * custom {@link LuminanceSource} that reads directly from the {@link BitMatrix},
     * so it is also Android-compatible.
     *
     * @param matrix the QR {@link BitMatrix} to decode
     * @return the payload string
     * @throws NotFoundException if the matrix cannot be decoded as a valid QR code
     */
    public static String decode(BitMatrix matrix) throws NotFoundException {
        final BinaryBitmap bitmap = new BinaryBitmap(
                new GlobalHistogramBinarizer(new BitMatrixLuminanceSource(matrix)));
        final Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

    // ── Private ───────────────────────────────────────────────────────────────

    /**
     * {@link LuminanceSource} backed by a {@link BitMatrix}.
     * Dark modules produce luminance 0, light modules produce 255.
     */
    private static final class BitMatrixLuminanceSource extends LuminanceSource {

        private final BitMatrix matrix;

        BitMatrixLuminanceSource(BitMatrix matrix) {
            super(matrix.getWidth(), matrix.getHeight());
            this.matrix = matrix;
        }

        @Override
        public byte[] getRow(int y, byte[] row) {
            final int width = getWidth();
            if (row == null || row.length < width) {
                row = new byte[width];
            }
            for (int x = 0; x < width; x++) {
                row[x] = matrix.get(x, y) ? LUMINANCE_DARK : LUMINANCE_LIGHT;
            }
            return row;
        }

        @Override
        public byte[] getMatrix() {
            final int width = getWidth();
            final int height = getHeight();
            final byte[] data = new byte[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    data[y * width + x] = matrix.get(x, y) ? LUMINANCE_DARK : LUMINANCE_LIGHT;
                }
            }
            return data;
        }
    }
}
