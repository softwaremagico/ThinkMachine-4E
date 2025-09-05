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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class PerkOptions extends OptionSelector<Perk, PerkOption> {
    @JsonIgnore
    private LinkedHashSet<PerkOption> finalPerks;
    @JsonProperty("openPerks")
    private boolean includeOpenPerks = true;

    public PerkOptions() {
        super();
    }

    public PerkOptions(PerkOptions perkOptions) {
        super(perkOptions);
        if (perkOptions.finalPerks != null) {
            this.finalPerks = new LinkedHashSet<>(perkOptions.finalPerks);
        }
        this.includeOpenPerks = perkOptions.includeOpenPerks;
    }

    public PerkOptions(PerkOptions optionSelector, List<PerkOption> finalPerks) {
        super(optionSelector);
        this.finalPerks = new LinkedHashSet<>(finalPerks);
        setOptions(this.finalPerks);
    }

    public PerkOptions(Collection<PerkOption> sourcePerks) {
        super();
        setTotalOptions(1);
        setOptions(new LinkedHashSet<>(sourcePerks));
        setIncludeOpenPerks(false);
        initFinalPerks();
    }

    public LinkedHashSet<PerkOption> getFinalPerks() {
        return finalPerks;
    }

    @Override
    public LinkedHashSet<PerkOption> getOptions() {
        if (finalPerks == null || finalPerks.isEmpty()) {
            initFinalPerks();
        }
        return new LinkedHashSet<>(finalPerks);
    }

    private void initFinalPerks() {
        finalPerks = new LinkedHashSet<>();
        for (PerkOption perkOption : super.getOptions()) {
            finalPerks.addAll(perkOption.expandGroup());
        }
        if (isIncludeOpenPerks()) {
            PerkFactory.getInstance().getOpenElements().forEach(element -> finalPerks.add(new PerkOption(element)));
        }
    }


    public boolean isIncludeOpenPerks() {
        return includeOpenPerks;
    }

    public void setIncludeOpenPerks(boolean includeOpenPerks) {
        this.includeOpenPerks = includeOpenPerks;
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
                } else if (option.getGroup() != null) {
                    if (PerkFactory.getInstance().getElementsByGroup(option.getGroup()).isEmpty()) {
                        throw new InvalidXmlElementException("Invalid group '" + option.getGroup() + "' on perk. ");
                    }
                }
            });
        }
    }
}
