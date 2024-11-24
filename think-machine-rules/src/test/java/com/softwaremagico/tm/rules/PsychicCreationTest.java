package com.softwaremagico.tm.rules;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Selection;
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
        characterPlayer.setSpecie("ukar");
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
    public void humanCannotLearnOpenTheurgyRites() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        Assert.assertTrue(OccultismPathFactory.getInstance().getElement("ecumenicalRituals").getRestrictions().isRestricted(characterPlayer));
    }

    @Test
    public void occultismPathCannotBeAddedWithoutPerks() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("societyOfStPaulus");
        characterPlayer.setCalling("psychic");
        Assert.assertFalse(characterPlayer.canAddOccultismPower(OccultismPathFactory.getInstance().getElement("farHand").getOccultismPowers().get("liftingHand")));
    }


    @Test
    public void perksGivesOccultismPowerLevels() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("societyOfStPaulus");
        characterPlayer.setCalling("psychic");
        characterPlayer.getCalling().getPerksOptions().get(0).getSelections().add(new Selection("psychicPowers"));
        Assert.assertTrue(characterPlayer.canAddOccultismPower(OccultismPathFactory.getInstance().getElement("farHand").getOccultismPowers().get("liftingHand")));
    }

    @Test
    public void priestWithoutTheurgistCallingCannotHaveRites() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("spy");
        Assert.assertFalse(characterPlayer.canAddOccultismPower(OccultismPathFactory.getInstance().getElement("templeAvestiRituals").getOccultismPowers().get("knowingTheFalseHeart")));
    }

    @Test
    public void perksGivesTheurgyRitesLevels() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("theurgist");
        characterPlayer.getCalling().getPerksOptions().get(0).getSelections().add(new Selection("theurgicRites"));
        Assert.assertTrue(characterPlayer.canAddOccultismPower(OccultismPathFactory.getInstance().getElement("templeAvestiRituals").getOccultismPowers().get("knowingTheFalseHeart")));
    }


    @Test
    public void dervishCalling() {
        CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("hazat");
        characterPlayer.setCalling("dervish");
        characterPlayer.getCalling().getPerksOptions().get(0).getSelections().add(new Selection("psychicPowers"));
        Assert.assertTrue(characterPlayer.canAddOccultismPower(OccultismPathFactory.getInstance().getElement("farHand").getOccultismPowers().get("liftingHand")));
    }
}
