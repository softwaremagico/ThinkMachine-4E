package com.softwaremagico.tm.random.character;

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
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Rank;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.IRandomPreference;
import com.softwaremagico.tm.random.character.selectors.NamesPreferences;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RandomName extends RandomSelector<Name> {

    public RandomName(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getFaction() == null || getCharacterPlayer().getSpecie() == null || getCharacterPlayer().getInfo().getPlanet() == null
                || getCharacterPlayer().getInfo().getGender() == null) {
            throw new InvalidRandomElementSelectedException("Please, set gender, faction, specie and planet first.");
        }
        NamesPreferences namesPreference = NamesPreferences.getSelected(getPreferences());
        final Rank rank = getCharacterPlayer().getRank();
        // Nobility with more names. Unless set by the user.
        if (rank != null && namesPreference == NamesPreferences.LOW
                && !getPreferences().contains(NamesPreferences.LOW)) {
            namesPreference = NamesPreferences.getByStatus(rank.getLevel());
        }

        for (int i = 0; i < namesPreference.randomGaussian(); i++) {
            try {
                final Name selectedName = selectElementByWeight();
                getCharacterPlayer().getInfo().addName(selectedName);
                removeElementWeight(selectedName);
                // Remove names from different factions. All names must be from the same faction
                for (final Name name : FactionFactory.getInstance().getAllNames()) {
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

    @Override
    protected Collection<Name> getAllElements() throws InvalidXmlElementException {
        return FactionFactory.getInstance().getAllNames();
    }

    @Override
    protected void assignMandatoryValues(Set<Name> mandatoryValues) throws InvalidXmlElementException {
        //Not needed.
    }

    @Override
    protected void assignIfMandatory(Name element) throws InvalidXmlElementException {
        //Not needed.
    }


    @Override
    protected int getWeight(Name name) throws InvalidRandomElementSelectedException {
        // Only names of its gender.
        if (!name.getGender().equals(getCharacterPlayer().getInfo().getGender())) {
            throw new InvalidRandomElementSelectedException("Name '" + name + "' not valid for gender '"
                    + getCharacterPlayer().getInfo().getGender() + "'.");
        }
        // Nobility almost always names of her planet.
        if (getCharacterPlayer().getFaction() != null
                && FactionGroup.get(getCharacterPlayer().getFaction().getGroup()) == FactionGroup.NOBILITY
                && !FactionFactory.getInstance().getAllNames(getCharacterPlayer().getFaction().getId()).isEmpty()) {
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
            if (new HashSet<>(PlanetFactory.getInstance().getElement(getCharacterPlayer().getInfo().getPlanet())
                    .getHumanFactions()).contains(name.getFaction())) {
                return BASIC_PROBABILITY;
            } else {
                throw new InvalidRandomElementSelectedException("Name '" + name + "' not present in planet '"
                        + getCharacterPlayer().getInfo().getPlanet() + "'.");
            }
        }
        // Planet without factions. Then choose own faction names
        if (getCharacterPlayer().getFaction() != null
                && !FactionFactory.getInstance().getAllNames(getCharacterPlayer().getFaction().getId()).isEmpty()
                && !getCharacterPlayer().getFaction().getId().equals(name.getFaction())) {
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
