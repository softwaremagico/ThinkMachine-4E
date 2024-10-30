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
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOptions;
import com.softwaremagico.tm.character.equipment.EquipmentOption;
import com.softwaremagico.tm.character.equipment.EquipmentOptions;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CharacterDefinitionStep<T extends Element> extends Element {
    private static final int TOTAL_CHARACTERISTICS_OPTIONS = 0;
    private static final int TOTAL_SKILL_OPTIONS = 0;

    @JsonProperty("capabilities")
    private List<CapabilityOptions> capabilityOptions;
    @JsonProperty("characteristics")
    private List<CharacteristicBonusOptions> characteristicBonusOptions;
    @JsonProperty("skills")
    private List<SkillBonusOptions> skillBonusOptions;
    @JsonProperty("perks")
    private List<PerkOptions> perksOptions;
    @JsonProperty("materialAwards")
    private List<EquipmentOptions> materialAwards;


    public List<CapabilityOptions> getCapabilityOptions() {
        return capabilityOptions;
    }

    public void setCapabilityOptions(List<CapabilityOptions> capabilityOptions) {
        this.capabilityOptions = capabilityOptions;
    }

    public List<CharacteristicBonusOptions> getCharacteristicOptions() {
        return Objects.requireNonNullElseGet(characteristicBonusOptions, ArrayList::new);
    }

    public void setCharacteristicOptions(List<CharacteristicBonusOptions> characteristicBonusOptions) {
        this.characteristicBonusOptions = characteristicBonusOptions;
    }

    public List<SkillBonusOptions> getSkillOptions() {
        return Objects.requireNonNullElseGet(skillBonusOptions, ArrayList::new);
    }

    public void setSkillOptions(List<SkillBonusOptions> skillBonusOptions) {
        this.skillBonusOptions = skillBonusOptions;
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

    public Set<EquipmentOption> getMaterialAwards(Collection<Selection> selectedMaterialAwards) {
        return getMaterialAwards().stream().map(m -> m.getOptions(selectedMaterialAwards)).flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public List<EquipmentOptions> getMaterialAwards() {
        if (materialAwards == null) {
            return new ArrayList<>();
        }
        return materialAwards;
    }

    public void setMaterialAwards(List<EquipmentOptions> materialAwards) {
        this.materialAwards = materialAwards;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();

        if (capabilityOptions != null) {
            capabilityOptions.forEach(OptionSelector::validate);
        }
        if (characteristicBonusOptions != null) {
            characteristicBonusOptions.forEach(OptionSelector::validate);
        }
        if (skillBonusOptions != null) {
            skillBonusOptions.forEach(OptionSelector::validate);
        }
        if (perksOptions != null) {
            perksOptions.forEach(OptionSelector::validate);
        }
        if (materialAwards != null) {
            materialAwards.forEach(EquipmentOptions::validate);
        }

        int totalCharacteristicsPoints = 0;
        for (CharacteristicBonusOptions characteristicBonusOptions : getCharacteristicOptions()) {
            totalCharacteristicsPoints += characteristicBonusOptions.getTotalOptions() * characteristicBonusOptions.getOptions().get(0).getBonus();
        }
        if (totalCharacteristicsPoints > getCharacteristicsTotalPoints()) {
            throw new InvalidXmlElementException("Element '" + getId() + "' has more than '" + getCharacteristicsTotalPoints() + "' characteristics options. "
                    + "Currently has '" + totalCharacteristicsPoints + "' characteristic points.");
        }


        int totalSkillPoints = 0;
        for (SkillBonusOptions skillBonusOptions : getSkillOptions()) {
            totalSkillPoints += skillBonusOptions.getTotalOptions() * skillBonusOptions.getOptions().get(0).getBonus();
        }
        if (totalSkillPoints > getSkillsTotalPoints()) {
            throw new InvalidXmlElementException("Element '" + getId() + "' has more than " + getSkillsTotalPoints() + " skill options. "
                    + "Currently has '" + totalSkillPoints + "' skill points.");
        }
    }
}
