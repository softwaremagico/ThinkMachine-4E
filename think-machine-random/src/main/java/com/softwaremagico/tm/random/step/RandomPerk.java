package com.softwaremagico.tm.random.step;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.OccultismPreference;
import com.softwaremagico.tm.random.preferences.RandomSelector;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RandomPerk extends RandomSelector<Selection> {

    private final CharacterPerkOptions perkOptions;
    private final Phase phase;
    private final Integer level;

    public RandomPerk(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences,
                      CharacterPerkOptions perkOptions, Phase phase, Integer level) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.perkOptions = perkOptions;
        this.phase = phase;
        this.level = level;
    }

    @Override
    protected Collection<Selection> getAllElements() throws InvalidXmlElementException {
        return perkOptions.getAvailableSelections();
    }


    @Override
    protected int getWeight(Selection element) throws InvalidRandomElementSelectedException {
        // Already has a perk.
        if (!element.isRepeatable() && getCharacterPlayer().hasSelection(element, phase, null)) {
            return 0;
        }
        //Reduce the elements with multiple specializations.
        final Perk sourcePerk = PerkFactory.getInstance().getElement(element);
        if (sourcePerk.getSpecializations() != null && !sourcePerk.getSpecializations().isEmpty()) {
            return (int) Math.ceil((double) BASIC_PROBABILITY / sourcePerk.getSpecializations().size());
        }
        //Ensure occultists and theurgist has the needed perks.
        if (getPreferences().contains(OccultismPreference.PSI)
                && Objects.equals(element.getId(), PerkFactory.PSYCHIC_POWERS_PERK)) {
            return GOOD_PROBABILITY;
        }
        if (getPreferences().contains(OccultismPreference.THEURGY)
                && Objects.equals(element.getId(), PerkFactory.THEURGY_RITES_PERK)) {
            return GOOD_PROBABILITY;
        }
        return super.getWeight(element);
    }
}
