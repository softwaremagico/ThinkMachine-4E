package com.softwaremagico.tm.rules;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2024 Softwaremagico
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
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidCallingException;
import com.softwaremagico.tm.exceptions.InvalidFactionException;
import com.softwaremagico.tm.exceptions.InvalidUpbringingException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "characterCreation")
public class CharacterCreationTest {


    @Test(expectedExceptions = InvalidUpbringingException.class)
    public void invalidUpbringing() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("ukar");
        characterPlayer.setUpbringing("noble");
    }

    public void validUpbringing() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
    }

    @Test(expectedExceptions = InvalidFactionException.class)
    public void invalidFaction() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("brotherBattle");
    }

    public void validFaction() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
    }

    @Test(expectedExceptions = InvalidCallingException.class)
    public void invalidCalling() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("brotherBattle");
        characterPlayer.setCalling("commander");
    }

    @Test
    public void validCalling() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        characterPlayer.setCalling("commander");
    }

    @Test
    public void characterCreationCharacteristics() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");

        characterPlayer.setUpbringing("noble");
        final Upbringing upbringing = UpbringingFactory.getInstance().getElement("noble");
        for (int i = 0; i < upbringing.getCharacteristicOptions().size(); i++) {
            for (int j = 0; j < upbringing.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getCharacteristicOptions().get(i).getSelections()
                        .add(upbringing.getCharacteristicOptions().get(i).getCharacteristics().get(j).getCharacteristic());
            }
        }

        characterPlayer.setFaction("alMalik");
        final Faction faction = FactionFactory.getInstance().getElement("alMalik");
        for (int i = 0; i < faction.getCharacteristicOptions().size(); i++) {
            for (int j = 0; j < faction.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getCharacteristicOptions().get(i).getSelections()
                        .add(faction.getCharacteristicOptions().get(i).getCharacteristics().get(j).getCharacteristic());
            }
        }


        characterPlayer.setCalling("commander");
        final Calling calling = CallingFactory.getInstance().getElement("commander");
        for (int i = 0; i < calling.getCharacteristicOptions().size(); i++) {
            for (int j = 0; j < calling.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getCharacteristicOptions().get(i).getSelections()
                        .add(calling.getCharacteristicOptions().get(i).getCharacteristics().get(j).getCharacteristic());
            }
        }

        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.STRENGTH), 3);
        //+1 Upbringing, +1 faction,
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.DEXTERITY), 5);
        //+1 calling
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.ENDURANCE), 4);
        //+1 Upbringing, +1 faction, +2 calling
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.WITS), 7);
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.PERCEPTION), 3);
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.WILL), 3);
        //+2 Upbringing, +2 faction, +2 calling
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.PRESENCE), 8);
        //+1 faction, +1 calling
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.INTUITION), 5);
        //+1 Upbringing,
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.FAITH), 4);
    }
}
