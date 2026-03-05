package com.softwaremagico.tm.random;

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

import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.equipment.RandomArmor;
import com.softwaremagico.tm.random.character.equipment.RandomMeleeWeapon;
import com.softwaremagico.tm.random.character.equipment.RandomRangeWeapon;
import com.softwaremagico.tm.random.character.equipment.RandomShield;
import com.softwaremagico.tm.random.preferences.AttackPreferences;
import com.softwaremagico.tm.random.preferences.DefensePreference;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Test(groups = {"equipment"})
public class RandomEquipmentTests {

    private CharacterPlayer generateCharacterPlayerWithCash() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleHawkwoodCommander();
        final CharacterPerkOptions selectedElement = characterPlayer.getUpbringing().getNotSelectedPerksOptions(true).get(1);
        characterPlayer.getUpbringing().getSelectedPerksOptions().get(1).getSelections().clear();
        characterPlayer.getUpbringing().getSelectedPerksOptions().get(1).getSelections().add(
                selectedElement.getAvailableSelections().stream().filter(s -> Objects.equals(s.getId(), "cash1000")).findFirst().orElse(null));
        return characterPlayer;
    }

    private Set<IRandomPreference> convert(IRandomPreference... preferences) {
        if (preferences != null) {
            final List<IRandomPreference> customPreferences = Arrays.asList(preferences);
            customPreferences.removeIf(Objects::isNull);
            return new HashSet<>(customPreferences);
        } else {
            return new HashSet<>();
        }
    }

    @Test(expectedExceptions = InvalidXmlElementException.class)
    public void checkMeleeWeaponsNotAllowedWithRangePreference() {
        final CharacterPlayer characterPlayer = generateCharacterPlayerWithCash();
        RandomMeleeWeapon randomMeleeWeapon = new RandomMeleeWeapon(characterPlayer, convert(AttackPreferences.RANGED));
        randomMeleeWeapon.updateWeights();
    }

    @Test
    public void checkMeleeWeaponsAllowedWithMeleePreference() {
        final CharacterPlayer characterPlayer = generateCharacterPlayerWithCash();
        RandomMeleeWeapon randomMeleeWeapon = new RandomMeleeWeapon(characterPlayer, convert(AttackPreferences.MELEE));
        randomMeleeWeapon.updateWeights();
        Assert.assertFalse(randomMeleeWeapon.getWeightedElements().isEmpty());
    }

    @Test(expectedExceptions = InvalidXmlElementException.class)
    public void checkRangeWeaponsNotAllowedWithMeleePreference() {
        final CharacterPlayer characterPlayer = generateCharacterPlayerWithCash();
        RandomRangeWeapon randomRangeWeapon = new RandomRangeWeapon(characterPlayer, convert(AttackPreferences.MELEE));
        randomRangeWeapon.updateWeights();
    }

    @Test
    public void checkRangedWeaponsAllowedWithRangePreference() {
        final CharacterPlayer characterPlayer = generateCharacterPlayerWithCash();
        RandomRangeWeapon randomRangeWeapon = new RandomRangeWeapon(characterPlayer, convert(AttackPreferences.RANGED));
        randomRangeWeapon.updateWeights();
        Assert.assertFalse(randomRangeWeapon.getWeightedElements().isEmpty());
    }

    @Test(expectedExceptions = InvalidXmlElementException.class)
    public void checkArmorNotAllowedWithShieldPreference() {
        final CharacterPlayer characterPlayer = generateCharacterPlayerWithCash();
        RandomArmor randomArmor = new RandomArmor(characterPlayer, convert(DefensePreference.SHIELD));
        randomArmor.updateWeights();
    }

    @Test
    public void checkArmorAllowedWithArmorPreference() {
        final CharacterPlayer characterPlayer = generateCharacterPlayerWithCash();
        RandomArmor randomArmor = new RandomArmor(characterPlayer, convert(DefensePreference.ARMOR));
        randomArmor.updateWeights();
        Assert.assertFalse(randomArmor.getWeightedElements().isEmpty());
    }

    @Test(expectedExceptions = InvalidXmlElementException.class)
    public void checkShieldNotAllowedWithArmorPreference() {
        final CharacterPlayer characterPlayer = generateCharacterPlayerWithCash();
        RandomShield randomShield = new RandomShield(characterPlayer, convert(DefensePreference.ARMOR));
        randomShield.updateWeights();
    }

    @Test
    public void checkShieldAllowedWithShieldPreference() {
        final CharacterPlayer characterPlayer = generateCharacterPlayerWithCash();
        RandomShield randomShield = new RandomShield(characterPlayer, convert(DefensePreference.SHIELD));
        randomShield.updateWeights();
        Assert.assertFalse(randomShield.getWeightedElements().isEmpty());
    }

    @Test
    public void checkFencingPerkHasAlwaysSword() {

    }
}
