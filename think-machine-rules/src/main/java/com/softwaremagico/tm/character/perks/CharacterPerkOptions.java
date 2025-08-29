package com.softwaremagico.tm.character.perks;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CharacterPerkOptions extends PerkOptions {
    private List<PerkOption> finalPerks;

    public CharacterPerkOptions(PerkOptions perkOptions) {
        setTotalOptions(perkOptions.getTotalOptions());
        setIncludeOpenPerks(perkOptions.isIncludeOpenPerks());
        if (perkOptions.getSourceOptions() != null) {
            setOptions(new ArrayList<>(perkOptions.getSourceOptions()));
        }
        finalPerks = new ArrayList<>();
        super.getOptions().forEach(option -> finalPerks.addAll(option.expandGroup()));
    }

    @Override
    public List<PerkOption> getOptions() {
        return finalPerks;
    }

    public Set<Selection> getAvailableSelections() {
        final Set<Selection> selections = new HashSet<>();
        for (PerkOption option : getOptions()) {
            if (option.getSpecializations() != null) {
                for (Specialization specialization : option.getSpecializations()) {
                    if (option.getId() == null) {
                        throw new RuntimeException("Not woreking");
                    }
                    selections.add(new Selection(option.getId(), specialization));
                }
            } else {
                if (option.getId() == null) {
                    throw new RuntimeException("Not woreking");
                }
                selections.add(new Selection(option.getId()));
            }
        }
        return selections;
    }

    @Override
    public void addOptions(Collection<PerkOption> options) {
        options.forEach(option -> finalPerks.addAll(option.expandGroup()));
    }
}
