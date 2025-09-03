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
import com.softwaremagico.tm.OptionSelector;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShield;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.thinkmachines.ThinkMachineFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponType;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EquipmentOptions extends OptionSelector<Equipment, EquipmentOption> {

    @JsonIgnore
    private LinkedHashSet<EquipmentOption> finalItems;

    @JsonProperty("requiredProperty")
    private Set<String> requiredProperty;


    @Override
    public LinkedHashSet<EquipmentOption> getOptions() {
        if (finalItems == null) {
            finalItems = new LinkedHashSet<>();
            if (super.getOptions() != null && !super.getOptions().isEmpty()) {
                super.getOptions().forEach(item -> {
                    final List<EquipmentOption> optionItems = new ArrayList<>();
                    if (item.getId() != null) {
                        optionItems.add(new EquipmentOption(item));
                    } else if (item.getGroup() != null) {
                        //Can be any
                        optionItems.addAll(ItemFactory.getInstance().getElements().stream()
                                .filter(item2 -> Objects.equals(item2.getGroup(), item.getGroup()))
                                .map(EquipmentOption::new).collect(Collectors.toList()));
                        optionItems.addAll(WeaponFactory.getInstance().getElements().stream()
                                .filter(item2 -> Objects.equals(item2.getGroup(), item.getGroup()))
                                .map(EquipmentOption::new).collect(Collectors.toList()));
                        optionItems.addAll(ArmorFactory.getInstance().getElements().stream()
                                .filter(item2 -> Objects.equals(item2.getGroup(), item.getGroup()))
                                .map(EquipmentOption::new).collect(Collectors.toList()));
                        optionItems.addAll(ShieldFactory.getInstance().getElements().stream()
                                .filter(item2 -> Objects.equals(item2.getGroup(), item.getGroup()))
                                .map(EquipmentOption::new).collect(Collectors.toList()));
                        optionItems.addAll(HandheldShieldFactory.getInstance().getElements().stream()
                                .filter(item2 -> Objects.equals(item2.getGroup(), item.getGroup()))
                                .map(EquipmentOption::new).collect(Collectors.toList()));
                        optionItems.addAll(ThinkMachineFactory.getInstance().getElements().stream()
                                .filter(item2 -> Objects.equals(item2.getGroup(), item.getGroup()))
                                .map(EquipmentOption::new).collect(Collectors.toList()));
                    } else if (item.getWeaponType() != null || item.getWeaponClass() != null) {
                        final List<Weapon> customizedWeapons = new ArrayList<>();
                        if (item.getWeaponType() == WeaponType.ANY) {
                            customizedWeapons.addAll(WeaponFactory.getInstance().getElements());
                        } else if ((item.getType() != null && item.getWeaponClass() != null)) {
                            if (item.getWeaponType() != null) {
                                customizedWeapons.addAll(
                                        WeaponFactory.getInstance().getWeaponsByClass(item.getWeaponClass()).stream().distinct()
                                                .filter(WeaponFactory.getInstance().getWeapons(item.getWeaponType())::contains)
                                                .collect(Collectors.toSet()));
                            } else {
                                customizedWeapons.addAll(
                                        new HashSet<>(WeaponFactory.getInstance().getWeaponsByClass(item.getWeaponClass())));
                            }
                        } else if (item.getType() != null) {
                            customizedWeapons.addAll(WeaponFactory.getInstance().getWeapons(item.getWeaponType()));
                        } else if (item.getWeaponClass() != null) {
                            customizedWeapons.addAll(WeaponFactory.getInstance().getWeaponsByClass(item.getWeaponClass()));
                        }
                        customizedWeapons.forEach(customizedWeapon ->
                                optionItems.add(new EquipmentOption(customizedWeapon, item.getQuality(),
                                        item.getStatus(), item.getQuantity(), item.getWeaponType(), item.getWeaponClass(),
                                        item.getType())));
                    } else if (Objects.equals(item.getType(), "handheldShield")) {
                        final List<HandheldShield> customizedHandheldShields =
                                new ArrayList<>(HandheldShieldFactory.getInstance().getElements());
                        customizedHandheldShields.forEach(handheldShield ->
                                optionItems.add(new EquipmentOption(handheldShield, item.getQuality(),
                                        item.getStatus(), item.getQuantity(), item.getWeaponType(), item.getWeaponClass(),
                                        item.getType())));
                    }
                    if (item.getExtras() != null) {
                        optionItems.forEach(optionItem -> optionItem.getExtras().addAll(item.getExtras()));
                    }
                    finalItems.addAll(optionItems);
                });
            } else {
                //Can be any
                finalItems = new LinkedHashSet<>();
                finalItems.addAll(ItemFactory.getInstance().getElements().stream()
                        .map(EquipmentOption::new).collect(Collectors.toList()));
                finalItems.addAll(WeaponFactory.getInstance().getElements().stream()
                        .map(EquipmentOption::new).collect(Collectors.toList()));
                finalItems.addAll(ArmorFactory.getInstance().getElements().stream()
                        .map(EquipmentOption::new).collect(Collectors.toList()));
                finalItems.addAll(ShieldFactory.getInstance().getElements().stream()
                        .map(EquipmentOption::new).collect(Collectors.toList()));
                finalItems.addAll(HandheldShieldFactory.getInstance().getElements().stream()
                        .map(EquipmentOption::new).collect(Collectors.toList()));
                finalItems.addAll(ThinkMachineFactory.getInstance().getElements().stream()
                        .map(EquipmentOption::new).collect(Collectors.toList()));
            }
            //Filter by required properties.
            if (getRequiredProperty() != null && !getRequiredProperty().isEmpty()) {
                finalItems = finalItems.stream().filter(item2 -> item2.getExtras().containsAll(getRequiredProperty()))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
            }
        }
        return finalItems;
    }

    public Set<EquipmentOption> getOptions(Collection<Selection> items) {
        return getOptions().stream().filter(e -> items.stream().map(Selection::getId).collect(Collectors.toList())
                .contains(e.getId())).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<String> getRequiredProperty() {
        return requiredProperty;
    }

    public void setRequiredProperty(Set<String> requiredProperty) {
        this.requiredProperty = requiredProperty;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (getOptions() != null) {
            getOptions().forEach(option -> {
                if (option.getId() != null && !option.getId().isEmpty()) {
                    if (option.getElement(option.getId()) == null) {
                        throw new InvalidXmlElementException("Option with id '" + option.getId() + "' does not exist");
                    }
                }
            });
        }
    }

    @Override
    public String toString() {
        return "EquipmentOptions{"
                //+ "options=" + getOptions()
                + '}';
    }
}
