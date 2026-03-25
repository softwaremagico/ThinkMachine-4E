package com.softwaremagico.tm.random;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
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

import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"randomDefintion"})
public class RandomDefinitionTests {

    @Test
    public void enduranceIsRecommendedToVorox() {
        Assert.assertFalse(CharacteristicsDefinitionFactory.getInstance().getElement("endurance").getRandomDefinition().getRecommendedSpecies().isEmpty());
        Assert.assertTrue(CharacteristicsDefinitionFactory.getInstance().getElement("endurance").getRandomDefinition().getRecommendedSpecies().contains("vorox"));
    }

    @Test
    public void enduranceIsRecommendedToMilitary() {
        Assert.assertFalse(WeaponFactory.getInstance().getElement("trenchKnife").getRandomDefinition().getRecommendedPerksGroups().isEmpty());
        Assert.assertTrue(WeaponFactory.getInstance().getElement("trenchKnife").getRandomDefinition().getRecommendedPerksGroups().contains("militaryRank"));
    }
}
