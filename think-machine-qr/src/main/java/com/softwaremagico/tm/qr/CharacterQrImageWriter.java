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
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.softwaremagico.tm.character.CharacterPlayer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Generates a QR code image from a {@link CharacterPlayer} and writes it to a file or stream.
 *
 * <p><strong>Desktop-only (NOT Android-compatible).</strong>
 * This class relies on {@code com.google.zxing:javase} which uses {@code java.awt} and
 * {@code javax.imageio}, neither of which is available on Android.
 * On Android, use {@link CharacterQrMatrix#encode(String)} to obtain a {@link BitMatrix}
 * and render it to a {@code android.graphics.Bitmap} using a platform canvas.
 *
 * <p>Usage example:
 * <pre>{@code
 * CharacterQrImageWriter.writePng(player, Path.of("/tmp/character.png"));
 * }</pre>
 */
public final class CharacterQrImageWriter {

    /** Default image format used when none is specified. */
    public static final String FORMAT_PNG = "PNG";

    /** JPEG image format constant. */
    public static final String FORMAT_JPEG = "JPEG";

    private CharacterQrImageWriter() {
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
        write(player, outputPath, FORMAT_PNG, CharacterQrMatrix.DEFAULT_SIZE);
    }

    /**
     * Encodes the character and writes a PNG QR code image of the given size.
     *
     * @param player     the character to encode
     * @param outputPath destination file path (will be created / overwritten)
     * @param size       width and height in pixels
     * @throws IOException     if writing fails
     * @throws WriterException if QR encoding fails
     */
    public static void writePng(CharacterPlayer player, Path outputPath, int size)
            throws IOException, WriterException {
        write(player, outputPath, FORMAT_PNG, size);
    }

    /**
     * Encodes the character and writes a QR code image in the requested format.
     *
     * @param player     the character to encode
     * @param outputPath destination file path (will be created / overwritten)
     * @param format     image format, e.g. {@link #FORMAT_PNG} or {@link #FORMAT_JPEG}
     * @param size       width and height in pixels
     * @throws IOException     if writing fails
     * @throws WriterException if QR encoding fails
     */
    public static void write(CharacterPlayer player, Path outputPath, String format, int size)
            throws IOException, WriterException {
        final String payload = CharacterQrCodec.encode(player);
        final BitMatrix matrix = CharacterQrMatrix.encode(payload, size);
        try (OutputStream os = Files.newOutputStream(outputPath)) {
            MatrixToImageWriter.writeToStream(matrix, format, os);
        }
    }

    /**
     * Encodes the character and writes a QR code image to an existing {@link OutputStream}.
     * The caller is responsible for closing the stream.
     *
     * @param player the character to encode
     * @param format image format, e.g. {@link #FORMAT_PNG}
     * @param size   width and height in pixels
     * @param out    the output stream to write to
     * @throws IOException     if writing fails
     * @throws WriterException if QR encoding fails
     */
    public static void write(CharacterPlayer player, String format, int size, OutputStream out)
            throws IOException, WriterException {
        final String payload = CharacterQrCodec.encode(player);
        final BitMatrix matrix = CharacterQrMatrix.encode(payload, size);
        MatrixToImageWriter.writeToStream(matrix, format, out);
    }
}
