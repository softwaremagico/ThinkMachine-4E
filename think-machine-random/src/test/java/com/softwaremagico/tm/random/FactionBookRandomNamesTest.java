package com.softwaremagico.tm.random;

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
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.random.character.names.RandomName;
import com.softwaremagico.tm.random.character.names.RandomSurname;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"random"})
public class FactionBookRandomNamesTest {

    @Test
    public void randomOroymNamesUseOroymPool() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("oroym");
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("vagabonds");
        characterPlayer.getInfo().setPlanet("madoc");
        characterPlayer.getInfo().setGender(Gender.MALE);

        final RandomName randomName = new RandomName(characterPlayer, null);
        randomName.assign();

        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        final Name selectedName = characterPlayer.getInfo().getNames().get(0);
        Assert.assertEquals(selectedName.getSpecie(), "oroym");
        Assert.assertTrue(SpecieFactory.getInstance().getAllNames("oroym").contains(selectedName));
    }

    @Test
    public void randomOroymSurnamesUseOroymPool() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("oroym");
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("vagabonds");
        characterPlayer.getInfo().setPlanet("madoc");
        characterPlayer.getInfo().setGender(Gender.MALE);

        final RandomSurname randomSurname = new RandomSurname(characterPlayer, null);
        randomSurname.assign();

        final Surname selectedSurname = characterPlayer.getInfo().getSurname();
        Assert.assertNotNull(selectedSurname);
        Assert.assertEquals(selectedSurname.getSpecie(), "oroym");
        Assert.assertTrue(SpecieFactory.getInstance().getAllSurnames("oroym").contains(selectedSurname));
    }

    @Test
    public void changedNamesUseHumanPools() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("mutant");
        characterPlayer.setUpbringing("brotherBattle");
        characterPlayer.setFaction("brotherBattle");
        characterPlayer.getInfo().setPlanet("istakhr");
        characterPlayer.getInfo().setGender(Gender.FEMALE);

        final RandomName randomName = new RandomName(characterPlayer, null);
        randomName.assign();
        final RandomSurname randomSurname = new RandomSurname(characterPlayer, null);
        randomSurname.assign();

        Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
        Assert.assertTrue(characterPlayer.getInfo().getNames().get(0).getSpecie() == null
                || "human".equals(characterPlayer.getInfo().getNames().get(0).getSpecie()));
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());
        Assert.assertTrue(characterPlayer.getInfo().getSurname().getSpecie() == null
                || "human".equals(characterPlayer.getInfo().getSurname().getSpecie()));
    }
}


