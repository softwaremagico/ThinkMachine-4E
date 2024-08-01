package com.softwaremagico.tm.character.combat;

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


import com.softwaremagico.tm.character.values.IValue;

import java.util.Set;

public class CombatActionRequirement {
    private Set<IValue> requirement;
    private int value;

    public CombatActionRequirement() {
        super();
    }

    protected CombatActionRequirement(Set<IValue> requirement, int value) {
        this.requirement = requirement;
        this.value = value;
    }

    public Set<IValue> getRequirements() {
        return requirement;
    }

    public int getValue() {
        return value;
    }

    public Set<IValue> getRequirement() {
        return requirement;
    }

    public void setRequirement(Set<IValue> requirement) {
        this.requirement = requirement;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
