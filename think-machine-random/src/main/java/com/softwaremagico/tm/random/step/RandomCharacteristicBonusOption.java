package com.softwaremagico.tm.random.step;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
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

import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.characteristics.RandomCharacteristics;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;

import java.util.Collection;
import java.util.Set;

class RandomCharacteristicBonusOption extends RandomCharacteristics {

    private final CharacteristicBonusOptions characteristicBonusOptions;

    RandomCharacteristicBonusOption(CharacterPlayer characterPlayer, Set<RandomPreference> preferences,
                                    CharacteristicBonusOptions characteristicBonusOptions) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.characteristicBonusOptions = characteristicBonusOptions;
    }

    @Override
    protected Collection<CharacteristicDefinition> getAllElements() throws InvalidXmlElementException {
        return characteristicBonusOptions.getOptions().stream().map(Option::getElement).toList();
    }
}
