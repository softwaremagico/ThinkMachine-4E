package com.softwaremagico.tm.random.step;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Set;

public class RandomizeCharacterDefinitionStep<T extends Element> {

    private final CharacterPlayer characterPlayer;
    private final CharacterDefinitionStepSelection characterDefinitionStepSelection;
    private final Set<RandomPreference> preferences;


    public RandomizeCharacterDefinitionStep(CharacterPlayer characterPlayer,
                                            CharacterDefinitionStepSelection characterDefinitionStepSelection,
                                            Set<RandomPreference> preferences) {
        this.characterPlayer = characterPlayer;
        this.characterDefinitionStepSelection = characterDefinitionStepSelection;
        this.preferences = preferences;
    }

    private CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
    }

    private Set<RandomPreference> getPreferences() {
        return preferences;
    }


    public void assign() throws InvalidSelectionException, InvalidRandomElementSelectedException {
        assignCharacteristics();
    }


    private void assignCharacteristics() throws InvalidRandomElementSelectedException {
        if (!characterDefinitionStepSelection.getCharacteristicOptions().isEmpty()) {
            for (int i = 0; i < characterDefinitionStepSelection.getCharacteristicOptions().size(); i++) {
                final RandomCharacteristicBonusOption randomCharacteristicBonusOption =
                        new RandomCharacteristicBonusOption(getCharacterPlayer(), getPreferences(),
                                characterDefinitionStepSelection.getCharacteristicOptions().get(i));

                for (int j = 0; j < characterDefinitionStepSelection.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                    characterDefinitionStepSelection.getSelectedCharacteristicOptions().get(i).getSelections()
                            .add(new Selection(randomCharacteristicBonusOption.selectElementByWeight().getId()));
                }
            }
        }
    }

}
