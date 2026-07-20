package com.softwaremagico.tm.character.equipment.weapons;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

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

public class Weapon extends Equipment {

    @JsonProperty("damageList")
    private List<WeaponDamage> weaponDamages;

    private boolean techLevelSpecial;

    private String special;
    @JsonProperty("weaponClass")
    private WeaponClass weaponClass;
    @JsonProperty("damageTypes")
    private Set<String> damageTypes;
    @JsonProperty("weaponType")
    private WeaponType type;

    private Set<String> ammunition;

    /**
     * For creating empty elements.
     */
    public Weapon() {
        super();
        this.ammunition = new HashSet<>();
        this.damageTypes = new HashSet<>();
        this.weaponDamages = new ArrayList<>();
        this.type = null;
        this.techLevelSpecial = false;
        this.special = "";
    }

    public WeaponType getType() {
        return this.type;
    }

    @SuppressWarnings({"java:S3655"})
    public boolean isMeleeWeapon() {
        return this.getType() == WeaponType.MELEE || this.getType() == WeaponType.MELEE_ARTIFACT
                || this.getType() == WeaponType.MELEE_SHIELD || (this.getWeaponDamages().stream().findFirst().isPresent()
                        && this.getWeaponDamages().stream().findFirst().get().getRange() == null);
    }

    public boolean isRangedWeapon() {
        return !this.isMeleeWeapon();
    }

    public boolean isAutomaticWeapon() {
        if (!this.weaponDamages.isEmpty() && this.weaponDamages.get(0).getRate() != null) {
            return this.weaponDamages.get(0).getRate().toLowerCase().contains("a");
        }
        return false;
    }

    public String getSpecial() {
        return this.special;
    }

    public Set<String> getDamageTypes() {
        return this.damageTypes;
    }

    public boolean isTechLevelSpecial() {
        return this.techLevelSpecial;
    }

    public Set<String> getAmmunition() {
        return this.ammunition;
    }

    public void setWeaponDamages(List<WeaponDamage> weaponDamages) {
        this.weaponDamages = weaponDamages;
    }

    public void setTechLevelSpecial(boolean techLevelSpecial) {
        this.techLevelSpecial = techLevelSpecial;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public void setDamageTypes(Set<String> damageTypes) {
        this.damageTypes = damageTypes;
    }

    public void setType(WeaponType type) {
        this.type = type;
    }

    public void setAmmunition(Set<String> ammunition) {
        this.ammunition = ammunition;
    }

    public WeaponClass getWeaponClass() {
        return this.weaponClass;
    }

    public void setWeaponClass(WeaponClass weaponClass) {
        this.weaponClass = weaponClass;
    }

    public String getWeaponOthersText() {
        // Damage types
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String damageType : this.getDamageTypes()) {
            if (stringBuilder.isEmpty()) {
                stringBuilder.append(", ");
            }
            try {
                stringBuilder
                        .append(DamageTypeFactory.getInstance().getElement(damageType).getName().getTranslatedText());
            } catch (final InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass(), e);
            }
        }

        // Others
        if (this.getSpecial() != null && !this.getSpecial().isEmpty()) {
            if (stringBuilder.isEmpty()) {
                stringBuilder.append(" / ");
            }
            stringBuilder.append(this.getSpecial());
        }
        return stringBuilder.toString();
    }

    public List<WeaponDamage> getWeaponDamages() {
        return this.weaponDamages;
    }

    public void copy(Weapon weapon) {
        super.copy(weapon);
        this.setWeaponDamages(weapon.getWeaponDamages());
        this.setTechLevelSpecial(weapon.isTechLevelSpecial());
        this.setSpecial(weapon.getSpecial());
        this.setWeaponClass(weapon.getWeaponClass());
        this.setDamageTypes(weapon.getDamageTypes());
        this.setType(weapon.getType());
        if (weapon.getAmmunition() != null) {
            this.setAmmunition(new HashSet<>(weapon.getAmmunition()));
        }
        if (weapon.getOthers() != null) {
            this.setOthers(new HashSet<>(weapon.getOthers()));
        }
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (this.damageTypes != null) {
            this.damageTypes.forEach(damageType -> DamageTypeFactory.getInstance().getElement(damageType));
        }
        if (this.ammunition != null) {
            this.ammunition.forEach(ammo -> AmmunitionFactory.getInstance().getElement(ammo));
        }
        for (final WeaponDamage weaponDamage : this.getWeaponDamages()) {
            weaponDamage.validate();
        }
    }
}
