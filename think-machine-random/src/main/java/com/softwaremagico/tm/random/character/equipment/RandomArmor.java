package com.softwaremagico.tm.random.character.equipment;

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
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.log.RandomSelectorLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.DefensePreference;
import com.softwaremagico.tm.random.preferences.IRandomPreference;

import java.util.Collection;
import java.util.Set;

public class RandomArmor extends RandomEquipment<Armor> {

    private static final int VERY_EXPENSIVE_FRACTION = 2;
    private static final int EXPENSIVE_FRACTION = 4;
    private static final int AFFORDABLE_FRACTION = 6;

    public RandomArmor(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    public RandomArmor(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences, Set<Armor> suggestedElements) {
        super(characterPlayer, preferences, suggestedElements);
    }

    @Override
    public boolean isDisabledElement() {
        return getPreferences().contains(DefensePreference.SHIELD);
    }

    @Override
    protected double getVeryExpensiveFraction() {
        return VERY_EXPENSIVE_FRACTION;
    }

    @Override
    protected double getExpensiveFraction() {
        return EXPENSIVE_FRACTION;
    }

    @Override
    protected double getAffordableFraction() {
        return AFFORDABLE_FRACTION;
    }

    @Override
    protected Collection<Armor> getAllElements() throws InvalidXmlElementException {
        return ArmorFactory.getInstance().getSelectableElements();
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException, UnofficialElementNotAllowedException {
        if (getCharacterPlayer().getPurchasedArmor() == null) {
            getCharacterPlayer().setPurchasedArmor(selectElementByWeight());
        } else {
            RandomSelectorLog.warning(this.getClass(), "Armor already assigned!.");
        }
    }
}
