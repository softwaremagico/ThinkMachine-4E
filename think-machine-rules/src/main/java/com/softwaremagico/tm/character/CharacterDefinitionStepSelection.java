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
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.InvalidGeneratedCharacter;
import com.softwaremagico.tm.exceptions.InvalidSelectedElementException;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.TooManySelectionsException;
import com.softwaremagico.tm.utils.ComparableUtils;

import java.util.ArrayList;
import java.util.Arrays;
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

    public CharacterDefinitionStepSelection(CharacterPlayer characterPlayer, CharacterDefinitionStep characterDefinitionStep)
            throws InvalidGeneratedCharacter {
        copy(characterDefinitionStep);

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

        if (getNotRepeatedPerksOptions() != null) {
            setSelectedPerksOptions(Arrays.asList(new CharacterSelectedElement[getNotRepeatedPerksOptions().size()]));
            for (int i = 0; i < getNotRepeatedPerksOptions().size(); i++) {
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
        setDefaultOptions(new ArrayList<>(getNotRepeatedCapabilityOptions()), selectedCapabilityOptions);
        setDefaultOptions(new ArrayList<>(getCharacteristicOptions()), selectedCharacteristicOptions);
        setDefaultOptions(new ArrayList<>(getSkillOptions()), selectedSkillOptions);
        if (getNotRepeatedPerksOptions() != null) {
            setDefaultOptions(new ArrayList<>(getNotRepeatedPerksOptions()), selectedPerksOptions);
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
    public List<Selection> getSelectedCapabilities() {
        final List<Selection> selectedCapabilities = new ArrayList<>();
        selectedCapabilityOptions.forEach(capabilityOption ->
                selectedCapabilities.addAll(capabilityOption.getSelections().stream().filter(selection -> selection.getId() != null)
                        .collect(Collectors.toSet())));
        return selectedCapabilities;
    }

    @JsonIgnore
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
            final List<Selection> availableOptions = getNotRepeatedCapabilityOptions(getPhase()).get(i).getOptions()
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
                    .stream().map(co -> new Selection(co.getId())).collect(Collectors.toList());
            for (Selection selection : selectedCharacteristicOptions.get(i).getSelections()) {
                if (!availableOptions.contains(selection)) {
                    throw new InvalidSelectedElementException("Selected characteristic '" + selection + "' does not exist.", selection);
                }
                if (getCharacteristicOptions().get(i).getOptions().size() != 1 || !getCharacteristicOptions().get(i).getOptions().get(0).isExtra()) {
                    totalBonus += getCharacteristicOptions().get(i).getCharacteristicBonus(selection.getId()).getBonus();
                }
            }
        }

        //Check total
        if (totalBonus != getCharacteristicTotalPoints()) {
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
                    .stream().map(so -> new Selection(so.getId())).collect(Collectors.toList());
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
        validatePerks(selectedPerksOptions, getNotRepeatedPerksOptions(), getPerksOptions());
    }

    protected void validatePerks(List<CharacterSelectedElement> selectedPerksOptions, List<PerkOptions> perkOptions,
                                 List<PerkOptions> completePerkList) {
        if (perkOptions != null && selectedPerksOptions != null) {
            for (int i = 0; i < selectedPerksOptions.size(); i++) {
                if (selectedPerksOptions.get(i).getSelections().size() > perkOptions.get(i).getOptions().size()) {
                    throw new TooManySelectionsException("You have selected '" + selectedPerksOptions.get(i).getSelections().size()
                            + "' perks options and only '"
                            + perkOptions.get(i).getOptions().size()
                            + "' are available.");
                }
                final List<Selection> availableOptions = completePerkList.get(i).getOptions()
                        .stream().map(po -> new Selection(po.getId())).collect(Collectors.toList());
                for (Selection selection : selectedPerksOptions.get(i).getSelections()) {
                    if (!availableOptions.contains(selection)) {
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

    /**
     * Gets all options that are valid selections by the user.
     *
     * @return
     */
    private List<CapabilityOptions> getAllCapabilityOptions() {
        final List<CapabilityOptions> capabilityOptions = new ArrayList<>();
        for (CapabilityOptions capabilityOption : getCharacterDefinitionStep().getCapabilityOptions()) {
            final List<CapabilityOption> availableOptions = capabilityOption.getOptions().stream().filter(
                    e -> !e.getRestrictions().isRestricted(characterPlayer)).collect(Collectors.toList());
            //If no option is available. Must select between any not restricted to the character.
            if (!availableOptions.isEmpty()) {
                capabilityOptions.add(new CapabilityOptions(capabilityOption, availableOptions));
            } else {
                final CapabilityOptions newCapabilityOptions = new CapabilityOptions(capabilityOption, CapabilityFactory.getInstance().getElements()
                        .stream().filter(
                                e -> !e.getRestrictions().isRestricted(characterPlayer))
                        .map(CapabilityOption::new).collect(Collectors.toList()));
                capabilityOptions.add(newCapabilityOptions);
            }
        }
        return capabilityOptions;
    }

    public abstract Phase getPhase();

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
            final List<CapabilityOption> options = capabilityOption.getOptions().stream().filter(o -> !getCharacterPlayer()
                            .hasCapability(o.getId(), o.getSelectedSpecialization() != null ? o.getSelectedSpecialization().getId() : null,
                                    phase, getId())).filter(o -> !o.getRestrictions().isRestricted(characterPlayer))
                    .collect(Collectors.toList());
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
                                        phase, getId())).collect(Collectors.toList()));
                newCapabilityOptions.getOptions().removeAll(oldOptions);
                capabilityOptions.add(newCapabilityOptions);
            }
        }
        return capabilityOptions;
    }

    public List<CharacteristicBonusOptions> getCharacteristicOptions() {
        return getCharacterDefinitionStep().getCharacteristicOptions();
    }

    public List<SkillBonusOptions> getSkillOptions() {
        return getCharacterDefinitionStep().getSkillOptions();
    }

    public List<PerkOptions> getPerksOptions() {
        return getCharacterDefinitionStep().getFinalPerksOptions();
    }

    public List<PerkOptions> getNotRepeatedPerksOptions() {
        if (getCharacterDefinitionStep().getFinalPerksOptions() == null) {
            return new ArrayList<>();
        }
        final List<PerkOptions> finalPerkOptions = new ArrayList<>();
        for (PerkOptions perkOptions : getCharacterDefinitionStep().getFinalPerksOptions()) {
            //Get not duplicated options that are selected on previous steps. We need to filter again by restriction, as some perks are restricted by
            // character's current level.
            final List<PerkOption> oldOptions = perkOptions.getOptions().stream().filter(e -> !e.getRestrictions().isRestricted(characterPlayer))
                    .collect(Collectors.toList());
            final List<PerkOption> options = oldOptions.stream().filter(o -> !getCharacterPlayer()
                    .hasPerk(o.getId(), getPhase())).collect(Collectors.toList());
            //If no option is available. Must select between any not restricted to the character.
            if (!options.isEmpty()) {
                finalPerkOptions.add(new PerkOptions(perkOptions, options));
            } else {
                final PerkOptions newPerkOptions = new PerkOptions(perkOptions, PerkFactory.getInstance().getElements()
                        .stream().filter(e -> !e.getRestrictions().isRestricted(characterPlayer))
                        .map(PerkOption::new).collect(Collectors.toList()));
                //Remove old options. As if no option is available means that all oldOptions are already selected.
                newPerkOptions.getOptions().removeAll(oldOptions);
                finalPerkOptions.add(newPerkOptions);
            }
        }
        return finalPerkOptions;
    }

    public List<EquipmentOptions> getMaterialAwardsOptions() {
        return getCharacterDefinitionStep().getMaterialAwards();
    }
}
