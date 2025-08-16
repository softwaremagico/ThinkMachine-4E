package com.softwaremagico.tm.random;

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
import com.softwaremagico.tm.random.character.RandomizeCharacter;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"randomCharacter"})
public class RandomCharacterTest {

    @Test
    public void createFullRandomCharacterTest() throws InvalidRandomElementSelectedException {
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
}
