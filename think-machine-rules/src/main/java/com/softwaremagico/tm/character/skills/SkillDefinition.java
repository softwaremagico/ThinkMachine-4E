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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.values.IValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SkillDefinition extends Skill<SkillDefinition> implements ISkillRandomDefintions, IValue {
    public static final int MAX_RANKS_TO_SKILLS_FACTION_LIMITED = 5;
    public static final String FACTION_LORE_ID = "factionLore";
    public static final String PLANETARY_LORE_ID = "planetaryLore";

    @JsonProperty("factionSkill")
    private Set<String> factions = new HashSet<>();
    private boolean natural = false;
    private Set<Specialization> specializations = new HashSet<>();
    private Set<String> requiredSkills = new HashSet<>();
    @JsonProperty("group")
    private SkillGroup skillGroup;
    // Number of times that a skill (generalizable) is shown in the PDF.
    private int numberToShow = 1;

    public SkillDefinition() {
        super();
    }

    public SkillDefinition(String id, TranslatedText name, TranslatedText description, String language, String moduleName) {
        super(id, name, description, language, moduleName);
    }

    public boolean isNatural() {
        return natural;
    }

    public boolean isSpecializable() {
        return !specializations.isEmpty();
    }

    public SkillGroup getSkillGroup() {
        return skillGroup;
    }

    public void setSkillGroup(SkillGroup skillGroup) {
        if (skillGroup == null) {
            throw new RuntimeException("Skill group cannot be null in skill '" + this + "'");
        }
        this.skillGroup = skillGroup;
    }

    public void setNatural(boolean natural) {
        this.natural = natural;
    }

    public Set<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Set<Specialization> specializations) {
        this.specializations = specializations;
    }

    public int getNumberToShow() {
        return numberToShow;
    }

    public void setNumberToShow(int numberToShow) {
        this.numberToShow = numberToShow;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + skillGroup + ") " + getSpecializations();
    }

    public Set<String> getFactions() {
        return factions;
    }

    public void addFaction(String faction) {
        factions.add(faction);
    }

    public void addFactions(Collection<String> factions) {
        this.factions.addAll(factions);
    }

    public void addRequiredSkill(String requiredSkillId) {
        this.requiredSkills.add(requiredSkillId);
    }

    public void addRequiredSkills(Collection<String> requiredSkills) {
        this.requiredSkills.addAll(requiredSkills);
    }

    public Set<String> getRequiredSkills() {
        return requiredSkills;
    }

    public boolean isLimitedToFaction() {
        return !factions.isEmpty();
    }

    public void setFactions(String factionsContent) {
        factions = new HashSet<>();
        readCommaSeparatedTokens(factions, factionsContent);
    }

    public void setFactions(Set<String> factions) {
        this.factions = factions;
    }

    public void setRequiredSkills(String requiredSkillsContent) {
        requiredSkills = new HashSet<>();
        readCommaSeparatedTokens(requiredSkills, requiredSkillsContent);
    }

    public void setRequiredSkills(Set<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}
