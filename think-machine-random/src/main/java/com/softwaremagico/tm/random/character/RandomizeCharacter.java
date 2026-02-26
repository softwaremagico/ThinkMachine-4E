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
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicReassign;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.skills.SkillsReassign;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.MaxValueExceededException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.character.callings.RandomCalling;
import com.softwaremagico.tm.random.character.factions.RandomFaction;
import com.softwaremagico.tm.random.character.level.RandomLevel;
import com.softwaremagico.tm.random.character.names.RandomName;
import com.softwaremagico.tm.random.character.names.RandomSurname;
import com.softwaremagico.tm.random.character.planets.RandomPlanet;
import com.softwaremagico.tm.random.character.selectors.IRandomPreference;
import com.softwaremagico.tm.random.character.species.RandomSpecie;
import com.softwaremagico.tm.random.character.upbringings.RandomUpbringing;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomCharacteristics;
import com.softwaremagico.tm.random.step.RandomSkill;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RandomizeCharacter {
    private final CharacterPlayer characterPlayer;
    private final Set<IRandomPreference> preferences;
    private final int desiredLevel;

    private final RandomSpecie randomSpecie;
    private final RandomCalling randomCalling;
    private final RandomFaction randomFaction;
    private final RandomUpbringing randomUpbringing;

    public RandomizeCharacter(CharacterPlayer characterPlayer, IRandomPreference... preferences) {
        this(characterPlayer, 1, preferences);
    }

    public RandomizeCharacter(CharacterPlayer characterPlayer, int level, IRandomPreference... preferences) {
        this.characterPlayer = characterPlayer;
        this.desiredLevel = level;
        if (preferences != null) {
            final List<IRandomPreference> customPreferences = Arrays.asList(preferences);
            customPreferences.removeIf(Objects::isNull);
            this.preferences = new HashSet<>(customPreferences);
        } else {
            this.preferences = new HashSet<>();
        }
        randomSpecie = new RandomSpecie(characterPlayer, this.preferences);
        randomUpbringing = new RandomUpbringing(characterPlayer, this.preferences);
        randomFaction = new RandomFaction(characterPlayer, this.preferences);
        randomCalling = new RandomCalling(characterPlayer, this.preferences);
    }

    public void createCharacter() throws InvalidRandomElementSelectedException {
        try {
            selectSpecie();
            selectGender();
            selectUpbringing();
            selectFaction();
            selectPlanet();
            selectNames();
            selectCalling();
            selectPrimaryCharacteristics();
            completeSpecie();
            completeUpbringing();
            completeFaction();
            completeCalling();
            reassignCharacteristics();
            reassignSkills();
            setLevels();
            RandomGenerationLog.info(this.getClass(), "Character created: " + characterPlayer.toString());
        } catch (InvalidXmlElementException | MaxValueExceededException e) {
            throw new InvalidXmlElementException("Error on '" + characterPlayer + "'.", e);
        }
    }

    private void selectSpecie() throws InvalidRandomElementSelectedException {
        randomSpecie.assign();
    }

    private void completeSpecie() throws InvalidRandomElementSelectedException {
        randomSpecie.complete();
    }

    private void selectGender() {
        characterPlayer.getInfo().setGender(Gender.randomGender());
    }

    private void selectUpbringing() throws InvalidRandomElementSelectedException {
        randomUpbringing.assign();
    }

    private void completeUpbringing() throws InvalidRandomElementSelectedException {
        randomUpbringing.complete();
    }

    private void selectFaction() throws InvalidRandomElementSelectedException {
        randomFaction.assign();
    }

    private void completeFaction() throws InvalidRandomElementSelectedException {
        randomFaction.complete();
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

    private void selectCalling() throws InvalidRandomElementSelectedException {
        randomCalling.assign();
    }

    private void completeCalling() throws InvalidRandomElementSelectedException {
        randomCalling.complete();
    }

    private void setLevels() throws InvalidRandomElementSelectedException {
        while (characterPlayer.getLevel() < desiredLevel) {
            final RandomLevel randomLevel = new RandomLevel(characterPlayer, preferences);
            randomLevel.assign();
            randomLevel.complete();
            reassignCharacteristics();
            reassignSkills();
        }
    }

    private void selectPrimaryCharacteristics() throws InvalidRandomElementSelectedException {
        final RandomCharacteristics randomCharacteristics = new RandomCharacteristics(characterPlayer, preferences,
                CharacteristicDefinition.PRIMARY_CHARACTERISTIC_VALUE - CharacteristicDefinition.INITIAL_CHARACTERISTIC_VALUE);
        randomCharacteristics.assign();
    }

    private void reassignCharacteristics() throws InvalidRandomElementSelectedException {
        for (CharacteristicName characteristicName : CharacteristicName.getModificableCharacteristics()) {
            try {
                characterPlayer.getCharacteristicValue(characteristicName);
            } catch (MaxValueExceededException e) {
                for (int i = 0; i < e.getBonus() - e.getMaxValue(); i++) {
                    final RandomCharacteristics randomCharacteristics = new RandomCharacteristics(characterPlayer, preferences, 1);
                    characterPlayer.getCharacteristicReassigns().add(new CharacteristicReassign(e.getElement(),
                            randomCharacteristics.selectElementByWeight().getId()));
                }
            }
        }
    }

    private void reassignSkills() throws InvalidRandomElementSelectedException {
        for (Skill skill : SkillFactory.getInstance().getElements()) {
            try {
                characterPlayer.getSkillValue(skill.getId());
            } catch (MaxValueExceededException e) {
                for (int i = 0; i < e.getBonus() - e.getMaxValue(); i++) {
                    final RandomSkill randomSkill = new RandomSkill(characterPlayer, preferences, 1);
                    characterPlayer.getSkillsReassigns().add(new SkillsReassign(e.getElement(),
                            randomSkill.selectElementByWeight().getId()));
                }
            }
        }
    }
}
