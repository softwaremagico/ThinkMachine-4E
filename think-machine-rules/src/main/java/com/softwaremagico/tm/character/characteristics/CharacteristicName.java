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

    STRENGTH(CharacteristicType.BODY, 3),

    DEXTERITY(CharacteristicType.BODY, 3),

    ENDURANCE(CharacteristicType.BODY, 3),

    WITS(CharacteristicType.MIND, 3),

    PERCEPTION(CharacteristicType.MIND, 3),

    WILL(CharacteristicType.MIND, 3),

    PRESENCE(CharacteristicType.SPIRIT, 3),

    INTUITION(CharacteristicType.SPIRIT, 3),

    FAITH(CharacteristicType.SPIRIT, 3),

    PSI(CharacteristicType.OCCULTISM, 0),

    URGE(CharacteristicType.OCCULTISM, 0),

    THEURGY(CharacteristicType.OCCULTISM, 0),

    HUBRIS(CharacteristicType.OCCULTISM, 0),

    INITIATIVE(CharacteristicType.OTHERS, 0),

    MOVEMENT(CharacteristicType.OTHERS, 5),

    TECH(CharacteristicType.OTHERS, 4);

    private final CharacteristicType characteristicType;
    private final int initialValue;
    private static final Map<CharacteristicType, List<CharacteristicName>> CHARACTERISTIC_TYPE_LIST;

    static {
        CHARACTERISTIC_TYPE_LIST = new HashMap<>();
        for (final CharacteristicName characteristic : CharacteristicName.values()) {
            CHARACTERISTIC_TYPE_LIST.computeIfAbsent(characteristic.getCharacteristicType(), k -> new ArrayList<>());
            CHARACTERISTIC_TYPE_LIST.get(characteristic.getCharacteristicType()).add(characteristic);
        }
    }

    CharacteristicName(CharacteristicType characteristicType, int initialValue) {
        this.characteristicType = characteristicType;
        this.initialValue = initialValue;
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
                CharacteristicName.WILL, CharacteristicName.PRESENCE, CharacteristicName.INTUITION, CharacteristicName.FAITH};
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

    public int getInitialValue() {
        return initialValue;
    }
}
