package com.softwaremagico.tm.random.character;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.IRandomPreference;
import com.softwaremagico.tm.random.character.selectors.NamesPreferences;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomName extends RandomSelector<Name> {

    public RandomName(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getFaction() == null || getCharacterPlayer().getSpecie() == null || getCharacterPlayer().getInfo().getPlanet() == null) {
            throw new InvalidRandomElementSelectedException("Please, set faction, race and planet first.");
        }
        NamesPreferences namesPreference = NamesPreferences.getSelected(getPreferences());
        final BeneficeSpecialization status = getCharacterPlayer().getStatus();
        // Nobility with more names. Unless set by the user.
        if (status != null && namesPreference == NamesPreferences.LOW
                && !getPreferences().contains(NamesPreferences.LOW)) {
            namesPreference = NamesPreferences.getByStatus(status.getCost());
        }

        for (int i = 0; i < namesPreference.randomGaussian(); i++) {
            try {
                final Name selectedName = selectElementByWeight();
                getCharacterPlayer().getInfo().addName(selectedName);
                removeElementWeight(selectedName);
                // Remove names from different factions. All names must be from
                // same faction
                for (final Name name : FactionsFactory.getInstance().getAllNames()) {
                    if (!Objects.equals(name.getFaction(), selectedName.getFaction())) {
                        removeElementWeight(name);
                    }
                }
            } catch (InvalidRandomElementSelectedException e) {
                throw new InvalidRandomElementSelectedException("No possible name for faction '"
                        + getCharacterPlayer().getFaction() + "' at '" + getCharacterPlayer().getInfo().getPlanet()
                        + "'.", e);
            }
        }
    }


    protected int getWeight(Name name) throws InvalidRandomElementSelectedException {
        // Only names of its gender.
        if (!name.getGender().equals(getCharacterPlayer().getInfo().getGender())) {
            throw new InvalidRandomElementSelectedException("Name '" + name + "' not valid for gender '"
                    + getCharacterPlayer().getInfo().getGender() + "'.");
        }
        // Nobility almost always names of her planet.
        if (getCharacterPlayer().getFaction() != null
                && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY
                && !FactionsFactory.getInstance().getAllNames(getCharacterPlayer().getFaction()).isEmpty()) {
            if (getCharacterPlayer().getFaction().get().getId().equals(name.getFaction())) {
                return BASIC_PROBABILITY;
            } else {
                throw new InvalidRandomElementSelectedException("Name '" + name
                        + "' not allowed for a nobility based character.");
            }
        }
        // Not nobility, use names available on the planet.
        if (getCharacterPlayer().getInfo().getPlanet() != null
                && !PlanetFactory.getInstance().getElement(getCharacterPlayer().getInfo().getPlanet()).getNames().isEmpty()) {
            //Only human names. Ignore xenos.
            if (PlanetFactory.getInstance().getElement(getCharacterPlayer().getInfo().getPlanet()).getHumanFactions().stream()
                    .map(Element::getId).collect(Collectors.toSet()).contains(name.getFaction())) {
                return BASIC_PROBABILITY;
            } else {
                throw new InvalidRandomElementSelectedException("Name '" + name + "' not present in planet '"
                        + getCharacterPlayer().getInfo().getPlanet() + "'.");
            }
        }
        // Planet without factions. Then choose own faction names
        if (getCharacterPlayer().getFaction() != null
                && !FactionsFactory.getInstance().getAllNames(getCharacterPlayer().getFaction()).isEmpty()
                && !getCharacterPlayer().getFaction().equals(name.getFaction())) {
            throw new InvalidRandomElementSelectedException("Name '" + name + "' from an invalid faction '"
                    + getCharacterPlayer().getFaction() + "'.");
        }

        // Surname already set, use same faction to avoid weird mix.
        if (getCharacterPlayer().getInfo().getSurname() != null) {
            if (getCharacterPlayer().getInfo().getSurname().getFaction() != null
                    && !Objects.equals(name.getFaction(), getCharacterPlayer().getInfo().getSurname().getFaction())) {
                return 0;
            }
        }

        return BASIC_PROBABILITY;
    }
}
