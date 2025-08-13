package com.softwaremagico.tm.character.equipment.weapons;

import java.util.Objects;

public enum WeaponClass {
    SWORD,
    PISTOL;

    public static WeaponClass getClass(String name) {
        for (final WeaponClass weaponClass : WeaponClass.values()) {
            if (Objects.equals(weaponClass.name().toLowerCase(), name.toLowerCase())) {
                return weaponClass;
            }
        }
        return null;
    }
}
