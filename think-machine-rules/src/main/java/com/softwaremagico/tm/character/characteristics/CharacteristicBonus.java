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
import com.softwaremagico.tm.XmlData;

public class CharacteristicBonus extends XmlData {
    @JsonProperty("bonus")
    private int bonus;
    @JsonProperty("id")
    private String characteristic;

    public CharacteristicBonus() {
        super();
    }

    public CharacteristicBonus(CharacteristicDefinition characteristic) {
        this(characteristic.getId());
    }

    public CharacteristicBonus(String characteristic) {
        this();
        setCharacteristic(characteristic);
    }

    public CharacteristicBonus(String characteristic, int bonus) {
        this(characteristic);
        setBonus(bonus);
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    @Override
    public String toString() {
        return characteristic + " (+" + bonus + ")";
    }
}
