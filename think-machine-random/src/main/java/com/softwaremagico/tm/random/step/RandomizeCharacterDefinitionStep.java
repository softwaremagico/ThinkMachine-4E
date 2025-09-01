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
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOptions;
import com.softwaremagico.tm.character.equipment.EquipmentOptions;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.InvalidSkillException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.List;
import java.util.Set;

public class RandomizeCharacterDefinitionStep {
    private static final int TRIES = 20;

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

    protected CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
    }

    protected Set<RandomPreference> getPreferences() {
        return preferences;
    }


    public void assign() throws InvalidSelectionException, InvalidRandomElementSelectedException {
        characterDefinitionStepSelection.updateDefaultOptions();
        assignCharacteristics();
        assignCapabilities();
        assignSkills();
        assignPerks();
        assignMaterialAwards();
    }


    private void assignCharacteristics() throws InvalidRandomElementSelectedException {
        final List<CharacteristicBonusOptions> characteristicBonusOptions = characterDefinitionStepSelection.getCharacteristicOptions();
        if (characteristicBonusOptions != null && !characteristicBonusOptions.isEmpty()) {
            for (int i = 0; i < characteristicBonusOptions.size(); i++) {
                try {
                    for (int j = characterDefinitionStepSelection.getSelectedCharacteristicOptions().get(i).getSelections().size();
                         j < characteristicBonusOptions.get(i).getTotalOptions(); j++) {
                        final RandomCharacteristicBonusOption randomCharacteristicBonusOption =
                                new RandomCharacteristicBonusOption(getCharacterPlayer(), getPreferences(),
                                        characteristicBonusOptions.get(i));
                        characterDefinitionStepSelection.getSelectedCharacteristicOptions().get(i).getSelections()
                                .add(new Selection(randomCharacteristicBonusOption.selectElementByWeight().getId()));
                    }
                } catch (InvalidXmlElementException e) {
                    //If no options are available. Force one characteristic and later, will be balanced.
                    characterDefinitionStepSelection.getSelectedCharacteristicOptions().get(i).getSelections()
                            .add(new Selection(characteristicBonusOptions.get(i).getOptions().get(0).getId()));
                }
            }
        }
    }


    private void assignCapabilities() throws InvalidRandomElementSelectedException {
        final List<CapabilityOptions> capabilityOptions = characterDefinitionStepSelection.getNotRepeatedCapabilityOptions();
        if (capabilityOptions != null && !capabilityOptions.isEmpty()) {
            for (int i = 0; i < capabilityOptions.size(); i++) {
                for (int j = characterDefinitionStepSelection.getSelectedCapabilityOptions().get(i).getSelections().size();
                     j < capabilityOptions.get(i).getTotalOptions(); j++) {
                    final RandomCapabilityOption randomCapability =
                            new RandomCapabilityOption(getCharacterPlayer(), getPreferences(),
                                    capabilityOptions.get(i),
                                    characterDefinitionStepSelection.getPhase());
                    try {
                        final CapabilityOption selectedCapability = randomCapability.selectElementByWeight();
                        //Here already exists one capability by specialization.
                        final Selection selection = new Selection(selectedCapability.getId(),
                                selectedCapability.getSelectedSpecialization() != null
                                        ? selectedCapability.getSelectedSpecialization() : null);
                        characterDefinitionStepSelection.getSelectedCapabilityOptions().get(i).getSelections()
                                .add(selection);
                    } catch (InvalidXmlElementException e) {
                        throw new InvalidXmlElementException("Error on capabilities options '"
                                + capabilityOptions.get(i) + "'.", e);
                    }
                }
            }
        }
    }


    private void assignSkills() throws InvalidRandomElementSelectedException {
        final List<SkillBonusOptions> skillOptions = characterDefinitionStepSelection.getSkillOptions();
        if (skillOptions != null && !skillOptions.isEmpty()) {
            for (int i = 0; i < skillOptions.size(); i++) {
                try {
                    for (int j = characterDefinitionStepSelection.getSelectedSkillOptions().get(i).getSelections().size();
                         j < skillOptions.get(i).getTotalOptions(); j++) {

                        int tries = 0;
                        boolean skillFound = false;
                        Skill selectedSkill;
                        do {
                            final RandomSkillBonusOption randomSkill =
                                    new RandomSkillBonusOption(getCharacterPlayer(), getPreferences(),
                                            skillOptions.get(i));
                            selectedSkill = randomSkill.selectElementByWeight();
                            try {
                                //Check if skill selections does not exceed skill level limit.
                                getCharacterPlayer().checkMaxValueByLevel(selectedSkill, getCharacterPlayer().getSkillValue(selectedSkill)
                                        + skillOptions.get(i).getSkillBonus(selectedSkill.getId()).getBonus());
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
                            + skillOptions.get(i) + "'.", e);
                }
            }
        }
    }


    protected void assignPerks() throws InvalidRandomElementSelectedException {
        final List<CharacterPerkOptions> perkOptions = characterDefinitionStepSelection.getNotRepeatedPerksOptions();
        if (perkOptions != null && !perkOptions.isEmpty()) {
            for (int i = 0; i < perkOptions.size(); i++) {
                try {
                    for (int j = characterDefinitionStepSelection.getSelectedPerksOptions().get(i).getSelections().size();
                         j < perkOptions.get(i).getTotalOptions(); j++) {
                        final RandomPerk randomPerk =
                                new RandomPerk(getCharacterPlayer(), getPreferences(),
                                        perkOptions.get(i),
                                        characterDefinitionStepSelection.getPhase());
                        characterDefinitionStepSelection.getSelectedPerksOptions().get(i).getSelections()
                                .add(randomPerk.selectElementByWeight());
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidXmlElementException("Error on perks options '"
                            + perkOptions.get(i) + "'.", e);
                }
            }
        }
    }


    private void assignMaterialAwards() throws InvalidRandomElementSelectedException {
        final List<EquipmentOptions> materialAwardsOptions = characterDefinitionStepSelection.getMaterialAwardsOptions();
        if (materialAwardsOptions != null && !materialAwardsOptions.isEmpty()) {
            for (int i = 0; i < materialAwardsOptions.size(); i++) {
                try {
                    for (int j = characterDefinitionStepSelection.getSelectedMaterialAwards().get(i).getSelections().size();
                         j < materialAwardsOptions.get(i).getTotalOptions(); j++) {
                        final RandomMaterialAward randomMaterialAward =
                                new RandomMaterialAward(getCharacterPlayer(), getPreferences(),
                                        materialAwardsOptions.get(i));
                        characterDefinitionStepSelection.getSelectedMaterialAwards().get(i).getSelections()
                                .add(new Selection(randomMaterialAward.selectElementByWeight().getId()));
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidXmlElementException("Error on material awards '"
                            + materialAwardsOptions.get(i) + "'.", e);
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
