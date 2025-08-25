package com.softwaremagico.tm.character.equipment.weapons;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public enum WeaponType {

    VIGOR, BOW, CROSSBOW, SLUG, MINE, MELEE, MELEE_SHIELD, MELEE_ARTIFACT, LASER, BLASTER, FLAMMER, ARTIFACT_ENERGY,

    SCREECHER, STUNNER, NEURAL, HEAVY, ROCKETEER, GRENADE, GRENADE_LAUNCHER, ANY;

    private static final Set<WeaponType> MELEE_TYPES = new HashSet<>(Arrays.asList(WeaponType.MELEE,
            WeaponType.MELEE_SHIELD, WeaponType.MELEE_ARTIFACT));

    public static WeaponType get(String typeName) {
        for (final WeaponType type : WeaponType.values()) {
            if (type.name().equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        return null;
    }

    public static Set<WeaponType> getMeleeTypes() {
        return MELEE_TYPES;
    }

    public static Set<WeaponType> getRangedTypes() {
        final Set<WeaponType> rangedWeapons = new HashSet<>(Arrays.asList(WeaponType.values()));
        rangedWeapons.removeAll(MELEE_TYPES);
        return Collections.unmodifiableSet(rangedWeapons);
    }
}
