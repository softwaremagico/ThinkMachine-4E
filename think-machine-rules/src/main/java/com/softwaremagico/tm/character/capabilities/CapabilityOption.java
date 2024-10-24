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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.character.skills.Specialization;

public class CapabilityOption extends Option<Capability> {
    @JsonProperty("id")
    private String id;
    @JsonProperty("group")
    private String group;
    @JsonProperty("specialization")
    private Specialization specialization;

    public CapabilityOption() {
        super(CapabilityFactory.getInstance());
    }

    public CapabilityOption(Capability capability) {
        this();
        setId(capability.getId());
    }

    public CapabilityOption(Capability capability, Specialization specialization) {
        this();
        setId(capability.getId());
        if (specialization != null) {
            setSpecialization(specialization);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return (id != null ? id : (group != null ? group : null)) + (specialization != null ? " (" + specialization.getId() + ")" : "");
    }
}
