package com.softwaremagico.tm.character.equipment.armours;

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
import com.softwaremagico.tm.character.equipment.Equipment;

import java.util.HashSet;
import java.util.Set;

public class Armour extends Equipment<Armour> {
    private int protection;
    @JsonProperty("damageType")
    private Set<String> damageTypes;
    @JsonProperty("standardPenalizations")
    private ArmourPenalization standardPenalization;
    @JsonProperty("specialPenalizations")
    private ArmourPenalization specialPenalization;
    @JsonProperty("shield")
    private Set<String> allowedShields;
    @JsonProperty("others")
    private Set<String> specifications;

    /**
     * For creating empty elements.
     */
    public Armour() {
        super();
        this.protection = 0;
        this.damageTypes = new HashSet<>();
        this.standardPenalization = new ArmourPenalization(0, 0, 0, 0);
        this.specialPenalization = new ArmourPenalization(0, 0, 0, 0);
        this.allowedShields = new HashSet<>();
        this.specifications = new HashSet<>();
    }

    public Armour(String id, TranslatedText name, TranslatedText description, String language, String moduleName, int techLevel, int protection,
                  Set<String> damageTypes, float cost) {
        super(id, name, description, cost, techLevel, language, moduleName);
        this.protection = protection;
        this.damageTypes = damageTypes;
        this.standardPenalization = new ArmourPenalization(0, 0, 0, 0);
        this.specialPenalization = new ArmourPenalization(0, 0, 0, 0);
        this.allowedShields = new HashSet<>();
        this.specifications = new HashSet<>();
    }

    public Armour(String id, TranslatedText name, TranslatedText description, String language, String moduleName, int techLevel, int protection,
                  Set<String> damageTypes, ArmourPenalization specialPenalization, ArmourPenalization otherPenalization,
                  Set<String> allowedShields, Set<String> specifications, float cost) {
        super(id, name, description, cost, techLevel, language, moduleName);
        this.protection = protection;
        this.damageTypes = damageTypes;
        this.standardPenalization = specialPenalization;
        this.specialPenalization = otherPenalization;
        this.allowedShields = allowedShields;
        this.specifications = specifications;
    }

    public int getProtection() {
        return protection;
    }

    public Set<String> getDamageTypes() {
        return damageTypes;
    }

    public ArmourPenalization getStandardPenalization() {
        return standardPenalization;
    }

    public ArmourPenalization getSpecialPenalization() {
        return specialPenalization;
    }

    public Set<String> getAllowedShields() {
        return allowedShields;
    }

    public boolean isHeavy() {
        return standardPenalization.getDexterityModification() > 0 || standardPenalization.getStrengthModification() > 0
                || standardPenalization.getEnduranceModification() > 0;
    }

    public void setProtection(int protection) {
        this.protection = protection;
    }

    public void setDamageTypes(String damageTypesContent) {
        damageTypes = new HashSet<>();
        readCommaSeparatedTokens(damageTypes, damageTypesContent);
    }

    public void setDamageTypes(Set<String> damageTypes) {
        this.damageTypes = damageTypes;
    }

    public void setStandardPenalization(ArmourPenalization standardPenalization) {
        this.standardPenalization = standardPenalization;
    }

    public void setSpecialPenalization(ArmourPenalization specialPenalization) {
        this.specialPenalization = specialPenalization;
    }

    public void setAllowedShields(String allowedShieldsContent) {
        allowedShields = new HashSet<>();
        readCommaSeparatedTokens(allowedShields, allowedShieldsContent);
    }

    public void setAllowedShields(Set<String> allowedShields) {
        this.allowedShields = allowedShields;
    }

    public void setSpecifications(String specificationsContent) {
        specifications = new HashSet<>();
        readCommaSeparatedTokens(specifications, specificationsContent);
    }


    public void setSpecifications(Set<String> specifications) {
        this.specifications = specifications;
    }

    public Set<String> getSpecifications() {
        return specifications;
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
