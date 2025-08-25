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
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.InvalidSkillException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Set;

public class RandomizeCharacterDefinitionStep {
    private static final int TRIES = 10;

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
        assignCapabilities();
        assignSkills();
        assignPerks();
        assignMaterialAwards();
    }


    private void assignCharacteristics() throws InvalidRandomElementSelectedException {
        if (!characterDefinitionStepSelection.getCharacteristicOptions().isEmpty()) {
            for (int i = 0; i < characterDefinitionStepSelection.getCharacteristicOptions().size(); i++) {
                try {
                    for (int j = 0; j < characterDefinitionStepSelection.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                        //No default selections.
                        if (characterDefinitionStepSelection.getSelectedCharacteristicOptions().get(i).getSelections().size() > j) {
                            continue;
                        }
                        final RandomCharacteristicBonusOption randomCharacteristicBonusOption =
                                new RandomCharacteristicBonusOption(getCharacterPlayer(), getPreferences(),
                                        characterDefinitionStepSelection.getCharacteristicOptions().get(i));
                        characterDefinitionStepSelection.getSelectedCharacteristicOptions().get(i).getSelections()
                                .add(new Selection(randomCharacteristicBonusOption.selectElementByWeight().getId()));
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidXmlElementException("Error on characteristics options '"
                            + characterDefinitionStepSelection.getCharacteristicOptions().get(i) + "'.", e);
                }
            }
        }
    }


    private void assignCapabilities() throws InvalidRandomElementSelectedException {
        if (!characterDefinitionStepSelection.getNotRepeatedCapabilityOptions().isEmpty()) {
            for (int i = 0; i < characterDefinitionStepSelection.getNotRepeatedCapabilityOptions().size(); i++) {
                for (int j = 0; j < characterDefinitionStepSelection.getNotRepeatedCapabilityOptions().get(i).getTotalOptions(); j++) {
                    //No default selections.
                    if (characterDefinitionStepSelection.getSelectedCapabilityOptions().get(i).getSelections().size() > j) {
                        continue;
                    }
                    final RandomCapabilityOption randomCapability =
                            new RandomCapabilityOption(getCharacterPlayer(), getPreferences(),
                                    characterDefinitionStepSelection.getNotRepeatedCapabilityOptions().get(i));
                    try {
                        final CapabilityOption selectedCapability = randomCapability.selectElementByWeight();
                        //Here already exists one capability by specialization.
                        characterDefinitionStepSelection.getSelectedCapabilityOptions().get(i).getSelections()
                                .add(new Selection(selectedCapability.getId(),
                                        selectedCapability.getSelectedSpecialization() != null
                                                ? selectedCapability.getSelectedSpecialization() : null));
                    } catch (InvalidXmlElementException e) {
                        throw new InvalidXmlElementException("Error on capabilities options '"
                                + characterDefinitionStepSelection.getNotRepeatedCapabilityOptions().get(i) + "'.", e);
                    }
                }
            }
        }
    }


    private void assignSkills() throws InvalidRandomElementSelectedException {
        if (!characterDefinitionStepSelection.getSkillOptions().isEmpty()) {
            for (int i = 0; i < characterDefinitionStepSelection.getSkillOptions().size(); i++) {
                try {
                    for (int j = 0; j < characterDefinitionStepSelection.getSkillOptions().get(i).getTotalOptions(); j++) {
                        //No default selections.
                        if (characterDefinitionStepSelection.getSelectedSkillOptions().get(i).getSelections().size() > j) {
                            continue;
                        }

                        int tries = 0;
                        boolean skillFound = false;
                        Skill selectedSkill;
                        do {
                            final RandomSkill randomSkill =
                                    new RandomSkill(getCharacterPlayer(), getPreferences(),
                                            characterDefinitionStepSelection.getSkillOptions().get(i));
                            selectedSkill = randomSkill.selectElementByWeight();
                            try {
                                //Check if skill selections does not exceed skill level limit.
                                getCharacterPlayer().checkMaxValueByLevel(selectedSkill, getCharacterPlayer().getSkillValue(selectedSkill)
                                        + characterDefinitionStepSelection.getSkillOptions().get(i).getSkillBonus(selectedSkill.getId()).getBonus());
                            } catch (InvalidSkillException e) {
                                tries++;
                                if (randomSkill.getWeightedElements().size() > 1) {
                                    randomSkill.removeElementWeight(selectedSkill);
                                }
                                continue;
                            }
                            skillFound = true;
                        } while (!skillFound && tries < TRIES);

                        characterDefinitionStepSelection.getSelectedSkillOptions().get(i).getSelections()
                                .add(new Selection(selectedSkill.getId()));
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidXmlElementException("Error on skill options '"
                            + characterDefinitionStepSelection.getSkillOptions().get(i) + "'.", e);
                }
            }
        }
    }


    private void assignPerks() throws InvalidRandomElementSelectedException {
        if (characterDefinitionStepSelection.getNotRepeatedPerksOptions() != null
                && !characterDefinitionStepSelection.getNotRepeatedPerksOptions().isEmpty()) {
            for (int i = 0; i < characterDefinitionStepSelection.getNotRepeatedPerksOptions().size(); i++) {
                try {
                    for (int j = 0; j < characterDefinitionStepSelection.getNotRepeatedPerksOptions().get(i).getTotalOptions(); j++) {
                        //No default selections.
                        if (characterDefinitionStepSelection.getSelectedPerksOptions().get(i).getSelections().size() > j) {
                            continue;
                        }
                        final RandomPerk randomPerk =
                                new RandomPerk(getCharacterPlayer(), getPreferences(),
                                        characterDefinitionStepSelection.getNotRepeatedPerksOptions().get(i),
                                        characterDefinitionStepSelection.getPhase());
                        characterDefinitionStepSelection.getSelectedPerksOptions().get(i).getSelections()
                                .add(new Selection(randomPerk.selectElementByWeight().getId()));
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidXmlElementException("Error on perks options '"
                            + characterDefinitionStepSelection.getNotRepeatedPerksOptions().get(i) + "'.", e);
                }
            }
        }
    }


    private void assignMaterialAwards() throws InvalidRandomElementSelectedException {
        if (characterDefinitionStepSelection.getMaterialAwardsOptions() != null
                && !characterDefinitionStepSelection.getMaterialAwardsOptions().isEmpty()) {
            for (int i = 0; i < characterDefinitionStepSelection.getMaterialAwardsOptions().size(); i++) {
                try {
                    for (int j = 0; j < characterDefinitionStepSelection.getMaterialAwardsOptions().get(i).getTotalOptions(); j++) {
                        //No default selections.
                        if (characterDefinitionStepSelection.getSelectedMaterialAwards().get(i).getSelections().size() > j) {
                            continue;
                        }
                        final RandomMaterialAward randomMaterialAward =
                                new RandomMaterialAward(getCharacterPlayer(), getPreferences(),
                                        characterDefinitionStepSelection.getMaterialAwardsOptions().get(i));
                        characterDefinitionStepSelection.getSelectedMaterialAwards().get(i).getSelections()
                                .add(new Selection(randomMaterialAward.selectElementByWeight().getId()));
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidXmlElementException("Error on material awards '"
                            + characterDefinitionStepSelection.getMaterialAwardsOptions().get(i) + "'.", e);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "RandomizeCharacterDefinitionStep{"
                + "characterDefinitionStepSelection=" + characterDefinitionStepSelection
                + '}';
    }
}
