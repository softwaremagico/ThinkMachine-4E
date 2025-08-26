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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.InvalidGeneratedCharacter;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;

import java.util.Collections;
import java.util.List;

public class SpecieCharacterDefinitionStepSelection extends CharacterDefinitionStepSelection {

    public SpecieCharacterDefinitionStepSelection(CharacterPlayer characterPlayer, String specie) throws InvalidGeneratedCharacter {
        super(characterPlayer, SpecieFactory.getInstance().getElement(specie));
        setId(specie);
    }

    @JsonIgnore
    protected int getCharacteristicTotalPoints() {
        return 0;
    }

    @JsonIgnore
    protected int getSkillTotalPoints() {
        return 0;
    }

    @Override
    public void validate() throws InvalidSelectionException {
        //Vorox forces main characteristics.
        final Specie specie = SpecieFactory.getInstance().getElement(getCharacterPlayer().getSpecie().getId());
        if (specie != null && getCharacterPlayer().getPrimaryCharacteristic() != null && getCharacterPlayer().getSecondaryCharacteristic() != null) {
            if (specie.getPrimaryCharacteristics() != null && !specie.getPrimaryCharacteristics().isEmpty()
                    && Collections.disjoint(specie.getPrimaryCharacteristics(),
                    List.of(getCharacterPlayer().getPrimaryCharacteristic(), getCharacterPlayer().getSecondaryCharacteristic()))) {
                throw new InvalidSpecieException("Primary or secondary characteristic must be any of '" + specie.getPrimaryCharacteristics() + "'.");
            }
        }
        try {
            super.validate();
        } catch (InvalidSelectionException e) {
            throw new InvalidSpecieException(e.getMessage(), e);
        }
    }

    @Override
    public Phase getPhase() {
        return Phase.SPECIE;
    }
}
