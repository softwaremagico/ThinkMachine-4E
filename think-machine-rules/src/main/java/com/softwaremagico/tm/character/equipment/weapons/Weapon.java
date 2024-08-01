package com.softwaremagico.tm.character.equipment.weapons;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

public class Weapon extends Equipment<Weapon> {

    @JsonProperty("damageList")
    private List<WeaponDamage> weaponDamages;


    private Size size;
    private boolean techLevelSpecial;

    private String special;
    private Set<String> damageTypes;
    private WeaponType type;

    private Set<String> ammunition;
    @JsonProperty("others")
    private Set<String> accessories;


    /**
     * For creating empty elements.
     */
    public Weapon() {
        super();
        this.ammunition = new HashSet<>();
        this.accessories = new HashSet<>();
        this.damageTypes = new HashSet<>();
        this.weaponDamages = new ArrayList<>();
        this.type = null;
        this.techLevelSpecial = false;
        this.special = "";
        this.size = null;
    }

    public Weapon(String id, TranslatedText name, TranslatedText description, String language, String moduleName, WeaponType type,
                  List<WeaponDamage> damages, int techLevel, boolean techLevelSpecial, Size size, String special,
                  Set<String> damageTypes, float cost, Set<String> ammunition, Set<String> accessories) {
        super(id, name, description, cost, techLevel, language, moduleName);
        this.weaponDamages = damages;
        this.size = size;
        this.techLevelSpecial = techLevelSpecial;
        this.type = type;
        this.special = special;
        this.damageTypes = damageTypes;
        this.ammunition = ammunition;
        this.accessories = accessories;
    }

    public WeaponType getType() {
        return type;
    }


    @SuppressWarnings({"java:S3655"})
    public boolean isMeleeWeapon() {
        return getType() == WeaponType.MELEE || getType() == WeaponType.MELEE_ARTIFACT || getType() == WeaponType.MELEE_SHIELD
                || (getWeaponDamages().stream().findFirst().isPresent() && getWeaponDamages().stream().findFirst().get().getRange() == null);
    }

    public boolean isRangedWeapon() {
        return !isMeleeWeapon();
    }

    public boolean isAutomaticWeapon() {
        if (!weaponDamages.isEmpty()) {
            if (weaponDamages.get(0).getRate() != null) {
                return weaponDamages.get(0).getRate().toLowerCase().contains("a");
            }
        }
        return false;
    }

    public Size getSize() {
        return size;
    }

    public String getSpecial() {
        return special;
    }

    public Set<String> getDamageTypes() {
        return damageTypes;
    }

    public boolean isTechLevelSpecial() {
        return techLevelSpecial;
    }

    public Set<String> getAmmunition() {
        return ammunition;
    }

    public Set<String> getAccessories() {
        return accessories;
    }

    public void setWeaponDamages(List<WeaponDamage> weaponDamages) {
        this.weaponDamages = weaponDamages;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setTechLevelSpecial(boolean techLevelSpecial) {
        this.techLevelSpecial = techLevelSpecial;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public void setDamageTypes(String damageTypesContent) {
        this.damageTypes = new HashSet<>();
        readCommaSeparatedTokens(damageTypes, damageTypesContent);
    }

    public void setDamageTypes(Set<String> damageTypes) {
        this.damageTypes = damageTypes;
    }

    public void setType(WeaponType type) {
        this.type = type;
    }

    public void setAmmunition(String ammunitionContent) {
        this.ammunition = new HashSet<>();
        readCommaSeparatedTokens(ammunition, ammunitionContent);
    }

    public void setAmmunition(Set<String> ammunition) {
        this.ammunition = ammunition;
    }

    public void setAccessories(String accessoriesContent) {
        this.accessories = new HashSet<>();
        readCommaSeparatedTokens(accessories, accessoriesContent);
    }

    public void setAccessories(Set<String> accessories) {
        this.accessories = accessories;
    }

    public String getWeaponOthersText() {
        // Damage types
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String damageType : getDamageTypes()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            try {
                stringBuilder.append(DamageTypeFactory.getInstance().getElement(damageType).getName().getTranslatedText());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        }

        // Others
        if (getSpecial() != null && !getSpecial().isEmpty()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(" / ");
            }
            stringBuilder.append(getSpecial());
        }
        return stringBuilder.toString();
    }

    public List<WeaponDamage> getWeaponDamages() {
        return weaponDamages;
    }
}
