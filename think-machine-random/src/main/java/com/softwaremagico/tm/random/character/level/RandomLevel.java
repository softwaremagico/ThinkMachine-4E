package com.softwaremagico.tm.random.character.level;

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
import com.softwaremagico.tm.character.level.Level;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.AssignableRandomSelector;
import com.softwaremagico.tm.random.character.selectors.RandomInnerStepsSelector;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomizeCharacterLevelStep;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RandomLevel extends RandomSelector<Level> implements AssignableRandomSelector, RandomInnerStepsSelector {
    private LevelSelector levelSelector;

    public RandomLevel(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }


    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        levelSelector = getCharacterPlayer().addLevel();
    }

    @Override
    public void complete() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        final RandomizeCharacterLevelStep randomizeCharacterDefinitionStep = new RandomizeCharacterLevelStep(
                getCharacterPlayer(),
                levelSelector,
                getPreferences()
        );

        randomizeCharacterDefinitionStep.assign();
    }

    @Override
    protected Collection<Level> getAllElements() throws InvalidXmlElementException {
        //Levels are auto-generated.
        return List.of();
    }
}
