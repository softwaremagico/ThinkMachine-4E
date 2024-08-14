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
import com.softwaremagico.tm.XmlData;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CapabilityOptions extends XmlData {
    @JsonProperty("total")
    private int totalOptions;
    @JsonProperty("capabilities")
    private List<CapabilityOption> capabilities;
    @JsonIgnore
    private List<CapabilityOption> finalCapabilities;


    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
    }

    public List<CapabilityOption> getCapabilities() {
        if (finalCapabilities == null) {
            finalCapabilities = new ArrayList<>();
            if (this.capabilities == null || this.capabilities.isEmpty()) {
                try {
                    finalCapabilities.addAll(CapabilityFactory.getInstance().getElements().stream()
                            .map(CapabilityOption::new).collect(Collectors.toList()));
                } catch (InvalidXmlElementException e) {
                    MachineXmlReaderLog.errorMessage(this.getClass(), e);
                }
            } else {
                //Add Groups
                for (CapabilityOption capabilityOption : this.capabilities) {
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
                        if (capability.getSpecializations() != null && !capability.getSpecializations().isEmpty()
                                && capabilityOption.getSpecialization() == null) {
                            //No specialization defined, can be any.
                            for (Specialization specialization : capability.getSpecializations()) {
                                finalCapabilities.add(new CapabilityOption(capability, specialization));
                            }
                        } else {
                            finalCapabilities.add(capabilityOption);
                        }
                    }
                }
            }
        }
        return finalCapabilities;
    }

    public void setCapabilities(List<CapabilityOption> capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public String toString() {
        return "CapabilityOptions{"
                + "(x" + totalOptions + "): "
                + capabilities
                + '}';
    }
}
