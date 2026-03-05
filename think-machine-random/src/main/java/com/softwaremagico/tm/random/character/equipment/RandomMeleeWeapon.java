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
import com.softwaremagico.tm.character.equipment.weapons.WeaponClass;
import com.softwaremagico.tm.character.equipment.weapons.WeaponType;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.random.character.RandomModifier;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.AttackPreferences;
import com.softwaremagico.tm.random.preferences.IRandomPreference;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomMeleeWeapon extends RandomWeapon {


    private static final String FENCING_GROUP = "fencing";
    private final boolean fencingPerk;

    public RandomMeleeWeapon(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        this(characterPlayer, preferences, new HashSet<>());
    }

    public RandomMeleeWeapon(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences, Set<Weapon> suggestedElements) {
        super(characterPlayer, preferences, suggestedElements);
        fencingPerk = characterPlayer.getPerks().stream().anyMatch(p -> Objects.equals(p.getGroup(), FENCING_GROUP));
    }

    @Override
    public boolean isDisabledElement() {
        return getPreferences().contains(AttackPreferences.RANGED);
    }

    @Override
    protected double getVeryExpensiveFraction() {
        return RandomModifier.MELEE_VERY_EXPENSIVE_FRACTION;
    }

    @Override
    protected double getExpensiveFraction() {
        return RandomModifier.MELEE_EXPENSIVE_FRACTION;
    }

    @Override
    protected double getAffordableFraction() {
        return RandomModifier.MELEE_AFFORDABLE_FRACTION;
    }

    @Override
    protected Collection<Weapon> getAllElements() throws InvalidXmlElementException {
        return super.getAllElements().stream().filter(weapon -> WeaponType.getMeleeTypes().contains(weapon.getType()))
                .collect(Collectors.toList());
    }

    @Override
    protected int getWeight(Weapon equipment) throws InvalidRandomElementSelectedException {
        //If he has fencing, select sword.
        if (fencingPerk && equipment.getWeaponClass() != WeaponClass.SWORD) {
            return 0;
        }

        return super.getWeight(equipment);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException, UnofficialElementNotAllowedException {
        getCharacterPlayer().setPurchasedMeleeWeapons(Collections.singletonList(selectElementByWeight()), false);
    }
}
