package com.softwaremagico.tm.random.character;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
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
import com.softwaremagico.tm.exceptions.MaxValueExceededException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.log.RandomTestGenerationLog;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"randomCharacter"})
public class RandomCharacterTest {
    private static final int CHARACTERS_CREATED = 100;

    @Test
    public void createFullRandomCharacterTest() throws InvalidRandomElementSelectedException {
        for (int i = 0; i < CHARACTERS_CREATED; i++) {
            final CharacterPlayer characterPlayer = new CharacterPlayer();
            final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
            randomizeCharacter.createCharacter();

            Assert.assertNotNull(characterPlayer.getSpecie());
            Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
            Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
            Assert.assertNotNull(characterPlayer.getFaction());
            Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
            Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
            Assert.assertNotNull(characterPlayer.getInfo().getSurname());

            try {
                characterPlayer.validate();
            } catch (Exception e) {
                RandomTestGenerationLog.severe(this.getClass(), "Error on character: " + characterPlayer);
                RandomTestGenerationLog.errorMessage(this.getClass(), e);
                throw e;
            }
        }
    }


    @Test
    public void createBrotherBattleCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("brotherBattle");
        characterPlayer.setFaction("brotherBattle");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());
    }


    @Test
    public void createNobleCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("societyOfStPaulus");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        try {
            characterPlayer.validate();
        } catch (Exception e) {
            RandomTestGenerationLog.errorMessage(this.getClass(), e);
            throw e;
        }
    }

    @Test
    public void createPriestCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("societyOfStPaulus");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }


    @Test
    public void createDervishCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("liHalan");
        characterPlayer.setCalling("dervish");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }


    @Test
    public void createDispossessedCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("theDispossessed");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }


    @Test
    public void createTheurgistCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("eskatonic");
        characterPlayer.setCalling("theurgist");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }


    @Test
    public void createPirateCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("far");
        characterPlayer.setCalling("pirate");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void createBountyHunterCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("reeves");
        characterPlayer.setCalling("bountyHunter");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void createDuelistCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("hawkwood");
        characterPlayer.setCalling("pistolDuelist");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void createScholarCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("theDispossessed");
        characterPlayer.setCalling("scholar");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void createVoroxCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("vorox");
        characterPlayer.getInfo().setPlanet("ungavorox");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("musters");
        characterPlayer.setCalling("scout");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void createOccultistCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("eskatonic");
        characterPlayer.setCalling("occultist");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void createConfessorCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("orthodox");
        characterPlayer.setCalling("confessor");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void createChainerCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("societyOfStPaulus");
        characterPlayer.setCalling("chainer");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void createDreamtenderCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("dreamtender");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void createReclaimerCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("societyOfStPaulus");
        characterPlayer.setCalling("reclaimer");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void selectCallingForNoble() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("hazat");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertNotNull(characterPlayer.getCalling());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void selectPriestCombination() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("orthodox");
        characterPlayer.setCalling("explorer");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertNotNull(characterPlayer.getCalling());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void selectVuldrokCombination() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("vuldrok");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertNotNull(characterPlayer.getCalling());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void selectVagabondCombination() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("vagabonds");
        characterPlayer.setCalling("amateur");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertNotNull(characterPlayer.getCalling());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void selectAmateurCombination() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("charioteers");
        characterPlayer.setCalling("amateur");
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertNotNull(characterPlayer.getCalling());
        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());

        characterPlayer.validate();
    }

    @Test
    public void alMalikCommanderReassignPresence() throws InvalidRandomElementSelectedException {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        characterPlayer.setCalling("commander");

        characterPlayer.setPrimaryCharacteristic("presence");
        characterPlayer.setSecondaryCharacteristic("wits");

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        characterPlayer.getCharacteristicComposition("presence");

        characterPlayer.validate();

        //1 points from presence reassigned to different characteristics.
        Assert.assertEquals(characterPlayer.getCharacteristicReassigns().size(), 1);
    }
}
