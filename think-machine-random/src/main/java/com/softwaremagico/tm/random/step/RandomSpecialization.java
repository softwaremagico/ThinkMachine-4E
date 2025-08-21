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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RandomSpecialization extends RandomSelector<Specialization> {
    private final List<Specialization> specializations;
    private final String capability;

    public RandomSpecialization(CharacterPlayer characterPlayer, Set<RandomPreference> preferences,
                                List<Specialization> specializations, String capability) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.specializations = specializations;
        this.capability = capability;
    }


    @Override
    protected Collection<Specialization> getAllElements() throws InvalidXmlElementException {
        return specializations;
    }


    @Override
    protected int getWeight(Specialization element) throws InvalidRandomElementSelectedException {
        //Selected specializations cannot be reselected.
        if (getCharacterPlayer().hasCapability(capability, element.getId())) {
            return 0;
        }
        return super.getWeight(element);
    }
}
