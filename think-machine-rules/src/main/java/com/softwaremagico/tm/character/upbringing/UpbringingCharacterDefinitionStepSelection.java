package com.softwaremagico.tm.character.upbringing;

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

import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidGeneratedCharacter;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.InvalidUpbringingException;

public class UpbringingCharacterDefinitionStepSelection extends CharacterDefinitionStepSelection<UpbringingCharacterDefinitionStepSelection> {

    public UpbringingCharacterDefinitionStepSelection(CharacterPlayer characterPlayer, String upbringing) throws InvalidGeneratedCharacter {
        super(characterPlayer, UpbringingFactory.getInstance().getElement(upbringing));
        setId(upbringing);
    }

    @Override
    public void validate() throws InvalidSelectionException {
        if (getCharacterPlayer().getSpecie() == null) {
            throw new InvalidUpbringingException("No specie selected. Select a specie first.");
        }
        try {
            super.validate();
        } catch (InvalidSelectionException e) {
            throw new InvalidUpbringingException(e.getMessage(), e);
        }
    }
}
