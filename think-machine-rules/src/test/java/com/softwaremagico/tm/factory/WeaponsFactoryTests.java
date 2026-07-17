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
import com.softwaremagico.tm.character.equipment.weapons.AccessoryFactory;
import com.softwaremagico.tm.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.random.definition.ProbabilityMultiplier;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Test(groups = {"weaponsFactory"})
public class WeaponsFactoryTests extends FactoryTest {

    // 131 base weapons + 1 from Imperial Dossier - Charioteers Guild + 2 from Vuldrok Space + 79 from Fading Suns Revised Edition.
    private static final int DEFINED_WEAPONS = 213;


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
        Assert.assertEquals(WeaponFactory.getInstance().getElement("typicalShotgun")
                .getAmmunition().size(), 1);
    }

    @Test
    public void checkBasicHuntingRifle() throws InvalidXmlElementException {
        Assert.assertEquals(WeaponFactory.getInstance().getElement("basicHuntingRifle")
                .getOthers().size(), 2);
    }

    @Test
    public void checkRandomModifications() throws InvalidXmlElementException {
        Assert.assertEquals(WeaponFactory.getInstance().getElement("arbata")
                .getRandomDefinition().getProbabilityMultiplier(), ProbabilityMultiplier.EXOTIC);
    }

    @Test
    public void imperialDossierBrotherBattleWeaponsAreLoaded() throws InvalidXmlElementException {
        Assert.assertNotNull(WeaponFactory.getInstance().getElement("brotherCassHeavyRevolver"));
        Assert.assertNotNull(WeaponFactory.getInstance().getElement("sisterCassAutofeedRifle"));
        Assert.assertNotNull(WeaponFactory.getInstance().getElement("pyreBlade"));
    }

    @Test
    public void imperialDossierBrotherBattleWeaponsAreRestricted() throws InvalidXmlElementException {
        final var brotherCass = WeaponFactory.getInstance().getElement("brotherCassHeavyRevolver");
        Assert.assertTrue(brotherCass.getRestrictions().getRestrictedToFactions().contains("brotherBattle"));
        Assert.assertTrue(brotherCass.getRestrictions().getRestrictedToCallings().contains("brotherBattle"));
    }

    @Test
    public void fadingSunsRevisedEditionWeaponsAreLoaded() throws InvalidXmlElementException {
        Assert.assertNotNull(WeaponFactory.getInstance().getElement("cestus"));
        Assert.assertNotNull(WeaponFactory.getInstance().getElement("saber"));
        Assert.assertNotNull(WeaponFactory.getInstance().getElement("flail"));
    }

    @Test
    public void getMainDamage() throws InvalidXmlElementException {
        Assert.assertEquals(WeaponFactory.getInstance().getElement("arbata").getWeaponDamages().get(0).getMainDamage(), 6);
        Assert.assertEquals(WeaponFactory.getInstance().getElement("typicalShotgun")
                .getWeaponDamages().get(0).getMainDamage(), 8);
        Assert.assertEquals(WeaponFactory.getInstance().getElement("soeCrucible")
                .getWeaponDamages().get(0).getMainDamage(), 9);
    }


    @Test
    public void checkMultipleDamage() throws InvalidXmlElementException {
        final Weapon nitobiAxe = WeaponFactory.getInstance().getElement("nitobiBlasterAxe");
        Assert.assertEquals(nitobiAxe.getWeaponDamages().size(), 2);
    }

    @Test
    public void setRangeWeapons() throws InvalidXmlElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer player = new CharacterPlayer();
        player.setPurchasedRangedWeapons(List.of(
                WeaponFactory.getInstance().getElement("typicalShotgun"),
                WeaponFactory.getInstance().getElement("martechSafireSniper")), true);
        Assert.assertEquals(player.getWeapons().size(), 2);

        player.setPurchasedRangedWeapons(List.of(
                WeaponFactory.getInstance().getElement("typicalShotgun"),
                WeaponFactory.getInstance().getElement("soeCrucible")), true);
        Assert.assertEquals(player.getWeapons().size(), 2);

        player.setPurchasedMeleeWeapons(List.of(
                WeaponFactory.getInstance().getElement("arbata")), true);
        Assert.assertEquals(player.getWeapons().size(), 3);

        player.setPurchasedRangedWeapons(List.of(
                WeaponFactory.getInstance().getElement("nitobiBlasterAxe")), true);
        Assert.assertEquals(player.getWeapons().size(), 2);

        player.setPurchasedRangedWeapons(List.of(), true);
        Assert.assertEquals(player.getWeapons().size(), 1);

        player.setPurchasedMeleeWeapons(List.of(), true);
        Assert.assertEquals(player.getWeapons().size(), 0);
    }

}
