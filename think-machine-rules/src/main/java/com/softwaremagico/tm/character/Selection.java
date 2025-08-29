package com.softwaremagico.tm.character;

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
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.utils.ComparableUtils;
import com.softwaremagico.tm.utils.IComparable;

import java.util.Objects;

public class Selection extends Element implements IComparable {
    private Specialization specialization;

    public Selection(String id) {
        super(id);
    }

    public Selection(String id, Specialization specialization) {
        this(id);
        this.specialization = specialization;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @JsonIgnore
    public Selection getMainSelection() {
        return new Selection(getId());
    }

    @Override
    public String getComparisonId() {
        return ComparableUtils.getComparisonId(getId(), getSpecialization());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Selection selection = (Selection) o;
        return Objects.equals(getId(), selection.getId()) && Objects.equals(specialization, selection.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), specialization);
    }

    @Override
    public String toString() {
        return getId() + (getSpecialization() != null ? " (" + getSpecialization() + ")" : "");
    }
}
