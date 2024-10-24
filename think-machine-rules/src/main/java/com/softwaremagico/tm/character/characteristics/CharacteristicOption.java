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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CharacteristicOption extends Option<CharacteristicDefinition> {
    @JsonProperty("total")
    private int totalOptions;
    @JsonProperty("characteristics")
    private List<CharacteristicBonus> characteristics;
    @JsonIgnore
    private List<CharacteristicBonus> finalCharacteristics;
    @JsonIgnore
    private Map<String, CharacteristicBonus> characteristicBonusById;

    public CharacteristicOption() {
        super(CharacteristicsDefinitionFactory.getInstance());
    }

    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
    }

    public List<CharacteristicBonus> getCharacteristics() {
        if (finalCharacteristics == null) {
            try {
                if (characteristics == null || characteristics.isEmpty()) {
                    finalCharacteristics = new ArrayList<>();
                    finalCharacteristics.addAll(CharacteristicsDefinitionFactory.getInstance().getElements().stream()
                            .map(CharacteristicBonus::new).collect(Collectors.toList()));
                } else {
                    finalCharacteristics = characteristics;
                }
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass(), e);
            }
        }
        return finalCharacteristics;
    }

    public void setCharacteristics(List<CharacteristicBonus> characteristics) {
        this.characteristics = characteristics;
    }

    public CharacteristicBonus getCharacteristicBonus(String characteristic) {
        if (characteristicBonusById == null) {
            characteristicBonusById = getCharacteristics().stream().collect(Collectors.toMap(CharacteristicBonus::getCharacteristic, item -> item));
        }
        if (characteristicBonusById.get(characteristic) != null) {
            return characteristicBonusById.get(characteristic);
        }
        return new CharacteristicBonus(characteristic, 0);
    }

    @Override
    public String toString() {
        return "CharacteristicOption{"
                + "(x" + totalOptions + "): "
                + characteristics
                + '}';
    }
}
