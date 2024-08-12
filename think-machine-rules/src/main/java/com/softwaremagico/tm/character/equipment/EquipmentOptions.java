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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.XmlData;
import com.softwaremagico.tm.character.equipment.item.Item;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.weapons.CustomizedWeapon;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EquipmentOptions extends XmlData {
    @JsonProperty("total")
    private int totalOptions;
    @JsonProperty("items")
    private List<Equipment<?>> items;
    @JsonIgnore
    private List<Equipment<?>> finalItems;

    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
    }

    public List<Equipment<?>> getItems() {
        if (finalItems == null) {
            finalItems = new ArrayList<>();
            if (items != null && !items.isEmpty()) {
                items.forEach(item -> {
                    if (item.getId() != null) {
                        final Item sourceItem = ItemFactory.getInstance().getElement(item.getId());
                        final Item finalItem = new Item();
                        finalItem.copy(sourceItem);
                        finalItem.setQuantity(item.getQuantity());
                        finalItems.add(finalItem);
                    } else if (item instanceof Weapon) {
                        final List<Weapon> customizedWeapons = new ArrayList<>();
                        if (((Weapon) item).getType() != null && ((Weapon) item).getWeaponClass() != null) {
                            customizedWeapons.addAll(
                                    WeaponFactory.getInstance().getWeaponsByClass(((Weapon) item).getWeaponClass()).stream().distinct()
                                            .filter(WeaponFactory.getInstance().getWeapons(((Weapon) item).getType())::contains)
                                            .collect(Collectors.toSet()));
                        } else if (((Weapon) item).getType() != null) {
                            customizedWeapons.addAll(WeaponFactory.getInstance().getWeapons(((Weapon) item).getType()));
                        } else if (((Weapon) item).getWeaponClass() != null) {
                            customizedWeapons.addAll(WeaponFactory.getInstance().getWeaponsByClass(((Weapon) item).getWeaponClass()));
                        }
                        customizedWeapons.forEach(weapon -> {
                            final CustomizedWeapon customizedWeapon = new CustomizedWeapon();
                            customizedWeapon.copy(weapon);
                            if (item instanceof CustomizedWeapon) {
                                customizedWeapon.setQuality(((CustomizedWeapon) item).getQuality());
                                customizedWeapon.setStatus(((CustomizedWeapon) item).getStatus());
                                customizedWeapon.setQuantity(item.getQuantity());
                            }
                            finalItems.add(customizedWeapon);
                        });
                    }
                });
            }
        }
        return finalItems;
    }

    public void setItems(List<Equipment<?>> items) {
        this.items = items;
    }
}
