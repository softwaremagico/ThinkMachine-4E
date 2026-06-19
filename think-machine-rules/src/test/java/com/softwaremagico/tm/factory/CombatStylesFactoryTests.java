package com.softwaremagico.tm.factory;

/*-
 * #%L
 * Think Machine 4E (Rules)
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
import com.softwaremagico.tm.character.combat.CombatActionRequirement;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Test(groups = {"combatStyleFactory"})
public class CombatStylesFactoryTests extends FactoryTest {

    private static final int DEFINED_STYLES = 13;
    private static final int DEFINED_ACTIONS = 37;
    private static final int DEFINED_STANCES = 12;

    private static final class TestCharacterPlayer extends CharacterPlayer {
        private final Map<String, Integer> skillValues = new HashMap<>();
        private final Map<String, Integer> characteristicValues = new HashMap<>();

        private TestCharacterPlayer withSkill(String skillId, int value) {
            skillValues.put(skillId, value);
            return this;
        }

        private TestCharacterPlayer withCharacteristic(String characteristicId, int value) {
            characteristicValues.put(characteristicId, value);
            return this;
        }

        @Override
        public int getSkillValue(Skill skill) {
            return skillValues.getOrDefault(skill.getId(), 0);
        }

        @Override
        public CombatActionRequirement getCharacteristicCombatValue(String id) {
            final Integer value = characteristicValues.get(id);
            if (value == null) {
                return null;
            }
            final CombatActionRequirement combatActionRequirement = new CombatActionRequirement();
            combatActionRequirement.setValue(value);
            return combatActionRequirement;
        }
    }


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
        Assert.assertEquals(combatActions, DEFINED_ACTIONS);
    }

    @Test
    public void readStances() throws InvalidXmlElementException {
        int combatStances = 0;
        for (final CombatStyle combatStyle : CombatStyleFactory.getInstance().getElements()) {
            combatStances += combatStyle.getCombatStances().size();
        }
        Assert.assertEquals(combatStances, DEFINED_STANCES);
    }

    @Test
    public void readBrotherBattleManeuverAndRestrictions() throws InvalidXmlElementException {
        final var brotherBattleManeuvers = CombatStyleFactory.getInstance().getElement("brotherBattleManeuvers");

        Assert.assertNotNull(brotherBattleManeuvers);
        Assert.assertEquals(brotherBattleManeuvers.getCombatActions().size(), 1);
        Assert.assertNotNull(brotherBattleManeuvers.getCombatAction("defendAlly"));
        Assert.assertTrue(brotherBattleManeuvers.getRestrictions().getRestrictedToFactions().contains("brotherBattle"));
        Assert.assertTrue(brotherBattleManeuvers.getRestrictions().getRestrictedToCallings().contains("brotherBattle"));
    }

    @Test
    public void checkSkillRestrictions() throws InvalidXmlElementException {
        final CharacterPlayer characterPlayer = new TestCharacterPlayer()
                .withSkill("melee", 6)
                .withSkill("vigor", 5);

        final CombatStyle torero = CombatStyleFactory.getInstance().getElement("torero");
        Assert.assertTrue(torero.getCombatAction("maskingStrike").isAvailable(characterPlayer));
        Assert.assertTrue(torero.getCombatAction("disarmingCloak").isAvailable(characterPlayer));
        Assert.assertFalse(torero.getCombatAction("entaglingStrike").isAvailable(characterPlayer));
    }

    @Test
    public void checkOptionalSkillRestrictions() throws InvalidXmlElementException {
        final CharacterPlayer characterPlayer = new TestCharacterPlayer()
                .withSkill("shoot", 6)
                .withSkill("vigor", 5);

        final CombatStyle pistola = CombatStyleFactory.getInstance().getElement("pistola");
        Assert.assertTrue(pistola.getCombatAction("snapShot").isAvailable(characterPlayer));
        Assert.assertTrue(pistola.getCombatAction("rollAndShoot").isAvailable(characterPlayer));
        Assert.assertFalse(pistola.getCombatAction("runAndGun").isAvailable(characterPlayer));
    }

    @Test
    public void checkOptionalSkillRestrictionsAgain() throws InvalidXmlElementException {
        final CharacterPlayer characterPlayer = new TestCharacterPlayer()
                .withSkill("shoot", 6)
                .withSkill("vigor", 5);

        final CombatStyle pistola = CombatStyleFactory.getInstance().getElement("pistola");
        Assert.assertTrue(pistola.getCombatAction("snapShot").isAvailable(characterPlayer));
        Assert.assertTrue(pistola.getCombatAction("rollAndShoot").isAvailable(characterPlayer));
        Assert.assertFalse(pistola.getCombatAction("runAndGun").isAvailable(characterPlayer));
    }

    @Test
    public void checkCharacteristicsRestrictions() throws InvalidXmlElementException {
        final CharacterPlayer characterPlayer = new TestCharacterPlayer()
                .withSkill("fight", 6)
                .withCharacteristic("faith", 6);

        final CombatStyle mantok = CombatStyleFactory.getInstance().getElement("mantok");
        Assert.assertTrue(mantok.getCombatAction("closePalmReachHeart").isAvailable(characterPlayer));
        Assert.assertTrue(mantok.getCombatAction("crossArmsDonTheRobe").isAvailable(characterPlayer));
        Assert.assertFalse(mantok.getCombatAction("strechSpineSpeakTheWord").isAvailable(characterPlayer));
    }

    @Test
    public void checkRestrictedSpecies() throws InvalidXmlElementException {
        final CombatStyle graa = CombatStyleFactory.getInstance().getElement("graa");
        Assert.assertEquals(graa.getRestrictions().getRestrictedToSpecies().size(), 1);
        Assert.assertTrue(graa.getRestrictions().getRestrictedToSpecies().contains("vorox"));
    }

    @Test
    public void checkRestrictedToCharacter() throws InvalidXmlElementException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        Assert.assertFalse(CombatStyleFactory.getInstance().getElement("graa")
                .canUseCombatStyle(characterPlayer));
    }
}
