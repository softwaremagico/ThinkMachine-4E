package com.softwaremagico.tm.random.step;

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
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.MaxValueExceededException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.log.RandomSelectorLog;
import com.softwaremagico.tm.random.character.selectors.AssignableRandomSelector;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RandomCharacteristics extends RandomSelector<CharacteristicDefinition> implements AssignableRandomSelector {
    private final int bonus;

    public RandomCharacteristics(CharacterPlayer characterPlayer, Set<RandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.bonus = 0;
    }

    public RandomCharacteristics(CharacterPlayer characterPlayer, Set<RandomPreference> preferences, int bonus) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.bonus = bonus;
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        selectPrimaryCharacteristics();
    }

    @Override
    protected Collection<CharacteristicDefinition> getAllElements() throws InvalidXmlElementException {
        return CharacteristicsDefinitionFactory.getInstance().getElements();
    }

    @Override
    protected double getUserPreferenceBonus(CharacteristicDefinition element) {
        double multiplier = super.getUserPreferenceBonus(element);
        if (getPreferences().contains(RandomPreference.SPECIALIZED)) {
            multiplier += getCharacterPlayer().getCharacteristicValue(element.getCharacteristicName());
        } else if (getPreferences().contains(RandomPreference.BALANCED)) {
            multiplier += CharacteristicDefinition.MAX_CHARACTERISTIC_VALUE
                    - getCharacterPlayer().getCharacteristicValue(element.getCharacteristicName());
        }
        return multiplier;
    }

    @Override
    protected int getWeight(CharacteristicDefinition element) throws InvalidRandomElementSelectedException {
        if (element.getType() == CharacteristicType.OTHERS) {
            return 0;
        }

        //Max value achieved by specie and Max characteristic at some levels.
        try {
            if (getCharacterPlayer().getSpecie() != null
                    && (getCharacterPlayer().getCharacteristicValue(element.getCharacteristicName())
                    >= SpecieFactory.getInstance().getElement(getCharacterPlayer().getSpecie().getId())
                    .getSpecieCharacteristic(element.getCharacteristicName()).getMaximumValue())) {
                return 0;
            }
        } catch (MaxValueExceededException e) {
            return 0;
        }

        //Theurgy cannot have psi points and viceversa.
        final OccultismType occultismType = getCharacterPlayer().getOccultismType();
        if (occultismType != null && CharacteristicName.get(element.getId()) == CharacteristicName.THEURGY
                && CharacteristicName.get(occultismType.getId()) == CharacteristicName.PSI) {
            return 0;
        }
        if (occultismType != null && CharacteristicName.get(element.getId()) == CharacteristicName.PSI
                && CharacteristicName.get(occultismType.getId()) == CharacteristicName.THEURGY) {
            return 0;
        }

        if (!getPreferences().contains(RandomPreference.BALANCED) && Objects.equals(getCharacterPlayer().getPrimaryCharacteristic(), element.getId())
                || Objects.equals(getCharacterPlayer().getSecondaryCharacteristic(), element.getId())) {
            return LITTLE_PROBABILITY;
        }

        //No occultists without points does not add extra points.
        if (element.getType() == CharacteristicType.OCCULTISM && !getCharacterPlayer().isOccultist()) {
            return EXOTIC_PROBABILITY;
        }

        //Max characteristic achieved.
        try {
            final int charValue = getCharacterPlayer().getCharacteristicValue(element.getCharacteristicName());
            getCharacterPlayer().checkMaxValueByLevel(element, charValue + bonus);
        } catch (InvalidXmlElementException | MaxValueExceededException e) {
            return 0;
        }

        return super.getWeight(element);
    }


    private void selectPrimaryCharacteristics() throws InvalidRandomElementSelectedException {
        final String mainCharacteristic = selectStandardCharacteristic();
        getCharacterPlayer().setPrimaryCharacteristic(mainCharacteristic);
        RandomSelectorLog.debug(this.getClass().getSimpleName(), "Selecting primary characteristic '" + mainCharacteristic
                + "' with current value '" + getCharacterPlayer().getCharacteristicValue(mainCharacteristic) + "'.");

        String secondaryCharacteristic;
        do {
            secondaryCharacteristic = selectStandardCharacteristic();
        } while (Objects.equals(mainCharacteristic, secondaryCharacteristic));
        getCharacterPlayer().setSecondaryCharacteristic(secondaryCharacteristic);
        RandomSelectorLog.debug(this.getClass().getSimpleName(), "Selecting secondary characteristic '" + secondaryCharacteristic
                + "' with current value '" + getCharacterPlayer().getCharacteristicValue(secondaryCharacteristic) + "'.");

        //Some species forces primary characteristics.
        try {
            getCharacterPlayer().getSpecie().validate();
        } catch (InvalidSelectionException | InvalidSpecieException e) {
            //Not valid, try again.
            selectPrimaryCharacteristics();
        }
        RandomGenerationLog.debug(this.getClass(), "Primary characteristic is '{}' and secondary is '{}'.",
                mainCharacteristic, secondaryCharacteristic);
    }

    private String selectStandardCharacteristic() throws InvalidRandomElementSelectedException {
        String characteristic;
        final Specie specie = SpecieFactory.getInstance().getElement(getCharacterPlayer().getSpecie());
        boolean validCharacteristic = false;
        do {
            characteristic = selectElementByWeight().getId();
            try {
                getCharacterPlayer().checkMaxValueByLevel(characteristic, getCharacterPlayer().getCharacteristicValue(characteristic)
                        + CharacteristicDefinition.PRIMARY_CHARACTERISTIC_VALUE);
                validCharacteristic = true;
            } catch (MaxValueExceededException | InvalidXmlElementException ignored) {
                //Select another characteristic.
            }
        } while (CharacteristicsDefinitionFactory.getInstance().getElement(characteristic).getType() == CharacteristicType.OCCULTISM
                || CharacteristicsDefinitionFactory.getInstance().getElement(characteristic).getType() == CharacteristicType.OTHERS
                || (specie != null && specie.getPrimaryCharacteristics() != null && !specie.getPrimaryCharacteristics().contains(characteristic))
                || !validCharacteristic);
        return characteristic;
    }
}
