package com.softwaremagico.tm.character.equipment.weapons;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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
import com.softwaremagico.tm.character.equipment.Size;

import java.util.HashSet;
import java.util.Set;

public class Ammunition extends Element<Ammunition> {
    private String goal;
    private String damage;
    private Integer strength;
    private String range;
    private Integer cost;
    private Size size;

    private Set<String> damageTypes;
    private Set<String> accessories;

    public Ammunition() {
        super();
    }

    public Ammunition(String id, TranslatedText name, TranslatedText description, String language, String moduleName, String goal, String damage,
                      Integer strength, String range, Size size, Integer cost, Set<String> damageTypes,
                      Set<String> accessories) {
        super(id, name, description, language, moduleName);
        this.goal = goal;
        this.damage = damage;
        this.strength = strength;
        this.range = range;
        this.cost = cost;
        this.size = size;
        this.damageTypes = damageTypes;
        this.accessories = accessories;
    }

    public String getGoal() {
        return goal;
    }

    public String getDamage() {
        return damage;
    }

    public Integer getStrength() {
        return strength;
    }

    public String getRange() {
        return range;
    }

    public String getStrengthOrRange() {
        if (range == null) {
            if (strength != null) {
                return strength + "";
            }
            return "--";
        }
        return range;
    }

    public Integer getCost() {
        return cost;
    }

    public Set<String> getAccessories() {
        return accessories;
    }

    public Set<String> getDamageTypes() {
        return damageTypes;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setDamageTypes(String damageTypesContent) {
        this.damageTypes = new HashSet<>();
        readCommaSeparatedTokens(this.damageTypes, damageTypesContent);
    }

    public void setDamageTypes(Set<String> damageTypes) {
        this.damageTypes = damageTypes;
    }

    public void setAccessories(String accessoriesContent) {
        this.accessories = new HashSet<>();
        readCommaSeparatedTokens(this.accessories, accessoriesContent);
    }

    public void setAccessories(Set<String> accessories) {
        this.accessories = accessories;
    }

    public Size getSize() {
        return size;
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
