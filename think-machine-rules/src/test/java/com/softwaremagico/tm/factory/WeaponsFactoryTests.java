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
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.equipment.weapons.AccessoryFactory;
import com.softwaremagico.tm.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"weaponsFactory"})
public class WeaponsFactoryTests {
    private static final int DEFINED_WEAPONS = 176;


    @Test
    public void readWeapons() throws InvalidXmlElementException {
        Assert.assertEquals(WeaponFactory.getInstance().getElements().size(), DEFINED_WEAPONS);
    }

    @Test
    public void readAmmunition() throws InvalidXmlElementException {
        Assert.assertFalse(AmmunitionFactory.getInstance().getElements().isEmpty());
    }

    @Test
    public void readAccessory() throws InvalidXmlElementException {
        Assert.assertFalse(AccessoryFactory.getInstance().getElements().isEmpty());
    }

    @Test
    public void checkShotgun() throws InvalidXmlElementException {
        Assert.assertEquals(1, WeaponFactory.getInstance().getElements("typicalShotgun")
                .getAmmunition().size());
    }

    @Test
    public void checkBasicHuntingRifle() throws InvalidXmlElementException {
        Assert.assertEquals(3, WeaponFactory.getInstance().getElements("basicHuntingRifle")
                .getAccessories().size());
    }

    @Test
    public void checkRandomModifications() throws InvalidXmlElementException {
        Assert.assertEquals(0.01d, WeaponFactory.getInstance().getElements("arbata")
                .getRandomDefinition().getProbabilityMultiplier());
    }

    @Test
    public void getMainDamage() throws InvalidXmlElementException {
        Assert.assertEquals(6, WeaponFactory.getInstance().getElements("arbata").getWeaponDamages().get(0).getMainDamage());
        Assert.assertEquals(8, WeaponFactory.getInstance().getElements("typicalShotgun")
                .getWeaponDamages().get(0).getMainDamage());
        Assert.assertEquals(12, WeaponFactory.getInstance().getElements("wireGrenade").getWeaponDamages().get(0).getMainDamage());
    }

    @Test
    public void getAreaDamage() throws InvalidXmlElementException {
        Assert.assertEquals(1, WeaponFactory.getInstance().getElements("goboLobberJetPistol")
                .getWeaponDamages().get(0).getAreaMeters());
        Assert.assertEquals(2, WeaponFactory.getInstance().getElements("goboGarbageChucker")
                .getWeaponDamages().get(0).getAreaMeters());
        Assert.assertEquals(3, WeaponFactory.getInstance().getElements("musterNightstorm")
                .getWeaponDamages().get(0).getAreaMeters());
        Assert.assertEquals(5, WeaponFactory.getInstance().getElements("fragGrenades").getWeaponDamages().get(0).getAreaMeters());
    }

    @Test
    public void getDamageWithoutArea() throws InvalidXmlElementException {
        Assert.assertEquals("3", WeaponFactory.getInstance().getElements("blastPellet")
                .getWeaponDamages().get(0).getDamageWithoutArea());
        Assert.assertEquals("12", WeaponFactory.getInstance().getElements("fragGrenades")
                .getWeaponDamages().get(0).getDamageWithoutArea());
    }


    @Test
    public void checkMultipleDamage() throws InvalidXmlElementException {
        final Weapon nitobiAxe = WeaponFactory.getInstance().getElements("nitobiBlasterAxe");
        Assert.assertEquals(nitobiAxe.getWeaponDamages().size(), 2);
    }

    @Test
    public void checkMultipleDamageDifferentTechLevel() throws InvalidXmlElementException {
        final Weapon javelin = WeaponFactory.getInstance().getElements("javelin");
        Assert.assertEquals(javelin.getWeaponDamages().size(), 2);
        Assert.assertEquals((int) javelin.getWeaponDamages().get(1).getDamageTechLevel(), 1);
    }

    @Test
    public void checkMultipleDamageNames() throws InvalidXmlElementException {
        final Weapon rock = WeaponFactory.getInstance().getElements("rock");
        Assert.assertEquals(rock.getWeaponDamages().size(), 5);
        Assert.assertEquals(rock.getWeaponDamages().get(1).getName().getSpanish(), "Media");

        final Weapon heavyFuthangaBow = WeaponFactory.getInstance().getElements("heavyFuthangaBow");
        Assert.assertEquals(heavyFuthangaBow.getWeaponDamages().size(), 2);
        Assert.assertEquals(heavyFuthangaBow.getWeaponDamages().get(1).getName().getSpanish(), "Arco Compuesto");
    }

    @Test
    public void checkMultipleDamageSize() throws InvalidXmlElementException {
        final Weapon rock = WeaponFactory.getInstance().getElements("rock");
        Assert.assertEquals(rock.getWeaponDamages().size(), 5);
        Assert.assertEquals(rock.getWeaponDamages().get(1).getSize(), Size.S);
        Assert.assertEquals(rock.getWeaponDamages().get(4).getSize(), Size.XL);
    }

    @Test
    public void extraCost() throws InvalidXmlElementException {
        final Weapon heavyFuthangaBow = WeaponFactory.getInstance().getElements("heavyFuthangaBow");
        Assert.assertEquals(heavyFuthangaBow.getWeaponDamages().size(), 2);
        Assert.assertEquals(heavyFuthangaBow.getWeaponDamages().get(1).getExtraCost(), 30);
    }

//    @Test
//    public void setRangeWeapons() throws InvalidXmlElementException, UnofficialElementNotAllowedException {
//        final CharacterPlayer player = new CharacterPlayer();
//        Set<Weapon> weaponsToAdd = new HashSet<>();
//        weaponsToAdd.add(WeaponFactory.getInstance().getElement("typicalShotgun"));
//        weaponsToAdd.add(WeaponFactory.getInstance().getElement("martechSafireSniper"));
//        player.setRangedWeapons(weaponsToAdd);
//        Assert.assertEquals(2, player.getAllWeapons().size());
//
//        weaponsToAdd = new HashSet<>();
//        weaponsToAdd.add(WeaponFactory.getInstance().getElement("typicalShotgun"));
//        weaponsToAdd.add(WeaponFactory.getInstance().getElement("soeCrucible"));
//        player.setRangedWeapons(weaponsToAdd);
//        Assert.assertEquals(2, player.getAllWeapons().size());
//
//        weaponsToAdd = new HashSet<>();
//        weaponsToAdd.add(WeaponFactory.getInstance().getElement("arbata"));
//        player.setMeleeWeapons(weaponsToAdd);
//        Assert.assertEquals(3, player.getAllWeapons().size());
//
//        weaponsToAdd = new HashSet<>();
//        weaponsToAdd.add(WeaponFactory.getInstance().getElement("nitobiBlasterAxe"));
//        player.setRangedWeapons(weaponsToAdd);
//        Assert.assertEquals(2, player.getAllWeapons().size());
//
//        weaponsToAdd = new HashSet<>();
//        player.setRangedWeapons(weaponsToAdd);
//        Assert.assertEquals(1, player.getAllWeapons().size());
//
//        weaponsToAdd = new HashSet<>();
//        player.setMeleeWeapons(weaponsToAdd);
//        Assert.assertEquals(0, player.getAllWeapons().size());
//    }

}
