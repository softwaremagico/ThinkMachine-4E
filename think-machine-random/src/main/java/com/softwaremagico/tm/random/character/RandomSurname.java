package com.softwaremagico.tm.random.character;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.character.selectors.IRandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RandomSurname extends RandomSelector<Surname> {

    public RandomSurname(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getFaction() == null || getCharacterPlayer().getSpecie() == null || getCharacterPlayer().getInfo().getPlanet() == null) {
            throw new InvalidRandomElementSelectedException("Please, set faction, race and planet first.");
        }
        try {
            getCharacterPlayer().getInfo().setSurname(selectElementByWeight());
        } catch (InvalidRandomElementSelectedException e) {
            //If no surnames available choose any.
            getCharacterPlayer().getInfo().setSurname(FactionFactory.getInstance().getAllSurnames().stream()
                    .skip(RANDOM.nextInt(FactionFactory.getInstance().getAllSurnames().size())).findFirst().orElse(null));
        }
    }

    @Override
    protected Collection<Surname> getAllElements() {
        return FactionFactory.getInstance().getAllSurnames();
    }

    @Override
    protected int getWeight(Surname surname) throws InvalidRandomElementSelectedException {
        // Human nobility has faction as surname
        if (getCharacterPlayer().getRace() != null && !getCharacterPlayer().getRace().isXeno() && getCharacterPlayer().getFaction() != null
                && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
            if (getCharacterPlayer().getFaction().getName().contains(surname.getName())) {
                return BASIC_PROBABILITY;
            } else {
                throw new InvalidRandomElementSelectedException("Surname '" + surname + "' is restricted to non nobility factions.");
            }
        }
        // Not nobility no faction as surname.
        try {
            for (final Faction faction : FactionsFactory.getInstance().getElements(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())) {
                if (faction.getName().contains(surname.getName())) {
                    throw new InvalidRandomElementSelectedException("Surname '" + surname + "' is restricted to faction '"
                            + faction + "'.");
                }
            }
        } catch (InvalidXmlElementException e) {
            RandomGenerationLog.errorMessage(this.getClass().getName(), e);
        }
        // Name already set, use same faction to avoid weird mix.
        if (getCharacterPlayer().getInfo().getNames() != null && !getCharacterPlayer().getInfo().getNames().isEmpty()) {
            final Name firstName = getCharacterPlayer().getInfo().getNames().get(0);
            if (firstName.getFaction() != null && !Objects.equals(firstName.getFaction(), surname.getFaction())) {
                return 0;
            } else {
                return BASIC_PROBABILITY;
            }
        }

        // Not nobility and not name set, use surnames of the planet.
        if (getCharacterPlayer().getInfo().getPlanet() != null
                && !getCharacterPlayer().getInfo().getPlanet().getSurnames().isEmpty()) {
            if (getCharacterPlayer().getInfo().getPlanet().getHumanFactions().contains(surname.getFaction())) {
                return BASIC_PROBABILITY;
            } else {
                throw new InvalidRandomElementSelectedException("Surname '" + surname + "' not existing in planet '"
                        + getCharacterPlayer().getInfo().getPlanet() + "'.");
            }
        }
        // Planet without factions. Then choose own faction names
        if (getCharacterPlayer().getFaction() != null
                && !FactionsFactory.getInstance().getAllSurnames(getCharacterPlayer().getFaction()).isEmpty()
                && !getCharacterPlayer().getFaction().equals(surname.getFaction())) {
            throw new InvalidRandomElementSelectedException("Surname '" + surname + "' from an invalid faction '"
                    + getCharacterPlayer().getFaction() + "'.");
        }
        return BASIC_PROBABILITY;
    }

}
