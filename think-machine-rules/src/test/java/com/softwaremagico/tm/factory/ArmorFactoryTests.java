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


import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.resistances.ResistanceCategory;
import com.softwaremagico.tm.character.resistances.ResistanceType;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.softwaremagico.tm.file.modules.ModuleManager;
import org.testng.annotations.BeforeClass;

@Test(groups = {"armorFactory"})
public class ArmorFactoryTests extends FactoryTest {
    @Override
    @BeforeClass
    public void enableBasicModule() {
        ModuleManager.enableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.enableModule(ModuleManager.LOST_WORLDS_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.IMPERIAL_DOSSIER_BROTHER_BATTLE_MODULE);
        ModuleManager.resetModules();
    }

    private static final int DEFINED_ARMOURS = 33;


    @Test
    public void readArmors() throws InvalidXmlElementException {
        Assert.assertEquals(ArmorFactory.getInstance().getElements().size(),
                DEFINED_ARMOURS);
    }

    @Test
    public void readArmorsSpecifications() throws InvalidXmlElementException {
        Assert.assertEquals(ArmorFactory.getInstance().getElement("adeptRobes")
                .getSpecifications().size(), 7);
    }

    @Test
    public void readShieldsFromArmor() throws InvalidXmlElementException {
        Assert.assertEquals(ArmorFactory.getInstance().getElement("synthsilk")
                .getAllowedShields().size(), 4);
        Assert.assertEquals(ArmorFactory.getInstance().getElement("adeptRobes")
                .getAllowedShields().size(), 1);
    }

    @Test
    public void readDamagesFromArmor() throws InvalidXmlElementException {
        Assert.assertEquals(
                ArmorFactory.getInstance().getElement("ceramsteelExoframe")
                        .getDamageTypes().size(), 4);
        Assert.assertEquals(ArmorFactory.getInstance().getElement("spacesuit")
                .getDamageTypes().size(), 4);
    }

    @Test
    public void readOthersFromArmor() throws InvalidXmlElementException {
        Assert.assertEquals(
                ArmorFactory.getInstance().getElement("ceramsteelExoframe")
                        .getSpecifications().size(), 2);
        Assert.assertEquals(ArmorFactory.getInstance().getElement("halfPlateMetal")
                .getSpecifications().size(), 1);
        Assert.assertTrue(ArmorFactory.getInstance().getElement("chainMailMetal")
                .getSpecifications().contains("metal"));
    }

    @Test
    public void getPenalization() throws InvalidXmlElementException {
        Assert.assertEquals(
                ArmorFactory.getInstance().getElement("spacesuit")
                        .getStandardPenalization().getDexterityModification(), 0);
        Assert.assertEquals(
                ArmorFactory.getInstance().getElement("spacesuit")
                        .getSpecialPenalization().getDexterityModification(), -2);
    }

    @Test
    public void getResistances() throws InvalidXmlElementException {
        Assert.assertEquals(
                ArmorFactory.getInstance().getElement("marauderSpacesuit")
                        .getResistances().size(), 1);
        Assert.assertEquals(
                ArmorFactory.getInstance().getElement("marauderSpacesuit")
                        .getResistances().get(0).getBonus(), 9);
        Assert.assertEquals(
                ArmorFactory.getInstance().getElement("marauderSpacesuit")
                        .getResistances().get(0).getType(), ResistanceType.BODY);
        Assert.assertEquals(
                ArmorFactory.getInstance().getElement("marauderSpacesuit")
                        .getResistances().get(0).getCategory(), ResistanceCategory.ITEM);
    }
}
