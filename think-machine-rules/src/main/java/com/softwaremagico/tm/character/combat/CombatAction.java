package com.softwaremagico.tm.character.combat;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.exceptions.MaxInitialValueExceededException;

import java.util.Objects;
import java.util.Set;

public class CombatAction extends Element {
    private String goal;
    private String damage;
    private String others;
    private Set<CombatActionRequirement> requirements;

    public CombatAction() {
        super();
    }

    public CombatAction(String id, TranslatedText name, TranslatedText description, String language, String moduleName,
                        String goal, String damage, String others, Set<CombatActionRequirement> requirements) {
        super(id, name, description, language, moduleName);
        this.goal = goal;
        this.damage = damage;
        this.others = others;
        this.requirements = requirements;
    }

    public String getGoal() {
        return goal;
    }

    public String getDamage() {
        return damage;
    }

    public String getOthers() {
        return others;
    }

    public Set<CombatActionRequirement> getRequirements() {
        return requirements;
    }

    public boolean isAvailable(CharacterPlayer characterPlayer) {
        for (final CombatActionRequirement requirement : getRequirements()) {
            boolean allowed = false;
            for (final IValue restriction : requirement.getRequirements()) {
                if (restriction instanceof Skill) {
                    try {
                        if (characterPlayer.getSkillValue((Skill) restriction) >= requirement.getValue()) {
                            allowed = true;
                        }
                    } catch (MaxInitialValueExceededException e) {
                        allowed = true;
                    }
                } else if (restriction instanceof CharacteristicDefinition) {
                    if (characterPlayer.getCharacteristicCombatValue(restriction.getId()) != null
                            && characterPlayer.getCharacteristicCombatValue(restriction.getId()).getValue() >= requirement.getValue()) {
                        allowed = true;
                    }
                }
            }
            if (!allowed) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRestriction(String skillId) {
        for (final CombatActionRequirement requirement : getRequirements()) {
            for (final IValue restriction : requirement.getRequirements()) {
                if (restriction instanceof Skill) {
                    if (Objects.equals(restriction.getId(), skillId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public void setRequirements(Set<CombatActionRequirement> requirements) {
        this.requirements = requirements;
    }

    public boolean isMeleeAction() {
        return checkRestriction("melee");
    }

    public boolean isFightAction() {
        return checkRestriction("fight");
    }

    public boolean isShootAction() {
        return checkRestriction("slugGuns") || checkRestriction("energyGuns");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
