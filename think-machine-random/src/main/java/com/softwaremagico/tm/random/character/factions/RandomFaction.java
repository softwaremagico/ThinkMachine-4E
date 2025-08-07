package com.softwaremagico.tm.random.character.factions;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomizeCharacterDefinitionStep;

import java.util.Collection;
import java.util.Set;

public class RandomFaction extends RandomSelector<Faction> {

    public RandomFaction(CharacterPlayer characterPlayer, Set<RandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getFaction() == null || getCharacterPlayer().getFaction().getId() == null) {
            getCharacterPlayer().setFaction(getCharacterPlayer().getFaction().getId());
        }

        final RandomizeCharacterDefinitionStep<Faction> randomizeCharacterDefinitionStep = new RandomizeCharacterDefinitionStep<>(
                getCharacterPlayer(),
                getCharacterPlayer().getFaction(),
                getPreferences()
        );

        randomizeCharacterDefinitionStep.assign();
    }

    @Override
    protected Collection<Faction> getAllElements() throws InvalidXmlElementException {
        return FactionFactory.getInstance().getElements();
    }

    @Override
    protected int getWeight(Faction faction) throws InvalidRandomElementSelectedException {
        // Humans only humans factions.
        if (!faction.getRestrictions().getRestrictedToSpecies().isEmpty() && getCharacterPlayer().getSpecie() != null
                && !faction.getRestrictions().getRestrictedToSpecies().contains(getCharacterPlayer().getSpecie().getId())) {
            throw new InvalidRandomElementSelectedException("Faction '" + faction + "' restricted for species '"
                    + faction.getRestrictions().getRestrictedToSpecies()
                    + "'. Character is '" + getCharacterPlayer().getSpecie() + "'.");
        }

        return super.getWeight(faction);
    }

    @Override
    protected void assignMandatoryValues(Set<Faction> mandatoryValues) throws InvalidXmlElementException {

    }

    @Override
    protected void assignIfMandatory(Faction element) throws InvalidXmlElementException {

    }
}
