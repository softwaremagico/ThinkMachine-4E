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
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
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
    protected void assignMandatoryValues(Set<Surname> mandatoryValues) throws InvalidXmlElementException {
        //Not needed.
    }

    @Override
    protected void assignIfMandatory(Surname element) throws InvalidXmlElementException {
        //Not needed.
    }

    @Override
    protected int getWeight(Surname surname) throws InvalidRandomElementSelectedException {
        // Human nobility has faction as surname
        if (getCharacterPlayer().getSpecie() != null
                && !SpecieFactory.getInstance().getElement(getCharacterPlayer().getSpecie()).isXeno()
                && getCharacterPlayer().getFaction() != null
                && Objects.equals(getCharacterPlayer().getFaction().getGroup(), FactionGroup.NOBILITY.toString())) {
            if (Objects.equals(getCharacterPlayer().getFaction().getId(), surname.getFaction())) {
                return BASIC_PROBABILITY;
            } else {
                throw new InvalidRandomElementSelectedException("Surname '" + surname + "' is restricted to non nobility factions.");
            }
        }
        // Not nobility no faction as surname.
        try {
            for (final Faction faction : FactionFactory.getInstance().getElements()) {
                if (faction.getId().contains(surname.getFaction())) {
                    throw new InvalidRandomElementSelectedException("Surname '" + surname + "' is restricted to faction '"
                            + faction + "'.");
                }
            }
        } catch (InvalidXmlElementException e) {
            RandomGenerationLog.errorMessage(this.getClass().getName(), e);
        }
        // Name already set, use the same faction to avoid weird mix.
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
                && !PlanetFactory.getInstance().getElement(getCharacterPlayer().getInfo().getPlanet()).getSurnames().isEmpty()) {
            if (PlanetFactory.getInstance().getElement(getCharacterPlayer().getInfo().getPlanet()).getHumanFactions().contains(surname.getFaction())) {
                return BASIC_PROBABILITY;
            } else {
                throw new InvalidRandomElementSelectedException("Surname '" + surname + "' not existing in planet '"
                        + getCharacterPlayer().getInfo().getPlanet() + "'.");
            }
        }
        // Planet without factions. Then choose own faction names
        if (getCharacterPlayer().getFaction() != null
                && !FactionFactory.getInstance().getAllSurnames(getCharacterPlayer().getFaction().getId()).isEmpty()
                && !getCharacterPlayer().getFaction().getId().equals(surname.getFaction())) {
            throw new InvalidRandomElementSelectedException("Surname '" + surname + "' from an invalid faction '"
                    + getCharacterPlayer().getFaction() + "'.");
        }
        return BASIC_PROBABILITY;
    }

}
