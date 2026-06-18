package com.softwaremagico.tm.file.modules;

/*-
 * #%L
 * Think Machine (Rules)
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

import com.softwaremagico.tm.character.TimeFactory;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.cybernetics.CyberdeviceFactory;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.EquipmentTraitFactory;
import com.softwaremagico.tm.character.equipment.TechCompulsionFactory;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ModuleManager {
    public static final String FADING_SUNS_PLAYER_GUIDE_MODULE = "Fading Suns 4E";
    public static final String FACTION_BOOK_MODULE = "Faction Book";
    public static final String LOST_WORLDS_BOOK_MODULE = "Lost Worlds";
    public static final String IMPERIAL_DOSSIER_BROTHER_BATTLE_MODULE = "Imperial Dossier - Brother Battle";
    private static final String[] TOTAL_MODULES = {FADING_SUNS_PLAYER_GUIDE_MODULE, FACTION_BOOK_MODULE, LOST_WORLDS_BOOK_MODULE};
    private static final Set<String> ENABLED_MODULES = new HashSet<>(Arrays.asList(TOTAL_MODULES));

    private ModuleManager() {

    }

    public static String[] getAllModules() {
        return TOTAL_MODULES.clone();
    }

    public static Set<String> getEnabledModules() {
        return ENABLED_MODULES;
    }

    public static void enableModule(String module) {
        ENABLED_MODULES.add(module);
    }

    public static void resetModules() {

        PlanetFactory.getInstance().reset();

        SpecieFactory.getInstance().reset();
        UpbringingFactory.getInstance().reset();
        FactionFactory.getInstance().reset();
        CallingFactory.getInstance().reset();

        CharacteristicsDefinitionFactory.getInstance().reset();
        SkillFactory.getInstance().reset();
        CapabilityFactory.getInstance().reset();
        PerkFactory.getInstance().reset();
        AfflictionFactory.getInstance().reset();

        CombatStyleFactory.getInstance().reset();

        WeaponFactory.getInstance().reset();
        ArmorFactory.getInstance().reset();
        ShieldFactory.getInstance().reset();
        HandheldShieldFactory.getInstance().reset();
        AccessoryFactory.getInstance().reset();
        AmmunitionFactory.getInstance().reset();
        DamageTypeFactory.getInstance().reset();
        EquipmentTraitFactory.getInstance().reset();
        SpecialValueFactory.getInstance().reset();

        CyberdeviceFactory.getInstance().reset();
        TechCompulsionFactory.getInstance().reset();
        ThinkMachineFactory.getInstance().reset();

        OccultismPathFactory.getInstance().reset();
        OccultismTypeFactory.getInstance().reset();
        TheurgyComponentFactory.getInstance().reset();

        TimeFactory.getInstance().reset();
    }

    public static void disableModule(String module) {
        ENABLED_MODULES.remove(module);
    }
}
