package com.softwaremagico.tm.character;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonus;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.skills.SkillBonus;
import com.softwaremagico.tm.exceptions.InvalidGeneratedCharacter;
import com.softwaremagico.tm.exceptions.InvalidSelectedElementException;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.TooManySelectionsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CharacterDefinitionStepSelection<T extends CharacterDefinitionStepSelection<?>> extends Element<T> {

    private final CharacterPlayer characterPlayer;
    private final CharacterDefinitionStep<?> characterDefinitionStep;

    @JsonProperty("capabilities")
    private List<CharacterSelectedElement> capabilityOptions;
    @JsonProperty("characteristics")
    private List<CharacterSelectedElement> characteristicOptions;
    @JsonProperty("skills")
    private List<CharacterSelectedElement> skillOptions;
    @JsonProperty("perks")
    private List<CharacterSelectedElement> perksOptions;

    //TODO(softwaremagico): implement this.
    private boolean raisedInSpace;

    public CharacterDefinitionStepSelection(CharacterPlayer characterPlayer, CharacterDefinitionStep<?> characterDefinitionStep)
            throws InvalidGeneratedCharacter {
        copy(characterDefinitionStep);

        this.characterPlayer = characterPlayer;
        this.characterDefinitionStep = characterDefinitionStep;

        setCapabilityOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getCapabilityOptions().size()]));
        for (int i = 0; i < characterDefinitionStep.getCapabilityOptions().size(); i++) {
            capabilityOptions.set(i, new CharacterSelectedElement());
        }

        setCharacteristicOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getCharacteristicOptions().size()]));
        for (int i = 0; i < characterDefinitionStep.getCharacteristicOptions().size(); i++) {
            characteristicOptions.set(i, new CharacterSelectedElement());
        }

        setSkillOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getSkillOptions().size()]));
        for (int i = 0; i < characterDefinitionStep.getSkillOptions().size(); i++) {
            skillOptions.set(i, new CharacterSelectedElement());
        }

        setPerksOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getPerksOptions().size()]));
        for (int i = 0; i < characterDefinitionStep.getPerksOptions().size(); i++) {
            perksOptions.set(i, new CharacterSelectedElement());
        }
    }

    public CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
    }

    public List<CharacterSelectedElement> getCapabilityOptions() {
        return capabilityOptions;
    }

    public void setCapabilityOptions(List<CharacterSelectedElement> capabilityOptions) {
        this.capabilityOptions = capabilityOptions;
    }

    public List<CharacterSelectedElement> getCharacteristicOptions() {
        return characteristicOptions;
    }

    public void setCharacteristicOptions(List<CharacterSelectedElement> characteristicOptions) {
        this.characteristicOptions = characteristicOptions;
    }

    public List<CharacterSelectedElement> getSkillOptions() {
        return skillOptions;
    }

    public void setSkillOptions(List<CharacterSelectedElement> skillOptions) {
        this.skillOptions = skillOptions;
    }

    public List<CharacterSelectedElement> getPerksOptions() {
        return perksOptions;
    }

    public void setPerksOptions(List<CharacterSelectedElement> perksOptions) {
        this.perksOptions = perksOptions;
    }

    public int getCharacteristicBonus(String characteristic) {
        int bonus = 0;
        for (int i = 0; i < getCharacteristicOptions().size(); i++) {
            if (getCharacteristicOptions().get(i).getSelections().contains(characteristic)) {
                bonus += characterDefinitionStep.getCharacteristicOptions().get(i).getCharacteristicBonus(characteristic).getBonus();
            }
        }
        return bonus;
    }

    public int getSkillBonus(String skill) {
        int bonus = 0;
        for (int i = 0; i < getSkillOptions().size(); i++) {
            for (int j = 0; j < getSkillOptions().get(i).getSelections().size(); j++) {
                if (getSkillOptions().get(i).getSelections().contains(skill)) {
                    bonus += characterDefinitionStep.getSkillOptions().get(i).getSkillBonus(skill).getBonus();
                }
            }
        }
        return bonus;
    }

    public List<String> getSelectedCapabilities() {
        final List<String> selectedCapabilities = new ArrayList<>();
        capabilityOptions.forEach(capabilityOption ->
                selectedCapabilities.addAll(capabilityOption.getSelections()));
        return selectedCapabilities;
    }

    public List<String> getSelectedPerks() {
        final List<String> selectedPerks = new ArrayList<>();
        perksOptions.forEach(perkOption ->
                selectedPerks.addAll(perkOption.getSelections()));
        return selectedPerks;
    }

    @Override
    public void validate() throws InvalidSelectionException {
        super.validate();
        if (getRestrictions().isRestricted(characterPlayer)) {
            throw new InvalidSelectionException("Restrictions for  '" + getId() + "' are not meet.");
        }

        //Capabilities
        for (int i = 0; i < capabilityOptions.size(); i++) {
            if (capabilityOptions.get(i).getSelections().size() > characterDefinitionStep.getCapabilityOptions().get(i).getCapabilities().size()) {
                throw new TooManySelectionsException("You have selected '" + capabilityOptions.get(i).getSelections().size()
                        + "' capabilities options and only '"
                        + characterDefinitionStep.getCapabilityOptions().get(i).getCapabilities().size() + "' are available.");
            }
            final List<String> availableOptions = characterDefinitionStep.getCapabilityOptions().get(i).getCapabilities()
                    .stream().map(CapabilityOption::getId).collect(Collectors.toList());
            for (String selection : capabilityOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected capability '" + selection + "' does not exist.", selection);
                }
            }
        }

        //Characteristics
        for (int i = 0; i < characteristicOptions.size(); i++) {
            if (characteristicOptions.get(i).getSelections().size() > characterDefinitionStep.getCharacteristicOptions().get(i).getCharacteristics().size()) {
                throw new TooManySelectionsException("You have selected '" + characteristicOptions.get(i).getSelections().size()
                        + "' characteristics options and only '"
                        + characterDefinitionStep.getCharacteristicOptions().get(i).getCharacteristics().size()
                        + "' are available.");
            }
            final List<String> availableOptions = characterDefinitionStep.getCharacteristicOptions().get(i).getCharacteristics()
                    .stream().map(CharacteristicBonus::getCharacteristic).collect(Collectors.toList());
            for (String selection : characteristicOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected characteristic '" + selection + "' does not exist.", selection);
                }
            }
        }

        //Skills
        for (int i = 0; i < skillOptions.size(); i++) {
            if (skillOptions.get(i).getSelections().size() > characterDefinitionStep.getSkillOptions().get(i).getSkills().size()) {
                throw new TooManySelectionsException("You have selected '" + skillOptions.get(i).getSelections().size()
                        + "' capabilities options and only '"
                        + characterDefinitionStep.getSkillOptions().get(i).getSkills().size()
                        + "' are available.");
            }
            final List<String> availableOptions = characterDefinitionStep.getSkillOptions().get(i).getSkills()
                    .stream().map(SkillBonus::getSkill).collect(Collectors.toList());
            for (String selection : skillOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected skill '" + selection + "' does not exist.", selection);
                }
            }
        }

        //Perks
        for (int i = 0; i < perksOptions.size(); i++) {
            if (perksOptions.get(i).getSelections().size() > characterDefinitionStep.getPerksOptions().get(i).getPerks().size()) {
                throw new TooManySelectionsException("You have selected '" + perksOptions.get(i).getSelections().size()
                        + "' capabilities options and only '"
                        + characterDefinitionStep.getPerksOptions().get(i).getPerks().size()
                        + "' are available.");
            }
            final List<String> availableOptions = characterDefinitionStep.getPerksOptions().get(i).getPerks()
                    .stream().map(PerkOption::getId).collect(Collectors.toList());
            for (String selection : perksOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected perk '" + selection + "' does not exist.", selection);
                }
            }
        }
    }
}
