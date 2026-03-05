package com.softwaremagico.tm.random.character;

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

public final class RandomModifier {

    // WEIGHT VALUES

    //Planets
    public static final int FACTION_PLANET = 50;
    public static final int SPECIE_PLANET = 100;
    public static final int ENEMY_PLANET = 0;

    // MULTIPLIERS

    public static final int VERY_EXPENSIVE_DIVIDER = 10;
    public static final int EXPENSIVE_DIVIDER = 5;
    public static final int AFFORDABLE_DIVIDER = 2;


    //AgoraGroups
    public static final int AGORA_MODIFIER_FACTION = 10;
    public static final int AGORA_MODIFIER_SPECIE = 10;
    public static final int AGORA_MODIFIER_PLANET = 15;
    public static final int AGORA_MODIFIER_UPBRINGING = 5;

    // COST RELATED FRACTIONS

    //Shield cost fractions
    public static final double SHIELD_VERY_EXPENSIVE_FRACTION = 1.1;
    public static final double SHIELD_EXPENSIVE_FRACTION = 2;
    public static final double SHIELD_AFFORDABLE_FRACTION = 4;

    //Handheld shield cost fractions
    public static final double HANDHELD_SHIELD_VERY_EXPENSIVE_FRACTION = 1.1;
    public static final double HANDHELD_SHIELD_EXPENSIVE_FRACTION = 1.5;
    public static final double HANDHELD_SHIELD_AFFORDABLE_FRACTION = 2;

    //Armor cost fractions
    public static final int ARMOR_VERY_EXPENSIVE_FRACTION = 2;
    public static final int ARMOR_EXPENSIVE_FRACTION = 4;
    public static final int ARMOR_AFFORDABLE_FRACTION = 6;

    //Range cost fractions
    public static final double RANGE_VERY_EXPENSIVE_FRACTION = 1.1;
    public static final double RANGE_EXPENSIVE_FRACTION = 1.5;
    public static final double RANGE_AFFORDABLE_FRACTION = 2;

    // Melee cost fractions
    public static final double MELEE_VERY_EXPENSIVE_FRACTION = 3;
    public static final double MELEE_EXPENSIVE_FRACTION = 6;
    public static final double MELEE_AFFORDABLE_FRACTION = 10;

    private RandomModifier() {

    }
}
