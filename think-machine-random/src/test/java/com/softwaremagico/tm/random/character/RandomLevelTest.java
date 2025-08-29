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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.random.character.level.RandomLevel;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.stream.Collectors;

@Test(groups = {"randomLevel"})
public class RandomLevelTest {
    private static final int LEVEL_TEST = 10;

    private static final int CHARACTERS_CREATED = 100;

    @Test
    public void testRandomLevel() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        final RandomLevel randomLevel = new RandomLevel(characterPlayer, null);
        randomLevel.assign();
        randomLevel.complete();
        characterPlayer.getLevels().forEach(LevelSelector::validate);
    }

    @Test
    public void createFullRandomCharacterTest() throws InvalidRandomElementSelectedException {
        for (int i = 0; i < CHARACTERS_CREATED; i++) {
            final CharacterPlayer characterPlayer = new CharacterPlayer();
            final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, LEVEL_TEST);
            randomizeCharacter.createCharacter();

            Assert.assertEquals(characterPlayer.getLevel(), LEVEL_TEST);
            characterPlayer.validate();
        }
    }

    @Test
    public void createHawkwoodCommanderRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("hawkwood");
        characterPlayer.setCalling("commander");

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, LEVEL_TEST);
        randomizeCharacter.createCharacter();

        Assert.assertEquals(characterPlayer.getLevel(), LEVEL_TEST);
        characterPlayer.validate();
    }

    @Test
    public void createScraverImperialCohortMerchantRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("scravers");
        characterPlayer.setCalling("imperialCohortMerchant");

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, LEVEL_TEST);
        randomizeCharacter.createCharacter();

        Assert.assertEquals(characterPlayer.getLevel(), LEVEL_TEST);
        characterPlayer.validate();
    }

    @Test
    public void createObunPriestRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("inquisitor");

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, LEVEL_TEST);
        randomizeCharacter.createCharacter();

        Assert.assertEquals(characterPlayer.getLevel(), LEVEL_TEST);
        characterPlayer.validate();
    }

    @Test
    public void createDervishRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("societyOfStPaulus");
        characterPlayer.setCalling("dervish");

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, LEVEL_TEST);
        randomizeCharacter.createCharacter();

        Assert.assertEquals(characterPlayer.getLevel(), LEVEL_TEST);
        characterPlayer.validate();
    }

    @Test(enabled = false)
    public void createTwoRanksPerksRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("orthodox");
        characterPlayer.setCalling("clergy");

        //If I select Novitiate, on orthodox must add the next rank
        characterPlayer.getUpbringing().getSelectedPerksOptions().get(1).getSelections().add(new Selection("churchOrdinationNovitiate"));

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertTrue(characterPlayer.getPerks().stream().map(Element::getId).collect(Collectors.toSet())
                .contains("churchOrdinationCanon"));
        characterPlayer.validate();
    }
}
