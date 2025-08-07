package com.softwaremagico.tm.random.character.characteristics;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RandomCharacteristics extends RandomSelector<CharacteristicDefinition> {

    public RandomCharacteristics(CharacterPlayer characterPlayer, Set<RandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        selectMainCharacteristics();
    }

    @Override
    protected Collection<CharacteristicDefinition> getAllElements() throws InvalidXmlElementException {
        return CharacteristicsDefinitionFactory.getInstance().getElements();
    }

    @Override
    protected void assignMandatoryValues(Set<CharacteristicDefinition> mandatoryValues) throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {

    }

    @Override
    protected void assignIfMandatory(CharacteristicDefinition element) throws InvalidXmlElementException, RestrictedElementException {

    }

    @Override
    protected int getWeight(CharacteristicDefinition element) throws InvalidRandomElementSelectedException {
        if (Objects.equals(getCharacterPlayer().getPrimaryCharacteristic(), element.getId())) {
            return LITTLE_PROBABILITY;
        } else if (Objects.equals(getCharacterPlayer().getSecondaryCharacteristic(), element.getId())) {
            return LITTLE_PROBABILITY;
        }
        return BASIC_PROBABILITY;
    }


    private void selectMainCharacteristics() throws InvalidRandomElementSelectedException {
        final String mainCharacteristic = selectStandardCharacteristic();
        getCharacterPlayer().setPrimaryCharacteristic(mainCharacteristic);

        String secondaryCharacteristic;
        do {
            secondaryCharacteristic = selectStandardCharacteristic();
        } while (Objects.equals(mainCharacteristic, secondaryCharacteristic));
        getCharacterPlayer().setSecondaryCharacteristic(secondaryCharacteristic);

        //Some species forces primary characteristics.
        try {
            getCharacterPlayer().getSpecie().validate();
        } catch (InvalidSelectionException | InvalidSpecieException e) {
            //Not valid, try again.
            selectMainCharacteristics();
        }
        RandomGenerationLog.debug(this.getClass(), "Primary characteristic is '{}' and secondary is '{}'.",
                mainCharacteristic, secondaryCharacteristic);
    }

    private String selectStandardCharacteristic() throws InvalidRandomElementSelectedException {
        String characteristic;
        do {
            characteristic = selectElementByWeight().getId();
        } while (CharacteristicsDefinitionFactory.getInstance().getElement(characteristic).getType() == CharacteristicType.OCCULTISM
                || CharacteristicsDefinitionFactory.getInstance().getElement(characteristic).getType() == CharacteristicType.OTHERS);
        return characteristic;
    }
}
