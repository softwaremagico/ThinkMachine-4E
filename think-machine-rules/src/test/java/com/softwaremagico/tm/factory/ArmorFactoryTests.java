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


import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.character.equipment.armors.ArmourFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"armourFactory"})
public class ArmorFactoryTests {
    private static final int DEFINED_ARMOURS = 30;


    @Test
    public void readArmours() throws InvalidXmlElementException {
        Assert.assertEquals(ArmourFactory.getInstance().getElements().size(),
                DEFINED_ARMOURS);
    }

    @Test
    public void readArmoursSpecifications() throws InvalidXmlElementException {
        Assert.assertEquals(ArmourFactory.getInstance().getElements("adeptRobes")
                .getSpecifications().size(), 7);
    }

    @Test
    public void readShieldsFromArmour() throws InvalidXmlElementException {
        Assert.assertEquals(ArmourFactory.getInstance().getElements("synthsilk")
                .getAllowedShields().size(), 4);
        Assert.assertEquals(ArmourFactory.getInstance().getElements("adeptRobes")
                .getAllowedShields().size(), 1);
    }

    @Test
    public void readDamagesFromArmour() throws InvalidXmlElementException {
        Assert.assertEquals(
                ArmourFactory.getInstance().getElements("ceramsteelExoframe")
                        .getDamageTypes().size(), 4);
        Assert.assertEquals(ArmourFactory.getInstance().getElements("spacesuit")
                .getDamageTypes().size(), 4);
    }

    @Test
    public void readOthersFromArmour() throws InvalidXmlElementException {
        Assert.assertEquals(
                ArmourFactory.getInstance().getElements("ceramsteelExoframe")
                        .getSpecifications().size(), 2);
        Assert.assertEquals(ArmourFactory.getInstance().getElements("halfPlateMetal")
                .getSpecifications().size(), 1);
        Assert.assertTrue(ArmourFactory.getInstance().getElements("chainMailMetal")
                .getSpecifications().contains("metal"));
    }

    @Test
    public void getPenalization() throws InvalidXmlElementException {
        Assert.assertEquals(
                ArmourFactory.getInstance().getElements("spacesuit")
                        .getStandardPenalization().getDexterityModification(), 0);
        Assert.assertEquals(
                ArmourFactory.getInstance().getElements("spacesuit")
                        .getSpecialPenalization().getDexterityModification(), -2);
    }
}
