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
import com.softwaremagico.tm.OptionSelector;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.equipment.CharacterSelectedEquipment;
import com.softwaremagico.tm.exceptions.InvalidGeneratedCharacter;
import com.softwaremagico.tm.exceptions.InvalidSelectedElementException;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.TooManySelectionsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class CharacterDefinitionStepSelection extends Element {

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
    @JsonProperty("materialAwards")
    private List<CharacterSelectedEquipment> materialAwards;

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

        setMaterialAwards(Arrays.asList(new CharacterSelectedEquipment[characterDefinitionStep.getMaterialAwards().size()]));
        for (int i = 0; i < characterDefinitionStep.getMaterialAwards().size(); i++) {
            materialAwards.set(i, new CharacterSelectedEquipment());
        }

        selectDefaultOptions();
    }

    public void selectDefaultOptions() {
        setDefaultOptions(new ArrayList<>(characterDefinitionStep.getCapabilityOptions()), capabilityOptions);
        setDefaultOptions(new ArrayList<>(characterDefinitionStep.getCharacteristicOptions()), characteristicOptions);
        setDefaultOptions(new ArrayList<>(characterDefinitionStep.getSkillOptions()), skillOptions);
        setDefaultOptions(new ArrayList<>(characterDefinitionStep.getPerksOptions()), perksOptions);
    }

    private void setDefaultOptions(List<OptionSelector<?, ?>> options, List<CharacterSelectedElement> selectedElements) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getOptions().size() == 1) {
                if (options.get(i).getOptions().get(0) instanceof CapabilityOption) {
                    if (((CapabilityOption) options.get(i).getOptions().get(0)).getSelectedSpecialization() != null) {
                        selectedElements.get(i).setSelections(new ArrayList<>(List.of(new Selection(options
                                .get(i).getOptions().get(0).getId(),
                                ((CapabilityOption) options.get(i).getOptions().get(0)).getSelectedSpecialization()))));
                    }
                } else if (options.get(i).getOptions().get(0).getSpecializations() == null
                        || options.get(i).getOptions().get(0).getSpecializations().isEmpty()) {
                    selectedElements.get(i).setSelections(new ArrayList<>(List.of(new Selection(options
                            .get(i).getOptions().get(0).getId()))));
                } else if (options.get(i).getOptions().get(0).getSpecializations().size() == 1) {
                    selectedElements.get(i).setSelections(new ArrayList<>(List.of(new Selection(options
                            .get(i).getOptions().get(0).getId(), options
                            .get(i).getOptions().get(0).getSpecializations().get(0)))));
                }
            }
        }
    }

    public CharacterDefinitionStep<?> getCharacterDefinitionStep() {
        return characterDefinitionStep;
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
            if (getCharacteristicOptions().get(i).getSelections().stream().map(Selection::getId).collect(Collectors.toSet()).contains(characteristic)) {
                bonus += characterDefinitionStep.getCharacteristicOptions().get(i).getCharacteristicBonus(characteristic).getBonus();
            }
        }
        return bonus;
    }

    public int getSkillBonus(String skill) {
        int bonus = 0;
        for (int i = 0; i < getSkillOptions().size(); i++) {
            for (int j = 0; j < getSkillOptions().get(i).getSelections().size(); j++) {
                if (getSkillOptions().get(i).getSelections().stream().map(Selection::getId)
                        .filter(Objects::nonNull).collect(Collectors.toSet()).contains(skill)) {
                    bonus += characterDefinitionStep.getSkillOptions().get(i).getSkillBonus(skill).getBonus();
                }
            }
        }
        return bonus;
    }

    public List<Selection> getSelectedCapabilities() {
        final List<Selection> selectedCapabilities = new ArrayList<>();
        capabilityOptions.forEach(capabilityOption ->
                selectedCapabilities.addAll(capabilityOption.getSelections().stream().filter(selection -> selection.getId() != null)
                        .collect(Collectors.toSet())));
        return selectedCapabilities;
    }

    public List<Selection> getSelectedPerks() {
        final List<Selection> selectedPerks = new ArrayList<>();
        perksOptions.forEach(perkOption ->
                selectedPerks.addAll(perkOption.getSelections().stream().filter(selection -> selection.getId() != null)
                        .collect(Collectors.toSet())));
        return selectedPerks;
    }

    public List<CharacterSelectedEquipment> getMaterialAwards() {
        return materialAwards;
    }

    public void setMaterialAwards(List<CharacterSelectedEquipment> materialAwards) {
        this.materialAwards = materialAwards;
    }

    @Override
    public void validate() throws InvalidSelectionException {
        super.validate();
        if (getRestrictions().isRestricted(characterPlayer)) {
            throw new InvalidSelectionException("Restrictions for  '" + getId() + "' are not meet.");
        }

        //Capabilities
        for (int i = 0; i < capabilityOptions.size(); i++) {
            if (capabilityOptions.get(i).getSelections().size() > characterDefinitionStep.getCapabilityOptions().get(i).getOptions().size()) {
                throw new TooManySelectionsException("You have selected '" + capabilityOptions.get(i).getSelections().size()
                        + "' capabilities options and only '"
                        + characterDefinitionStep.getCapabilityOptions().get(i).getOptions().size() + "' are available.");
            }
            final List<Selection> availableOptions = characterDefinitionStep.getCapabilityOptions().get(i).getOptions()
                    .stream().map(co -> new Selection(co.getId(), co.getSelectedSpecialization())).collect(Collectors.toList());
            for (Selection selection : capabilityOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected capability '" + selection + "' does not exist.", selection);
                }
            }
        }

        //Characteristics
        for (int i = 0; i < characteristicOptions.size(); i++) {
            if (characteristicOptions.get(i).getSelections().size() > characterDefinitionStep.getCharacteristicOptions().get(i).getOptions().size()) {
                throw new TooManySelectionsException("You have selected '" + characteristicOptions.get(i).getSelections().size()
                        + "' characteristics options and only '"
                        + characterDefinitionStep.getCharacteristicOptions().get(i).getOptions().size()
                        + "' are available.");
            }
            final List<Selection> availableOptions = characterDefinitionStep.getCharacteristicOptions().get(i).getOptions()
                    .stream().map(co -> new Selection(co.getId())).collect(Collectors.toList());
            for (Selection selection : characteristicOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected characteristic '" + selection + "' does not exist.", selection);
                }
            }
        }

        //Skills
        for (int i = 0; i < skillOptions.size(); i++) {
            if (skillOptions.get(i).getSelections().size() > characterDefinitionStep.getSkillOptions().get(i).getOptions().size()) {
                throw new TooManySelectionsException("You have selected '" + skillOptions.get(i).getSelections().size()
                        + "' capabilities options and only '"
                        + characterDefinitionStep.getSkillOptions().get(i).getOptions().size()
                        + "' are available.");
            }
            final List<Selection> availableOptions = characterDefinitionStep.getSkillOptions().get(i).getOptions()
                    .stream().map(so -> new Selection(so.getId())).collect(Collectors.toList());
            for (Selection selection : skillOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected skill '" + selection + "' does not exist.", selection);
                }
            }
        }

        //Perks
        for (int i = 0; i < perksOptions.size(); i++) {
            if (perksOptions.get(i).getSelections().size() > characterDefinitionStep.getPerksOptions().get(i).getOptions().size()) {
                throw new TooManySelectionsException("You have selected '" + perksOptions.get(i).getSelections().size()
                        + "' capabilities options and only '"
                        + characterDefinitionStep.getPerksOptions().get(i).getOptions().size()
                        + "' are available.");
            }
            final List<Selection> availableOptions = characterDefinitionStep.getPerksOptions().get(i).getOptions()
                    .stream().map(po -> new Selection(po.getId())).collect(Collectors.toList());
            for (Selection selection : perksOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected perk '" + selection + "' does not exist.", selection);
                }
            }
        }
    }
}
