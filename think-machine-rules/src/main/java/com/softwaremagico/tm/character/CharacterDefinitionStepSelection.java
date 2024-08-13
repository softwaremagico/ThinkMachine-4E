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
import com.softwaremagico.tm.exceptions.InvalidGeneratedCharacter;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;

import java.util.Arrays;
import java.util.List;

public abstract class CharacterDefinitionStepSelection<T extends CharacterDefinitionStepSelection<?>> extends Element<T> {

    private final CharacterPlayer characterPlayer;

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

        setCapabilityOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getCapabilityOptions().size()]));
        setCharacteristicOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getCharacteristicOptions().size()]));
        setSkillOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getSkillOptions().size()]));
        setPerksOptions(Arrays.asList(new CharacterSelectedElement[characterDefinitionStep.getPerksOptions().size()]));
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

    public void validate() throws InvalidSelectionException {
        super.validate();
        if (getRestrictions().isRestricted(characterPlayer)) {
            throw new InvalidSelectionException("Restrictions for  '" + getId() + "' are not meet.");
        }
    }
}
