package com.softwaremagico.tm.random.character.species;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomizeCharacterDefinitionStep;

import java.util.Collection;
import java.util.Set;

public class RandomSpecie extends RandomSelector<Specie> {

    public RandomSpecie(CharacterPlayer characterPlayer, Set<RandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getSpecie() == null || getCharacterPlayer().getSpecie().getId() == null) {
            getCharacterPlayer().setSpecie(selectElementByWeight().getId());
        }

        RandomizeCharacterDefinitionStep<Specie> randomizeCharacterDefinitionStep = new RandomizeCharacterDefinitionStep<>(
                getCharacterPlayer(),
                SpecieFactory.getInstance().getElement(getCharacterPlayer().getSpecie().getId()),
                getCharacterPlayer().getSpecie(),
                getPreferences()
        );

        randomizeCharacterDefinitionStep.assign();
    }

    @Override
    protected Collection<Specie> getAllElements() throws InvalidXmlElementException {
        return SpecieFactory.getInstance().getElements();
    }

    @Override
    protected void assignMandatoryValues(Set<Specie> mandatoryValues) throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        //Not needed.
    }

    @Override
    protected void assignIfMandatory(Specie element) throws InvalidXmlElementException, RestrictedElementException {
        //Not needed.
    }
}
