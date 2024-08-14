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
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicOption;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.character.skills.SkillOption;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharacterDefinitionStep<T extends Element<?>> extends Element<T> {
    private static final int TOTAL_CHARACTERISTICS_OPTIONS = 0;
    private static final int TOTAL_SKILL_OPTIONS = 0;

    @JsonProperty("capabilities")
    private List<CapabilityOptions> capabilityOptions;
    @JsonProperty("characteristics")
    private List<CharacteristicOption> characteristicOptions;
    @JsonProperty("skills")
    private List<SkillOption> skillOptions;
    @JsonProperty("perks")
    private List<PerkOptions> perksOptions;


    public List<CapabilityOptions> getCapabilityOptions() {
        return capabilityOptions;
    }

    public void setCapabilityOptions(List<CapabilityOptions> capabilityOptions) {
        this.capabilityOptions = capabilityOptions;
    }

    public List<CharacteristicOption> getCharacteristicOptions() {
        return Objects.requireNonNullElseGet(characteristicOptions, ArrayList::new);
    }

    public void setCharacteristicOptions(List<CharacteristicOption> characteristicOptions) {
        this.characteristicOptions = characteristicOptions;
    }

    public List<SkillOption> getSkillOptions() {
        return Objects.requireNonNullElseGet(skillOptions, ArrayList::new);
    }

    public void setSkillOptions(List<SkillOption> skillOptions) {
        this.skillOptions = skillOptions;
    }

    public List<PerkOptions> getPerksOptions() {
        return perksOptions;
    }

    public void setPerksOptions(List<PerkOptions> perksOptions) {
        this.perksOptions = perksOptions;
    }

    public int getCharacteristicsTotalPoints() {
        return TOTAL_CHARACTERISTICS_OPTIONS;
    }

    public int getSkillsTotalPoints() {
        return TOTAL_SKILL_OPTIONS;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();

        int totalCharacteristicsPoints = 0;
        for (CharacteristicOption characteristicOption : getCharacteristicOptions()) {
            totalCharacteristicsPoints += characteristicOption.getTotalOptions() * characteristicOption.getCharacteristics().get(0).getBonus();
        }
        if (totalCharacteristicsPoints > getCharacteristicsTotalPoints()) {
            throw new InvalidXmlElementException("Element '" + getId() + "' has more than '" + getCharacteristicsTotalPoints() + "' characteristics options. "
                    + "Currently has '" + totalCharacteristicsPoints + "' characteristic points.");
        }


        int totalSkillPoints = 0;
        for (SkillOption skillOption : getSkillOptions()) {
            totalSkillPoints += skillOption.getTotalOptions() * skillOption.getSkills().get(0).getBonus();
        }
        if (totalSkillPoints > getSkillsTotalPoints()) {
            throw new InvalidXmlElementException("Element '" + getId() + "' has more than " + getSkillsTotalPoints() + " skill options. "
                    + "Currently has '" + totalSkillPoints + "' skill points.");
        }
    }
}
