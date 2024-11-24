package com.softwaremagico.tm.character.specie;

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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;


@JacksonXmlRootElement(localName = "characteristic")
public class SpecieCharacteristic extends Element {

    @JsonAlias("id")
    private CharacteristicName characteristic;
    @JsonProperty("value")
    private int initialValue;
    @JsonProperty("maximumValue")
    private int maximumValue;
    @JsonProperty("maximumInitialValue")
    private int maximumInitialValue;

    public SpecieCharacteristic() {
        super();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        setCharacteristic(CharacteristicName.get(id));
    }

    public CharacteristicName getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(CharacteristicName characteristic) {
        this.characteristic = characteristic;
    }

    public int getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(int value) {
        this.initialValue = value;
    }

    public int getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(int max) {
        this.maximumValue = max;
    }

    public int getMaximumInitialValue() {
        return maximumInitialValue;
    }

    public void setMaximumInitialValue(int maximumInitialValue) {
        this.maximumInitialValue = maximumInitialValue;
    }

}
