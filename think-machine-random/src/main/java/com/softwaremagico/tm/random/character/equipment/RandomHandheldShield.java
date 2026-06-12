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
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShield;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.log.RandomSelectorLog;
import com.softwaremagico.tm.random.character.RandomModifier;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.DefensePreference;
import com.softwaremagico.tm.random.preferences.IRandomPreference;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RandomHandheldShield extends RandomEquipment<HandheldShield> {

    private static final String HANDHELD_SHIELD_CAPABILITY = "handheldShield";
    private final boolean handheldShield;

    public RandomHandheldShield(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        this(characterPlayer, preferences, new HashSet<>());
    }

    public RandomHandheldShield(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences, Set<HandheldShield> suggestedElements) {
        super(characterPlayer, preferences, suggestedElements);
        handheldShield = getCharacterPlayer().hasCapability(HANDHELD_SHIELD_CAPABILITY, null, Phase.ANY, null);
    }

    @Override
    public boolean isDisabledElement() {
        return getPreferences().contains(DefensePreference.SHIELD);
    }

    @Override
    protected double getVeryExpensiveFraction() {
        return RandomModifier.HANDHELD_SHIELD_VERY_EXPENSIVE_FRACTION;
    }

    @Override
    protected double getExpensiveFraction() {
        return RandomModifier.HANDHELD_SHIELD_EXPENSIVE_FRACTION;
    }

    @Override
    protected double getAffordableFraction() {
        return RandomModifier.HANDHELD_SHIELD_AFFORDABLE_FRACTION;
    }

    @Override
    protected Collection<HandheldShield> getAllElements() throws InvalidXmlElementException {
        return HandheldShieldFactory.getInstance().getSelectableElements();
    }

    @Override
    protected int getWeight(HandheldShield handheldShield) throws InvalidRandomElementSelectedException {
        //If he hasn't handheldShield, cannot be used.
        if (!this.handheldShield) {
            throw new InvalidRandomElementSelectedException("Element '" + handheldShield + "' requires '" + HANDHELD_SHIELD_CAPABILITY + "' capability.");
        }
        if (getCharacterPlayer().getBestShield() != null) {
            throw new InvalidRandomElementSelectedException("Element '" + handheldShield + "' cannot be combined with an e-shield.");
        }

        return super.getWeight(handheldShield);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException, UnofficialElementNotAllowedException {
        if (getCharacterPlayer().getPurchasedShield() == null) {
            getCharacterPlayer().setPurchasedHandheldShield(selectElementByWeight(), true);
        } else {
            RandomSelectorLog.warning(this.getClass(), "Handheld shield already assigned!.");
        }
    }
}
