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

import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.List;
import java.util.Set;

public class RandomizeCharacterLevelStep extends RandomizeCharacterDefinitionStep {
    private final LevelSelector levelSelector;

    public RandomizeCharacterLevelStep(CharacterPlayer characterPlayer, CharacterDefinitionStepSelection characterDefinitionStepSelection,
                                       Set<RandomPreference> preferences) {
        super(characterPlayer, characterDefinitionStepSelection, preferences);
        this.levelSelector = (LevelSelector) characterDefinitionStepSelection;
    }

    @Override
    protected void assignPerks() throws InvalidRandomElementSelectedException {
        assignClassPerks();
        assignCallingPerks();
    }

    private void assignClassPerks() throws InvalidRandomElementSelectedException {
        final List<CharacterPerkOptions> perkOptions = levelSelector.getNotSelectedPerksOptions();

        if (perkOptions != null && !perkOptions.isEmpty()) {
            for (int i = 0; i < perkOptions.size(); i++) {
                try {
                    for (int j = levelSelector.getSelectedClassPerksOptions().get(i).getSelections().size();
                         j < perkOptions.get(i).getTotalOptions(); j++) {
                        final RandomPerk randomPerk =
                                new RandomPerk(getCharacterPlayer(), getPreferences(),
                                        perkOptions.get(i),
                                        levelSelector.getPhase(), levelSelector.getLevel());
                        levelSelector.getSelectedClassPerksOptions().get(i).getSelections()
                                .add(randomPerk.selectElementByWeight());
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidXmlElementException("Error on perks options '"
                            + perkOptions.get(i) + "' from level.", e);
                }
            }
        }
    }

    private void assignCallingPerks() throws InvalidRandomElementSelectedException {
        final List<CharacterPerkOptions> perkOptions = levelSelector.getNotRepeatedCallingPerksOptions();

        if (perkOptions != null && !perkOptions.isEmpty()) {
            for (int i = 0; i < perkOptions.size(); i++) {
                try {
                    for (int j = levelSelector.getSelectedCallingPerksOptions().get(i).getSelections().size();
                         j < perkOptions.get(i).getTotalOptions(); j++) {
                        final RandomPerk randomPerk =
                                new RandomPerk(getCharacterPlayer(), getPreferences(),
                                        perkOptions.get(i),
                                        levelSelector.getPhase(), levelSelector.getLevel());
                        levelSelector.getSelectedCallingPerksOptions().get(i).getSelections()
                                .add(randomPerk.selectElementByWeight());
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidXmlElementException("Error on perks options '"
                            + perkOptions.get(i) + "' from level.", e);
                }
            }
        }
    }

}
