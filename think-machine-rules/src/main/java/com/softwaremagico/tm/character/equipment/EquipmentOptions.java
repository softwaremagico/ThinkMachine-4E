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
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.OptionSelector;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShield;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.CustomizedWeapon;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EquipmentOptions extends OptionSelector<Equipment, EquipmentOption> {
    @JsonProperty("items")
    private List<Equipment> items;
    @JsonIgnore
    private List<EquipmentOption> finalItems;

    @Override
    public List<EquipmentOption> getOptions() {
        if (finalItems == null) {
            finalItems = new ArrayList<>();
            if (items != null && !items.isEmpty()) {
                items.forEach(item -> {
                    if (item.getId() != null) {
                        finalItems.add(new EquipmentOption(item));
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
                            if (item instanceof CustomizedWeapon) {
                                finalItems.add(new EquipmentOption(weapon, ((CustomizedWeapon) item).getQuality(),
                                        ((CustomizedWeapon) item).getStatus(), item.getQuantity()));
                            } else {
                                finalItems.add(new EquipmentOption(weapon, item.getQuantity()));
                            }
                        });
                    } else if (item instanceof HandheldShield) {
                        final List<HandheldShield> customizedHandheldShields =
                                new ArrayList<>(HandheldShieldFactory.getInstance().getElements());
                        customizedHandheldShields.forEach(handheldShield -> {
                            finalItems.add(new EquipmentOption(handheldShield, item.getQuantity()));
                        });
                    }
                });
            }
        }
        return finalItems;
    }

    public Set<EquipmentOption> getOptions(Collection<Selection> items) {
        return getOptions().stream().filter(e -> items.stream().map(Selection::getId).collect(Collectors.toList())
                .contains(e.getId())).collect(Collectors.toSet());
    }

    public void setItems(List<Equipment> items) {
        this.items = items;
    }

    public List<Equipment> getItems() {
        return items;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        if (items != null) {
            items.forEach(Element::validate);
        }
    }
}
