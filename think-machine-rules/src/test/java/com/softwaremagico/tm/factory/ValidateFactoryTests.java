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

import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.cybernetics.CyberdeviceFactory;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.EquipmentTraitFactory;
import com.softwaremagico.tm.character.equipment.TechCompulsionFactory;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.armors.ArmorSpecificationFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.thinkmachines.ThinkMachineFactory;
import com.softwaremagico.tm.character.equipment.weapons.AccessoryFactory;
import com.softwaremagico.tm.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.occultism.TheurgyComponentFactory;
import com.softwaremagico.tm.character.perks.AfflictionFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.character.values.SpecialValueFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import org.testng.annotations.Test;

@Test(groups = {"factoryValidation"})
public class ValidateFactoryTests {


    @Test
    public void validateAccessoryFactory() throws InvalidXmlElementException {
        AccessoryFactory.getInstance().validate();
    }

    @Test
    public void validateAmmunitionFactory() throws InvalidXmlElementException {
        AmmunitionFactory.getInstance().validate();
    }

    @Test
    public void validateArmorFactory() throws InvalidXmlElementException {
        ArmorFactory.getInstance().validate();
    }

    @Test
    public void validateArmorSpecificationsFactory() throws InvalidXmlElementException {
        ArmorSpecificationFactory.getInstance().validate();
    }

    @Test
    public void validateCallingFactory() throws InvalidXmlElementException {
        CallingFactory.getInstance().validate();
    }

    @Test
    public void validateCapabilityFactory() throws InvalidXmlElementException {
        CapabilityFactory.getInstance().validate();
    }

    @Test
    public void validateCharacteristicsDefinitionFactory() throws InvalidXmlElementException {
        CharacteristicsDefinitionFactory.getInstance().validate();
    }

    @Test
    public void validateCombatStyleFactory() throws InvalidXmlElementException {
        CombatStyleFactory.getInstance().validate();
    }

    @Test
    public void validateDamageTypeFactory() throws InvalidXmlElementException {
        DamageTypeFactory.getInstance().validate();
    }

    @Test
    public void validateEquipmentTraitFactory() throws InvalidXmlElementException {
        EquipmentTraitFactory.getInstance().validate();
    }

    @Test
    public void validateFactionFactory() throws InvalidXmlElementException {
        FactionFactory.getInstance().validate();
    }

    @Test
    public void validateHandheldShieldFactory() throws InvalidXmlElementException {
        HandheldShieldFactory.getInstance().validate();
    }

    @Test
    public void validateItemsFactory() throws InvalidXmlElementException {
        ItemFactory.getInstance().validate();
    }


    @Test
    public void validateOccultismFactoryFactory() throws InvalidXmlElementException {
        OccultismPathFactory.getInstance().validate();
        OccultismTypeFactory.getInstance().validate();
    }

    @Test
    public void validatePerksFactory() throws InvalidXmlElementException {
        PerkFactory.getInstance().validate();
    }

    @Test
    public void validateAfflictionsFactory() throws InvalidXmlElementException {
        AfflictionFactory.getInstance().validate();
    }

    @Test
    public void validatePlanetsFactory() throws InvalidXmlElementException {
        PlanetFactory.getInstance().validate();
    }


    @Test
    public void validateShieldFactory() throws InvalidXmlElementException {
        ShieldFactory.getInstance().validate();
    }

    @Test
    public void validateSkillsFactory() throws InvalidXmlElementException {
        SkillFactory.getInstance().validate();
    }

    @Test
    public void validateSpecialValueFactory() throws InvalidXmlElementException {
        SpecialValueFactory.getInstance().validate();
    }

    @Test
    public void validateSpecieFactory() throws InvalidXmlElementException {
        SpecieFactory.getInstance().validate();
    }

    @Test
    public void validateTechCompulsionFactory() throws InvalidXmlElementException {
        TechCompulsionFactory.getInstance().validate();
    }

    @Test
    public void validateTheurgyComponentFactory() throws InvalidXmlElementException {
        TheurgyComponentFactory.getInstance().validate();
    }

    @Test
    public void validateThinkMachineFactory() throws InvalidXmlElementException {
        ThinkMachineFactory.getInstance().validate();
    }

    @Test
    public void validateUpbringingFactory() throws InvalidXmlElementException {
        UpbringingFactory.getInstance().validate();
    }

    @Test
    public void validateWeaponFactory() throws InvalidXmlElementException {
        WeaponFactory.getInstance().validate();
    }

    @Test
    public void validateCyberdeviceFactory() throws InvalidXmlElementException {
        CyberdeviceFactory.getInstance().validate();
    }
}
