package com.softwaremagico.tm.random.character.characteristics;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RandomCharacteristics extends RandomSelector<Characteristic> {

    public RandomCharacteristics(CharacterPlayer characterPlayer, Set<RandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {

    }

    @Override
    protected Collection<Characteristic> getAllElements() throws InvalidXmlElementException {
        return List.of();
    }

    @Override
    protected void assignMandatoryValues(Set<Characteristic> mandatoryValues) throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {

    }

    @Override
    protected void assignIfMandatory(Characteristic element) throws InvalidXmlElementException, RestrictedElementException {

    }
}
