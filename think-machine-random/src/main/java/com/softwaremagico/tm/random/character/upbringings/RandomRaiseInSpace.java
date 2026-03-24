package com.softwaremagico.tm.random.character.upbringings;

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
