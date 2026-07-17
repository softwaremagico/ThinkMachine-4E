package com.softwaremagico.tm.rules;

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
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.values.Bonification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Test(groups = "cyberdeviceBonificationsRules")
public class CyberdeviceBonificationsTests extends RulesTest {

    private static final class TestCharacterPlayer extends CharacterPlayer {
        private final List<Cyberdevice> cyberdevices;

        private TestCharacterPlayer(List<Cyberdevice> cyberdevices) {
            this.cyberdevices = cyberdevices;
        }

        @Override
        public List<Cyberdevice> getCyberdevices() {
            return cyberdevices;
        }
    }

    @Test
    public void characteristicValueShouldIncludeAlwaysCyberdeviceBonifications() {
        final TestCharacterPlayer baseCharacter = new TestCharacterPlayer(List.of());
        final TestCharacterPlayer characterWithCyberdevices = new TestCharacterPlayer(List.of(
                cyberdeviceWithBonifications(alwaysBonification("strength", 2)),
                cyberdeviceWithBonifications(alwaysBonification("strength", 1), situationBonification("strength", 9, "only in darkness"))
        ));

        final int baseStrength = baseCharacter.getCharacteristicValue(CharacteristicName.STRENGTH);
        final int modifiedStrength = characterWithCyberdevices.getCharacteristicValue(CharacteristicName.STRENGTH);

        Assert.assertEquals(modifiedStrength, baseStrength + 3);
    }

    @Test
    public void skillValueShouldIncludeAlwaysCyberdeviceBonifications() {
        final String targetSkill = "vigor";
        final TestCharacterPlayer baseCharacter = new TestCharacterPlayer(List.of());
        final TestCharacterPlayer characterWithCyberdevices = new TestCharacterPlayer(List.of(
                cyberdeviceWithBonifications(alwaysBonification(targetSkill, 2)),
                cyberdeviceWithBonifications(situationBonification(targetSkill, 4, "while sprinting"))
        ));

        final int baseSkill = baseCharacter.getSkillValue(targetSkill);
        final int modifiedSkill = characterWithCyberdevices.getSkillValue(targetSkill);

        Assert.assertEquals(modifiedSkill, baseSkill + 2);
    }

    private Cyberdevice cyberdeviceWithBonifications(Bonification... bonifications) {
        final Cyberdevice cyberdevice = new Cyberdevice();
        cyberdevice.setId("testCyberdevice" + bonifications.length + "_" + System.nanoTime());
        cyberdevice.setTechLevel(4);
        cyberdevice.setTechCompulsion("inerrant");
        cyberdevice.setBonifications(Set.copyOf(new ArrayList<>(List.of(bonifications))));
        return cyberdevice;
    }

    private Bonification alwaysBonification(String affects, int value) {
        final Bonification bonification = new Bonification();
        bonification.setValue(value);
        try {
            bonification.setAffects(CharacteristicsDefinitionFactory.getInstance().getElement(affects));
        } catch (Exception e) {
            bonification.setAffects(SkillFactory.getInstance().getElement(affects));
        }
        bonification.setSituation(null);
        return bonification;
    }

    private Bonification situationBonification(String affects, int value, String situation) {
        final Bonification bonification = alwaysBonification(affects, value);
        bonification.setSituation(situation);
        return bonification;
    }
}
