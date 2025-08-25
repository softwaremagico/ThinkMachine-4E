package com.softwaremagico.tm.character.perks;

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


import com.softwaremagico.tm.character.skills.Specialization;

import java.util.ArrayList;
import java.util.List;

/**
 * For managing character's perk selections with specializations.
 */
public class SpecializedPerk extends Perk {
    private Specialization specialization;

    public SpecializedPerk(Perk perk, Specialization specialization) {
        super();
        copy(perk);
        setSpecialization(specialization);
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return getId() + (getSpecialization() != null ? " (" + getSpecialization().getId() + ")" : "");
    }

    @Override
    public String getNameRepresentation() {
        if (getName() != null) {
            return getName().getTranslatedText() + (getSpecialization() != null ? " (" + getSpecialization().getNameRepresentation() + ")" : "");
        }
        return "";
    }

    public static List<SpecializedPerk> create(List<Perk> perks) {
        final List<SpecializedPerk> specializedPerks = new ArrayList<>();
        perks.forEach(perk -> specializedPerks.add(new SpecializedPerk(perk, null)));
        return specializedPerks;
    }
}
