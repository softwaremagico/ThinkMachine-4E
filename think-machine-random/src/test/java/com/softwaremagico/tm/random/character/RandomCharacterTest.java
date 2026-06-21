package com.softwaremagico.tm.random.character;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
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
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.log.RandomTestGenerationLog;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = {"randomCharacter"})
public class RandomCharacterTest {

    @DataProvider(name = "characterScenarios")
    public Object[][] characterScenarios() {
        return new Object[][]{
                {"createBrotherBattleCharacterTest", "human", "nowhere", Gender.FEMALE, "brotherBattle",
                        "brotherBattle", null, false, null, false, null, null, null, null},
                {"createNobleCharacterTest", "human", "nowhere", Gender.FEMALE, "noble",
                        "societyOfStPaulus", null, false, null, true, null, null, null, null},
                {"createPriestCharacterTest", "human", "nowhere", Gender.MALE, "priest",
                        "societyOfStPaulus", null, false, null, false, null, null, null, null},
                {"createDervishCharacterTest", "human", "nowhere", Gender.FEMALE, "noble",
                        "liHalan", "dervish", false, null, false, null, null, null, null},
                {"createDispossessedCharacterTest", "human", "nowhere", Gender.FEMALE, "yeoman",
                        "theDispossessed", null, false, null, false, null, null, null, null},
                {"createTheurgistCharacterTest", "human", "nowhere", Gender.MALE, "priest",
                        "eskatonic", "theurgist", false, null, false, null, null, null, null},
                {"createPirateCharacterTest", "human", "nowhere", Gender.MALE, "yeoman",
                        "far", "pirate", false, null, false, null, null, null, null},
                {"createBountyHunterCharacterTest", "human", "nowhere", Gender.MALE, "merchant",
                        "reeves", "bountyHunter", false, null, false, null, null, null, null},
                {"createDuelistCharacterTest", "human", "nowhere", Gender.MALE, "noble",
                        "hawkwood", "pistolDuelist", false, null, false, null, null, null, null},
                {"createScholarCharacterTest", "human", "nowhere", Gender.MALE, "yeoman",
                        "theDispossessed", "scholar", false, null, false, null, null, null, null},
                {"createVoroxCharacterTest", "vorox", "ungavorox", Gender.MALE, "merchant",
                        "musters", "scout", false, null, false, null, null, null, null},
                {"createOccultistCharacterTest", "human", "nowhere", Gender.MALE, "priest",
                        "eskatonic", "occultist", false, null, false, null, null, null, null},
                {"createConfessorCharacterTest", "human", "nowhere", Gender.MALE, "priest",
                        "orthodox", "confessor", false, null, false, null, null, null, null},
                {"createChainerCharacterTest", "human", "nowhere", Gender.MALE, "merchant",
                        "societyOfStPaulus", "chainer", false, null, false, null, null, null, null},
                {"createDreamtenderCharacterTest", "human", "nowhere", Gender.MALE, "priest",
                        "avestites", "dreamtender", false, null, false, null, null, null, null},
                {"createReclaimerCharacterTest", "human", "nowhere", Gender.FEMALE, "merchant",
                        "societyOfStPaulus", "reclaimer", false, null, false, null, null, null, null},
                {"selectCallingForNoble", "human", "nowhere", Gender.FEMALE, "noble",
                        "hazat", null, true, null, false, null, null, null, null},
                {"selectPriestCombination", "human", "nowhere", Gender.FEMALE, "priest",
                        "orthodox", "explorer", true, null, false, null, null, null, null},
                {"selectVuldrokCombination", "human", "nowhere", Gender.FEMALE, "yeoman",
                        "vuldrok", null, true, null, false, null, null, null, null},
                {"selectVagabondCombination", "human", "nowhere", Gender.FEMALE, "yeoman",
                        "vagabonds", "amateur", true, null, false, null, null, null, null},
                {"selectAmateurCombination", "human", "nowhere", Gender.FEMALE, "merchant",
                        "charioteers", "amateur", true, null, false, null, null, null, null},
                {"merchantReevesFactotumTest", "human", "nowhere", Gender.MALE, "merchant",
                        "reeves", "factotum", false, "factotum", false, null, null, null, null},
                {"alMalikCommanderReassignPresence", "human", null, null, "noble",
                        "alMalik", "commander", false, null, false, "presence", "wits", 1, null},
                {"priestOrthodoxChoristerBalancePerform", "human", null, null, "priest",
                        "orthodox", "chorister", false, null, false, null, null, null, 2},
                {"yeomanSocietyOfStPaulusExplorerPerform", "human", null, null, "yeoman",
                        "societyOfStPaulus", "explorer", false, null, false, null, null, null, null}
        };
    }

    @Test(dataProvider = "characterScenarios")
    public void validateCharacterScenario(String scenario,
                                          String specie,
                                          String planet,
                                          Gender gender,
                                          String upbringing,
                                          String faction,
                                          String calling,
                                          boolean assertCallingNotNull,
                                          String expectedCallingId,
                                          boolean logValidationError,
                                          String primaryCharacteristic,
                                          String secondaryCharacteristic,
                                          Integer expectedCharacteristicReassigns,
                                          Integer expectedSkillReassigns)
            throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie(specie);
        if (planet != null) {
            characterPlayer.getInfo().setPlanet(planet);
        }
        if (gender != null) {
            characterPlayer.getInfo().setGender(gender);
        }
        characterPlayer.setUpbringing(upbringing);
        characterPlayer.setFaction(faction);
        if (calling != null) {
            characterPlayer.setCalling(calling);
        }
        if (primaryCharacteristic != null) {
            characterPlayer.setPrimaryCharacteristic(primaryCharacteristic);
        }
        if (secondaryCharacteristic != null) {
            characterPlayer.setSecondaryCharacteristic(secondaryCharacteristic);
        }

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie(), scenario + ": specie must not be null");
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic(),
                scenario + ": primary characteristic must not be null");
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic(),
                scenario + ": secondary characteristic must not be null");
        Assert.assertNotNull(characterPlayer.getFaction(), scenario + ": faction must not be null");
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet(), scenario + ": planet must not be null");
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty(), scenario + ": names must not be empty");
        Assert.assertNotNull(characterPlayer.getInfo().getSurname(), scenario + ": surname must not be null");

        if (assertCallingNotNull) {
            Assert.assertNotNull(characterPlayer.getCalling(), scenario + ": calling must not be null");
        }
        if (expectedCallingId != null) {
            Assert.assertNotNull(characterPlayer.getCalling(), scenario + ": calling must not be null");
            Assert.assertEquals(characterPlayer.getCalling().getId(), expectedCallingId,
                    scenario + ": unexpected calling id");
        }

        if (logValidationError) {
            try {
                characterPlayer.validate();
            } catch (Exception e) {
                RandomTestGenerationLog.errorMessage(this.getClass(), e);
                throw e;
            }
        } else {
            characterPlayer.validate();
        }

        if (expectedCharacteristicReassigns != null) {
            Assert.assertEquals(characterPlayer.getCharacteristicReassigns().size(),
                    expectedCharacteristicReassigns.intValue(),
                    scenario + ": unexpected characteristic reassign count");
        }
        if (expectedSkillReassigns != null) {
            Assert.assertEquals(characterPlayer.getSkillsReassigns().size(), expectedSkillReassigns.intValue(),
                    scenario + ": unexpected skill reassign count");
        }
    }
}
