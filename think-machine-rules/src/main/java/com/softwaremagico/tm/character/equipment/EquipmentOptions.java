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
import com.softwaremagico.tm.OptionSelector;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShield;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EquipmentOptions extends OptionSelector<Equipment, EquipmentOption> {

    @JsonIgnore
    private List<EquipmentOption> finalItems;


    @Override
    public List<EquipmentOption> getOptions() {
        if (finalItems == null) {
            finalItems = new ArrayList<>();
            if (super.getOptions() != null && !super.getOptions().isEmpty()) {
                super.getOptions().forEach(item -> {
                    if (item.getId() != null) {
                        finalItems.add(new EquipmentOption(item));
                    } else if (item.getWeaponType() != null) {
                        final List<Weapon> customizedWeapons = new ArrayList<>();
                        if ((item.getType() != null && item.getWeaponClass() != null)) {
                            customizedWeapons.addAll(
                                    WeaponFactory.getInstance().getWeaponsByClass(item.getWeaponClass()).stream().distinct()
                                            .filter(WeaponFactory.getInstance().getWeapons(item.getWeaponType())::contains)
                                            .collect(Collectors.toSet()));
                        } else if (item.getType() != null) {
                            customizedWeapons.addAll(WeaponFactory.getInstance().getWeapons(item.getWeaponType()));
                        } else if (item.getWeaponClass() != null) {
                            customizedWeapons.addAll(WeaponFactory.getInstance().getWeaponsByClass(item.getWeaponClass()));
                        }
                        customizedWeapons.forEach(customizedWeapon ->
                                finalItems.add(new EquipmentOption(customizedWeapon, item.getQuality(),
                                        item.getStatus(), item.getQuantity(), item.getWeaponType(), item.getWeaponClass(),
                                        item.getType())));
                    } else if (Objects.equals(item.getType(), "handheldShield")) {
                        final List<HandheldShield> customizedHandheldShields =
                                new ArrayList<>(HandheldShieldFactory.getInstance().getElements());
                        customizedHandheldShields.forEach(handheldShield ->
                                finalItems.add(new EquipmentOption(handheldShield, item.getQuality(),
                                        item.getStatus(), item.getQuantity(), item.getWeaponType(), item.getWeaponClass(),
                                        item.getType())));
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
}
