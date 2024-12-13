package com.softwaremagico.tm.character.equipment.weapons;

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


import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WeaponFactory extends XmlFactory<Weapon> {
    private static final String XML_FILE = "weapons.xml";

    private static Map<WeaponType, List<Weapon>> weaponsByType;
    private static Map<String, List<Weapon>> weaponsByClass;

    private static final class WeaponFactoryInit {
        public static final WeaponFactory INSTANCE = new WeaponFactory();
    }

    public static WeaponFactory getInstance() {
        return WeaponFactoryInit.INSTANCE;
    }

    public List<Weapon> getWeapons(WeaponType type) {
        if (weaponsByType == null) {
            weaponsByType = new HashMap<>();
            getElements().forEach(weapon -> {
                weaponsByType.computeIfAbsent(weapon.getType(), k -> new ArrayList<>());
                weaponsByType.get(weapon.getType()).add(weapon);
            });
        }
        return weaponsByType.get(type);
    }

    public List<Weapon> getWeaponsByClass(String weaponClass) {
        if (weaponsByClass == null) {
            weaponsByClass = new HashMap<>();
            getElements().forEach(weapon -> {
                weaponsByClass.computeIfAbsent(weaponClass, k -> new ArrayList<>());
                if (weapon != null && weapon.getWeaponClass() != null) {
                    weaponsByClass.get(weapon.getWeaponClass()).add(weapon);
                }
            });
        }
        return weaponsByClass.get(weaponClass);
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<Weapon> getElements() throws InvalidXmlElementException {
        return readXml(Weapon.class);
    }
}
