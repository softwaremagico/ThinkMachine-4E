package com.softwaremagico.tm.character.characteristics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.values.IValue;

import java.util.Objects;

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

public class Characteristic extends Element implements IValue {
    private int value = CharacteristicDefinition.INITIAL_CHARACTERISTIC_VALUE;

    @JsonIgnore
    private CharacteristicDefinition characteristicDefinition;

    public Characteristic() {
        super();
    }

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
    public int compareTo(Element element) {
        if (element instanceof Characteristic characteristic) {
            return getCharacteristicDefinition().getOrder().compareTo(characteristic.getCharacteristicDefinition().getOrder());
        }
        return super.compareTo(element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, characteristicDefinition);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Characteristic other)) {
            return false;
        }
        return value == other.value && Objects.equals(characteristicDefinition, other.characteristicDefinition);
    }
}
