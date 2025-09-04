package com.softwaremagico.tm.character.level;

/*-
 * #%L
 * Think Machine 4E (Rules)
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.CharacterSelectedElement;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.equipment.CharacterSelectedEquipment;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LevelSelector extends CharacterDefinitionStepSelection {

    @JsonProperty("classPerks")
    private List<CharacterSelectedElement> selectedClassPerksOptions;

    @JsonProperty("callingPerks")
    private List<CharacterSelectedElement> selectedCallingPerksOptions;

    private final int level;

    public LevelSelector(CharacterPlayer characterPlayer, int level) {
        super(characterPlayer, LevelFactory.getInstance().getElement(characterPlayer, level), Phase.LEVEL);
        this.level = level;

        setSelectedClassPerksOptions(Arrays.asList(new CharacterSelectedElement[getClassPerksOptions().size()]));
        for (int i = 0; i < getClassPerksOptions().size(); i++) {
            selectedClassPerksOptions.set(i, new CharacterSelectedElement());
        }

        setSelectedCallingPerksOptions(Arrays.asList(new CharacterSelectedElement[getCallingPerksOptions().size()]));
        for (int i = 0; i < getCallingPerksOptions().size(); i++) {
            selectedCallingPerksOptions.set(i, new CharacterSelectedElement());
        }
    }

    @Override
    public String getId() {
        return "Level" + level;
    }

    public List<CharacterSelectedElement> getSelectedClassPerksOptions() {
        return selectedClassPerksOptions;
    }

    public void setSelectedClassPerksOptions(List<CharacterSelectedElement> selectedClassPerksOptions) {
        this.selectedClassPerksOptions = selectedClassPerksOptions;
    }

    public List<CharacterSelectedElement> getSelectedCallingPerksOptions() {
        return selectedCallingPerksOptions;
    }

    public void setSelectedCallingPerksOptions(List<CharacterSelectedElement> selectedCallingPerksOptions) {
        this.selectedCallingPerksOptions = selectedCallingPerksOptions;
    }

    @Override
    public Set<Selection> getSelectedPerks() {
        final Set<Selection> selectedPerks = new HashSet<>();
        if (getSelectedClassPerksOptions() != null) {
            getSelectedClassPerksOptions().forEach(perkOption ->
                    selectedPerks.addAll(perkOption.getSelections().stream().filter(selection -> selection.getId() != null)
                            .collect(Collectors.toSet())));
        }
        if (getSelectedCallingPerksOptions() != null) {
            getSelectedCallingPerksOptions().forEach(perkOption ->
                    selectedPerks.addAll(perkOption.getSelections().stream().filter(selection -> selection.getId() != null)
                            .collect(Collectors.toSet())));
        }
        return selectedPerks;
    }

    @JsonIgnore
    public Level getLevelDefinition() {
        return ((Level) getCharacterDefinitionStep());
    }

    @Override
    public int getLevel() {
        return level;
    }

    @JsonIgnore
    public int getExtraSurgeRating() {
        return getLevelDefinition().getExtraSurgeRating();
    }

    @JsonIgnore
    public int getExtraSurgeNumber() {
        return getLevelDefinition().getExtraSurgeNumber();
    }

    @JsonIgnore
    public int getExtraVitality() {
        return getLevelDefinition().getExtraVitality();
    }

    @JsonIgnore
    public int getExtraVPBank() {
        return getLevelDefinition().getExtraVPBank();
    }

    @JsonIgnore
    public int getExtraRevivalRating() {
        return getLevelDefinition().getExtraRevivalRating();
    }

    @JsonIgnore
    public int getExtraRevivalNumber() {
        return getLevelDefinition().getExtraRevivalNumber();
    }

    @Override
    public void setSelectedPerksOptions(List<CharacterSelectedElement> selectedPerksOptions) {
        //Ignored
    }

    @Override
    public void setSelectedMaterialAwards(List<CharacterSelectedEquipment> selectedMaterialAwards) {
        //Ignored
    }


    @Override
    @JsonIgnore
    protected int getCharacteristicTotalPoints() {
        return getLevelDefinition().getCharacteristicsTotalPoints();
    }


    @Override
    @JsonIgnore
    protected int getSkillTotalPoints() {
        return getLevelDefinition().getSkillsTotalPoints();
    }

    @Override
    public void validate() throws InvalidSelectionException {
        try {
            super.validate();
            validateClassPerks();
            validateCallingPerks();
        } catch (InvalidSelectionException e) {
            throw new InvalidSelectionException("Error on level '" + getLevel() + "'.", e);
        }
    }

    @JsonIgnore
    public List<CharacterPerkOptions> getClassPerksOptions() {
        return ((Level) getCharacterDefinitionStep()).getUpbringingPerksOptions();
    }

    @JsonIgnore
    public List<CharacterPerkOptions> getFinalClassPerksOptions() {
        return getCharacterDefinitionStep().getCharacterAvailablePerksOptions(new ArrayList<>(getClassPerksOptions()));
    }

    @Override
    @JsonIgnore
    public List<CharacterPerkOptions> getNotSelectedPerksOptions() {
        return ((Level) getCharacterDefinitionStep()).getAvailableSelections();
    }

    @JsonIgnore
    public List<CharacterPerkOptions> getCallingPerksOptions() {
        return ((Level) getCharacterDefinitionStep()).getCallingPerksOptions();
    }

    @JsonIgnore
    public List<CharacterPerkOptions> getFinalCallingPerksOptions() {
        return getCharacterDefinitionStep().getCharacterAvailablePerksOptions(new ArrayList<>(getCallingPerksOptions()));
    }

    @JsonIgnore
    public List<CharacterPerkOptions> getNotRepeatedCallingPerksOptions() {
        return ((Level) getCharacterDefinitionStep()).getNotRepeatedCallingPerksOptions();
    }

    protected void validateClassPerks() {
        validatePerks(selectedClassPerksOptions, getNotSelectedPerksOptions(), getFinalClassPerksOptions());
    }

    protected void validateCallingPerks() {
        validatePerks(selectedCallingPerksOptions, getNotRepeatedCallingPerksOptions(), getFinalCallingPerksOptions());
    }

    @Override
    @JsonIgnore
    public List<CharacterSelectedElement> getSelectedPerksOptions() {
        final List<CharacterSelectedElement> selectedPerksOptions = new ArrayList<>();
        selectedPerksOptions.addAll(getSelectedClassPerksOptions());
        selectedPerksOptions.addAll(getSelectedCallingPerksOptions());
        return selectedPerksOptions;
    }
}
