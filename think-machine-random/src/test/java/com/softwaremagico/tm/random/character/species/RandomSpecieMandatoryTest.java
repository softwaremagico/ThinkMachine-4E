package com.softwaremagico.tm.random.character.species;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.profile.RandomPreferences;
import com.softwaremagico.tm.random.profile.RandomProfile;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * Tests for the {@code mandatorySpecies} restriction defined on a {@link RandomProfile}
 * and enforced by {@link RandomSpecie#selectElementByWeight()}.
 */
@Test(groups = {"randomSpecie"})
public class RandomSpecieMandatoryTest {

    private RandomProfile createMandatorySpeciesProfile(Set<String> species) {
        final RandomProfile profile = new RandomProfile();
        profile.setId("testMandatorySpeciesProfile");
        profile.setMandatorySpecies(species);
        return profile;
    }

    @Test
    public void mandatorySpeciesForcesSingleSpecie() throws InvalidRandomElementSelectedException {
        final RandomProfile profile = createMandatorySpeciesProfile(Set.of("human"));

        for (int i = 0; i < 10; i++) {
            final CharacterPlayer characterPlayer = new CharacterPlayer();
            final RandomSpecie randomSpecie = new RandomSpecie(characterPlayer,
                    new RandomPreferences(Set.of(), Set.of(profile)));
            randomSpecie.assign();

            Assert.assertNotNull(characterPlayer.getSpecie());
            Assert.assertEquals(characterPlayer.getSpecie().getId(), "human");
        }
    }

    @Test
    public void mandatorySpeciesOnlyPicksFromAllowedSet() throws InvalidRandomElementSelectedException {
        final Set<String> allowedSpecies = Set.of("human", "obun", "vorox");
        final RandomProfile profile = createMandatorySpeciesProfile(allowedSpecies);

        for (int i = 0; i < 30; i++) {
            final CharacterPlayer characterPlayer = new CharacterPlayer();
            final RandomSpecie randomSpecie = new RandomSpecie(characterPlayer,
                    new RandomPreferences(Set.of(), Set.of(profile)));
            randomSpecie.assign();

            Assert.assertTrue(allowedSpecies.contains(characterPlayer.getSpecie().getId()),
                    "Specie '" + characterPlayer.getSpecie().getId() + "' is not part of the mandatory set.");
        }
    }

    @Test
    public void noMandatorySpeciesAllowsAnySpecie() throws InvalidRandomElementSelectedException {
        final RandomProfile profile = createMandatorySpeciesProfile(Set.of());
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        final RandomSpecie randomSpecie = new RandomSpecie(characterPlayer,
                new RandomPreferences(Set.of(), Set.of(profile)));

        randomSpecie.assign();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getSpecie().getId());
    }
}

