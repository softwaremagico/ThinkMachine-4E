package com.softwaremagico.tm.random.character.selectors;

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

import com.softwaremagico.tm.XmlData;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.log.RandomSelectorLog;
import com.softwaremagico.tm.log.RandomValuesLog;
import com.softwaremagico.tm.random.definition.RandomElementDefinition;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.stream.Collectors;

public abstract class RandomSelector<Element extends com.softwaremagico.tm.Element> extends XmlData {
    protected static final int EXOTIC_PROBABILITY = 1;
    protected static final int RARE_PROBABILITY = 10;
    protected static final int BASIC_PROBABILITY = 100;
    protected static final int LITTLE_PROBABILITY = 600;
    protected static final int GOOD_PROBABILITY = 2100;

    protected static final int BASIC_MULTIPLIER = 5;
    protected static final int HIGH_MULTIPLIER = 10;
    protected static final int USER_SELECTION_MULTIPLIER = 15;

    public static final Random RANDOM = new Random();

    private final CharacterPlayer characterPlayer;
    private final Set<RandomPreference> preferences;

    private final Set<Element> suggestedElements;

    // Weight -> Element.
    private TreeMap<Integer, Element> weightedElements;
    private int totalWeight;

    protected RandomSelector(CharacterPlayer characterPlayer, Set<RandomPreference> preferences)
            throws InvalidXmlElementException {
        this(characterPlayer, preferences, new HashSet<>());
    }

    protected RandomSelector(CharacterPlayer characterPlayer, Set<RandomPreference> preferences, Set<Element> suggestedElements) {
        this.characterPlayer = characterPlayer;
        this.preferences = preferences;
        this.suggestedElements = suggestedElements;
    }

    public boolean updateWeights() throws InvalidXmlElementException {
        weightedElements = assignElementsWeight();
        totalWeight = assignTotalWeight();
        return !weightedElements.isEmpty();
    }

    private Integer assignTotalWeight() {
        try {
            return weightedElements.lastKey();
        } catch (NoSuchElementException nse) {
            return 0;
        }
    }

