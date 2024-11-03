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
import com.softwaremagico.tm.OptionSelector;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CharacteristicBonusOptions extends OptionSelector<CharacteristicDefinition, CharacteristicBonusOption> {
    @JsonIgnore
    private List<CharacteristicBonusOption> finalCharacteristics;
    @JsonIgnore
    private Map<String, CharacteristicBonusOption> characteristicBonusById;

    @Override
    public List<CharacteristicBonusOption> getOptions() {
        if (finalCharacteristics == null) {
            try {
                if (super.getOptions() == null || super.getOptions().isEmpty()) {
                    finalCharacteristics = new ArrayList<>();
                    finalCharacteristics.addAll(CharacteristicsDefinitionFactory.getInstance().getElements().stream()
                            .map(CharacteristicBonusOption::new).collect(Collectors.toList()));
                } else {
                    finalCharacteristics = super.getOptions();
                }
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass(), e);
            }
        }
        return finalCharacteristics;
    }

    public CharacteristicBonusOption getCharacteristicBonus(String characteristic) {
        if (characteristicBonusById == null) {
            characteristicBonusById = getOptions().stream().collect(Collectors.toMap(CharacteristicBonusOption::getId, item -> item));
        }
        if (characteristicBonusById.get(characteristic) != null) {
            return characteristicBonusById.get(characteristic);
        }
        return new CharacteristicBonusOption(characteristic, 0);
    }

    @Override
    public String toString() {
        return "CharacteristicOption{"
                + "(x" + getTotalOptions() + "): "
                + super.getOptions()
                + '}';
    }


    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (getOptions() != null) {
            getOptions().forEach(option -> {
                if (option.getId() != null) {
                    CharacteristicsDefinitionFactory.getInstance().getElement(option.getId());
                }
            });
        }
    }
}
