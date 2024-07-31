package com.softwaremagico.tm.character.characteristics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.values.IValue;

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

public class Characteristic extends Element<Characteristic> implements IValue {
    //Default value is 1, and later race will set as the correct minimum value.
    public static final int DEFAULT_INITIAL_VALUE = 1;
    public static final int DEFAULT_HUMAN_INITIAL_VALUE = 3;
    public static final int DEFAULT_INITIAL_MAX_VALUE = 8;
    public static final int MAX_VALUE = 12;
    private int value = DEFAULT_INITIAL_VALUE;

    @JsonIgnore
    private final CharacteristicDefinition characteristicDefinition;

    public Characteristic(CharacteristicDefinition characteristicDefinition) {
        super(characteristicDefinition.getId(), characteristicDefinition.getName(), characteristicDefinition.getDescription(),
                characteristicDefinition.getLanguage(), characteristicDefinition.getModuleName());
        this.characteristicDefinition = characteristicDefinition;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public CharacteristicDefinition getCharacteristicDefinition() {
        return characteristicDefinition;
    }

    @Override
    public String toString() {
        return getName() + " (" + getValue() + ")";
    }

    @Override
    public int compareTo(Characteristic element) {
        return getCharacteristicDefinition().getOrder().compareTo(element.getCharacteristicDefinition().getOrder());
    }
}
