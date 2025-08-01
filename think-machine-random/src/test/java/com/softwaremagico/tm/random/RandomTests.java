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
import com.softwaremagico.tm.random.character.RandomName;
import com.softwaremagico.tm.random.character.RandomPlanet;
import com.softwaremagico.tm.random.character.RandomSurname;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"random"})
public class RandomTests {

    @Test
    public void randomNames() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        characterPlayer.getInfo().setPlanet("sutek");
        characterPlayer.getInfo().setGender(Gender.MALE);
        final RandomName randomName = new RandomName(characterPlayer, null);
        randomName.updateWeights();
        randomName.assign();
        Assert.assertNotNull(characterPlayer.getInfo().getNames());
    }

    @Test
    public void randomSurnames() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        characterPlayer.getInfo().setPlanet("sutek");
        final RandomSurname randomSurname = new RandomSurname(characterPlayer, null);
        randomSurname.updateWeights();
        randomSurname.assign();
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());
    }


    @Test
    public void randomPlanet() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        final RandomPlanet randomSurname = new RandomPlanet(characterPlayer, null);
        randomSurname.updateWeights();
        randomSurname.assign();
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
    }
}
