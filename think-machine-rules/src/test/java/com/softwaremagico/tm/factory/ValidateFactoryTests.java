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
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.armors.ArmourSpecificationFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.occultism.OccultismDurationFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismRangeFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.skills.SkillFactory;
import org.testng.annotations.Test;

@Test(groups = {"factoryValidation"})
public class ValidateFactoryTests {

    @Test
    public void validateFactories() throws InvalidXmlElementException {
        ArmorFactory.getInstance().validate();
    }

    @Test
    public void validateArmourSpecificationsFactory() throws InvalidXmlElementException {
        ArmourSpecificationFactory.getInstance().validate();
    }

    @Test
    public void validateCallingFactory() throws InvalidXmlElementException {
        CallingFactory.getInstance().validate();
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
    public void validateFactionFactoryFactory() throws InvalidXmlElementException {
        FactionFactory.getInstance().validate();
    }

    @Test
    public void validateOccultismFactoryFactory() throws InvalidXmlElementException {
        OccultismPathFactory.getInstance().validate();
        OccultismRangeFactory.getInstance().validate();
        OccultismDurationFactory.getInstance().validate();
        OccultismTypeFactory.getInstance().validate();
    }

    @Test
    public void validatePlanetsFactory() throws InvalidXmlElementException {
        PlanetFactory.getInstance().validate();
    }

    @Test
    public void validateRaceFactory() throws InvalidXmlElementException {
        SpecieFactory.getInstance().validate();
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
    public void validatePerksFactory() throws InvalidXmlElementException {
        PerkFactory.getInstance().validate();
    }


}
