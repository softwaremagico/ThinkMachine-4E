package com.softwaremagico.tm.random.character;

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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.character.selectors.IRandomPreference;
import com.softwaremagico.tm.random.definition.RandomElementDefinition;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
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

    public void updateWeights() throws InvalidXmlElementException {
        weightedElements = assignElementsWeight();
        totalWeight = assignTotalWeight();
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

    public abstract void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException;

    protected Set<IRandomPreference<?>> getPreferences() {
        if (preferences == null) {
            return new HashSet<>();
        }
        return preferences;
    }

    protected abstract Collection<Element> getAllElements() throws InvalidXmlElementException;

    /**
     * This mandatories values are defined by the user and must be assigned directly
     * without random approach. This method uses a list of elements that must be
     * directly assigned to the user.
     *
     * @param mandatoryValues set of elements to be assigned.
     * @throws InvalidXmlElementException
     */
    protected abstract void assignMandatoryValues(Set<Element> mandatoryValues) throws InvalidXmlElementException, RestrictedElementException,
            UnofficialElementNotAllowedException;

    /**
     * Must check if an element is mandatory, and if it is, it must be assigned.
     * This method defines a set of property for any element, that if accomplished
     * must be assigned.
     *
     * @param element element to check.
     * @throws InvalidXmlElementException
     */
    protected abstract void assignIfMandatory(Element element)
            throws InvalidXmlElementException, RestrictedElementException;

    private void assignMandatory() throws InvalidXmlElementException {
        final List<Element> shuffledList = new ArrayList<>(getAllElements());
        Collections.shuffle(shuffledList);
        for (final Element element : shuffledList) {
            try {
                assignIfMandatory(element);
            } catch (RestrictedElementException e) {
                throw new InvalidXmlElementException("Mandatory element cannot be assigned.", e);
            }
        }
    }

    private TreeMap<Integer, Element> assignElementsWeight() throws InvalidXmlElementException {
        final TreeMap<Integer, Element> calculatedWeight = new TreeMap<>();
        int count = 1;
        for (final Element element : getAllElements()) {
            try {
                validateElement(element);
            } catch (InvalidRandomElementSelectedException e) {
                // Element not valid. Ignore it.
                continue;
            }

            final int weight = getTotalWeight(element);
            if (weight > 0) {
                calculatedWeight.put(count, element);
                count += weight;
            }
        }
        return calculatedWeight;
    }

    public int getTotalWeight(Element element) {
        try {
            //Restricted elements has 0 weight.
            if (element.getRestrictions().isRestricted(characterPlayer)) {
                return 0;
            }

            if (characterPlayer != null && characterPlayer.getSettings().isOnlyOfficialAllowed() && !element.isOfficial()) {
                return 0;
            }

            int weight = getWeight(element);
            weight = (int) ((weight) * getRandomDefinitionBonus(element));
            if (weight > 0) {
                // Some probabilities are defined directly.
                if (element.getRandomDefinition().getStaticProbability() != null) {
                    weight = element.getRandomDefinition().getStaticProbability();
                }

                // Suggested ones.
                if (suggestedElements != null && suggestedElements.contains(element)) {
                    weight *= GOOD_PROBABILITY;
                }
            }
            return weight;
        } catch (InvalidRandomElementSelectedException e) {
            return 0;
        }
    }

    private double getRandomDefinitionBonus(Element element) {
        return getRandomDefinitionBonus(element.getRandomDefinition());
    }

    protected double getRandomDefinitionBonus(RandomElementDefinition randomDefinition) {
        double multiplier = 1d;

        if (randomDefinition == null) {
            return multiplier;
        }

        if (randomDefinition.getProbabilityMultiplier() != null) {
            RandomGenerationLog.debug(this.getClass().getName(),
                    "Random definition multiplier is '{}'.", randomDefinition.getProbabilityMultiplier());
            multiplier *= randomDefinition.getProbabilityMultiplier();
        }

        // Recommended to specie.
        if (getCharacterPlayer() != null && getCharacterPlayer().getSpecie() != null
                && randomDefinition.getRecommendedSpecies().contains(getCharacterPlayer().getSpecie().getId())) {
            RandomGenerationLog.debug(this.getClass().getName(),
                    "Random definition as recommended for '{}'.", getCharacterPlayer().getSpecie());
            multiplier *= HIGH_MULTIPLIER;
        }

        // Recommended to my faction group.
        if (getCharacterPlayer() != null && getCharacterPlayer().getFaction() != null
                && randomDefinition
                .getRecommendedFactionsGroups().contains(getCharacterPlayer().getFaction().getGroup())) {
            RandomGenerationLog.debug(this.getClass().getName(), "Random definition as recommended for '{}'.",
                    getCharacterPlayer().getFaction().getGroup());
            multiplier *= BASIC_MULTIPLIER;
        }

        // Recommended to my faction.
        if (getCharacterPlayer() != null && getCharacterPlayer().getFaction() != null
                && randomDefinition.getRecommendedFactions().contains(getCharacterPlayer().getFaction().getId())) {
            RandomGenerationLog.debug(this.getClass().getName(),
                    "Random definition as recommended for '{}'.", getCharacterPlayer().getFaction());
            multiplier *= HIGH_MULTIPLIER;
        }

        // Probability definition by preference.
        if (randomDefinition.getProbability() != null) {
            multiplier *= randomDefinition.getProbability().getProbabilityMultiplier();
            RandomGenerationLog.debug(this.getClass().getName(), "Random definition defines with bonus probability of '"
                    + randomDefinition.getProbability().getProbabilityMultiplier() + "'.");
        }

        RandomGenerationLog.debug(this.getClass().getName(),
                "Random definitions bonus multiplier is '{}'.", multiplier);
        return multiplier;
    }

    public void validateElement(Element element) throws InvalidRandomElementSelectedException {
        if (element == null) {
            throw new InvalidRandomElementSelectedException("Null elements not allowed.");
        }

        if (element.getRestrictions().isRestrictedToSpecie(characterPlayer)) {
            throw new InvalidRandomElementSelectedException("Element '" + element + "' is restricted to '"
                    + element.getRestrictions().getRestrictedToSpecies() + "'.");
        }

        if (element.getRestrictions().isRestrictedToFactionGroup(characterPlayer)) {
            throw new InvalidRandomElementSelectedException("Element '" + element + "' is restricted to '"
                    + element.getRestrictions().getRestrictedToFactionGroups() + "'.");
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

        // Race limitation
        if (getCharacterPlayer() != null && randomDefinition.getRestrictedSpecies() != null
                && !randomDefinition.getRestrictedSpecies().isEmpty()
                && !randomDefinition.getRestrictedSpecies().contains(getCharacterPlayer().getSpecie().getId())) {
            throw new InvalidRandomElementSelectedException(
                    "Element restricted to species '" + randomDefinition.getRestrictedSpecies() + "'.");
        }

        if (getCharacterPlayer() != null && randomDefinition.getForbiddenSpecies() != null
                && randomDefinition.getForbiddenSpecies().contains(getCharacterPlayer().getSpecie().getId())) {
            throw new InvalidRandomElementSelectedException(
                    "Element forbidden to species '" + randomDefinition.getForbiddenSpecies() + "'.");
        }

        // Faction restriction.
        if (getCharacterPlayer() != null && getCharacterPlayer().getFaction() != null
                && !randomDefinition.getRestrictedFactions().isEmpty()
                && !randomDefinition.getRestrictedFactions().contains(getCharacterPlayer().getFaction().getId())) {
            throw new InvalidRandomElementSelectedException(
                    "Element restricted to factions '" + randomDefinition.getRestrictedFactions() + "'.");
        }

        // Faction groups restriction.
        if (getCharacterPlayer() != null && getCharacterPlayer().getFaction() != null
                && !randomDefinition.getRestrictedFactionsGroups().isEmpty()
                && !randomDefinition.getRestrictedFactionsGroups().contains(getCharacterPlayer().getFaction().getGroup())) {
            throw new InvalidRandomElementSelectedException(
                    "Element restricted to factions groups '" + randomDefinition.getRestrictedFactionsGroups() + "'.");
        }
    }

    /**
     * Assign a weight to an element depending on the preferences selected.
     *
     * @param element to get the weight
     * @return weight as integer
     */
    protected abstract int getWeight(Element element) throws InvalidRandomElementSelectedException;

    /**
     * Selects a characteristic depending on its weight.
     *
     * @throws InvalidRandomElementSelectedException
     */
    protected Element selectElementByWeight() throws InvalidRandomElementSelectedException {
        if (weightedElements == null || weightedElements.isEmpty() || totalWeight == 0) {
            throw new InvalidRandomElementSelectedException("No elements to select");
        }
        final int value = RANDOM.nextInt(totalWeight) + 1;
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
        return selectedElement;
    }

    protected void removeElementWeight(Element element) {
        Integer keyToDelete = null;
        for (final Map.Entry<Integer, Element> entry : weightedElements.entrySet()) {
            if (entry.getValue().equals(element)) {
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
        int previousWeight = 0;
        for (final Map.Entry<Integer, Element> entry : weightedElements.entrySet()) {
            if (entry.getValue().equals(element)) {
                return entry.getKey() - previousWeight;
            }
            previousWeight = entry.getKey();
        }
        return null;
    }

    public boolean isMandatory(Element element) {
        return mandatoryValues.contains(element);
    }
}
