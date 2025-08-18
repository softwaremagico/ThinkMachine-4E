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
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.callings.RandomCalling;
import com.softwaremagico.tm.random.character.factions.RandomFaction;
import com.softwaremagico.tm.random.character.names.RandomName;
import com.softwaremagico.tm.random.character.names.RandomSurname;
import com.softwaremagico.tm.random.character.planets.RandomPlanet;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.species.RandomSpecie;
import com.softwaremagico.tm.random.character.upbringings.RandomUpbringing;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomCharacteristics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RandomizeCharacter {
    private final CharacterPlayer characterPlayer;
    private final Set<RandomPreference> preferences;

    public RandomizeCharacter(CharacterPlayer characterPlayer, RandomPreference... preferences) {
        this.characterPlayer = characterPlayer;
        if (preferences != null) {
            final List<RandomPreference> customPreferences = Arrays.asList(preferences);
            customPreferences.removeIf(Objects::isNull);
            this.preferences = new HashSet<>(customPreferences);
        } else {
            this.preferences = new HashSet<>();
        }
    }


    public void createCharacter() throws InvalidRandomElementSelectedException {
        try {
            selectSpecie();
            selectGender();
            selectPrimaryCharacteristics();
            selectUpbringing();
            selectFaction();
            selectPlanet();
            selectNames();
            selectCallings();
        } catch (InvalidXmlElementException e) {
            throw new InvalidXmlElementException("Error on '" + characterPlayer + "'.", e);
        }
    }

    private void selectSpecie() throws InvalidRandomElementSelectedException {
        final RandomSpecie randomSpecie = new RandomSpecie(characterPlayer, preferences);
        randomSpecie.assign();
    }

    private void selectGender() throws InvalidRandomElementSelectedException {
        characterPlayer.getInfo().setGender(Gender.randomGender());
    }

    private void selectUpbringing() throws InvalidRandomElementSelectedException {
        final RandomUpbringing randomUpbringing = new RandomUpbringing(characterPlayer, preferences);
        randomUpbringing.assign();
    }

    private void selectFaction() throws InvalidRandomElementSelectedException {
        final RandomFaction randomFaction = new RandomFaction(characterPlayer, preferences);
        randomFaction.assign();
    }

    private void selectPlanet() throws InvalidRandomElementSelectedException {
        final RandomPlanet randomPlanet = new RandomPlanet(characterPlayer, preferences);
        randomPlanet.assign();
    }

    private void selectNames() throws InvalidRandomElementSelectedException {
        final RandomName randomName = new RandomName(characterPlayer, preferences);
        randomName.assign();
        final RandomSurname randomSurname = new RandomSurname(characterPlayer, preferences);
        randomSurname.assign();
    }

    private void selectCallings() throws InvalidRandomElementSelectedException {
        final RandomCalling randomCalling = new RandomCalling(characterPlayer, preferences);
        randomCalling.assign();
    }


    private void selectPrimaryCharacteristics() throws InvalidRandomElementSelectedException {
        final RandomCharacteristics randomCharacteristics = new RandomCharacteristics(characterPlayer, preferences);
        randomCharacteristics.selectPrimaryCharacteristics();
    }
}
