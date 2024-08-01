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

import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.random.definition.RandomElementDefinition;

import java.util.Set;


/**
 * A skill that already has been split in different generalizations.
 */
public class AvailableSkill extends Skill<AvailableSkill> implements IValue {
    private Specialization specialization = null;

    private final SkillDefinition skillDefinition;

    public AvailableSkill(SkillDefinition skillDefinition) {
        super(skillDefinition.getId(), skillDefinition.getName(), skillDefinition.getDescription(), skillDefinition.getLanguage(),
                skillDefinition.getModuleName());
        this.skillDefinition = skillDefinition;
    }

    public AvailableSkill(SkillDefinition skillDefinition, Specialization specialization) {
        this(skillDefinition);
        this.specialization = specialization;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization generalization) {
        this.specialization = generalization;
    }

    public SkillDefinition getSkillDefinition() {
        return skillDefinition;
    }

    public TranslatedText getCompleteName() {
        return getCompleteName(getName(), getSpecialization());
    }

    public static TranslatedText getCompleteName(TranslatedText name, Specialization specialization) {
        return getCompleteName(name, specialization != null ? specialization.getName() : null);
    }

    public static TranslatedText getCompleteName(TranslatedText name, TranslatedText specializationName) {
        if (specializationName == null) {
            return name;
        }
        return new TranslatedText(name.getSpanish() + " [" + specializationName.getSpanish() + "]",
                name.getEnglish() + " [" + specializationName.getEnglish() + "]");
    }

    @Override
    public String getUniqueId() {
        return getUniqueId(getId(), getSpecialization());
    }

    public static String getUniqueId(String id, Specialization specialization) {
        if (specialization == null) {
            return id;
        }
        return id + "_" + specialization.getId();
    }

    @Override
    public String toString() {
        return getCompleteName().getTranslatedText();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((specialization == null) ? 0 : specialization.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AvailableSkill other = (AvailableSkill) obj;
        if (specialization == null) {
            return other.specialization == null;
        } else {
            return specialization.equals(other.specialization);
        }
    }

    @Override
    public RandomElementDefinition getRandomDefinition() {
        if (getSpecialization() != null) {
            return getSpecialization().getRandomDefinition();
        }
        return getSkillDefinition().getRandomDefinition();
    }

    @Override
    public int compareTo(AvailableSkill element) {
        if (getCompleteName() == null) {
            if (element.getCompleteName() == null) {
                return 0;
            }
            return -1;
        }
        if (element.getCompleteName() == null) {
            return 1;
        }
        return getCompleteName().compareTo(element.getCompleteName());
    }

    @Override
    public Set<String> getRestrictedToRaces() {
        if (getSpecialization() != null) {
            if (!getSpecialization().getRestrictedToRaces().isEmpty()) {
                return getSpecialization().getRestrictedToRaces();
            }
        }
        return getSkillDefinition().getRestrictedToRaces();
    }

    @Override
    public FactionGroup getRestrictedToFactionGroup() {
        if (getSpecialization() != null) {
            if (getSpecialization().getRestrictedToFactionGroup() != null) {
                return getSpecialization().getRestrictedToFactionGroup();
            }
        }
        return getSkillDefinition().getRestrictedToFactionGroup();
    }

    @Override
    public boolean isOfficial() {
        if (getSpecialization() != null) {
            if (!getSpecialization().isOfficial()) {
                return false;
            }
        }
        return getSkillDefinition().isOfficial();
    }

    @Override
    public boolean isRestricted() {
        if (getSpecialization() != null) {
            if (!getSpecialization().isRestricted()) {
                return false;
            }
        }
        return getSkillDefinition().isRestricted();
    }
}
