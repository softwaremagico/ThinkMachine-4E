package com.softwaremagico.tm.rules;

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
}
