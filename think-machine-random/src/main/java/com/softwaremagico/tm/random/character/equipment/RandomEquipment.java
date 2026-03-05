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
import com.softwaremagico.tm.character.equipment.AgoraGroup;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.RandomModifier;
import com.softwaremagico.tm.random.character.selectors.AssignableRandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.OperationalRolePreference;
import com.softwaremagico.tm.random.preferences.RandomSelector;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class RandomEquipment<E extends Equipment> extends RandomSelector<E> implements AssignableRandomSelector {

    private final boolean disabledElements;

    private final String characterFaction;
    private final String characterPlanet;
    private final String characterUpbringing;
    private final String characterSpecie;

    protected RandomEquipment(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        this(characterPlayer, preferences, new HashSet<>());
    }

    protected RandomEquipment(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences, Set<E> suggestedElements) {
        super(characterPlayer, preferences, suggestedElements);
        disabledElements = isDisabledElement();
        if (characterPlayer != null && characterPlayer.getFaction() != null) {
            characterFaction = characterPlayer.getFaction().getId();
        } else {
            characterFaction = null;
        }
        if (characterPlayer != null && characterPlayer.getSpecie() != null) {
            characterSpecie = characterPlayer.getSpecie().getId();
        } else {
            characterSpecie = null;
        }
        if (characterPlayer != null && characterPlayer.getUpbringing() != null) {
            characterUpbringing = characterPlayer.getUpbringing().getId();
        } else {
            characterUpbringing = null;
        }
        if (characterPlayer != null && characterPlayer.getInfo() != null && characterPlayer.getInfo().getPlanet() != null) {
            characterPlanet = characterPlayer.getInfo().getPlanet();
        } else {
            characterPlanet = null;
        }
    }

    public abstract boolean isDisabledElement();

    /**
     * Not so expensive weapons.
     *
     * @param equipment element.
     * @return weight
     */
    protected int getWeightCostModifier(E equipment) throws InvalidRandomElementSelectedException {
        final double remainingCash = getCharacterPlayer().getRemainingCash();
        if (getPreferences().contains(OperationalRolePreference.COMBAT)) {
            //Combat people can purchase more expensive weapons.
            return 0;
        }
        if (equipment.getCost() > remainingCash) {
            throw new InvalidRandomElementSelectedException("Not enough money!");
        } else if (equipment.getCost() > remainingCash / getVeryExpensiveFraction()) {
            return RandomModifier.VERY_EXPENSIVE_DIVIDER;
        } else if (equipment.getCost() > remainingCash / getExpensiveFraction()) {
            return RandomModifier.EXPENSIVE_DIVIDER;
        } else if (equipment.getCost() > remainingCash / getAffordableFraction()) {
            return RandomModifier.AFFORDABLE_DIVIDER;
        } else {
            return 1;
        }
    }

    protected abstract double getVeryExpensiveFraction();

    protected abstract double getExpensiveFraction();

    protected abstract double getAffordableFraction();

    protected int getWeightTechModifier(E equipment) {
        final int techDifference = getCharacterPlayer().getTechLevel() - equipment.getTechLevel();
        final double weight = Math.pow(10, 3d - techDifference);
        if (weight > 0) {
            return (int) weight;
        }
        return 0;
    }

    protected int getAgoraModifier(E equipment) {
        int modifier = 0;
        if (equipment.getAgoraGroups() != null) {
            for (AgoraGroup agoraGroup : equipment.getAgoraGroups()) {
                try {
                    if (Objects.equals(characterFaction, agoraGroup.name().toLowerCase())) {
                        modifier += RandomModifier.AGORA_MODIFIER_FACTION;
                    } else if (Objects.equals(characterSpecie, agoraGroup.name().toLowerCase())) {
                        modifier += RandomModifier.AGORA_MODIFIER_SPECIE;
                    } else if (Objects.equals(characterUpbringing, agoraGroup.name().toLowerCase())) {
                        modifier += RandomModifier.AGORA_MODIFIER_UPBRINGING;
                    } else if (Objects.equals(characterPlanet, agoraGroup.name().toLowerCase())) {
                        modifier += RandomModifier.AGORA_MODIFIER_PLANET;
                    }
                } catch (Exception e) {
                    throw new InvalidXmlElementException("Invalid xml element '" + equipment + "' with agora group '" + agoraGroup + "'. ");
                }
            }
        }
        return modifier != 0 ? modifier : 1;
    }

    @Override
    protected int getWeight(E equipment) throws InvalidRandomElementSelectedException {
        if (disabledElements) {
            throw new InvalidRandomElementSelectedException("Element '" + equipment + "' is disabled by user options.");
        }
        // Weapons only if technology is enough.
        if (equipment.getTechLevel() > getCharacterPlayer().getTechLevel()) {
            throw new InvalidRandomElementSelectedException("Technology requirements not met for '" + equipment + "'.");
        }

        // I can't afford it.
        if (equipment.getCost() > getCharacterPlayer().getRemainingCash()) {
            throw new InvalidRandomElementSelectedException("Cost not affordable for '" + equipment + "'.");
        }

        int basicWeight = super.getWeight(equipment);
        if (basicWeight > 0) {
            basicWeight += getWeightTechModifier(equipment);
        }

        final int weightCostModifier = getWeightCostModifier(equipment);
        if (weightCostModifier == 0) {
            return basicWeight * getAgoraModifier(equipment);
        }
        return (basicWeight / weightCostModifier) * getAgoraModifier(equipment);
    }
}
