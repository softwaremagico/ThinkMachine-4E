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

import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.thinkmachines.ThinkMachineFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.skills.SkillFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.stream.Stream;

@Test(groups = "freeOptionsSelectionTest")
public class FreeOptionsSelectionTests extends RulesTest {

    @Test
    public void spyHasFreeSkillsOptions() {
        Assert.assertEquals(CallingFactory.getInstance().getElement("spy").getSkillOptions().get(3).getOptions().size(),
                SkillFactory.getInstance().getElements().size());
    }

    @Test
    public void spyHasFreeMaterialAwardsOptions() {
        final long expectedOpenEquipment = Stream.of(
                        ItemFactory.getInstance().getElements().stream(),
                        WeaponFactory.getInstance().getElements().stream(),
                        ArmorFactory.getInstance().getElements().stream(),
                        ShieldFactory.getInstance().getElements().stream(),
                        HandheldShieldFactory.getInstance().getElements().stream(),
                        ThinkMachineFactory.getInstance().getElements().stream())
                .flatMap(s -> s)
                .filter(Equipment::isOpenEquipment)
                .count();

        Assert.assertEquals(CallingFactory.getInstance().getElement("spy").getMaterialAwards().get(0).getOptions().size(),
                expectedOpenEquipment);
    }
}
