package com.softwaremagico.tm.character.skills;

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
import com.softwaremagico.tm.XmlData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SkillOption extends XmlData {
    @JsonProperty("total")
    private int totalOptions;
    @JsonProperty("skills")
    private List<SkillBonus> skills;
    @JsonIgnore
    private Map<String, SkillBonus> skillBonusById;

    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
    }

    public List<SkillBonus> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillBonus> skills) {
        this.skills = skills;
    }

    public SkillBonus getSkillBonus(String skill) {
        if (skillBonusById == null) {
            skillBonusById = skills.stream().collect(Collectors.toMap(SkillBonus::getSkill, item -> item));
        }
        return skillBonusById.get(skill);
    }
}
