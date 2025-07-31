package com.softwaremagico.tm.random.character;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.IRandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public abstract class RandomSelector<Element extends com.softwaremagico.tm.Element> {
    protected static final int MAX_PROBABILITY = 1000000;

    protected static final int TERRIBLE_PROBABILITY = -20;
    protected static final int DIFFICULT_PROBABILITY = -10;
    protected static final int BAD_PROBABILITY = -5;
    protected static final int PENALIZED_PROBABILITY = -1;
    protected static final int BASIC_PROBABILITY = 1;
    protected static final int LITTLE_PROBABILITY = 6;
    protected static final int FAIR_PROBABILITY = 11;
    protected static final int GOOD_PROBABILITY = 21;
    protected static final int VERY_GOOD_PROBABILITY = 31;

    protected static final int BASIC_MULTIPLIER = 5;
    protected static final int HIGH_MULTIPLIER = 10;

    public static final Random RANDOM = new Random();

    private final CharacterPlayer characterPlayer;
    private final Set<IRandomPreference<?>> preferences;

    private final Set<Element> suggestedElements;
    private final Set<Element> mandatoryValues;

    // Weight -> Element.
    private TreeMap<Integer, Element> weightedElements;
    private int totalWeight;

    protected RandomSelector(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences)
            throws InvalidXmlElementException {
        this(characterPlayer, preferences, new HashSet<>(), new HashSet<>());
    }

    protected RandomSelector(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences, Set<Element> suggestedElements,
                             Set<Element> mandatoryValues) {
        this.characterPlayer = characterPlayer;
        this.preferences = preferences;
        this.suggestedElements = suggestedElements;
        this.mandatoryValues = mandatoryValues;
    }

    public CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
    }

    public abstract void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException;

    protected Set<IRandomPreference<?>> getPreferences() {
        if (preferences == null) {
            return new HashSet<>();
        }
        return preferences;
    }
}
