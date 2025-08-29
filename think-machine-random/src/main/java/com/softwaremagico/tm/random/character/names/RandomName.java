package com.softwaremagico.tm.random.character.names;

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
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.AssignableRandomSelector;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RandomName extends RandomSelector<Name> implements AssignableRandomSelector {

    public RandomName(CharacterPlayer characterPlayer, Set<RandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getInfo().getNames() != null && !getCharacterPlayer().getInfo().getNames().isEmpty()) {
            return;
        }
        if (getCharacterPlayer().getFaction() == null || getCharacterPlayer().getSpecie() == null
                || getCharacterPlayer().getInfo().getPlanet() == null || getCharacterPlayer().getInfo().getGender() == null) {
            throw new InvalidRandomElementSelectedException("Please, set gender, faction, specie and planet first.");
        }

        final NamesProbability namesProbability = NamesProbability.getByStatus(getCharacterPlayer().getRank());

        final Set<Name> selectedNames = new HashSet<>();
        for (int i = 0; i < namesProbability.randomGaussian(); i++) {
            try {
                final Name selectedName = selectElementByWeight();
                if (!selectedNames.contains(selectedName)
                        && (selectedNames.isEmpty()) || selectedNames.iterator().next().getFaction().equals(selectedName.getFaction())) {
                    getCharacterPlayer().getInfo().addName(selectedName);
                    selectedNames.add(selectedName);
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
        final Set<Name> names = new HashSet<>();
        names.addAll(FactionFactory.getInstance().getAllNames());
        names.addAll(SpecieFactory.getInstance().getAllNames());
        return names;
    }


    @Override
    protected int getWeight(Name name) throws InvalidRandomElementSelectedException {
        // Only names of its gender.
        if (!name.getGender().equals(getCharacterPlayer().getInfo().getGender())) {
            throw new InvalidRandomElementSelectedException("Name '" + name + "' not valid for gender '"
                    + getCharacterPlayer().getInfo().getGender() + "'.");
        }
        //Xenos have different names.
        if (getCharacterPlayer().getSpecie() != null
                && SpecieFactory.getInstance().getElement(getCharacterPlayer().getSpecie().getId()).isXeno()) {
            if (Objects.equals(name.getSpecie(), getCharacterPlayer().getSpecie().getId())) {
                return super.getWeight(name);
            } else {
                throw new InvalidRandomElementSelectedException("Name '" + name + "' is restricted to specie.");
            }
        }
        // If faction has names (nobility, vuldroks). Use them.
        if (getCharacterPlayer().getFaction() != null
                && !FactionFactory.getInstance().getAllNames(getCharacterPlayer().getFaction().getId(), getCharacterPlayer().getInfo().getGender())
                .isEmpty()) {
            if (Objects.equals(getCharacterPlayer().getFaction().getId(), name.getFaction())) {
                return super.getWeight(name);
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
                return super.getWeight(name);
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

        return super.getWeight(name);
    }
}
