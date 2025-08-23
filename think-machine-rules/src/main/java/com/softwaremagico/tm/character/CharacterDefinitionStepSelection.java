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
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOptions;
import com.softwaremagico.tm.character.equipment.CharacterSelectedEquipment;
import com.softwaremagico.tm.character.equipment.EquipmentOptions;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;
import com.softwaremagico.tm.exceptions.InvalidGeneratedCharacter;
import com.softwaremagico.tm.exceptions.InvalidSelectedElementException;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.TooManySelectionsException;
import com.softwaremagico.tm.utils.ComparableUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class CharacterDefinitionStepSelection extends Element {

    private final CharacterPlayer characterPlayer;
    private final CharacterDefinitionStep<?> characterDefinitionStep;

    @JsonProperty("capabilities")
    private List<CharacterSelectedElement> selectedCapabilityOptions;
    @JsonProperty("characteristics")
    private List<CharacterSelectedElement> selectedCharacteristicOptions;
    @JsonProperty("skills")
    private List<CharacterSelectedElement> selectedSkillOptions;
    @JsonProperty("perks")
    private List<CharacterSelectedElement> selectedPerksOptions;
    @JsonProperty("materialAwards")
    private List<CharacterSelectedEquipment> selectedMaterialAwards;

    public CharacterDefinitionStepSelection(CharacterPlayer characterPlayer, CharacterDefinitionStep<?> characterDefinitionStep)
            throws InvalidGeneratedCharacter {
        copy(characterDefinitionStep);

        this.characterPlayer = characterPlayer;
        this.characterDefinitionStep = characterDefinitionStep;

        setSelectedCapabilityOptions(Arrays.asList(new CharacterSelectedElement[getCapabilityOptions().size()]));
        for (int i = 0; i < getCapabilityOptions().size(); i++) {
            selectedCapabilityOptions.set(i, new CharacterSelectedElement());
        }

        setSelectedCharacteristicOptions(Arrays.asList(new CharacterSelectedElement[getCharacteristicOptions().size()]));
        for (int i = 0; i < getCharacteristicOptions().size(); i++) {
            selectedCharacteristicOptions.set(i, new CharacterSelectedElement());
        }

        setSelectedSkillOptions(Arrays.asList(new CharacterSelectedElement[getSkillOptions().size()]));
        for (int i = 0; i < getSkillOptions().size(); i++) {
            selectedSkillOptions.set(i, new CharacterSelectedElement());
        }

        if (getPerksOptions() != null) {
            setSelectedPerksOptions(Arrays.asList(new CharacterSelectedElement[getPerksOptions().size()]));
            for (int i = 0; i < getPerksOptions().size(); i++) {
                selectedPerksOptions.set(i, new CharacterSelectedElement());
            }
        }

        if (getMaterialAwardsOptions() != null) {
            setSelectedMaterialAwards(Arrays.asList(new CharacterSelectedEquipment[getMaterialAwardsOptions().size()]));
            for (int i = 0; i < getMaterialAwardsOptions().size(); i++) {
                selectedMaterialAwards.set(i, new CharacterSelectedEquipment());
            }
        }

        selectDefaultOptions();
    }

    public void selectDefaultOptions() {
        setDefaultOptions(new ArrayList<>(getCapabilityOptions()), selectedCapabilityOptions);
        setDefaultOptions(new ArrayList<>(getCharacteristicOptions()), selectedCharacteristicOptions);
        setDefaultOptions(new ArrayList<>(getSkillOptions()), selectedSkillOptions);
        if (getPerksOptions() != null) {
            setDefaultOptions(new ArrayList<>(getPerksOptions()), selectedPerksOptions);
        }
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

    protected CharacterDefinitionStep<?> getCharacterDefinitionStep() {
        return characterDefinitionStep;
    }

    public CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
    }

    public List<CharacterSelectedElement> getSelectedCapabilityOptions() {
        return selectedCapabilityOptions;
    }

    public void setSelectedCapabilityOptions(List<CharacterSelectedElement> selectedCapabilityOptions) {
        this.selectedCapabilityOptions = selectedCapabilityOptions;
    }

    public List<CharacterSelectedElement> getSelectedCharacteristicOptions() {
        return selectedCharacteristicOptions;
    }

    public void setSelectedCharacteristicOptions(List<CharacterSelectedElement> selectedCharacteristicOptions) {
        this.selectedCharacteristicOptions = selectedCharacteristicOptions;
    }

    public List<CharacterSelectedElement> getSelectedSkillOptions() {
        return selectedSkillOptions;
    }

    public void setSelectedSkillOptions(List<CharacterSelectedElement> selectedSkillOptions) {
        this.selectedSkillOptions = selectedSkillOptions;
    }

    public List<CharacterSelectedElement> getSelectedPerksOptions() {
        return selectedPerksOptions;
    }

    public void setSelectedPerksOptions(List<CharacterSelectedElement> selectedPerksOptions) {
        this.selectedPerksOptions = selectedPerksOptions;
    }

    public int getCharacteristicBonus(String characteristic) {
        int bonus = 0;
        for (int i = 0; i < getSelectedCharacteristicOptions().size(); i++) {
            if (getSelectedCharacteristicOptions().get(i).getSelections().stream().map(Selection::getId).collect(Collectors.toSet()).contains(characteristic)) {
                bonus += getCharacteristicOptions().get(i).getCharacteristicBonus(characteristic).getBonus();
            }
        }
        return bonus;
    }

    public int getSkillBonus(String skill) {
        int bonus = 0;
        for (int i = 0; i < getSelectedSkillOptions().size(); i++) {
            for (int j = 0; j < getSelectedSkillOptions().get(i).getSelections().size(); j++) {
                if (getSelectedSkillOptions().get(i).getSelections().stream().map(Selection::getId)
                        .filter(Objects::nonNull).collect(Collectors.toSet()).contains(skill)) {
                    bonus += getSkillOptions().get(i).getSkillBonus(skill).getBonus();
                }
            }
        }
        return bonus;
    }

    public boolean hasCapability(String capability, String specialization) {
        return getSelectedCapabilities().stream().map(selection -> ComparableUtils.getComparisonId(selection.getId(), selection.getSpecialization()))
                .collect(Collectors.toSet()).contains(ComparableUtils.getComparisonId(capability, specialization));
    }

    public List<Selection> getSelectedCapabilities() {
        final List<Selection> selectedCapabilities = new ArrayList<>();
        selectedCapabilityOptions.forEach(capabilityOption ->
                selectedCapabilities.addAll(capabilityOption.getSelections().stream().filter(selection -> selection.getId() != null)
                        .collect(Collectors.toSet())));
        return selectedCapabilities;
    }

    public List<Selection> getSelectedPerks() {
        final List<Selection> selectedPerks = new ArrayList<>();
        selectedPerksOptions.forEach(perkOption ->
                selectedPerks.addAll(perkOption.getSelections().stream().filter(selection -> selection.getId() != null)
                        .collect(Collectors.toSet())));
        return selectedPerks;
    }

    public List<CharacterSelectedEquipment> getSelectedMaterialAwards() {
        return selectedMaterialAwards;
    }

    public void setSelectedMaterialAwards(List<CharacterSelectedEquipment> selectedMaterialAwards) {
        this.selectedMaterialAwards = selectedMaterialAwards;
    }

    @Override
    public void validate() throws InvalidSelectionException {
        super.validate();
        if (getRestrictions().isRestricted(characterPlayer)) {
            throw new InvalidSelectionException("Restrictions for  '" + getId() + "' are not meet.");
        }

        validateCapabilities();

        validateCharacteristics();

        validatePerks();

        validateSkills();

    }

    protected void validateCapabilities() {
        for (int i = 0; i < selectedCapabilityOptions.size(); i++) {
            if (selectedCapabilityOptions.get(i).getSelections().size() > getCapabilityOptions().get(i).getOptions().size()) {
                throw new TooManySelectionsException("You have selected '" + selectedCapabilityOptions.get(i).getSelections().size()
                        + "' capabilities options and only '"
                        + getCapabilityOptions().get(i).getOptions().size() + "' are available.");
            }
            final List<Selection> availableOptions = getCapabilityOptions().get(i).getOptions()
                    .stream().map(co -> new Selection(co.getId(), co.getSelectedSpecialization())).collect(Collectors.toList());
            for (Selection selection : selectedCapabilityOptions.get(i).getSelections()) {
                //Compare specializations, or capabilities without specialization if not defined in the options.
                if (!availableOptions.contains(selection) && !availableOptions.contains(selection.getMainSelection())) {
                    throw new InvalidSelectedElementException("Selected capability '" + selection + "' does not exist. Options are '"
                            + availableOptions + "'", selection);
                }
            }
        }
    }

    protected void validateCharacteristics() {
        for (int i = 0; i < selectedCharacteristicOptions.size(); i++) {
            if (selectedCharacteristicOptions.get(i).getSelections().size() > getCharacteristicOptions().get(i).getOptions().size()) {
                throw new TooManySelectionsException("You have selected '" + selectedCharacteristicOptions.get(i).getSelections().size()
                        + "' characteristics options and only '"
                        + getCharacteristicOptions().get(i).getOptions().size()
                        + "' are available.");
            }
            final List<Selection> availableOptions = getCharacteristicOptions().get(i).getOptions()
                    .stream().map(co -> new Selection(co.getId())).collect(Collectors.toList());
            for (Selection selection : selectedCharacteristicOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected characteristic '" + selection + "' does not exist.", selection);
                }
            }
        }
    }

    protected void validateSkills() {
        for (int i = 0; i < selectedSkillOptions.size(); i++) {
            if (selectedSkillOptions.get(i).getSelections().size() > getSkillOptions().get(i).getOptions().size()) {
                throw new TooManySelectionsException("You have selected '" + selectedSkillOptions.get(i).getSelections().size()
                        + "' capabilities options and only '"
                        + getSkillOptions().get(i).getOptions().size()
                        + "' are available.");
            }
            final List<Selection> availableOptions = getSkillOptions().get(i).getOptions()
                    .stream().map(so -> new Selection(so.getId())).collect(Collectors.toList());
            for (Selection selection : selectedSkillOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected skill '" + selection + "' does not exist.", selection);
                }
            }
        }
    }

    protected void validatePerks() {
        validatePerks(selectedPerksOptions, getPerksOptions());
    }

    protected void validatePerks(List<CharacterSelectedElement> selectedClassPerksOptions, List<PerkOptions> perkOptions) {
        if (perkOptions != null) {
            for (int i = 0; i < selectedClassPerksOptions.size(); i++) {
                if (selectedClassPerksOptions.get(i).getSelections().size() > perkOptions.get(i).getOptions().size()) {
                    throw new TooManySelectionsException("You have selected '" + selectedClassPerksOptions.get(i).getSelections().size()
                            + "' capabilities options and only '"
                            + perkOptions.get(i).getOptions().size()
                            + "' are available.");
                }
                final List<Selection> availableOptions = perkOptions.get(i).getOptions()
                        .stream().map(po -> new Selection(po.getId())).collect(Collectors.toList());
                for (Selection selection : selectedClassPerksOptions.get(i).getSelections()) {
                    if (!availableOptions.contains(selection)) {
                        throw new InvalidSelectedElementException("Selected perk '" + selection + "' does not exist.", selection);
                    }
                }
            }
        }
    }

    public List<CapabilityOptions> getCapabilityOptions() {
        return getCharacterDefinitionStep().getCapabilityOptions();
    }

    public List<CharacteristicBonusOptions> getCharacteristicOptions() {
        return getCharacterDefinitionStep().getCharacteristicOptions();
    }

    public List<SkillBonusOptions> getSkillOptions() {
        return getCharacterDefinitionStep().getSkillOptions();
    }

    public List<PerkOptions> getPerksOptions() {
        return getCharacterDefinitionStep().getPerksOptions();
    }

    public List<EquipmentOptions> getMaterialAwardsOptions() {
        return getCharacterDefinitionStep().getMaterialAwards();
    }
}
