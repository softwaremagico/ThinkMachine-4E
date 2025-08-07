package com.softwaremagico.tm.random.step;

import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.characteristics.RandomCharacteristics;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;

import java.util.Collection;
import java.util.Set;

class RandomCharacteristicBonusOption extends RandomCharacteristics {

    private final CharacteristicBonusOptions characteristicBonusOptions;

    RandomCharacteristicBonusOption(CharacterPlayer characterPlayer, Set<RandomPreference> preferences,
                                           CharacteristicBonusOptions characteristicBonusOptions) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.characteristicBonusOptions = characteristicBonusOptions;
    }

    @Override
    protected Collection<CharacteristicDefinition> getAllElements() throws InvalidXmlElementException {
        return characteristicBonusOptions.getOptions().stream().map(Option::getElement).toList();
    }
}
