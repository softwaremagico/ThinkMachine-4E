package com.softwaremagico.tm.factory;

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


import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"combatStyleFactory"})
public class CombatStylesFactoryTests {
    private static final int DEFINED_STYLES = 12;
    private static final int DEFINED_ACTIONS = DEFINED_STYLES * 3;


    @Test
    public void readCombatStyles() throws InvalidXmlElementException {
        Assert.assertEquals(CombatStyleFactory.getInstance().getElements().size(), DEFINED_STYLES);
    }

    @Test
    public void readCombatActions() throws InvalidXmlElementException {
        Assert.assertEquals(CombatStyleFactory.getInstance()
                .getElement("graa").getCombatActions().size(), 3);
    }

    @Test
    public void readAllCombatActions() throws InvalidXmlElementException {
        int combatActions = 0;
        for (final CombatStyle combatStyle : CombatStyleFactory.getInstance().getElements()) {
            combatActions += combatStyle.getCombatActions().size();
        }
        Assert.assertEquals(DEFINED_ACTIONS, combatActions);
    }

    @Test
    public void readStances() throws InvalidXmlElementException {
        int combatStances = 0;
        for (final CombatStyle combatStyle : CombatStyleFactory.getInstance().getElements()) {
            combatStances += combatStyle.getCombatStances().size();
        }
        // One stance by style.
        Assert.assertEquals(DEFINED_STYLES, combatStances);
    }

//    @Test
//    public void checkSkillRestrictions() throws InvalidXmlElementException {
//        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
//        characterPlayer.setSkillRank(
//                AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER),
//                6);
//        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("athletics", LANGUAGE,
//                PathManager.DEFAULT_MODULE_FOLDER), 5);
//
//        final CombatStyle torero = CombatStyleFactory.getInstance().getElement("torero", LANGUAGE,
//                PathManager.DEFAULT_MODULE_FOLDER);
//        Assert.assertTrue(torero.getCombatAction("maskingStrike").isAvailable(characterPlayer));
//        Assert.assertTrue(torero.getCombatAction("disarmingCloak").isAvailable(characterPlayer));
//        Assert.assertFalse(torero.getCombatAction("entaglingStrike").isAvailable(characterPlayer));
//    }
//
//    @Test
//    public void checkOptionalSkillRestrictions() throws InvalidXmlElementException, InvalidRanksException, RestrictedElementException,
//            UnofficialElementNotAllowedException {
//        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
//        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("slugGuns", LANGUAGE,
//                PathManager.DEFAULT_MODULE_FOLDER), 6);
//        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("athletics", LANGUAGE,
//                PathManager.DEFAULT_MODULE_FOLDER), 5);
//
//        final CombatStyle pistola = CombatStyleFactory.getInstance().getElement("pistola", LANGUAGE,
//                PathManager.DEFAULT_MODULE_FOLDER);
//        Assert.assertTrue(pistola.getCombatAction("snapShot").isAvailable(characterPlayer));
//        Assert.assertTrue(pistola.getCombatAction("rollAndShoot").isAvailable(characterPlayer));
//        Assert.assertFalse(pistola.getCombatAction("runAndGun").isAvailable(characterPlayer));
//    }
//
//    @Test
//    public void checkOptionalSkillRestrictionsAgain() throws InvalidXmlElementException, InvalidRanksException, RestrictedElementException,
//            UnofficialElementNotAllowedException {
//        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
//        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE,
//                PathManager.DEFAULT_MODULE_FOLDER), 6);
//        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("athletics", LANGUAGE,
//                PathManager.DEFAULT_MODULE_FOLDER), 5);
//
//        final CombatStyle pistola = CombatStyleFactory.getInstance().getElement("pistola", LANGUAGE,
//                PathManager.DEFAULT_MODULE_FOLDER);
//        Assert.assertTrue(pistola.getCombatAction("snapShot").isAvailable(characterPlayer));
//        Assert.assertTrue(pistola.getCombatAction("rollAndShoot").isAvailable(characterPlayer));
//        Assert.assertFalse(pistola.getCombatAction("runAndGun").isAvailable(characterPlayer));
//    }
//
//    @Test
//    public void checkCharacteristicsRestrictions() throws InvalidXmlElementException, InvalidRanksException, RestrictedElementException,
//            UnofficialElementNotAllowedException {
//        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
//        characterPlayer.setSkillRank(
//                AvailableSkillsFactory.getInstance().getElement("fight", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER),
//                6);
//        characterPlayer.setCharacteristic(CharacteristicName.FAITH, 6);
//
//        final CombatStyle mantok = CombatStyleFactory.getInstance().getElement("mantok", LANGUAGE,
//                PathManager.DEFAULT_MODULE_FOLDER);
//        Assert.assertTrue(mantok.getCombatAction("closePalmReachHeart").isAvailable(characterPlayer));
//        Assert.assertTrue(mantok.getCombatAction("crossArmsDonTheRobe").isAvailable(characterPlayer));
//        Assert.assertFalse(mantok.getCombatAction("strechSpineSpeakTheWord").isAvailable(characterPlayer));
//    }
//
//    @Test
//    public void checkRestrictedSpecies() throws InvalidXmlElementException {
//        final CombatStyle graa = CombatStyleFactory.getInstance().getElement("graa", LANGUAGE,
//                PathManager.DEFAULT_MODULE_FOLDER);
//        Assert.assertEquals(RaceFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER),
//                graa.getRestrictedToSpecies().iterator().next());
//    }
//
//    @Test
//    public void checkRestrictedRToCharacter() throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
//        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
//        characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
//        Assert.assertFalse(CombatStyleFactory.getInstance().getElement("graa", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
//                .canUseCombatStyle(characterPlayer));
//    }
}
