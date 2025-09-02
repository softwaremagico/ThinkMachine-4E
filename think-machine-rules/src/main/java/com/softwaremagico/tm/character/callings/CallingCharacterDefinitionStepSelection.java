package com.softwaremagico.tm.character.callings;

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
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.InvalidCallingException;
import com.softwaremagico.tm.exceptions.InvalidGeneratedCharacter;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CallingCharacterDefinitionStepSelection extends CharacterDefinitionStepSelection {
    private static final int CALLING_SKILL_POINTS = 10;

    public CallingCharacterDefinitionStepSelection(CharacterPlayer characterPlayer, String calling) throws InvalidGeneratedCharacter {
        super(characterPlayer, CallingFactory.getInstance().getElement(calling));
        setId(calling);
    }


    @Override
    public void validate() throws InvalidSelectionException {
        if (getCharacterPlayer().getFaction() == null) {
            throw new InvalidCallingException("No faction selected. Select a faction first.");
        }
        try {
            super.validate();
        } catch (InvalidSelectionException e) {
            throw new InvalidCallingException(e.getMessage(), e);
        }
    }

    @Override
    public Phase getPhase() {
        return Phase.CALLING;
    }


    @Override
    @JsonIgnore
    protected int getSkillTotalPoints() {
        return CALLING_SKILL_POINTS;
    }

    @Override
    public List<CharacterPerkOptions> getPerksOptions() {
        final List<CharacterPerkOptions> callingPerks = getCharacterDefinitionStep().getCharacterAvailablePerksOptions();
        addSpeciePerks(callingPerks);
        return callingPerks;
    }

    /**
     * Some species have default perks for callings
     *
     * @return
     */
    @Override
    public List<CharacterPerkOptions> getNotSelectedPerksOptions() {
        final Set<CharacterPerkOptions> callingPerks = new HashSet<>();
        super.getNotSelectedPerksOptions().forEach(perkOptions -> callingPerks.add(new CharacterPerkOptions(perkOptions)));
        addSpeciePerks(callingPerks);
        //List without duplicates.
        return new ArrayList<>(callingPerks);
    }

    private void addSpeciePerks(final Collection<CharacterPerkOptions> callingPerks) {
        if (getCharacterPlayer().getSpecie() != null) {
            final List<PerkOption> speciePerks = SpecieFactory.getInstance().getElement(getCharacterPlayer().getSpecie().getId()).getPerks();
            callingPerks.forEach(perkOptions -> {
                //Add no duplicates.
                if (speciePerks != null) {
                    speciePerks.forEach(speciePerk -> PerkFactory.getInstance().getElements(speciePerk).forEach(perk -> {
                        if (!perk.getRestrictions().isRestricted(getCharacterPlayer()) && !perkOptions.getOptions().contains(speciePerk)) {
                            perkOptions.getOptions().addAll(speciePerk.expandGroup());
                        }
                    }));
                }
            });
        }
    }
}

