package com.softwaremagico.tm.character.perks;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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
import java.util.Set;

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
        this.setOptions(this.finalPerks);
    }

    public PerkOptions(Collection<PerkOption> sourcePerks) {
        super();
        this.setTotalOptions(1);
        this.setOptions(new LinkedHashSet<>(sourcePerks));
        this.setIncludeOpenPerks(false);
        this.initFinalPerks();
    }

    public Set<PerkOption> getFinalPerks() {
        return this.finalPerks;
    }

    @Override
    public LinkedHashSet<PerkOption> getOptions() {
        if (this.finalPerks == null || this.finalPerks.isEmpty()) {
            this.initFinalPerks();
        }
        return new LinkedHashSet<>(this.finalPerks);
    }

    private void initFinalPerks() {
        this.finalPerks = new LinkedHashSet<>();
        for (final PerkOption perkOption : super.getOptions()) {
            this.finalPerks.addAll(perkOption.expandGroup());
        }
        if (this.isIncludeOpenPerks()) {
            PerkFactory.getInstance().getOpenElements().forEach(element -> this.finalPerks.add(new PerkOption(element)));
        }
    }

    public boolean isIncludeOpenPerks() {
        return this.includeOpenPerks;
    }

    public void setIncludeOpenPerks(boolean includeOpenPerks) {
        this.includeOpenPerks = includeOpenPerks;
    }

    @Override
    public String toString() {
        return "PerkOptions{" + "(x" + this.getTotalOptions() + "): " + super.getOptions() + '}';
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (this.getOptions() != null) {
            this.getOptions().forEach(option -> {
                if (option.getId() != null) {
                    PerkFactory.getInstance().getElement(option.getId());
                } else if (option.getGroup() != null
                        && PerkFactory.getInstance().getElementsByGroup(option.getGroup()).isEmpty()) {
                    throw new InvalidXmlElementException("Invalid group '" + option.getGroup() + "' on perk. ");
                }
            });
        }
    }
}
