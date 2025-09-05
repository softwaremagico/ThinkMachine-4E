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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Intermediate class that converts perk definitions to real character selectable perk options.
 * Selections contains the current possible selections for the character.
 */
public class CharacterPerkOptions extends PerkOptions {
    private LinkedHashSet<PerkOption> finalPerks;
    private Set<Selection> availableSelections;

    public CharacterPerkOptions(PerkOptions perkOptions) {
        setTotalOptions(perkOptions.getTotalOptions());
        setIncludeOpenPerks(perkOptions.isIncludeOpenPerks());
        if (perkOptions.getSourceOptions() != null) {
            setOptions(new LinkedHashSet<>(perkOptions.getSourceOptions()));
        }
        updateFinalPerks();
    }

    private void updateFinalPerks() {
        finalPerks = new LinkedHashSet<>();
        if (getFinalPerks() != null) {
            getFinalPerks().clear();
        }
        super.getOptions().forEach(option -> finalPerks.addAll(option.expandGroup()));
    }

    public CharacterPerkOptions(CharacterPerkOptions perkOptions, Set<Selection> availableSelections) {
        this(perkOptions);
        setAvailableSelections(availableSelections);
    }

    @Override
    public LinkedHashSet<PerkOption> getOptions() {
        return finalPerks;
    }

    public Set<Selection> getAvailableSelections() {
        if (availableSelections == null) {
            availableSelections = new HashSet<>();
            getOptions().forEach(option -> availableSelections.addAll(option.getOptionsBySpecialization()));
        }
        return availableSelections;
    }

    public void setAvailableSelections(Set<Selection> availableSelections) {
        this.availableSelections = availableSelections;
    }

    public static CharacterPerkOptions combineCharacterPerks(List<CharacterPerkOptions> characterPerkOptionsList) {
        if (characterPerkOptionsList == null || characterPerkOptionsList.isEmpty()) {
            return null;
        }
        final CharacterPerkOptions characterPerkOptions = new CharacterPerkOptions(characterPerkOptionsList.get(0));
        for (int i = 1; i < characterPerkOptionsList.size(); i++) {
            characterPerkOptions.getSourceOptions().addAll(characterPerkOptionsList.get(i).getSourceOptions());
            if (characterPerkOptionsList.get(i).isIncludeOpenPerks()) {
                characterPerkOptions.setIncludeOpenPerks(true);
            }
        }
        characterPerkOptions.updateFinalPerks();
        return characterPerkOptions;
    }
}
