package com.softwaremagico.tm.character.level;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.CharacterSelectedElement;
import com.softwaremagico.tm.character.equipment.CharacterSelectedEquipment;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;

import java.util.List;

public class LevelSelector extends CharacterDefinitionStepSelection {

    @JsonProperty("classPerks")
    private List<CharacterSelectedElement> selectedClassPerksOptions;

    @JsonProperty("callingPerks")
    private List<CharacterSelectedElement> selectedCallingPerksOptions;

    private final int level;

    public LevelSelector(CharacterPlayer characterPlayer, int level) {
        super(characterPlayer, new Level(characterPlayer, level));
        this.level = level;
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

    public int getLevel() {
        return level;
    }

    @Override
    public void setSelectedPerksOptions(List<CharacterSelectedElement> selectedPerksOptions) {
        throw new UnsupportedOperationException("Not allowed!");
    }

    @Override
    public void setSelectedMaterialAwards(List<CharacterSelectedEquipment> selectedMaterialAwards) {
        throw new UnsupportedOperationException("Not allowed!");
    }

    @Override
    public void validate() throws InvalidSelectionException {
        super.validate();

        validateClassPerks();
        validateCallingPerks();
    }

    public List<PerkOptions> getClassPerksOptions() {
        return ((Level) getCharacterDefinitionStep()).getClassPerksOptions();
    }

    public List<PerkOptions> getCallingPerksOptions() {
        return ((Level) getCharacterDefinitionStep()).getCallingPerksOptions();
    }

    protected void validateClassPerks() {
        validatePerks(selectedClassPerksOptions, getClassPerksOptions());
    }

    protected void validateCallingPerks() {
        validatePerks(selectedClassPerksOptions, getCallingPerksOptions());
    }
}
