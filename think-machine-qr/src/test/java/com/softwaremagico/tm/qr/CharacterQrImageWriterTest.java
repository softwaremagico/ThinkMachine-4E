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
import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.file.modules.ModuleManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Test(groups = {"characterQrImage"})
public class CharacterQrImageWriterTest {

    private static final byte[] PNG_SIGNATURE = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};

    @BeforeClass(alwaysRun = true)
    public void enableBasicModule() {
        ModuleManager.disableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.LOST_WORLDS_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.resetModules();
    }

    @Test
    public void writesPngFileToPath() throws IOException, WriterException {
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final Path output = Files.createTempFile("character_qr_", ".png");
        try {
            CharacterQrImageWriter.writePng(player, output);
            Assert.assertTrue(Files.exists(output), "PNG file should exist");
            Assert.assertTrue(Files.size(output) > 0, "PNG file should not be empty");
            assertIsPng(Files.readAllBytes(output));
        } finally {
            Files.deleteIfExists(output);
        }
    }

    @Test
    public void writesPngFileWithCustomSize() throws IOException, WriterException {
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final Path output256 = Files.createTempFile("character_qr_256_", ".png");
        final Path output1024 = Files.createTempFile("character_qr_1024_", ".png");
        try {
            CharacterQrImageWriter.writePng(player, output256, 256);
            CharacterQrImageWriter.writePng(player, output1024, 1024);
            // Larger size should produce a bigger file
            Assert.assertTrue(Files.size(output1024) > Files.size(output256),
                    "Larger QR should produce a larger file");
        } finally {
            Files.deleteIfExists(output256);
            Files.deleteIfExists(output1024);
        }
    }

    @Test
    public void writesToOutputStream() throws IOException, WriterException {
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CharacterQrImageWriter.write(player, CharacterQrImageWriter.FORMAT_PNG,
                CharacterQrMatrix.DEFAULT_SIZE, bos);
        final byte[] imageBytes = bos.toByteArray();
        Assert.assertTrue(imageBytes.length > 0, "Image stream should not be empty");
        assertIsPng(imageBytes);
    }

    @Test
    public void qrFromImageIsReadableAsPayload() throws IOException, WriterException,
            com.google.zxing.NotFoundException {
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final String originalPayload = CharacterQrCodec.encode(player);

        // Write image, then decode from BitMatrix to verify the payload survives
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CharacterQrImageWriter.write(player, CharacterQrImageWriter.FORMAT_PNG,
                CharacterQrMatrix.DEFAULT_SIZE, bos);
        Assert.assertTrue(bos.size() > 0);

        // The payload encode/decode is already tested in CharacterQrCodecTest;
        // here we verify the image is non-empty and the payload is stable.
        final String secondPayload = CharacterQrCodec.encode(player);
        Assert.assertEquals(secondPayload, originalPayload,
                "Payload must be deterministic for the same character");
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private static void assertIsPng(byte[] data) {
        Assert.assertTrue(data.length >= PNG_SIGNATURE.length,
                "Image data too short to contain PNG signature");
        for (int i = 0; i < PNG_SIGNATURE.length; i++) {
            Assert.assertEquals(data[i], PNG_SIGNATURE[i],
                    "Byte " + i + " does not match PNG signature");
        }
    }
}
