package com.softwaremagico.tm.character.equipment;

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
import com.softwaremagico.tm.character.equipment.weapons.WeaponClass;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponType;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.restrictions.Restrictions;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class EquipmentOption extends Option<Equipment> {

    @JsonProperty("weaponType")
    private WeaponType weaponType;

    @JsonProperty("type")
    private String type;

    @JsonProperty("weaponClass")
    private WeaponClass weaponClass;

    private Quality quality;

    private Status status;

    @JsonProperty("extras")
    private Set<String> extras;

    public EquipmentOption() {
        super();
    }

    public EquipmentOption(Option<Equipment> equipment) {
        this();
        this.setId(equipment.getId());
        this.setQuantity(equipment.getQuantity());
    }

    public EquipmentOption(Equipment equipment) {
        this();
        this.setId(equipment.getId());
        this.setQuantity(equipment.getQuantity());
        this.setExtras(equipment.getOthers());
    }

    public EquipmentOption(Equipment equipment, int quantity) {
        this();
        this.setId(equipment.getId());
        this.setQuantity(quantity);
    }

    public EquipmentOption(Equipment equipment, Quality quality, Status status, int quantity, WeaponType weaponType,
            WeaponClass weaponClass, String type, String... extras) {
        this();
        this.setId(equipment.getId());
        this.setQuantity(quantity);
        this.setQuality(quality);
        this.setStatus(status);
        this.setWeaponType(weaponType);
        this.setWeaponClass(weaponClass);
        this.setType(type);
        this.setExtras(new HashSet<>(List.of(extras)));
    }

    public WeaponType getWeaponType() {
        return this.weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public WeaponClass getWeaponClass() {
        return this.weaponClass;
    }

    public void setWeaponClass(WeaponClass weaponClass) {
        this.weaponClass = weaponClass;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Restrictions getRestrictions() {
        if (this.getId() != null) {
            final Equipment equipment = this.getElement(this.getId());
            if (equipment == null) {
                MachineLog.severe(this.getClass(), "Invalid element '{}'", this.getId());
                throw new NullPointerException("Invalid element '" + this.getId() + "'.");
            }
            return equipment.getRestrictions();
        }
        return super.getRestrictions();
    }

    @Override
    public Equipment getElement(String id) {
        final Equipment equipment = this.resolveEquipment(id);
        if (equipment != null) {
            equipment.setQuantity(this.getQuantity());
            equipment.setQuality(this.quality);
            equipment.setStatus(this.status);
        }
        return equipment;
    }

    private Equipment resolveEquipment(final String id) {
        final List<Supplier<Equipment>> resolvers = List.of(
                () -> new CustomizedArmor(ArmorFactory.getInstance().getElement(id)),
                () -> new CustomizedItem(ItemFactory.getInstance().getElement(id)),
                () -> new CustomizedShield(ShieldFactory.getInstance().getElement(id)),
                () -> new CustomizedHandheldShield(HandheldShieldFactory.getInstance().getElement(id)),
                () -> new CustomizedThinkMachine(ThinkMachineFactory.getInstance().getElement(id)),
                () -> new CustomizedWeapon(WeaponFactory.getInstance().getElement(id)));
        for (final Supplier<Equipment> resolver : resolvers) {
            try {
                return resolver.get();
            } catch (final Exception ignored) {
                // Try next resolver.
            }
        }
        return null;
    }

    public Quality getQuality() {
        return this.quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<String> getExtras() {
        return this.extras;
    }

    public void setExtras(Set<String> extras) {
        this.extras = extras;
    }

    @Override
    public String toString() {
        return this.getId() + (this.getQuantity() > 1 ? " (" + this.getQuantity() + ")" : "");
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof EquipmentOption that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        return this.weaponType == that.weaponType && Objects.equals(this.type, that.type)
                && this.weaponClass == that.weaponClass && this.quality == that.quality && this.status == that.status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hashCode(this.weaponType);
        result = prime * result + Objects.hashCode(this.type);
        result = prime * result + Objects.hashCode(this.weaponClass);
        result = prime * result + Objects.hashCode(this.quality);
        result = prime * result + Objects.hashCode(this.status);
        return result;
    }
}
