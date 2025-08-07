package com.softwaremagico.tm.random.character.upbringings;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomizeCharacterDefinitionStep;

import java.util.Collection;
import java.util.Set;

public class RandomUpbringing extends RandomSelector<Upbringing> {

    public RandomUpbringing(CharacterPlayer characterPlayer, Set<RandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getUpbringing() == null || getCharacterPlayer().getUpbringing().getId() == null) {
            getCharacterPlayer().setUpbringing(getCharacterPlayer().getUpbringing().getId());
        }

        final RandomizeCharacterDefinitionStep<Upbringing> randomizeCharacterDefinitionStep = new RandomizeCharacterDefinitionStep<>(
                getCharacterPlayer(),
                getCharacterPlayer().getUpbringing(),
                getPreferences()
        );

        randomizeCharacterDefinitionStep.assign();
    }

    @Override
    protected Collection<Upbringing> getAllElements() throws InvalidXmlElementException {
        return UpbringingFactory.getInstance().getElements();
    }

    @Override
    protected int getWeight(Upbringing upbringing) throws InvalidRandomElementSelectedException {
        // Humans only humans factions.
        if (!upbringing.getRestrictions().getRestrictedToSpecies().isEmpty() && getCharacterPlayer().getSpecie() != null
                && !upbringing.getRestrictions().getRestrictedToSpecies().contains(getCharacterPlayer().getSpecie().getId())) {
            throw new InvalidRandomElementSelectedException("Faction '" + upbringing + "' restricted for species '"
                    + upbringing.getRestrictions().getRestrictedToSpecies()
                    + "'. Character is '" + getCharacterPlayer().getSpecie() + "'.");
        }

        return super.getWeight(upbringing);
    }

    @Override
    protected void assignMandatoryValues(Set<Upbringing> mandatoryValues) throws InvalidXmlElementException {

    }

    @Override
    protected void assignIfMandatory(Upbringing element) throws InvalidXmlElementException {

    }
}
