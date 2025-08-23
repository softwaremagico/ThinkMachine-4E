package com.softwaremagico.tm.rules;

/*-
 * #%L
 * Think Machine 4E (Rules)
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
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.exceptions.InvalidSelectedElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "perks")
public class PerksTests {

    @Test(expectedExceptions = InvalidSelectedElementException.class, enabled = false)
    public void epiphanyWithoutCapabilitiesGroup() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("monkScholarlyTradition");

        //We cannot add epiphany. (We can always add epiphany as the user always has factionLore).
        Assert.assertTrue(PerkFactory.getInstance().getElement("epiphany").getRestrictions().isRestricted(characterPlayer));
        characterPlayer.getCalling().getSelectedPerks().add(new Selection("epiphany"));

        characterPlayer.getCalling().validate();
    }

    @Test
    public void epiphanyWithCapabilitiesGroup() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("monkScholarlyTradition");

        //Add one lore that is needed by Epiphany
        characterPlayer.getCalling().getSelectedCapabilities().add(new Selection("diseaseLore"));

        //Now we can add epiphany
        Assert.assertFalse(PerkFactory.getInstance().getElement("epiphany").getRestrictions().isRestricted(characterPlayer));
        characterPlayer.getCalling().getSelectedPerks().add(new Selection("epiphany"));

        characterPlayer.getCalling().validate();
    }

    @Test
    public void gossipNetworkWithoutCapabilitiesGroup() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("monkScholarlyTradition");

        //We cannot add epiphany. Needs Customs Lore.
        Assert.assertTrue(PerkFactory.getInstance().getElement("gossipNetwork").getRestrictions().isRestricted(characterPlayer));
        characterPlayer.getCalling().getSelectedPerks().add(new Selection("gossipNetwork"));
    }

    @Test
    public void gossipNetworkWithCapabilitiesGroup() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("monkScholarlyTradition");

        //Add one lore that is needed by Epiphany
        characterPlayer.getCalling().getSelectedCapabilityOptions().get(0).getSelections().add(new Selection("cathedralCustoms"));

        //Now we can add gossipNetwork
        Assert.assertFalse(PerkFactory.getInstance().getElement("gossipNetwork").getRestrictions().isRestricted(characterPlayer));
        characterPlayer.getCalling().getSelectedPerks().add(new Selection("gossipNetwork"));
    }

    @Test
    public void investPhylacteryWithoutCharacteristic() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("theurgist");

        //Now we can add investPhylactery
        Assert.assertTrue(PerkFactory.getInstance().getElement("investPhylactery").getRestrictions().isRestricted(characterPlayer));
        characterPlayer.getCalling().getSelectedPerks().add(new Selection("investPhylactery"));
    }

    @Test(enabled = false)
    public void investPhylacteryWithCharacteristic() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("theurgist");

        // TODO(softwaremagico): missing level to set theurgy to 5.

        //Now we can add investPhylactery
        Assert.assertFalse(PerkFactory.getInstance().getElement("investPhylactery").getRestrictions().isRestricted(characterPlayer));
        characterPlayer.getCalling().getSelectedPerks().add(new Selection("investPhylactery"));
    }

    @Test(enabled = false)
    public void masterOfDeceptionWithoutSkill() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        characterPlayer.setCalling("conspiracist");

        // conspiracist always have knavery to 5.

        //Now we can add masterOfDeception
        Assert.assertTrue(PerkFactory.getInstance().getElement("masterOfDeception").getRestrictions().isRestricted(characterPlayer));
        characterPlayer.getCalling().getSelectedPerks().add(new Selection("masterOfDeception"));
    }

    @Test
    public void masterOfDeceptionWithSkill() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("liHalan");
        characterPlayer.setCalling("conspiracist");

        //Now we can add masterOfDeception
        Assert.assertFalse(PerkFactory.getInstance().getElement("masterOfDeception").getRestrictions().isRestricted(characterPlayer));
        characterPlayer.getCalling().getSelectedPerks().add(new Selection("masterOfDeception"));
    }

    @Test
    public void testAnyRequirement() {
        CharacterPlayer characterPlayer1 = new CharacterPlayer();
        characterPlayer1.setSpecie("human");
        characterPlayer1.setUpbringing("priest");

        //Now we can add invigorate
        Assert.assertFalse(PerkFactory.getInstance().getElement("invigorate").getRestrictions().isRestricted(characterPlayer1));

        CharacterPlayer characterPlayer2 = new CharacterPlayer();
        characterPlayer2.setSpecie("human");
        characterPlayer2.setUpbringing("noble");
        characterPlayer2.setFaction("alMalik");
        characterPlayer2.setCalling("commander");

        //Now we can add invigorate
        Assert.assertFalse(PerkFactory.getInstance().getElement("invigorate").getRestrictions().isRestricted(characterPlayer2));

        CharacterPlayer characterPlayer3 = new CharacterPlayer();
        characterPlayer3.setSpecie("human");
        characterPlayer3.setUpbringing("noble");
        characterPlayer3.setFaction("alMalik");

        //We cannot add invigorate
        Assert.assertTrue(PerkFactory.getInstance().getElement("invigorate").getRestrictions().isRestricted(characterPlayer3));
    }
}
