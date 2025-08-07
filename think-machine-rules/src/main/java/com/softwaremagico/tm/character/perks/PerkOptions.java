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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.OptionSelector;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerkOptions extends OptionSelector<Perk, PerkOption> {
    @JsonIgnore
    private List<PerkOption> finalPerks;
    @JsonProperty("openPerks")
    private boolean includeOpenPerks = true;

    public PerkOptions() {
        super();
    }

    public PerkOptions(PerkOptions perkOptions) {
        super(perkOptions);
        this.finalPerks = new ArrayList<>(perkOptions.finalPerks);
        this.includeOpenPerks = perkOptions.includeOpenPerks;
    }

    @Override
    public List<PerkOption> getOptions() {
        if (finalPerks == null) {
            finalPerks = new ArrayList<>();
            for (PerkOption perkOptions : super.getOptions()) {
                if (perkOptions.getGroup() != null) {
                    try {
                        finalPerks.addAll(PerkFactory.getInstance().getElementsByGroup(perkOptions.getGroup()).stream()
                                .map(PerkOption::new).collect(Collectors.toList()));
                    } catch (InvalidXmlElementException e) {
                        MachineLog.errorMessage(this.getClass(), e);
                    }
                } else {
                    finalPerks.add(perkOptions);
                }
            }
        }
        return finalPerks;
    }

    public boolean isIncludeOpenPerks() {
        return includeOpenPerks;
    }

    public void setIncludeOpenPerks(boolean includeOpenPerks) {
        this.includeOpenPerks = includeOpenPerks;
    }

    public PerkOptions copy() {
        final PerkOptions perkOptions = new PerkOptions();
        perkOptions.setTotalOptions(this.getTotalOptions());
        perkOptions.setIncludeOpenPerks(this.isIncludeOpenPerks());
        if (super.getOptions() != null) {
            perkOptions.setOptions(new ArrayList<>(super.getOptions()));
        }
        return perkOptions;
    }

    @Override
    public String toString() {
        return "PerkOptions{"
                + "(x" + getTotalOptions() + "): "
                + super.getOptions()
                + '}';
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (getOptions() != null) {
            getOptions().forEach(option -> {
                if (option.getId() != null) {
                    PerkFactory.getInstance().getElement(option.getId());
                }
            });
        }
    }
}
