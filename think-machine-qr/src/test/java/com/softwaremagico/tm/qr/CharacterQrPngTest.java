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
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.file.modules.ModuleManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Test(groups = {"characterQrPng"})
public class CharacterQrPngTest {

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
            CharacterQrPngWriter.writePng(player, output);
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
        final int size256 = 256;
        final int size1024 = 1024;
        try {
            CharacterQrPngWriter.writePng(player, output256, size256);
            CharacterQrPngWriter.writePng(player, output1024, size1024);
            Assert.assertTrue(Files.size(output1024) > Files.size(output256),
                    "Larger QR should produce a larger PNG file");
        } finally {
            Files.deleteIfExists(output256);
            Files.deleteIfExists(output1024);
        }
    }

    @Test
    public void writesToOutputStream() throws IOException, WriterException {
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CharacterQrPngWriter.writePng(player, bos);
        final byte[] imageBytes = bos.toByteArray();
        Assert.assertTrue(imageBytes.length > 0, "Image stream should not be empty");
        assertIsPng(imageBytes);
    }

    @Test
    public void roundTripThroughPngFile() throws IOException, WriterException,
            com.google.zxing.NotFoundException, InvalidXmlElementException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final Path output = Files.createTempFile("character_qr_roundtrip_", ".png");
        try {
            CharacterQrPngWriter.writePng(original, output);
            final CharacterPlayer decoded = CharacterQrPngReader.readPng(output);

            Assert.assertEquals(decoded.getInfo().getNameRepresentation(),
                    original.getInfo().getNameRepresentation(),
                    "Character name must survive PNG round-trip");
            Assert.assertEquals(decoded.getSpecie().getId(), original.getSpecie().getId(),
                    "Specie must survive PNG round-trip");
            Assert.assertEquals(decoded.getFaction().getId(), original.getFaction().getId(),
                    "Faction must survive PNG round-trip");
        } finally {
            Files.deleteIfExists(output);
        }
    }

    @Test
    public void roundTripThroughOutputStream() throws IOException, WriterException,
            com.google.zxing.NotFoundException, InvalidXmlElementException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();

        // Write to ByteArrayOutputStream
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CharacterQrPngWriter.writePng(original, bos);

        // Read from the same bytes via InputStream
        final CharacterPlayer decoded = CharacterQrPngReader.readPng(
                new ByteArrayInputStream(bos.toByteArray()));

        Assert.assertEquals(decoded.getInfo().getNameRepresentation(),
                original.getInfo().getNameRepresentation(),
                "Character name must survive OutputStream round-trip");
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private static void assertIsPng(byte[] data) {
        Assert.assertTrue(data.length >= CharacterQrPngWriter.PNG_SIGNATURE.length,
                "Image data too short to contain PNG signature");
        for (int i = 0; i < CharacterQrPngWriter.PNG_SIGNATURE.length; i++) {
            Assert.assertEquals(data[i], CharacterQrPngWriter.PNG_SIGNATURE[i],
                    "Byte " + i + " does not match PNG signature");
        }
    }
}
