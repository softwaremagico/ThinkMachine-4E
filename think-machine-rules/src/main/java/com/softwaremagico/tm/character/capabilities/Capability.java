package com.softwaremagico.tm.character.capabilities;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.skills.Specialization;

import java.util.List;
import java.util.Objects;

public class Capability extends Element<Capability> {
    private String group;
    private List<Specialization> specializations;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Specialization> getSpecializations() {
        return specializations;
    }

    public Specialization getSpecialization(String specialization) {
        return specializations.stream().filter(s -> Objects.equals(s.getId(), specialization)).findFirst().orElseThrow(() ->
                new InvalidSpecializationException("Specialization '" + specialization + "' not found on capability '" + getId() + "'."));
    }

    public void setSpecializations(List<Specialization> specializations) {
        this.specializations = specializations;
    }
}
