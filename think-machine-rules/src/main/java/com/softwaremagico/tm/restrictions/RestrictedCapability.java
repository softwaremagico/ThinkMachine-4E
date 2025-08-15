package com.softwaremagico.tm.restrictions;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
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

public class RestrictedCapability implements IComparableRestriction {
    @JsonProperty("id")
    private String id;
    @JsonProperty("selectedSpecialization")
    private String specialization;

    public RestrictedCapability() {
        super();
    }

    public RestrictedCapability(String id) {
        super();
        setId(id);
    }

    public RestrictedCapability(String id, String specialization) {
        this(id);
        this.specialization = specialization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String getComparisonId() {
        return getComparisonId(getId(), getSpecialization());
    }

    public static String getComparisonId(String id, String specialization) {
        return id + (specialization != null ? "_" + specialization : "");
    }

    @Override
    public String toString() {
        return "RestrictedCapability{"
                + "id='" + id + '\''
                + (specialization != null ? ", specialization=" + specialization : "")
                + '}';
    }
}
