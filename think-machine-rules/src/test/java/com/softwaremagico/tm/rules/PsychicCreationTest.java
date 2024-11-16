package com.softwaremagico.tm.rules;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "psychicCreation")
public class PsychicCreationTest {

    @Test
    public void psychicPowersNeedsPsychicCalling() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("alMalik");
        characterPlayer.setCalling("commander");
        Assert.assertTrue(PerkFactory.getInstance().getElement("psychicPowers").getRestrictions().isRestricted(characterPlayer));
    }

    @Test
    public void psiLevel() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("societyOfStPaulus");
        characterPlayer.setCalling("psychic");
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.PSI), 2);
    }


    @Test
    public void ukarCanLearnPsychicPowers() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("ukar");
        Assert.assertEquals(characterPlayer.getCharacteristicValue(CharacteristicName.PSI), 1);
        Assert.assertFalse(PerkFactory.getInstance().getElement("psychicPowers").getRestrictions().isRestricted(characterPlayer));
    }

    @Test
    public void obunCanLearnOpenTheurgyRites() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        Assert.assertFalse(OccultismPathFactory.getInstance().getElement("ecumenicalRituals").getRestrictions().isRestricted(characterPlayer));
    }

    @Test
    public void obunnotCanLearnOpenTheurgyRites() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        Assert.assertTrue(OccultismPathFactory.getInstance().getElement("ecumenicalRituals").getRestrictions().isRestricted(characterPlayer));
    }

    @Test
    public void occultismPathCannotBeAddedWithoutPerks() {
        Assert.fail();
    }


    @Test
    public void perksGivesOccultismPowerLevels() {
        Assert.fail();
    }

    @Test
    public void perksGivesTheurgyRitesLevels() {
        Assert.fail();
    }

    @Test
    public void priestWithoutTheurgistCallingCannotHaveRites() {
        Assert.fail();
    }


    @Test
    public void dervishCalling() {
        Assert.fail();
    }
}
