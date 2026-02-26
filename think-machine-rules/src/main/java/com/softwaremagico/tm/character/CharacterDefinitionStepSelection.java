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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.OptionSelector;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOptions;
import com.softwaremagico.tm.character.equipment.CharacterSelectedEquipment;
import com.softwaremagico.tm.character.equipment.EquipmentOptions;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.IncompleteSelectedElementException;
import com.softwaremagico.tm.exceptions.InvalidGeneratedCharacter;
import com.softwaremagico.tm.exceptions.InvalidSelectedElementException;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.TooManySelectionsException;
import com.softwaremagico.tm.utils.ComparableUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CharacterDefinitionStepSelection extends Element {
    private static final int CHARACTERISTIC_POINTS = 5;
    private static final int SKILL_POINTS = 5;

    @JsonIgnore
    private final CharacterPlayer characterPlayer;
    @JsonIgnore
    private final CharacterDefinitionStep characterDefinitionStep;

    @JsonProperty(value = "capabilities")
    private List<CharacterSelectedElement> selectedCapabilityOptions;
    @JsonProperty(value = "characteristics")
    private List<CharacterSelectedElement> selectedCharacteristicOptions;
    @JsonProperty(value = "skills")
    private List<CharacterSelectedElement> selectedSkillOptions;
    @JsonProperty(value = "perks")
    private List<CharacterSelectedElement> selectedPerksOptions;
    @JsonProperty(value = "materialAwards")
    private List<CharacterSelectedEquipment> selectedMaterialAwards;

    @JsonIgnore
    private final Phase phase;

    public CharacterDefinitionStepSelection(CharacterPlayer characterPlayer, CharacterDefinitionStep characterDefinitionStep, Phase phase)
            throws InvalidGeneratedCharacter {
        copy(characterDefinitionStep);
        this.phase = phase;

        this.characterPlayer = characterPlayer;
        this.characterDefinitionStep = characterDefinitionStep;

        setSelectedCapabilityOptions(Arrays.asList(new CharacterSelectedElement[getNotRepeatedCapabilityOptions().size()]));
        for (int i = 0; i < getNotRepeatedCapabilityOptions().size(); i++) {
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

        if (getCharacterAvailablePerksOptions() != null) {
            setSelectedPerksOptions(Arrays.asList(new CharacterSelectedElement[getCharacterAvailablePerksOptions().size()]));
            for (int i = 0; i < getCharacterAvailablePerksOptions().size(); i++) {
                selectedPerksOptions.set(i, new CharacterSelectedElement());
            }
        }

        if (getMaterialAwardsOptions() != null) {
            setSelectedMaterialAwards(Arrays.asList(new CharacterSelectedEquipment[getMaterialAwardsOptions().size()]));
            for (int i = 0; i < getMaterialAwardsOptions().size(); i++) {
                selectedMaterialAwards.set(i, new CharacterSelectedEquipment());
            }
        }
    }

    public void updateDefaultOptions() {
        resetDefaultOptions();
        selectDefaultOptions();
    }

    protected void resetDefaultOptions() {
        resetDefaultOptions(new ArrayList<>(getNotRepeatedCapabilityOptions()), selectedCapabilityOptions);
        resetDefaultOptions(new ArrayList<>(getCharacteristicOptions()), selectedCharacteristicOptions);
        resetDefaultOptions(new ArrayList<>(getSkillOptions()), selectedSkillOptions);
        final List<CharacterPerkOptions> perkOptions = getNotSelectedPerksOptions(true);
        if (perkOptions != null) {
            resetDefaultOptions(new ArrayList<>(perkOptions), selectedPerksOptions);
        }
    }

    protected void selectDefaultOptions() {
        setDefaultOptions(new ArrayList<>(getNotRepeatedCapabilityOptions()), selectedCapabilityOptions);
        setDefaultOptions(new ArrayList<>(getCharacteristicOptions()), selectedCharacteristicOptions);
        setDefaultOptions(new ArrayList<>(getSkillOptions()), selectedSkillOptions);
        final List<CharacterPerkOptions> perkOptions = getNotSelectedPerksOptions(true);
        if (perkOptions != null) {
            setDefaultOptions(new ArrayList<>(perkOptions), selectedPerksOptions);
        }
    }

    private void setDefaultOptions(List<OptionSelector<?, ?>> options, List<CharacterSelectedElement> selectedElements) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getOptions().size() == 1) {
                final Element option = options.get(i).getOptions().iterator().next();
                if (option instanceof CapabilityOption) {
                    if (((CapabilityOption) option).getSelectedSpecialization() != null) {
                        selectedElements.get(i).setSelections(new ArrayList<>(List.of(new Selection(option,
                                ((CapabilityOption) option).getSelectedSpecialization()))));
                    }
                } else if (option.getSpecializations() == null
                        || option.getSpecializations().isEmpty()) {
                    selectedElements.get(i).setSelections(new ArrayList<>(List.of(new Selection(option, null))));
                } else if (option.getSpecializations().size() == 1) {
                    selectedElements.get(i).setSelections(new ArrayList<>(List.of(new Selection(option,
                            option.getSpecializations().get(0)))));
                }
            }
        }
    }

    /**
     * Removes the default selected options.
     *
     * @param options
     * @param selectedElements
     */
    private void resetDefaultOptions(List<OptionSelector<?, ?>> options, List<CharacterSelectedElement> selectedElements) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getOptions().size() == 1) {
                final Element option = options.get(i).getOptions().iterator().next();
                if (option instanceof CapabilityOption) {
                    if (((CapabilityOption) option).getSelectedSpecialization() != null) {
                        selectedElements.get(i).setSelections(new ArrayList<>());
                    }
                } else if (option.getSpecializations() == null
                        || option.getSpecializations().isEmpty()) {
                    selectedElements.get(i).setSelections(new ArrayList<>());
                } else if (option.getSpecializations().size() == 1) {
                    selectedElements.get(i).setSelections(new ArrayList<>());
                }
            }
        }
    }

    protected CharacterDefinitionStep getCharacterDefinitionStep() {
        return characterDefinitionStep;
    }

    @JsonIgnore
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
            if (getSelectedSkillOptions().get(i).getSelections().stream().map(Selection::getId)
                    .filter(Objects::nonNull).collect(Collectors.toSet()).contains(skill)) {
                bonus += getSkillOptions().get(i).getSkillBonus(skill).getBonus();
            }
        }
        return bonus;
    }

    public boolean hasCapability(String capability, String specialization) {
        return getSelectedCapabilities().stream().map(selection -> ComparableUtils.getComparisonId(selection.getId(), selection.getSpecialization()))
                .collect(Collectors.toSet()).contains(ComparableUtils.getComparisonId(capability, specialization));
    }

    @JsonIgnore
    public Set<Selection> getSelectedCapabilities() {
        final Set<Selection> result = new HashSet<>();
        selectedCapabilityOptions.forEach(selection -> result.addAll(selection.getSelections()));
        return result;
    }

    @JsonIgnore
    public Set<Selection> getSelectedPerks() {
        final Set<Selection> result = new HashSet<>();
        selectedPerksOptions.forEach(selection -> result.addAll(selection.getSelections()));
        return result;
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
        updateDefaultOptions();
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
            final List<Selection> availableOptions = getNotRepeatedCapabilityOptions(getPhase()).get(i).getOptions()
                    .stream().map(co -> new Selection(co, co.getSelectedSpecialization())).collect(Collectors.toList());
            for (Selection selection : selectedCapabilityOptions.get(i).getSelections()) {
                //Compare specializations, or capabilities without specialization if not defined in the options.
                if (!availableOptions.contains(selection) && !availableOptions.contains(selection.getMainSelection())) {
                    throw new InvalidSelectedElementException("Selected capability '" + selection + "' does not exist. Options are '"
                            + availableOptions + "'", selection);
                }
            }
        }
    }

    @JsonIgnore
    protected int getCharacteristicTotalPoints() {
        return CHARACTERISTIC_POINTS;
    }

    protected void validateCharacteristics() {
        int totalBonus = 0;
        for (int i = 0; i < selectedCharacteristicOptions.size(); i++) {
            if (selectedCharacteristicOptions.get(i).getSelections().size() > getCharacteristicOptions().get(i).getOptions().size()) {
                throw new TooManySelectionsException("You have selected '" + selectedCharacteristicOptions.get(i).getSelections().size()
                        + "' characteristics options and only '"
                        + getCharacteristicOptions().get(i).getOptions().size()
                        + "' are available.");
            }
            final List<Selection> availableOptions = getCharacteristicOptions().get(i).getOptions()
                    .stream().map(Selection::new).collect(Collectors.toList());
            for (Selection selection : selectedCharacteristicOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected characteristic '" + selection + "' does not exist.", selection);
                }
                if (getCharacteristicOptions().get(i).getOptions().size() != 1 || !getCharacteristicOptions().get(i).getOptions().iterator().next()
                        .isExtra()) {
                    totalBonus += getCharacteristicOptions().get(i).getCharacteristicBonus(selection.getId()).getBonus();
                }
            }
        }

        //Check total
        if (totalBonus < getCharacteristicTotalPoints()) {
            throw new IncompleteSelectedElementException("Total points assigned to characteristics are '" + totalBonus + "' from '"
                    + getCharacteristicTotalPoints() + "'.");
        } else if (totalBonus > getCharacteristicTotalPoints()) {
            throw new InvalidSelectedElementException("Total points assigned to characteristics are '" + totalBonus + "' from '"
                    + getCharacteristicTotalPoints() + "'.");
        }
    }

    @JsonIgnore
    protected int getSkillTotalPoints() {
        return SKILL_POINTS;
    }

    protected void validateSkills() {
        int totalBonus = 0;
        for (int i = 0; i < selectedSkillOptions.size(); i++) {
            if (selectedSkillOptions.get(i).getSelections().size() > getSkillOptions().get(i).getOptions().size()) {
                throw new TooManySelectionsException("You have selected '" + selectedSkillOptions.get(i).getSelections().size()
                        + "' capabilities options and only '"
                        + getSkillOptions().get(i).getOptions().size()
                        + "' are available.");
            }
            final List<Selection> availableOptions = getSkillOptions().get(i).getOptions()
                    .stream().map(Selection::new).collect(Collectors.toList());
            for (Selection selection : selectedSkillOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected skill '" + selection + "' does not exist.", selection);
                }
                totalBonus += getSkillOptions().get(i).getSkillBonus(selection.getId()).getBonus();
            }
        }

        //Check total
        if (totalBonus != getSkillTotalPoints()) {
            throw new InvalidSelectedElementException("Total points assigned to skills are '" + totalBonus + "' from '"
                    + getSkillTotalPoints() + "'.");
        }
    }

    protected void validatePerks() {
        validatePerks(selectedPerksOptions, getNotSelectedPerksOptions(true), getCharacterAvailablePerksOptions());
    }


    /**
     * Validates the selected perks against the available perks by phase.
     *
     * @param selectedPerksOptions   Perks selected by the character.
     * @param availablePerkOptions   The final options available to the character.
     * @param originalSourcePerkList The original options for the character.
     */
    protected void validatePerks(List<CharacterSelectedElement> selectedPerksOptions, List<CharacterPerkOptions> availablePerkOptions,
                                 List<CharacterPerkOptions> originalSourcePerkList) {
        if (availablePerkOptions != null && selectedPerksOptions != null && originalSourcePerkList != null) {
            for (int i = 0; i < selectedPerksOptions.size(); i++) {
                if (selectedPerksOptions.get(i).getSelections().size() > availablePerkOptions.get(i).getOptions().size()) {
                    throw new TooManySelectionsException("You have selected '" + selectedPerksOptions.get(i).getSelections().size()
                            + "' perks options and only '"
                            + availablePerkOptions.get(i).getOptions().size()
                            + "' are available.");
                }
                final Set<Selection> availableOptions = availablePerkOptions.get(i).getAvailableSelections();
                for (Selection selection : selectedPerksOptions.get(i).getSelections()) {
                    if (!availableOptions.contains(selection)) {
                        getCharacterPlayer().hasSelection(selection, phase, getLevel());
                        throw new InvalidSelectedElementException("Selected perk '" + selection + "' does not exist. Available perks are: "
                                + availableOptions, selection);
                    }
                }
            }
        }
    }

    /**
     * Gets all options that are defined on the xml.
     *
     * @return
     */
    public List<CapabilityOptions> getCapabilityOptions() {
        return getCharacterDefinitionStep().getCapabilityOptions();
    }

    public Phase getPhase() {
        return phase;
    }

    /**
     * Gets all options that are valid selections by the user except the ones that have been already selected.
     *
     * @return
     */
    public List<CapabilityOptions> getNotRepeatedCapabilityOptions() {
        return getNotRepeatedCapabilityOptions(getPhase());
    }

    /**
     * Gets all options that are valid selections by the user except the ones that have been already selected.
     *
     * @return
     */
    public List<CapabilityOptions> getNotRepeatedCapabilityOptions(Phase phase) {
        final List<CapabilityOptions> capabilityOptions = new ArrayList<>();
        for (CapabilityOptions capabilityOption : getCharacterDefinitionStep().getCapabilityOptions()) {
            //Get not duplicated options that are selected on previous steps.
            final List<CapabilityOption> oldOptions = new ArrayList<>(capabilityOption.getOptions());
            final LinkedHashSet<CapabilityOption> options = capabilityOption.getOptions().stream().filter(o -> !getCharacterPlayer()
                            .hasCapability(o.getId(), o.getSelectedSpecialization() != null ? o.getSelectedSpecialization().getId() : null,
                                    phase, getId())).filter(o -> !o.getRestrictions().isRestricted(characterPlayer))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            //If no option is available. Must select between any not restricted to the character.
            if (!options.isEmpty()) {
                capabilityOptions.add(new CapabilityOptions(capabilityOption, options));
            } else {
                final CapabilityOptions newCapabilityOptions = new CapabilityOptions(capabilityOption, CapabilityFactory.getInstance().getElements()
                        .stream().filter(
                                e -> !e.getRestrictions().isRestricted(characterPlayer))
                        .map(CapabilityOption::getCapabilityOptions).collect(Collectors.toSet()).stream().flatMap(Set::stream)
                        .filter(o -> !getCharacterPlayer()
                                .hasCapability(o.getId(), o.getSelectedSpecialization() != null ? o.getSelectedSpecialization().getId() : null,
                                        phase, getId())).collect(Collectors.toCollection(LinkedHashSet::new)));
                oldOptions.forEach(newCapabilityOptions.getOptions()::remove);
                capabilityOptions.add(newCapabilityOptions);
            }
        }
        return capabilityOptions;
    }

    public List<CharacteristicBonusOptions> getCharacteristicOptions() {
        return getCharacterDefinitionStep().getCharacteristicOptions();
    }

    public List<PerkOptions> getSourcePerks() {
        return getCharacterDefinitionStep().getSourcePerks();
    }

    public List<SkillBonusOptions> getSkillOptions() {
        return getCharacterDefinitionStep().getSkillOptions();
    }

    public List<CharacterPerkOptions> getCharacterAvailablePerksOptions() {
        return getCharacterDefinitionStep().getCharacterAvailablePerksOptions();
    }

    public List<CharacterPerkOptions> getNotSelectedPerksOptions(boolean addStandardPerksIfEmpty) {
        return getNotSelectedPerksOptions(getPhase(), getLevel(), addStandardPerksIfEmpty);
    }

    public List<CharacterPerkOptions> getNotSelectedPerksOptions(Phase phase, int level, boolean addStandardPerksIfEmpty) {
        if (getCharacterDefinitionStep().getCharacterAvailablePerksOptions() == null) {
            return new ArrayList<>();
        }
        final List<CharacterPerkOptions> finalPerkOptions = new ArrayList<>();
        for (CharacterPerkOptions availablePerkOptions : getCharacterDefinitionStep().getCharacterAvailablePerksOptions()) {
            //Get not duplicated options that are selected on previous steps.
            final Set<Selection> originalSelections = getOriginalSelection(availablePerkOptions.getOptions());
            // We need to filter again by restriction, as some perks are restricted by character's current level.
            final Set<Selection> availableSelections = getAvailableSelections(originalSelections, getLevel());
            //If no option is available. Must select between any not restricted to the character.
            if (!addStandardPerksIfEmpty || !availableSelections.isEmpty()) {
                //Only are the filtered selections available.
                finalPerkOptions.add(new CharacterPerkOptions(availablePerkOptions, availableSelections));
            } else {
                //Create a new options with any available selection.
                final CharacterPerkOptions newPerkOptions = new CharacterPerkOptions(
                        new PerkOptions(availablePerkOptions, PerkFactory.getInstance().getElements()
                                .stream().filter(e -> !e.getRestrictions().isRestricted(characterPlayer))
                                .map(PerkOption::new).collect(Collectors.toList())));
                //Remove orinal options. As if no option is available means that all 'oldOptions' are already selected by the user.
                originalSelections.forEach(newPerkOptions.getAvailableSelections()::remove);
                finalPerkOptions.add(newPerkOptions);
            }
        }
        return finalPerkOptions;
    }

    protected Set<Selection> getOriginalSelection(Collection<PerkOption> perkOptions) {
        return perkOptions.stream()
                .filter(e -> !e.getRestrictions().isRestricted(characterPlayer))
                .map(PerkOption::getOptionsBySpecialization).flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    protected Set<Selection> getAvailableSelections(Collection<Selection> perkOptions, int level) {
        //Get not duplicated options that are selected on previous steps.
        return perkOptions.stream().filter(o ->
                //Levels' previous phase can vary depending on the level index.
                !getCharacterPlayer().hasSelection(o, level > 2 ? Phase.LEVEL : phase.getPreviousPhase(), level - 1)
                        || PerkFactory.getInstance().getElement(o).isRepeatable()).collect(Collectors.toSet());
    }

    public List<EquipmentOptions> getMaterialAwardsOptions() {
        return getCharacterDefinitionStep().getMaterialAwards();
    }

    public int getLevel() {
        return 0;
    }
}
