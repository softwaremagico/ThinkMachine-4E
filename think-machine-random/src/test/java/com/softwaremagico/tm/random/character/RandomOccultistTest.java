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
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"randomOccultism"})
public class RandomOccultistTest {

    @Test
    public void ensureOneOccultismPowerTheurgyIfPerkIsSelected() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("brotherBattle");
        characterPlayer.setFaction("brotherBattle");
        characterPlayer.setCalling("brotherBattle");

        characterPlayer.getCalling().getSelectedPerksOptions().get(0).getSelections().add(new Selection(PerkFactory.getInstance().getElement(PerkFactory.THEURGY_RITES_PERK)));


        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getCharacteristicValue(CharacteristicName.THEURGY) > 0);
        Assert.assertFalse(characterPlayer.getAllSelectedPowers().isEmpty());
    }

    @Test
    public void ensureOneOccultismPowerPsiIfPerkIsSelected() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("liHalan");
        characterPlayer.setCalling("dervish");

        characterPlayer.getCalling().getSelectedPerksOptions().get(0).getSelections().add(new Selection(PerkFactory.getInstance().getElement(PerkFactory.PSYCHIC_POWERS_PERK)));


        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getCharacteristicValue(CharacteristicName.PSI) > 0);
        Assert.assertFalse(characterPlayer.getAllSelectedPowers().isEmpty());
    }
}
