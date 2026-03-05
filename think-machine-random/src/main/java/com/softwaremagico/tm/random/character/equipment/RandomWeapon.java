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
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.IRandomPreference;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public abstract class RandomWeapon extends RandomEquipment<Weapon> {

    private static final String MILITARY_WEAPONS_CAPABILITY = "militaryWeapons";

    private final boolean disabledMilitaryElements;

    protected RandomWeapon(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        this(characterPlayer, preferences, null);
    }

    protected RandomWeapon(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences, Set<Weapon> suggestedElements) {
        super(characterPlayer, preferences, suggestedElements);
        disabledMilitaryElements = hasDisabledMilitaryElements();
    }

    private boolean hasDisabledMilitaryElements() {
        return !getCharacterPlayer().hasCapability(MILITARY_WEAPONS_CAPABILITY, null, Phase.ANY, null);
    }

    @Override
    protected int getWeight(Weapon weapon) throws InvalidRandomElementSelectedException {
        //If he hasn't handheldShield, cannot be used.
        if (disabledMilitaryElements && Objects.equals(weapon.getGroup(), MILITARY_WEAPONS_CAPABILITY)) {
            throw new InvalidRandomElementSelectedException("Element '" + weapon + "' needs 'militaryWeapons' perk.");
        }

        return super.getWeight(weapon);
    }


    @Override
    protected Collection<Weapon> getAllElements() throws InvalidXmlElementException {
        return WeaponFactory.getInstance().getSelectableElements();
    }
}
