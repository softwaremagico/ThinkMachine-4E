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
        super(characterPlayer, LevelFactory.getInstance().getElement(characterPlayer, level));
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

    public Level getLevelDefinition() {
        return ((Level) getCharacterDefinitionStep());
    }

    public int getLevel() {
        return level;
    }

    public int getExtraSurgeRating() {
        return getLevelDefinition().getExtraSurgeRating();
    }

    public int getExtraSurgeNumber() {
        return getLevelDefinition().getExtraSurgeNumber();
    }

    public int getExtraVitality() {
        return getLevelDefinition().getExtraVitality();
    }

    public int getExtraVPBank() {
        return getLevelDefinition().getExtraVPBank();
    }

    public int getExtraRevivalRating() {
        return getLevelDefinition().getExtraRevivalRating();
    }

    public int getExtraRevivalNumber() {
        return getLevelDefinition().getExtraRevivalNumber();
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
