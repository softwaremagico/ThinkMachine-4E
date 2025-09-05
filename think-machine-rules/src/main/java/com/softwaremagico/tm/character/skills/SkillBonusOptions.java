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
import com.softwaremagico.tm.OptionSelector;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class SkillBonusOptions extends OptionSelector<Skill, SkillBonusOption> {
    @JsonIgnore
    private LinkedHashSet<SkillBonusOption> finalSkills;
    @JsonIgnore
    private Map<String, SkillBonusOption> skillBonusById;

    public SkillBonusOptions() {
        super();
    }

    public SkillBonusOptions(SkillBonusOptions optionSelector) {
        this(optionSelector, new LinkedHashSet<>(optionSelector.getSourceOptions()));
    }

    public SkillBonusOptions(SkillBonusOptions optionSelector, LinkedHashSet<SkillBonusOption> finalSkills) {
        super(optionSelector);
        this.finalSkills = finalSkills;
    }

    @Override
    public LinkedHashSet<SkillBonusOption> getOptions() {
        if (finalSkills == null) {
            try {
                if (super.getOptions() == null || super.getOptions().isEmpty()
                        || (!super.getOptions().isEmpty() && super.getOptions().iterator().next().getId() == null)) {
                    finalSkills = new LinkedHashSet<>();
                    if (super.getOptions() != null && !super.getOptions().isEmpty()) {
                        finalSkills.addAll(SkillFactory.getInstance().getElements().stream()
                                .map(skill -> new SkillBonusOption(skill, super.getOptions().iterator().next().getBonus())).collect(Collectors.toList()));
                    } else {
                        finalSkills.addAll(SkillFactory.getInstance().getElements().stream()
                                .map(SkillBonusOption::new).collect(Collectors.toList()));
                    }
                } else {
                    finalSkills = super.getOptions();
                }
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass(), e);
            }
        }
        return finalSkills;
    }

    public SkillBonusOption getSkillBonus(String skill) {
        if (skillBonusById == null) {
            skillBonusById = getOptions().stream().collect(Collectors.toMap(SkillBonusOption::getId, item -> item));
        }
        return skillBonusById.get(skill);
    }

    public int getBonus() {
        if (super.getOptions() != null && !super.getOptions().isEmpty()) {
            return getOptions().iterator().next().getBonus();
        }
        return 1;
    }

    @Override
    public String toString() {
        return "SkillOption{"
                + "(x" + getTotalOptions() + "): "
                + super.getOptions()
                + '}';
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (getOptions() != null) {
            getOptions().forEach(option -> {
                if (option.getId() != null) {
                    SkillFactory.getInstance().getElement(option.getId());
                } else if (option.getGroup() != null
                        && SkillFactory.getInstance().getElementsByGroup(option.getGroup()).isEmpty()) {
                    throw new InvalidXmlElementException("Invalid group '" + option.getGroup() + "' on skill. ");
                }
            });
        }
    }
}
