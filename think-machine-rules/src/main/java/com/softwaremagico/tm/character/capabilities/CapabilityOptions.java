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
import com.softwaremagico.tm.OptionSelector;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CapabilityOptions extends OptionSelector<Capability, CapabilityOption> {
    @JsonIgnore
    private List<CapabilityOption> finalCapabilities;

    public CapabilityOptions() {
        super();
    }

    public CapabilityOptions(CapabilityOptions optionSelector) {
        super(optionSelector);
        if (optionSelector.finalCapabilities != null) {
            this.finalCapabilities = new ArrayList<>(optionSelector.finalCapabilities);
        }
    }

    public CapabilityOptions(CapabilityOptions optionSelector, List<CapabilityOption> finalCapabilities) {
        super(optionSelector);
        this.finalCapabilities = finalCapabilities;
    }

    @Override
    public List<CapabilityOption> getOptions() {
        if (finalCapabilities == null) {
            try {
                finalCapabilities = new ArrayList<>();
                if (super.getOptions() == null || super.getOptions().isEmpty()) {
                    try {
                        finalCapabilities.addAll(CapabilityFactory.getInstance().getElements().stream()
                                .map(CapabilityOption::new).collect(Collectors.toList()));
                    } catch (InvalidXmlElementException e) {
                        MachineXmlReaderLog.errorMessage(this.getClass(), e);
                    }
                } else {
                    //Add Groups
                    for (CapabilityOption capabilityOption : super.getOptions()) {
                        if (capabilityOption.getGroup() != null) {
                            try {
                                finalCapabilities.addAll(CapabilityFactory.getInstance().getElementsByGroup(capabilityOption.getGroup()).stream()
                                        .map(CapabilityOption::new).collect(Collectors.toList()));
                            } catch (InvalidXmlElementException e) {
                                MachineLog.errorMessage(this.getClass(), e);
                            }
                        } else {
                            //add specialities if needed
                            final Capability capability = CapabilityFactory.getInstance().getElement(capabilityOption.getId());
                            if (capability.getSpecializations() != null && !capability.getSpecializations().isEmpty()) {
                                if (capabilityOption.getSelectedSpecialization() == null) {
                                    //No specialization defined, can be any.
                                    for (Specialization specialization : capability.getSpecializations()) {
                                        finalCapabilities.add(new CapabilityOption(capability, specialization));
                                    }
                                } else {
                                    //Specializations defined. Can be only these.
                                    finalCapabilities.add(new CapabilityOption(capability,
                                            capability.getSpecialization(capabilityOption.getSelectedSpecialization().getId())));
                                }
                            } else {
                                finalCapabilities.add(capabilityOption);
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                throw new InvalidXmlElementException("Error on options '" + getOptions() + "'");
            }
        }
        return finalCapabilities;
    }

    @Override
    public String toString() {
        return "CapabilityOptions{"
                + "(x" + getTotalOptions() + "): "
                + super.getOptions()
                + '}';
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (getOptions() != null) {
            getOptions().forEach(CapabilityOption::validate);
        }
    }
}
