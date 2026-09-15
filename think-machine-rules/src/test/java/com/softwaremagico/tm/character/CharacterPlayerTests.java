package com.softwaremagico.tm.character;

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

import com.softwaremagico.tm.character.capabilities.CapabilityWithSpecialization;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.exceptions.InvalidCharacteristicException;
import com.softwaremagico.tm.rules.RulesTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Test(groups = "characterPlayer")
public class CharacterPlayerTests extends RulesTest {

    @Test
    public void newCharacterExposesSafeDefaults() {
        final CharacterPlayer character = new CharacterPlayer();

        Assert.assertNotNull(character.getInfo());
        Assert.assertNotNull(character.getSettings());
        Assert.assertEquals(character.getLevel(), 1);
        Assert.assertTrue(character.getLevels().isEmpty());
        Assert.assertTrue(character.getEquipmentPurchased().isEmpty());
        Assert.assertNull(character.getCallingId());
        Assert.assertFalse(character.isRaisedInSpace());
    }

    @Test
    public void callingCanBeClearedAndRestored() {
        final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

        Assert.assertEquals(character.getCallingId(), "commander");
        Assert.assertEquals(character.getCallingCombinationIds(), List.of("commander"));

        character.setCalling((String) null);

        Assert.assertNull(character.getCalling());
        Assert.assertNull(character.getCallingId());
        Assert.assertTrue(character.getCallingCombinationIds().isEmpty());
    }

    @Test
    public void capabilitiesAreCachedAndInvalidateWhenSelectionsChange() {
        final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

        final var capabilities = character.getCapabilitiesWithSpecialization();
        final CapabilityWithSpecialization selected = capabilities.iterator().next();

        Assert.assertSame(character.getCapabilitiesWithSpecialization(), capabilities);
        Assert.assertTrue(character.hasCapability(selected.getId(),
                selected.getSpecialization() != null ? selected.getSpecialization().getId() : null));

        character.getCacheManager().capabilitiesChanged();

        Assert.assertNotSame(character.getCapabilitiesWithSpecialization(), capabilities);
        Assert.assertTrue(character.hasCapability(selected.getId(),
                selected.getSpecialization() != null ? selected.getSpecialization().getId() : null));
    }

    @Test
    public void purchasedEquipmentUpdatesTotalsAndBestEquipment() {
        final CharacterPlayer character = new CharacterPlayer();
        final var armor = ArmorFactory.getInstance().getElement("synthsilk");
        final var shield = ShieldFactory.getInstance().getElement("duelingShield");
        final var weapon = WeaponFactory.getInstance().getElement("soeAlembic");

        character.addEquipmentPurchased(armor);
        character.addEquipmentPurchased(shield);
        character.addEquipmentPurchased(weapon);

        Assert.assertSame(character.getBestArmor(), armor);
        Assert.assertSame(character.getBestShield(), shield);
        Assert.assertTrue(character.hasWeapon(weapon));
        Assert.assertEquals(character.getSpentCash(), armor.getCost() + shield.getCost() + weapon.getCost());
        Assert.assertEquals(character.getCacheManager().getSpentCash().doubleValue(), character.getSpentCash());
    }

    @Test
    public void levelTracksCurrentCallingAndClearsDerivedCaches() {
        final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();
        character.getTechLevel();

        final var level = character.addLevel();

        Assert.assertEquals(character.getLevel(), 2);
        Assert.assertSame(character.getLatestLevel(), level);
        Assert.assertEquals(character.getCallingAtLevel(2).getId(), "commander");
        Assert.assertNull(character.getCacheManager().getTechLevel());
    }

    @Test
    public void validateRequiresBothChosenCharacteristics() {
        final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();
        character.setPrimaryCharacteristic(null);
        character.setSecondaryCharacteristic(null);

        Assert.expectThrows(InvalidCharacteristicException.class, character::validate);
    }
}
