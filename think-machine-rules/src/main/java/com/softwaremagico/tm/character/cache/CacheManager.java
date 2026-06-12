package com.softwaremagico.tm.character.cache;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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

import com.softwaremagico.tm.character.capabilities.CapabilityWithSpecialization;
import com.softwaremagico.tm.character.perks.SpecializedPerk;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CacheManager {


    private Double cash;
    private Double spentCash;
    private Integer techLevel;
    private Set<CapabilityWithSpecialization> capabilityWithSpecializations;
    private Set<SpecializedPerk> perksWithSpecializations;
    private Set<String> allowedShields;
    private Map<String, Integer> skillValues;
    private Map<String, Integer> characteristicsValues;
    private Boolean warArmor;
    private Boolean combatArmor;
    private Boolean militaryWeapons;

    public void reset() {
        cash = null;
        spentCash = null;
        techLevel = null;
        capabilityWithSpecializations = null;
        perksWithSpecializations = null;
        allowedShields = null;
        warArmor = null;
        combatArmor = null;
        militaryWeapons = null;
        skillValues = new HashMap<>();
        characteristicsValues = new HashMap<>();
    }

    public void capabilitiesChanged() {
        cash = null;
        techLevel = null;
        capabilityWithSpecializations = null;
        warArmor = null;
        combatArmor = null;
        militaryWeapons = null;
    }

    public void equipmentPurchasedChanged() {
        spentCash = null;
        allowedShields = null;
    }

    public void perksChanged() {
        perksWithSpecializations = null;
    }

    public void skillsChanged() {
        skillValues = new HashMap<>();
    }

    public void characteristicsChanged() {
        characteristicsValues = new HashMap<>();
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public Double getSpentCash() {
        return spentCash;
    }

    public void setSpentCash(Double spentCash) {
        this.spentCash = spentCash;
    }

    public Integer getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(Integer techLevel) {
        this.techLevel = techLevel;
    }

    public Set<CapabilityWithSpecialization> getCapabilityWithSpecializations() {
        return capabilityWithSpecializations;
    }

    public void setCapabilityWithSpecializations(Set<CapabilityWithSpecialization> capabilityWithSpecializations) {
        this.capabilityWithSpecializations = capabilityWithSpecializations;
    }

    public Set<SpecializedPerk> getPerksWithSpecializations() {
        return perksWithSpecializations;
    }

    public void setPerksWithSpecializations(Set<SpecializedPerk> perksWithSpecializations) {
        this.perksWithSpecializations = perksWithSpecializations;
    }

    public Set<String> getAllowedShields() {
        return allowedShields;
    }

    public void setAllowedShields(Set<String> allowedShields) {
        this.allowedShields = allowedShields;
    }

    public Boolean getWarArmor() {
        return warArmor;
    }

    public void setWarArmor(Boolean warArmor) {
        this.warArmor = warArmor;
    }

    public Boolean getCombatArmor() {
        return combatArmor;
    }

    public void setCombatArmor(Boolean combatArmor) {
        this.combatArmor = combatArmor;
    }

    public Boolean getMilitaryWeapons() {
        return militaryWeapons;
    }

    public void setMilitaryWeapons(Boolean militaryWeapons) {
        this.militaryWeapons = militaryWeapons;
    }

    public Integer getSkillValue(String skill) {
        return skillValues.get(skill);
    }

    public void setSkillValue(String skill, int value) {
        skillValues.put(skill, value);
    }

    public Integer getCharacteristicValue(String characteristic) {
        return characteristicsValues.get(characteristic);
    }

    public void setCharacteristicValue(String characteristic, int value) {
        characteristicsValues.put(characteristic, value);
    }
}
