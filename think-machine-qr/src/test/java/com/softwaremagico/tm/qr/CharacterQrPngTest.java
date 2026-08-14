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
import com.softwaremagico.tm.character.capabilities.CapabilityWithSpecialization;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.perks.SpecializedPerk;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.MaxValueExceededException;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.random.character.RandomizeCharacter;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

@Test(groups = {"characterQrPng"})
public class CharacterQrPngTest extends QrGeneration {

    private static final int SIZE_256 = 256;
    private static final int SIZE_1024 = 1024;

    @BeforeClass(alwaysRun = true)
    public void enableBasicModule() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
        ModuleManager.disableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.LOST_WORLDS_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.resetModules();
    }

    @Test
    public void writesPngFileToPath() throws IOException, WriterException {
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final Path output = getOutputPath("CharacterQr_Default.png");
        CharacterQrPngWriter.writePng(player, output);
        Assert.assertTrue(Files.exists(output), "PNG file should exist");
        Assert.assertTrue(Files.size(output) > 0, "PNG file should not be empty");
        assertIsPng(Files.readAllBytes(output));
    }

    @Test
    public void writesPngFileWithCustomSize() throws IOException, WriterException {
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final Path output256 = getOutputPath("CharacterQr_Size_256.png");
        final Path output1024 = getOutputPath("CharacterQr_Size_1024.png");
        CharacterQrPngWriter.writePng(player, output256, SIZE_256);
        CharacterQrPngWriter.writePng(player, output1024, SIZE_1024);
        Assert.assertTrue(Files.size(output1024) > Files.size(output256),
                "Larger QR should produce a larger PNG file");
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
            com.google.zxing.NotFoundException, InvalidXmlElementException, MaxValueExceededException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final Path output = getOutputPath("CharacterQr_RoundTrip.png");
        CharacterQrPngWriter.writePng(original, output);
        final CharacterPlayer decoded = CharacterQrPngReader.readPng(output);

        assertCharactersEqual(original, decoded);
    }

    @Test
    public void roundTripThroughOutputStream() throws IOException, WriterException,
            com.google.zxing.NotFoundException, InvalidXmlElementException, MaxValueExceededException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleHawkwoodCommander();

        // Write to ByteArrayOutputStream
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CharacterQrPngWriter.writePng(original, bos);

        // Read from the same bytes via InputStream
        final CharacterPlayer decoded = CharacterQrPngReader.readPng(
                new ByteArrayInputStream(bos.toByteArray()));

        assertCharactersEqual(original, decoded);
    }

    @Test
    public void writesRandomCharacterToOutputStream() throws IOException, WriterException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer player = generateRandomCharacter();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CharacterQrPngWriter.writePng(player, bos);
        final byte[] imageBytes = bos.toByteArray();
        Assert.assertTrue(imageBytes.length > 0, "Random character PNG should not be empty");
        assertIsPng(imageBytes);
    }

    @Test
    public void roundTripRandomCharacterThroughPngFile() throws IOException, WriterException,
            com.google.zxing.NotFoundException, InvalidXmlElementException, MaxValueExceededException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer original = generateRandomCharacter();
        final Path output = getOutputPath("CharacterQr_RandomRoundTrip.png");
        CharacterQrPngWriter.writePng(original, output);
        final CharacterPlayer decoded = CharacterQrPngReader.readPng(output);
        assertCharactersEqual(original, decoded);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void assertCharactersEqual(CharacterPlayer original, CharacterPlayer decoded)
            throws MaxValueExceededException {
        Assert.assertNotNull(decoded);

        Assert.assertEquals(decoded.getInfo().getNameRepresentation(),
                original.getInfo().getNameRepresentation(), "Name");
        Assert.assertEquals(decoded.getInfo().getSurname().getNameRepresentation(),
                original.getInfo().getSurname().getNameRepresentation(), "Surname");
        Assert.assertEquals(decoded.getInfo().getPlayer(), original.getInfo().getPlayer(), "Player");
        Assert.assertEquals(decoded.getInfo().getGender(), original.getInfo().getGender(), "Gender");
        Assert.assertEquals(decoded.getInfo().getAge(), original.getInfo().getAge(), "Age");
        Assert.assertEquals(decoded.getInfo().getPlanet(), original.getInfo().getPlanet(), "Planet");

        Assert.assertEquals(decoded.getSpecie().getId(), original.getSpecie().getId(), "Specie");
        Assert.assertEquals(decoded.getUpbringing().getId(), original.getUpbringing().getId(), "Upbringing");
        Assert.assertEquals(decoded.getFaction().getId(), original.getFaction().getId(), "Faction");
        Assert.assertEquals(decoded.getCalling().getId(), original.getCalling().getId(), "Calling");

        Assert.assertEquals(decoded.getPrimaryCharacteristic(), original.getPrimaryCharacteristic(),
                "Primary characteristic");
        Assert.assertEquals(decoded.getSecondaryCharacteristic(), original.getSecondaryCharacteristic(),
                "Secondary characteristic");

        for (final CharacteristicName name : CharacteristicName.values()) {
            Assert.assertEquals(decoded.getCharacteristicValue(name),
                    original.getCharacteristicValue(name),
                    "Characteristic " + name);
        }

        for (final com.softwaremagico.tm.character.skills.Skill skill : SkillFactory.getInstance().getElements()) {
            Assert.assertEquals(decoded.getSkillValue(skill), original.getSkillValue(skill),
                    "Skill " + skill.getId());
        }

        final Set<String> originalCapabilities = original.getCapabilitiesWithSpecialization().stream()
                .map(CapabilityWithSpecialization::getComparisonId)
                .collect(Collectors.toSet());
        final Set<String> decodedCapabilities = decoded.getCapabilitiesWithSpecialization().stream()
                .map(CapabilityWithSpecialization::getComparisonId)
                .collect(Collectors.toSet());
        Assert.assertEquals(decodedCapabilities, originalCapabilities, "Capabilities");

        final Set<String> originalPerks = original.getPerks().stream()
                .map(SpecializedPerk::toString)
                .collect(Collectors.toSet());
        final Set<String> decodedPerks = decoded.getPerks().stream()
                .map(SpecializedPerk::toString)
                .collect(Collectors.toSet());
        Assert.assertEquals(decodedPerks, originalPerks, "Perks");

        final Set<String> originalEquipment = original.getEquipmentPurchased().stream()
                .map(com.softwaremagico.tm.Element::getId)
                .collect(Collectors.toSet());
        final Set<String> decodedEquipment = decoded.getEquipmentPurchased().stream()
                .map(com.softwaremagico.tm.Element::getId)
                .collect(Collectors.toSet());
        Assert.assertEquals(decodedEquipment, originalEquipment, "Equipment");

        final Set<String> originalAwards = original.getMaterialAwardsSelected(false).stream()
                .map(eo -> eo.getElement().getId())
                .collect(Collectors.toSet());
        final Set<String> decodedAwards = decoded.getMaterialAwardsSelected(false).stream()
                .map(eo -> eo.getElement().getId())
                .collect(Collectors.toSet());
        Assert.assertEquals(decodedAwards, originalAwards, "Material awards");

        Assert.assertEquals(decoded.getCharacteristicReassigns().size(),
                original.getCharacteristicReassigns().size(), "Characteristic reassign count");
        for (int i = 0; i < original.getCharacteristicReassigns().size(); i++) {
            Assert.assertEquals(decoded.getCharacteristicReassigns().get(i).getFrom(),
                    original.getCharacteristicReassigns().get(i).getFrom(),
                    "Characteristic reassign source " + i);
            Assert.assertEquals(decoded.getCharacteristicReassigns().get(i).getTo(),
                    original.getCharacteristicReassigns().get(i).getTo(),
                    "Characteristic reassign target " + i);
        }

        Assert.assertEquals(decoded.getSkillsReassigns().size(),
                original.getSkillsReassigns().size(), "Skill reassign count");
        for (int i = 0; i < original.getSkillsReassigns().size(); i++) {
            Assert.assertEquals(decoded.getSkillsReassigns().get(i).getFrom(),
                    original.getSkillsReassigns().get(i).getFrom(),
                    "Skill reassign source " + i);
            Assert.assertEquals(decoded.getSkillsReassigns().get(i).getTo(),
                    original.getSkillsReassigns().get(i).getTo(),
                    "Skill reassign target " + i);
        }

        Assert.assertEquals(decoded.getLevel(), original.getLevel(), "Level");
    }

    private static void assertIsPng(byte[] data) {
        Assert.assertTrue(data.length >= CharacterQrPngWriter.PNG_SIGNATURE.length,
                "Image data too short to contain PNG signature");
        for (int i = 0; i < CharacterQrPngWriter.PNG_SIGNATURE.length; i++) {
            Assert.assertEquals(data[i], CharacterQrPngWriter.PNG_SIGNATURE[i],
                    "Byte " + i + " does not match PNG signature");
        }
    }

    private static CharacterPlayer generateRandomCharacter() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("decados");
        new RandomizeCharacter(characterPlayer).createCharacter();
        return characterPlayer;
    }
}
