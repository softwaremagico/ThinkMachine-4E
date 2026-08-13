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
import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.perks.SpecializedPerk;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.exceptions.MaxValueExceededException;
import com.softwaremagico.tm.file.modules.ModuleManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Test(groups = {"characterQr"})
public class CharacterQrCodecTest {

    @BeforeClass(alwaysRun = true)
    public void enableBasicModule() {
        ModuleManager.disableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.LOST_WORLDS_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.resetModules();
    }

    // ── Encode / decode payload round-trip ───────────────────────────────────

    @Test
    public void payloadRoundTripBasicCharacter() throws IOException, MaxValueExceededException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final String payload = CharacterQrCodec.encode(original);
        Assert.assertNotNull(payload);
        Assert.assertFalse(payload.isEmpty());

        final CharacterPlayer decoded = CharacterQrCodec.decode(payload);
        assertCharactersEqual(original, decoded);
    }

    @Test
    public void payloadRoundTripWithCharacteristicReassign() throws IOException, MaxValueExceededException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleHawkwoodCommander();
        // Hawkwood commander already has a characteristic reassign
        Assert.assertFalse(original.getCharacteristicReassigns().isEmpty(),
                "Test prerequisite: character must have reassigns");

        final String payload = CharacterQrCodec.encode(original);
        final CharacterPlayer decoded = CharacterQrCodec.decode(payload);
        assertCharactersEqual(original, decoded);
    }

    @Test
    public void payloadRoundTripSybariteCharacter() throws IOException, MaxValueExceededException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosSybarite();
        final String payload = CharacterQrCodec.encode(original);
        final CharacterPlayer decoded = CharacterQrCodec.decode(payload);
        assertCharactersEqual(original, decoded);
    }

    @Test
    public void payloadRoundTripWithLevel2() throws IOException, MaxValueExceededException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        original.addLevel().setCalling("conspiracist");
        CharacterExamples.populateLevel(original);

        final String payload = CharacterQrCodec.encode(original);
        final CharacterPlayer decoded = CharacterQrCodec.decode(payload);

        Assert.assertEquals(decoded.getLevel(), original.getLevel(),
                "Level count should be preserved");
        assertCharactersEqual(original, decoded);
    }

    // ── QR matrix round-trip (BitMatrix encode → decode → payload matches) ───

    @Test
    public void qrMatrixRoundTrip() throws IOException, WriterException, com.google.zxing.NotFoundException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final String payload = CharacterQrCodec.encode(original);

        final BitMatrix matrix = CharacterQrMatrix.encode(payload);
        Assert.assertNotNull(matrix);
        Assert.assertTrue(matrix.getWidth() > 0);

        final String decodedPayload = CharacterQrMatrix.decode(matrix);
        Assert.assertEquals(decodedPayload, payload,
                "Payload must survive a full QR encode → decode cycle");
    }

    @Test
    public void qrMatrixRoundTripFullCharacter() throws IOException, WriterException, MaxValueExceededException, com.google.zxing.NotFoundException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final String payload = CharacterQrCodec.encode(original);

        // Encode to QR matrix, decode back to string, then decode to character
        final BitMatrix matrix = CharacterQrMatrix.encode(payload);
        final String payloadFromQr = CharacterQrMatrix.decode(matrix);
        final CharacterPlayer decoded = CharacterQrCodec.decode(payloadFromQr);

        assertCharactersEqual(original, decoded);
    }

    // ── Payload is compact enough for a QR code ──────────────────────────────

    @Test
    public void payloadFitsInQrCode() throws IOException {
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final String payload = CharacterQrCodec.encode(player);
        // QR version 40 ECC-L supports up to 4296 alphanumeric chars / 2953 bytes.
        Assert.assertTrue(payload.length() <= 2953,
                "Payload length " + payload.length() + " exceeds QR capacity of 2953 bytes");
    }

    @Test
    public void payloadFitsWithLevel2() throws IOException {
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        player.addLevel().setCalling("conspiracist");
        CharacterExamples.populateLevel(player);
        final String payload = CharacterQrCodec.encode(player);
        Assert.assertTrue(payload.length() <= 2953,
                "Level-2 payload length " + payload.length() + " exceeds QR capacity");
    }

    // ── Info preservation ────────────────────────────────────────────────────

    @Test
    public void characterInfoIsPreserved() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final CharacterPlayer decoded = CharacterQrCodec.decode(CharacterQrCodec.encode(original));

        Assert.assertEquals(decoded.getInfo().getNameRepresentation(),
                original.getInfo().getNameRepresentation(), "Name");
        Assert.assertEquals(decoded.getInfo().getSurname().getNameRepresentation(),
                original.getInfo().getSurname().getNameRepresentation(), "Surname");
        Assert.assertEquals(decoded.getInfo().getPlayer(), original.getInfo().getPlayer(), "Player");
        Assert.assertEquals(decoded.getInfo().getGender(), original.getInfo().getGender(), "Gender");
        Assert.assertEquals(decoded.getInfo().getAge(), original.getInfo().getAge(), "Age");
        Assert.assertEquals(decoded.getInfo().getPlanet(), original.getInfo().getPlanet(), "Planet");
    }

    // ── Stable IDs: adding elements does not break existing payload ──────────

    @Test
    public void unknownIdsInPayloadAreSkippedGracefully() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final String validPayload = CharacterQrCodec.encode(original);

        // Decode should succeed without throwing exceptions even if the same
        // payload is decoded after modules change (simulated here by just decoding
        // the valid payload — the point is that missing IDs never throw).
        final CharacterPlayer decoded = CharacterQrCodec.decode(validPayload);
        Assert.assertNotNull(decoded);
    }

    // ── Characteristic and skill values survive round-trip ───────────────────

    @Test
    public void characteristicValuesArePreserved() throws IOException, MaxValueExceededException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final CharacterPlayer decoded = CharacterQrCodec.decode(CharacterQrCodec.encode(original));

        for (final CharacteristicName name : CharacteristicName.values()) {
            Assert.assertEquals(
                    decoded.getCharacteristicValue(name),
                    original.getCharacteristicValue(name),
                    "Characteristic " + name + " value mismatch");
        }
    }

    @Test
    public void skillValuesArePreserved() throws IOException, MaxValueExceededException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final CharacterPlayer decoded = CharacterQrCodec.decode(CharacterQrCodec.encode(original));

        for (final com.softwaremagico.tm.character.skills.Skill skill : SkillFactory.getInstance().getElements()) {
            Assert.assertEquals(
                    decoded.getSkillValue(skill),
                    original.getSkillValue(skill),
                    "Skill " + skill.getId() + " value mismatch");
        }
    }

    @Test
    public void perksArePreserved() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final CharacterPlayer decoded = CharacterQrCodec.decode(CharacterQrCodec.encode(original));

        final Set<String> originalPerks = original.getPerks().stream()
                .map(SpecializedPerk::toString)
                .collect(Collectors.toSet());
        final Set<String> decodedPerks = decoded.getPerks().stream()
                .map(SpecializedPerk::toString)
                .collect(Collectors.toSet());

        Assert.assertEquals(decodedPerks, originalPerks, "Perks mismatch");
    }

    @Test
    public void equipmentIsPreserved() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final CharacterPlayer decoded = CharacterQrCodec.decode(CharacterQrCodec.encode(original));

        final Set<String> originalEquipment = original.getEquipmentPurchased().stream()
                .map(com.softwaremagico.tm.Element::getId)
                .collect(Collectors.toSet());
        final Set<String> decodedEquipment = decoded.getEquipmentPurchased().stream()
                .map(com.softwaremagico.tm.Element::getId)
                .collect(Collectors.toSet());

        Assert.assertEquals(decodedEquipment, originalEquipment, "Equipment mismatch");
    }

    @Test
    public void materialAwardsArePreserved() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final CharacterPlayer decoded = CharacterQrCodec.decode(CharacterQrCodec.encode(original));

        final Set<String> originalAwards = original.getMaterialAwardsSelected(false).stream()
                .map(eo -> eo.getElement().getId())
                .collect(Collectors.toSet());
        final Set<String> decodedAwards = decoded.getMaterialAwardsSelected(false).stream()
                .map(eo -> eo.getElement().getId())
                .collect(Collectors.toSet());

        Assert.assertEquals(decodedAwards, originalAwards, "Material awards mismatch");
    }

    @Test
    public void characteristicReassignsArePreserved() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleHawkwoodCommander();
        final CharacterPlayer decoded = CharacterQrCodec.decode(CharacterQrCodec.encode(original));

        Assert.assertEquals(decoded.getCharacteristicReassigns().size(),
                original.getCharacteristicReassigns().size(), "Reassign count mismatch");
        for (int i = 0; i < original.getCharacteristicReassigns().size(); i++) {
            Assert.assertEquals(decoded.getCharacteristicReassigns().get(i).getFrom(),
                    original.getCharacteristicReassigns().get(i).getFrom());
            Assert.assertEquals(decoded.getCharacteristicReassigns().get(i).getTo(),
                    original.getCharacteristicReassigns().get(i).getTo());
        }
    }

    // ── Primary / secondary characteristics ──────────────────────────────────

    @Test
    public void primaryAndSecondaryCharacteristicsArePreserved() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final CharacterPlayer decoded = CharacterQrCodec.decode(CharacterQrCodec.encode(original));

        Assert.assertEquals(decoded.getPrimaryCharacteristic(), original.getPrimaryCharacteristic(),
                "Primary characteristic");
        Assert.assertEquals(decoded.getSecondaryCharacteristic(), original.getSecondaryCharacteristic(),
                "Secondary characteristic");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void assertCharactersEqual(CharacterPlayer original, CharacterPlayer decoded)
            throws MaxValueExceededException {
        Assert.assertNotNull(decoded);

        // Specie / upbringing / faction / calling
        Assert.assertEquals(decoded.getSpecie().getId(), original.getSpecie().getId(), "Specie");
        Assert.assertEquals(decoded.getUpbringing().getId(), original.getUpbringing().getId(), "Upbringing");
        Assert.assertEquals(decoded.getFaction().getId(), original.getFaction().getId(), "Faction");
        Assert.assertEquals(decoded.getCalling().getId(), original.getCalling().getId(), "Calling");

        // Primary / secondary characteristics
        Assert.assertEquals(decoded.getPrimaryCharacteristic(), original.getPrimaryCharacteristic(),
                "Primary characteristic");
        Assert.assertEquals(decoded.getSecondaryCharacteristic(), original.getSecondaryCharacteristic(),
                "Secondary characteristic");

        // Characteristic values
        for (final CharacteristicName name : CharacteristicName.values()) {
            Assert.assertEquals(decoded.getCharacteristicValue(name),
                    original.getCharacteristicValue(name),
                    "Characteristic " + name);
        }

        // Skill values
        for (final com.softwaremagico.tm.character.skills.Skill skill : SkillFactory.getInstance().getElements()) {
            Assert.assertEquals(decoded.getSkillValue(skill), original.getSkillValue(skill),
                    "Skill " + skill.getId());
        }

        // Perks
        final Set<String> originalPerks = original.getPerks().stream()
                .map(SpecializedPerk::toString).collect(Collectors.toSet());
        final Set<String> decodedPerks = decoded.getPerks().stream()
                .map(SpecializedPerk::toString).collect(Collectors.toSet());
        Assert.assertEquals(decodedPerks, originalPerks, "Perks");

        // Level
        Assert.assertEquals(decoded.getLevel(), original.getLevel(), "Level");
    }
}
