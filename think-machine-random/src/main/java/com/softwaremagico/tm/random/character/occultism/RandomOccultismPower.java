package com.softwaremagico.tm.random.character.occultism;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.random.character.selectors.AssignableRandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.RandomSelector;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RandomOccultismPower extends RandomSelector<OccultismPower> implements AssignableRandomSelector {

    public RandomOccultismPower(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException, UnofficialElementNotAllowedException {
        while (!getAllElements().isEmpty()
                && getCharacterPlayer().getOccultismPointsAvailable() > getCharacterPlayer().getOccultismPointsSpent()) {
            try {
                getCharacterPlayer().addOccultismPower(selectElementByWeight());
            } catch (InvalidXmlElementException | InvalidRandomElementSelectedException e) {
                break;
            }
        }
    }

    @Override
    protected Collection<OccultismPower> getAllElements() throws InvalidXmlElementException {
        if (getCharacterPlayer().getOccultismType() != null
                && Objects.equals(getCharacterPlayer().getOccultismType().getId(), OccultismTypeFactory.PSI_TAG)) {
            return OccultismPathFactory.getInstance().getPsiPowers();
        }
        if (getCharacterPlayer().getOccultismType() != null
                && Objects.equals(getCharacterPlayer().getOccultismType().getId(), OccultismTypeFactory.THEURGY_TAG)) {
            return OccultismPathFactory.getInstance().getTheurgyPowers();
        }
        return List.of();
    }

    @Override
    protected int getWeight(OccultismPower occultismPower) throws InvalidRandomElementSelectedException {
        if (occultismPower.getOccultismLevel() > getCharacterPlayer().getOccultismLevel()) {
            throw new InvalidRandomElementSelectedException("OccultismPower '" + occultismPower + "' restricted to level '"
                    + occultismPower.getOccultismLevel()
                    + "'. Character max allowed level is '" + getCharacterPlayer().getOccultismLevel() + "'.");
        }
        if (!getCharacterPlayer().canAddOccultismPower(occultismPower)) {
            throw new InvalidRandomElementSelectedException("OccultismPower '" + occultismPower + "' is invalid.'");
        }
        return super.getWeight(occultismPower);
    }
}
