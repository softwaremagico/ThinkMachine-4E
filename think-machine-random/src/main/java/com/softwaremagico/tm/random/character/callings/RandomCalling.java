package com.softwaremagico.tm.random.character.callings;

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
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.callings.CallingGroup;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.AssignableRandomSelector;
import com.softwaremagico.tm.random.character.selectors.RandomInnerStepsSelector;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomizeCharacterDefinitionStep;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RandomCalling extends RandomSelector<Calling> implements AssignableRandomSelector, RandomInnerStepsSelector {

    public RandomCalling(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getCalling() == null || getCharacterPlayer().getCalling().getId() == null) {
            getCharacterPlayer().setCalling(selectElementByWeight().getId());
        }
    }

    @Override
    public void complete() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        final RandomizeCharacterDefinitionStep randomizeCharacterDefinitionStep = new RandomizeCharacterDefinitionStep(
                getCharacterPlayer(),
                getCharacterPlayer().getCalling(),
                getPreferences()
        );

        randomizeCharacterDefinitionStep.assign();
    }

    @Override
    protected Collection<Calling> getAllElements() throws InvalidXmlElementException {
        return CallingFactory.getInstance().getElements();
    }

    @Override
    protected int getWeight(Calling calling) throws InvalidRandomElementSelectedException {
        //Avoid missing theurgy factions with psi and viceversa.
        if (calling.getGroup() != null && CallingGroup.get(calling.getGroup()) == CallingGroup.PSI
                && getCharacterPlayer().getOccultismType() == OccultismTypeFactory.getTheurgy()) {
            return 0;
        }
        if (calling.getGroup() != null && CallingGroup.get(calling.getGroup()) == CallingGroup.THEURGY
                && getCharacterPlayer().getOccultismType() == OccultismTypeFactory.getPsi()) {
            return 0;
        }

        //Brother Battle almos always brother Battle faction.
        if (Objects.equals(getCharacterPlayer().getFaction().getId(), BROTHER_BATTLE)) {
            if (Objects.equals(calling.getId(), BROTHER_BATTLE)) {
                return GOOD_PROBABILITY;
            } else {
                return EXOTIC_PROBABILITY;
            }
        }

        if (FactionFactory.getInstance().getElement(getCharacterPlayer().getFaction()).getFavoredCallings().contains(calling.getId())) {
            return GOOD_PROBABILITY;
        }
        return super.getWeight(calling);
    }
}
