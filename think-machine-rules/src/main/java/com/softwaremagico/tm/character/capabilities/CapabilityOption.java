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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.restrictions.Restrictions;
import com.softwaremagico.tm.utils.ComparableUtils;
import com.softwaremagico.tm.utils.IComparable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CapabilityOption extends Option<Capability> implements IComparable {
    @JsonProperty("selectedSpecialization")
    private Specialization selectedSpecialization;

    public CapabilityOption() {
        super();
    }

    public CapabilityOption(Capability capability) {
        this();
        setId(capability.getId());
    }

    public CapabilityOption(Capability capability, Specialization selectedSpecialization) {
        this();
        setId(capability.getId());
        if (selectedSpecialization != null) {
            setSelectedSpecialization(selectedSpecialization);
        }
    }

    public static Set<CapabilityOption> getCapabilityOptions(Collection<Capability> capabilities) {
        final Set<CapabilityOption> capabilityOptions = new HashSet<>();
        capabilities.forEach(capability -> capabilityOptions.addAll(getCapabilityOptions(capability)));
        return capabilityOptions;
    }

    public static Set<CapabilityOption> getCapabilityOptions(Capability capability) {
        final Set<CapabilityOption> capabilityOptions = new HashSet<>();
        if (capability.getSpecializations() == null || capability.getSpecializations().isEmpty()) {
            capabilityOptions.add(new CapabilityOption(capability));
        } else {
            capability.getSpecializations().forEach(specialization ->
                    capabilityOptions.add(new CapabilityOption(capability, specialization)));
        }
        return capabilityOptions;
    }


    public Specialization getSelectedSpecialization() {
        return selectedSpecialization;
    }

    public void setSelectedSpecialization(Specialization selectedSpecialization) {
        this.selectedSpecialization = selectedSpecialization;
    }

    @Override
    public Restrictions getRestrictions() {
        if (getId() != null) {
            return CapabilityFactory.getInstance().getElement(getId()).getRestrictions();
        }
        return super.getRestrictions();
    }

    @Override
    public Capability getElement(String id) {
        return CapabilityFactory.getInstance().getElement(id);
    }

    @Override
    public String toString() {
        return (getId() != null ? getId() : (getGroup() != null ? getGroup() : null))
                + (selectedSpecialization != null ? " (" + selectedSpecialization.getId() + ")" : "");
    }

    @Override
    public String getComparisonId() {
        return ComparableUtils.getComparisonId(getId(), getSelectedSpecialization());
    }

    @JsonIgnore
    @Override
    public TranslatedText getName() {
        if (getId() != null) {
            try {
                return new TranslatedText(CapabilityFactory.getInstance().getElement(getId()).getName(),
                        selectedSpecialization != null ? selectedSpecialization.getName() : null);
            } catch (InvalidXmlElementException e) {
                return new TranslatedText("{" + getId() + "}");
            }
        }
        return null;
    }


    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (getId() != null) {
            CapabilityFactory.getInstance().getElement(getId());
        } else if (getGroup() != null && CapabilityFactory.getInstance().getElementsByGroup(getGroup()).isEmpty()) {
            throw new InvalidXmlElementException("Invalid group '" + getGroup() + "' on capability. ");
        }
        if (selectedSpecialization != null && !CapabilityFactory.getInstance().getElement(getId()).getSpecializations().contains(selectedSpecialization)) {
            throw new InvalidXmlElementException("Capability " + getId() + " has not element with specialization '" + selectedSpecialization + "'.");
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof CapabilityOption)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final CapabilityOption that = (CapabilityOption) o;
        return Objects.equals(selectedSpecialization, that.selectedSpecialization);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        final int prime = 31;
        result = prime * result + Objects.hashCode(selectedSpecialization);
        return result;
    }
}
