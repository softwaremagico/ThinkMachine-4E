package com.softwaremagico.tm.character.characteristics;


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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CharacteristicName {

    STRENGTH(CharacteristicType.BODY),

    DEXTERITY(CharacteristicType.BODY),

    ENDURANCE(CharacteristicType.BODY),

    WITS(CharacteristicType.MIND),

    PERCEPTION(CharacteristicType.MIND),

    WILL(CharacteristicType.MIND),

    PRESENCE(CharacteristicType.SPIRIT),

    INTUITION(CharacteristicType.SPIRIT),

    FAITH(CharacteristicType.SPIRIT),

    INITIATIVE(CharacteristicType.OTHERS),

    MOVEMENT(CharacteristicType.OTHERS),

    TECH(CharacteristicType.OTHERS);

    private final CharacteristicType characteristicType;
    private static final Map<CharacteristicType, List<CharacteristicName>> CHARACTERISTIC_TYPE_LIST;

    static {
        CHARACTERISTIC_TYPE_LIST = new HashMap<>();
        for (final CharacteristicName characteristic : CharacteristicName.values()) {
            CHARACTERISTIC_TYPE_LIST.computeIfAbsent(characteristic.getCharacteristicType(), k -> new ArrayList<>());
            CHARACTERISTIC_TYPE_LIST.get(characteristic.getCharacteristicType()).add(characteristic);
        }
    }

    CharacteristicName(CharacteristicType characteristicType) {
        this.characteristicType = characteristicType;
    }

    public String getId() {
        return name().toLowerCase();
    }

    public CharacteristicType getCharacteristicType() {
        return characteristicType;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static CharacteristicName[] getBasicCharacteristics() {
        return new CharacteristicName[]{CharacteristicName.STRENGTH, CharacteristicName.DEXTERITY,
                CharacteristicName.ENDURANCE, CharacteristicName.WITS, CharacteristicName.PERCEPTION,
                CharacteristicName.TECH, CharacteristicName.PRESENCE, CharacteristicName.WILL, CharacteristicName.FAITH};
    }

    public static CharacteristicName get(String tag) {
        for (final CharacteristicName characteristicName : CharacteristicName.values()) {
            if (characteristicName.name().equalsIgnoreCase(tag)) {
                return characteristicName;
            }
        }
        return null;
    }

    public static List<CharacteristicName> getCharacteristics(CharacteristicType characteristicType) {
        return CHARACTERISTIC_TYPE_LIST.get(characteristicType);
    }
}
