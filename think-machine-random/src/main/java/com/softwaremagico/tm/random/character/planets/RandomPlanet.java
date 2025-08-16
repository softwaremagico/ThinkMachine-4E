package com.softwaremagico.tm.random.character.planets;

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
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.AssignableRandomSelector;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.Set;

public class RandomPlanet extends RandomSelector<Planet> implements AssignableRandomSelector {
    private static final int FACTION_PLANET = 50;
    private static final int SPECIE_PLANET = 80;
    private static final int NEUTRAL_PLANET = 8;
    private static final int ENEMY_PLANET = 1;

    public RandomPlanet(CharacterPlayer characterPlayer, Set<RandomPreference> preferences)
            throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        if (getCharacterPlayer().getInfo().getPlanet() == null) {
            getCharacterPlayer().getInfo().setPlanet(selectElementByWeight());
        }
    }

    @Override
    protected Collection<Planet> getAllElements() throws InvalidXmlElementException {
        return PlanetFactory.getInstance().getElements();
    }

    @Override
    protected int getWeight(Planet planet) throws InvalidRandomElementSelectedException {
        //By faction
        if (planet.getFactions().contains(getCharacterPlayer().getFaction().getId())) {
            return FACTION_PLANET;
        }
        //By specie
        if (planet.getSpecies().contains(getCharacterPlayer().getSpecie().getId())) {
            return SPECIE_PLANET;
        }
        for (final String factionsOfPlanet : planet.getFactions()) {
            if (factionsOfPlanet != null && getCharacterPlayer().getFaction() != null
                    && FactionFactory.getInstance().getElement(factionsOfPlanet).getRestrictions().isRestricted(getCharacterPlayer())) {
                return ENEMY_PLANET;
            }
        }
        return super.getWeight(planet);
    }
}
