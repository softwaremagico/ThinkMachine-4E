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

import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.skills.Specialization;

import java.util.ArrayList;
import java.util.List;

public class CapabilityWithSpecialization extends Capability {
    private Specialization specialization;

    public CapabilityWithSpecialization() {
        super();
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public List<Specialization> getSpecializations() {
        return new ArrayList<>();
    }

    @Override
    public void setSpecializations(List<Specialization> specializations) {
        //Do nothing
    }

    public static CapabilityWithSpecialization from(Selection selection) {
        final CapabilityWithSpecialization capabilityWithSpecialization = new CapabilityWithSpecialization();
        final Capability capability = CapabilityFactory.getInstance().getElement(selection);
        capabilityWithSpecialization.copy(capability);
        if (selection.getSpecialization() != null) {
            capabilityWithSpecialization.setSpecialization(capability.getSpecialization(selection.getSpecialization().getId()));
        }
        capabilityWithSpecialization.setSpecializations(null);
        return capabilityWithSpecialization;
    }

    @Override
    public String getNameRepresentation() {
        if (getName() != null) {
            return getName().getTranslatedText() + (getSpecialization() != null ? " " + getSpecialization().getNameRepresentation() : "");
        }
        return "";
    }
}