    public CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
    }

    protected Set<RandomPreference> getPreferences() {
        if (preferences == null) {
            return new HashSet<>();
        }
        return preferences;
    }

    public void setPreferences(String preferencesContent) {
        if (preferences != null) {
            final StringTokenizer collectionTokenizer = new StringTokenizer(preferencesContent, ",");
            while (collectionTokenizer.hasMoreTokens()) {
                preferences.add(RandomPreference.valueOf(collectionTokenizer.nextToken().trim()));
            }
        }
    }

    protected abstract Collection<Element> getAllElements() throws InvalidXmlElementException;


    private TreeMap<Integer, Element> assignElementsWeight() throws InvalidXmlElementException {
        final TreeMap<Integer, Element> calculatedWeight = new TreeMap<>();
        int count = 0;
        for (final Element element : getAllElements()) {
            try {
                validateElement(element);
            } catch (InvalidRandomElementSelectedException e) {
                // Element not valid. Ignore it.
                continue;
            }

            try {
                final int weight = getElementWeight(element);
                if (weight > 0) {
                    calculatedWeight.put(count, element);
                    count += weight;
                }
            } catch (InvalidRandomElementSelectedException e) {
                // Element not valid. Ignore it.
                continue;
            }
        }
        //Last element probability.
        if (!calculatedWeight.isEmpty()) {
            calculatedWeight.put(count, null);
        }
        if (calculatedWeight.size() <= 1) {
            throw new InvalidXmlElementException("No elements available,");
        }
        return calculatedWeight;
    }

    public int getElementWeight(Element element) throws InvalidRandomElementSelectedException {
        try {
            //Restricted elements has 0 weight.
            if (element.getRestrictions().isRestricted(characterPlayer)) {
                throw new InvalidRandomElementSelectedException("Element '" + element + "' is restricted to character.");
            }

            if (characterPlayer != null && characterPlayer.getSettings().isOnlyOfficialAllowed() && !element.isOfficial()) {
                throw new InvalidRandomElementSelectedException("Non official elements are disabled. Element '" + element + "' is non official.");
            }

            int weight = getWeight(element);
            weight = (int) ((weight) * getUserPreferenceBonus(element));
            if (weight > 0) {
                // Suggested ones by default profiles.
                if (suggestedElements != null && suggestedElements.contains(element)) {
                    weight *= GOOD_PROBABILITY;
                }
            }
            return weight;
        } catch (InvalidRandomElementSelectedException e) {
            return 0;
        }
    }

    protected double getUserPreferenceBonus(Element element) {
        double multiplier = 1d;

        if (element.getRandomDefinition() == null) {
            return multiplier;
        }

        // Recommended to specie.
        if (getCharacterPlayer() != null && getCharacterPlayer().getSpecie() != null
                && element.getRandomDefinition().getRecommendedSpecies().contains(getCharacterPlayer().getSpecie().getId())) {
            RandomGenerationLog.debug(this.getClass().getName(),
                    "Random definition as recommended for '{}'.", getCharacterPlayer().getSpecie());
            multiplier *= HIGH_MULTIPLIER;
            // Recommended to my upbringing.
        } else if (getCharacterPlayer() != null && getCharacterPlayer().getUpbringing() != null
                && element.getRandomDefinition().getRecommendedUpbringings().contains(getCharacterPlayer().getUpbringing().getId())) {
            RandomGenerationLog.debug(this.getClass().getName(),
                    "Random definition as recommended for '{}'.", getCharacterPlayer().getUpbringing());
            multiplier *= HIGH_MULTIPLIER;
            // Recommended to my faction group.
        } else if (getCharacterPlayer() != null && getCharacterPlayer().getFaction() != null
                && element.getRandomDefinition()
                .getRecommendedFactionsGroups().contains(getCharacterPlayer().getFaction().getGroup())) {
            RandomGenerationLog.debug(this.getClass().getName(), "Random definition as recommended for '{}'.",
                    getCharacterPlayer().getFaction().getGroup());
            multiplier *= BASIC_MULTIPLIER;
            // Recommended to my faction.
        } else if (getCharacterPlayer() != null && getCharacterPlayer().getFaction() != null
                && element.getRandomDefinition().getRecommendedFactions().contains(getCharacterPlayer().getFaction().getId())) {
            RandomGenerationLog.debug(this.getClass().getName(),
                    "Random definition as recommended for '{}'.", getCharacterPlayer().getFaction());
            multiplier *= HIGH_MULTIPLIER;
            // Recommended to my calling.
        } else if (getCharacterPlayer() != null && getCharacterPlayer().getCalling() != null
                && element.getRandomDefinition().getRecommendedCallings().contains(getCharacterPlayer().getCalling().getId())) {
            RandomGenerationLog.debug(this.getClass().getName(),
                    "Random definition as recommended for '{}'.", getCharacterPlayer().getCalling());
            multiplier *= HIGH_MULTIPLIER;
            // Recommended to my faction group.
        } else if (element.getRandomDefinition().getProbabilityMultiplier() != null) {
            RandomValuesLog.debug(this.getClass().getName(),
                    "Random definition multiplier is '{}'.", element.getRandomDefinition().getProbabilityMultiplier());
            multiplier *= element.getRandomDefinition().getProbabilityMultiplier().getValue();
        } else if (element.getRandomDefinition().getProbability() != null) {
            RandomGenerationLog.debug(this.getClass().getName(), "Random definition defines with bonus probability of '"
                    + element.getRandomDefinition().getProbability().getProbabilityMultiplier() + "'.");
            multiplier *= element.getRandomDefinition().getProbability().getProbabilityMultiplier();
        }

        // Recommended by user preferences.
        if (preferences != null && !preferences.isEmpty()) {
            final List<String> common = preferences.stream().map(Enum::name).collect(Collectors.toList());
            common.retainAll(element.getRandomDefinition().getRecommendedPreferences());
            RandomGenerationLog.debug(this.getClass().getName(),
                    "Random definition multiplier '{}'.", (USER_SELECTION_MULTIPLIER * common.size()));
            multiplier += (USER_SELECTION_MULTIPLIER * common.size());
        }

        RandomValuesLog.debug(this.getClass().getName(),
                "Random definitions bonus multiplier is '{}'.", multiplier);
        return multiplier;
    }

    public void validateElement(Element element) throws InvalidRandomElementSelectedException {
        if (element == null) {
            throw new InvalidRandomElementSelectedException("Null elements not allowed.");
        }

        if (element.getRestrictions().isRestricted(characterPlayer)) {
            throw new InvalidRandomElementSelectedException("Element '" + element + "' is restricted.");
        }

        try {
            validateElement(element.getRandomDefinition());
        } catch (InvalidRandomElementSelectedException e) {
            throw new InvalidRandomElementSelectedException("Invalid element  '" + element + "'.", e);
        }
    }

    public void validateElement(RandomElementDefinition randomDefinition) throws InvalidRandomElementSelectedException {
        if (randomDefinition == null) {
            return;
        }

        // Check technology limitations.
        if (getCharacterPlayer() != null && randomDefinition.getMinimumTechLevel() != null && randomDefinition
                .getMinimumTechLevel() > getCharacterPlayer().getTechLevel()) {
            throw new InvalidRandomElementSelectedException("The tech level of the character is insufficient.");
        }

        if (getCharacterPlayer() != null && randomDefinition.getMaximumTechLevel() != null && randomDefinition
                .getMaximumTechLevel() < getCharacterPlayer().getTechLevel()) {
            throw new InvalidRandomElementSelectedException("The tech level of the character is too high.");
        }

        // User preferences  forbidden.
        if (preferences != null && !preferences.isEmpty() && !Collections.disjoint(preferences.stream().map(Enum::name).toList(),
                randomDefinition.getForbiddenPreferences())) {
            throw new InvalidRandomElementSelectedException(
                    "Element ignored due to preferences '" + randomDefinition.getForbiddenPreferences() + "'.");
        }

        // User preferences restriction.
        if (preferences != null && !preferences.isEmpty() && Collections.disjoint(preferences.stream().map(Enum::name).toList(),
                randomDefinition.getRestrictedPreferences())) {
            throw new InvalidRandomElementSelectedException(
                    "Element ignored due as lacking mandatory preference '" + randomDefinition.getRestrictedPreferences() + "'.");
        }
    }

    /**
     * Assign a weight to an element depending on the preferences selected.
     *
     * @param element to get the weight
     * @return weight as integer
     */
    protected int getWeight(Element element) throws InvalidRandomElementSelectedException {
        // Some probabilities are defined directly.
        if (element.getRandomDefinition().getStaticProbability() != null) {
            return element.getRandomDefinition().getStaticProbability();
        }
        //Reduce the elements with multiple specializations.
        if (element.getSpecializations() != null && !element.getSpecializations().isEmpty()) {
            return (int) Math.ceil((double) BASIC_PROBABILITY / element.getSpecializations().size());
        }
        return BASIC_PROBABILITY;
    }

    /**
     * Selects an element depending on its weight.
     *
     * @throws InvalidRandomElementSelectedException
     */
    public Element selectElementByWeight() throws InvalidRandomElementSelectedException {
        if (weightedElements == null || weightedElements.isEmpty() || totalWeight == 0) {
            if (!updateWeights()) {
                throw new InvalidRandomElementSelectedException("No elements to select");
            }
        }
        final int value = RANDOM.nextInt(totalWeight);
        Element selectedElement;
        final SortedMap<Integer, Element> view = weightedElements.headMap(value, true);
        try {
            selectedElement = view.get(view.lastKey());
        } catch (NoSuchElementException nse) {
            // If weight of first element is greater than 1, it is possible that
            // the value is less that the first element weight. That means that
            // 'view' would be empty launching a NoSuchElementException. Select
            // the first one by default.
            selectedElement = weightedElements.values().iterator().next();
        }
        if (selectedElement == null) {
            throw new InvalidRandomElementSelectedException("No elements to select");
        }
        RandomSelectorLog.debug(this.getClass().getName(), "Selected element '" + selectedElement + "' from weighted elements '"
                + getWeightedElements() + "'.");
        return selectedElement;
    }

    public void removeElementWeight(Element element) {
        Integer keyToDelete = null;
        for (final Map.Entry<Integer, Element> entry : weightedElements.entrySet()) {
            if (Objects.equals(entry.getValue(), element)) {
                keyToDelete = entry.getKey();
                break;
            }
        }
        if (keyToDelete != null) {
            final int weightToDelete = getAssignedWeight(weightedElements.get(keyToDelete));

            // Remove desired element.
            weightedElements.remove(keyToDelete);
            final TreeMap<Integer, Element> elementsToUpdate = new TreeMap<>(weightedElements);

            // Update keys weight
            for (final Map.Entry<Integer, Element> entry : elementsToUpdate.entrySet()) {
                if (entry.getKey() >= keyToDelete) {
                    final int currentWeight = entry.getKey();
                    weightedElements.remove(entry.getKey());
                    weightedElements.put(currentWeight - weightToDelete, entry.getValue());
                }
            }
        }
    }

    protected void updateWeight(Element element, int newWeight) {
        removeElementWeight(element);
        weightedElements.put(newWeight, element);
    }

    public SortedMap<Integer, Element> getWeightedElements() {
        return weightedElements;
    }

    public Integer getAssignedWeight(Element element) {
        if (element == null) {
            return null;
        }
        Integer elementWeight = null;
        for (final Map.Entry<Integer, Element> entry : weightedElements.entrySet()) {
            if (Objects.equals(entry.getValue(), element)) {
                elementWeight = entry.getKey();
                continue;
            }
            if (elementWeight != null) {
                return entry.getKey() - elementWeight;
            }
        }
        return null;
    }
}
