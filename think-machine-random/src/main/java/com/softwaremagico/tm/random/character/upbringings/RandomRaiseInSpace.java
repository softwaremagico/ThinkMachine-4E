package com.softwaremagico.tm.random.character.upbringings;

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

import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.OriginPreference;
import com.softwaremagico.tm.random.preferences.RandomSelector;
import com.softwaremagico.tm.random.preferences.TechPreference;

import java.util.Random;
import java.util.Set;

public final class RandomRaiseInSpace {

    public static final Random RANDOM = new Random();
    public static final int TOTAL_PROBABILITY = RandomSelector.BASIC_PROBABILITY + RandomSelector.GOOD_PROBABILITY + RandomSelector.VERY_GOOD_PROBABILITY;

    private RandomRaiseInSpace() {

    }

    public static boolean isRaisedInSpace(Set<IRandomPreference> preferences) {
        int bonus = 0;
        if (preferences.contains(TechPreference.HI_TECH)) {
            bonus += RandomSelector.GOOD_PROBABILITY;
        }
        if (preferences.contains(OriginPreference.SPACER)) {
            bonus += RandomSelector.VERY_GOOD_PROBABILITY;
        }
        return RANDOM.nextInt(TOTAL_PROBABILITY) < bonus;
    }
}
