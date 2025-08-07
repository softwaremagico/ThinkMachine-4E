package com.softwaremagico.tm.random.character.upbringings;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomizeCharacterDefinitionStep;

import java.util.Collection;
import java.util.Set;

public class RandomUpbringing extends RandomSelector<Upbringing> {

    public RandomUpbringing(CharacterPlayer characterPlayer, Set<RandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getUpbringing() == null || getCharacterPlayer().getUpbringing().getId() == null) {
            getCharacterPlayer().setUpbringing(selectElementByWeight().getId());
        }

        final RandomizeCharacterDefinitionStep<Upbringing> randomizeCharacterDefinitionStep = new RandomizeCharacterDefinitionStep<>(
                getCharacterPlayer(),
                getCharacterPlayer().getUpbringing(),
                getPreferences()
        );

        randomizeCharacterDefinitionStep.assign();
    }

    @Override
    protected Collection<Upbringing> getAllElements() throws InvalidXmlElementException {
        return UpbringingFactory.getInstance().getElements();
    }

    @Override
    protected int getWeight(Upbringing upbringing) throws InvalidRandomElementSelectedException {
        // Humans only humans factions.
        if (!upbringing.getRestrictions().getRestrictedToSpecies().isEmpty() && getCharacterPlayer().getSpecie() != null
                && !upbringing.getRestrictions().getRestrictedToSpecies().contains(getCharacterPlayer().getSpecie().getId())) {
            throw new InvalidRandomElementSelectedException("Faction '" + upbringing + "' restricted for species '"
                    + upbringing.getRestrictions().getRestrictedToSpecies()
                    + "'. Character is '" + getCharacterPlayer().getSpecie() + "'.");
        }

        return super.getWeight(upbringing);
    }
}
