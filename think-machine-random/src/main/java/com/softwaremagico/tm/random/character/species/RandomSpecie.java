package com.softwaremagico.tm.random.character.species;

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
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.RandomSelectorLog;
import com.softwaremagico.tm.random.character.selectors.AssignableRandomSelector;
import com.softwaremagico.tm.random.character.selectors.RandomInnerStepsSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.RandomSelector;
import com.softwaremagico.tm.random.preferences.SpeciePreference;
import com.softwaremagico.tm.random.step.RandomizeCharacterDefinitionStep;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomSpecie extends RandomSelector<Specie> implements AssignableRandomSelector, RandomInnerStepsSelector {

    public RandomSpecie(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    protected int getWeight(Specie element) throws InvalidRandomElementSelectedException {
        final Set<String> mandatorySpecies = getProfiles().stream()
                .flatMap(profile -> profile.getMandatorySpecies().stream())
                .collect(Collectors.toSet());
        if (!mandatorySpecies.isEmpty() && !mandatorySpecies.contains(element.getId())) {
            return 0;
        }
        return super.getWeight(element);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getSpecie() == null || getCharacterPlayer().getSpecie().getId() == null) {
            getCharacterPlayer().setSpecie(getPreferredSpecieId());
            if (getCharacterPlayer().getSpecie() != null && getCharacterPlayer().getSpecie().getId() != null) {
                RandomSelectorLog.info(RandomSpecie.class, "Specie selected is '{}'.", getCharacterPlayer().getSpecie().getId());
            } else {
                RandomSelectorLog.warning(RandomSpecie.class, "No specie selected!.");
            }
        }
    }

    private String getPreferredSpecieId() throws InvalidRandomElementSelectedException {
        for (final IRandomPreference preference : getPreferences()) {
            if (SpeciePreference.XENO.name().equals(preference.name())
                    || SpeciePreference.HUMAN.name().equals(preference.name())) {
                final boolean xeno = SpeciePreference.XENO.name().equals(preference.name());
                final List<Specie> species = getAllElements().stream().filter(specie -> specie.isXeno() == xeno).toList();
                if (species.isEmpty()) {
                    throw new InvalidRandomElementSelectedException("No species available for preference '" + preference + "'.");
                }
                return species.get(RANDOM.nextInt(species.size())).getId();
            }
        }
        return selectElementByWeight().getId();
    }

    @Override
    public void complete() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        final RandomizeCharacterDefinitionStep randomizeCharacterDefinitionStep = new RandomizeCharacterDefinitionStep(
                getCharacterPlayer(),
                getCharacterPlayer().getSpecie(),
                getPreferences()
        );
        randomizeCharacterDefinitionStep.assign();
    }

    @Override
    protected Collection<Specie> getAllElements() throws InvalidXmlElementException {
        return SpecieFactory.getInstance().getSelectableElements();
    }
}
