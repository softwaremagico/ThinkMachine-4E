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
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "psychicCreation")
public class PsychicCreationTests {

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
