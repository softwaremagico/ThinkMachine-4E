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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.MaxValueExceededException;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.random.character.RandomizeCharacter;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Tests that high-level (ELITE tier, levels 9-15) randomly generated characters
 * produce payloads that fit within QR version-40 ECC-L capacity (2953 bytes).
 */
@Test(groups = {"highLevelCharacterQr"})
public class HighLevelCharacterQrTest {

    /** QR version 40 ECC-L byte capacity. */
    private static final int QR_MAX_BYTES = 2953;

    /** Maximum level in the ELITE power band. */
    private static final int MAX_ELITE_LEVEL = 15;

    /** Mid-range ELITE level. */
    private static final int MID_ELITE_LEVEL = 10;

    @BeforeClass(alwaysRun = true)
    public void enableBasicModule() {
        ModuleManager.disableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.LOST_WORLDS_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.resetModules();
    }

    // ── DataProvider: archetype combinations (specie, upbringing, faction, calling) ──

    @DataProvider(name = "highLevelArchetypes")
    public Object[][] highLevelArchetypes() {
        return new Object[][]{
                {"human",  "noble",    "decados",           "commander"},
                {"human",  "noble",    "hawkwood",          "commander"},
                {"human",  "priest",   "orthodox",          "clergy"},
                {"human",  "yeoman",   "societyOfStPaulus", "dervish"},
                {"human",  "merchant", "scravers",          "imperialCohortMerchant"},
                {"obun",   "priest",   "avestites",         "inquisitor"},
        };
    }

    // ── Payload size tests ────────────────────────────────────────────────────

    @Test(dataProvider = "highLevelArchetypes")
    public void payloadFitsInQrCodeAtLevel10(String specie, String upbringing,
                                             String faction, String calling)
            throws InvalidRandomElementSelectedException, IOException {
        final CharacterPlayer player = buildCharacter(specie, upbringing, faction, calling, MID_ELITE_LEVEL);
        final String payload = CharacterQrCodec.encode(player);
        Assert.assertTrue(payload.length() <= QR_MAX_BYTES,
                String.format("[level %d] %s/%s/%s/%s — payload %d bytes exceeds QR capacity %d",
                        MID_ELITE_LEVEL, specie, upbringing, faction, calling, payload.length(), QR_MAX_BYTES));
    }

    @Test(dataProvider = "highLevelArchetypes")
    public void payloadFitsInQrCodeAtMaxLevel(String specie, String upbringing,
                                              String faction, String calling)
            throws InvalidRandomElementSelectedException, IOException {
        final CharacterPlayer player = buildCharacter(specie, upbringing, faction, calling, MAX_ELITE_LEVEL);
        final String payload = CharacterQrCodec.encode(player);
        Assert.assertTrue(payload.length() <= QR_MAX_BYTES,
                String.format("[level %d] %s/%s/%s/%s — payload %d bytes exceeds QR capacity %d",
                        MAX_ELITE_LEVEL, specie, upbringing, faction, calling, payload.length(), QR_MAX_BYTES));
    }

    // ── Round-trip integrity at high level ───────────────────────────────────

    @Test
    public void roundTripHighLevelCommanderAtMaxLevel()
            throws InvalidRandomElementSelectedException, IOException, MaxValueExceededException {
        final CharacterPlayer original = buildCharacter("human", "noble", "decados", "commander", MAX_ELITE_LEVEL);
        final String payload = CharacterQrCodec.encode(original);
        final CharacterPlayer decoded = CharacterQrCodec.decode(payload);

        Assert.assertEquals(decoded.getLevel(), original.getLevel(), "Level must survive round-trip");
        Assert.assertEquals(decoded.getSpecie().getId(), original.getSpecie().getId(), "Specie");
        Assert.assertEquals(decoded.getUpbringing().getId(), original.getUpbringing().getId(), "Upbringing");
        Assert.assertEquals(decoded.getFaction().getId(), original.getFaction().getId(), "Faction");
        Assert.assertEquals(decoded.getCalling().getId(), original.getCalling().getId(), "Calling");
    }

    @Test
    public void roundTripFullyRandomHighLevelCharacter()
            throws InvalidRandomElementSelectedException, IOException, MaxValueExceededException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        // Fix specie to human to avoid alien characteristic caps being exceeded in the round-trip.
        characterPlayer.setSpecie("human");
        new RandomizeCharacter(characterPlayer, MAX_ELITE_LEVEL).createCharacter();

        final String payload = CharacterQrCodec.encode(characterPlayer);
        Assert.assertTrue(payload.length() <= QR_MAX_BYTES,
                "Fully random level-" + MAX_ELITE_LEVEL + " character payload " + payload.length()
                        + " exceeds QR capacity " + QR_MAX_BYTES);

        final CharacterPlayer decoded = CharacterQrCodec.decode(payload);
        Assert.assertEquals(decoded.getLevel(), characterPlayer.getLevel(), "Level round-trip");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static CharacterPlayer buildCharacter(String specie, String upbringing,
                                                  String faction, String calling, int level)
            throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie(specie);
        characterPlayer.setUpbringing(upbringing);
        characterPlayer.setFaction(faction);
        characterPlayer.setCalling(calling);
        new RandomizeCharacter(characterPlayer, level).createCharacter();
        return characterPlayer;
    }
}
