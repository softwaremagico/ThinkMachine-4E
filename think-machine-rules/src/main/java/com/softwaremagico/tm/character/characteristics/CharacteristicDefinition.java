package com.softwaremagico.tm.character.characteristics;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.values.IValue;

import java.util.Objects;

@JacksonXmlRootElement(localName = "characteristicDefinition")
public class CharacteristicDefinition extends Element implements Comparable<Element>, IValue {
    public static final int PRIMARY_CHARACTERISTIC_VALUE = 5;
    public static final int SECONDARY_CHARACTERISTIC_VALUE = 4;

    private Abbreviation abbreviation;

    @JsonProperty("characteristicType")
    private CharacteristicType type;

    private CharacteristicFunction function;

    public CharacteristicDefinition() {
        super();
    }

    public CharacteristicDefinition(String id, TranslatedText name, TranslatedText description, String language, String moduleName) {
        super(id, name, description, language, moduleName);
    }

    @Override
    public String toString() {
        return getName().toString();
    }

    public Abbreviation getAbbreviation() {
        return abbreviation;
    }

    protected void setAbbreviation(Abbreviation abbreviation) {
        this.abbreviation = abbreviation;
    }

    public CharacteristicType getType() {
        return type;
    }

    protected void setType(CharacteristicType type) {
        this.type = type;
    }

    public CharacteristicName getCharacteristicName() {
        for (final CharacteristicName characteristicName : CharacteristicName.values()) {
            if (Objects.equals(characteristicName.getId().toLowerCase(), getId().toLowerCase())) {
                return characteristicName;
            }
        }
        return null;
    }

    public CharacteristicFunction getFunction() {
        return function;
    }

    public void setFunction(CharacteristicFunction function) {
        this.function = function;
    }

    @Override
    public int compareTo(Element characteristic) {
        if (characteristic instanceof CharacteristicDefinition) {
            return getCharacteristicName().compareTo(((CharacteristicDefinition) characteristic).getCharacteristicName());
        }
        return super.compareTo(characteristic);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((abbreviation == null) ? 0 : abbreviation.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CharacteristicDefinition other = (CharacteristicDefinition) obj;
        if (abbreviation == null) {
            if (other.abbreviation != null) {
                return false;
            }
        } else if (!abbreviation.equals(other.abbreviation)) {
            return false;
        }
        return true;
    }
}
