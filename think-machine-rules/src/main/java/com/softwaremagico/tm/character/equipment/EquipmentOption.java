package com.softwaremagico.tm.character.equipment;

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
import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.armors.CustomizedArmor;
import com.softwaremagico.tm.character.equipment.handheldshield.CustomizedHandheldShield;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.item.CustomizedItem;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.item.Quality;
import com.softwaremagico.tm.character.equipment.item.Status;
import com.softwaremagico.tm.character.equipment.shields.CustomizedShield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.thinkmachines.CustomizedThinkMachine;
import com.softwaremagico.tm.character.equipment.thinkmachines.ThinkMachineFactory;
import com.softwaremagico.tm.character.equipment.weapons.CustomizedWeapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponType;

import java.util.Set;

public class EquipmentOption extends Option<Equipment> {

    @JsonProperty("weaponType")
    private WeaponType weaponType;

    @JsonProperty("type")
    private String type;

    @JsonProperty("weaponClass")
    private String weaponClass;

    private Quality quality;

    private Status status;

    @JsonProperty("others")
    private Set<String> others;

    public EquipmentOption() {
        super();
    }

    public EquipmentOption(Option<Equipment> equipment) {
        this();
        setId(equipment.getId());
        setQuantity(equipment.getQuantity());
    }

    public EquipmentOption(Equipment equipment) {
        this();
        setId(equipment.getId());
        setQuantity(equipment.getQuantity());
        setOthers(equipment.getOthers());
    }

    public EquipmentOption(Equipment equipment, int quantity) {
        this();
        setId(equipment.getId());
        setQuantity(quantity);
    }

    public EquipmentOption(Equipment equipment, Quality quality, Status status, int quantity, WeaponType weaponType,
                           String weaponClass, String type) {
        this();
        setId(equipment.getId());
        setQuantity(quantity);
        setQuality(quality);
        setStatus(status);
        setWeaponType(weaponType);
        setWeaponClass(weaponClass);
        setType(type);
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public String getWeaponClass() {
        return weaponClass;
    }

    public void setWeaponClass(String weaponClass) {
        this.weaponClass = weaponClass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Equipment getElement(String id) {
        Equipment equipment;
        try {
            equipment = new CustomizedArmor(ArmorFactory.getInstance().getElement(id));
        } catch (Exception e1) {
            try {
                equipment = new CustomizedItem(ItemFactory.getInstance().getElement(id));
            } catch (Exception e2) {
                try {
                    equipment = new CustomizedShield(ShieldFactory.getInstance().getElement(id));
                } catch (Exception e3) {
                    try {
                        equipment = new CustomizedHandheldShield(HandheldShieldFactory.getInstance().getElement(id));
                    } catch (Exception e4) {
                        try {
                            equipment = new CustomizedThinkMachine(ThinkMachineFactory.getInstance().getElement(id));
                        } catch (Exception e5) {
                            try {
                                equipment = new CustomizedWeapon(WeaponFactory.getInstance().getElement(id));
                            } catch (Exception e6) {
                                equipment = null;
                            }
                        }
                    }
                }
            }
        }
        if (equipment != null) {
            equipment.setQuantity(getQuantity());
            equipment.setQuality(quality);
            equipment.setStatus(status);
        }
        return equipment;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<String> getOthers() {
        return others;
    }

    public void setOthers(Set<String> others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return getId() + (getQuantity() > 1 ? " (" + getQuantity() + ")" : "");
    }
}
