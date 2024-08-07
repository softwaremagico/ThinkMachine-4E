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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.XmlData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PerkOptions extends XmlData {
    @JsonProperty("total")
    private int totalOptions = 1;
    @JsonProperty("perks")
    private List<PerkOption> perks;
    @JsonProperty("openPerks")
    private boolean includeOpenPerks = true;

    public PerkOptions() {
        super();
    }

    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
    }

    public List<PerkOption> getPerks() {
        return perks;
    }

    public void addPerks(Collection<PerkOption> perks) {
        if (this.perks == null) {
            this.perks = new ArrayList<>();
        }
        this.perks.addAll(perks);
    }

    public void setPerks(List<PerkOption> perks) {
        this.perks = perks;
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
        if (this.getPerks() != null) {
            perkOptions.setPerks(new ArrayList<>(this.getPerks()));
        }
        return perkOptions;
    }
}
