package com.softwaremagico.tm.character.callings;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softwaremagico.tm.character.CharacterDefinitionStep;
import com.softwaremagico.tm.character.perks.PerkOptions;

import java.util.List;

public class Calling extends CharacterDefinitionStep {
    private static final int TOTAL_CHARACTERISTICS_OPTIONS = 5;
    private static final int TOTAL_SKILL_OPTIONS = 10;
    private static final int TOTAL_PERKS_OPTIONS = 1;
    private static final int TOTAL_CAPABILITIES_OPTIONS = 2;

    public Calling() {
        super();
        setGroup(CallingGroup.NONE.name());
    }

    @Override
    public int getCharacteristicsTotalPoints() {
        return TOTAL_CHARACTERISTICS_OPTIONS;
    }

    @Override
    public int getSkillsTotalPoints() {
        return TOTAL_SKILL_OPTIONS;
    }

    @Override
    public int getTotalPerksOptions() {
        return TOTAL_PERKS_OPTIONS;
    }

    @Override
    public int getTotalCapabilitiesOptions() {
        return TOTAL_CAPABILITIES_OPTIONS;
    }
}
