package com.softwaremagico.tm.random.character;

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
import com.softwaremagico.tm.random.log.RandomTestGenerationLog;
import com.softwaremagico.tm.random.preferences.RandomSelector;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;

@Test(groups = {"randomCharacterSeed"})
public class FindFailingSeedTest {
    private static final int CHARACTERS_PER_SEED = 1;
    private static final long MAX_SEEDS_TO_TEST = 10000;

    @Test
    public void findFailingSeed() throws InvalidRandomElementSelectedException {
        final long startSeed = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE - MAX_SEEDS_TO_TEST + 1);
        System.out.println("Starting seed: " + startSeed + ", iterations: " + MAX_SEEDS_TO_TEST);

        for (long iteration = 0; iteration < MAX_SEEDS_TO_TEST; iteration++) {
            final long seed = startSeed + iteration;
            RandomSelector.setRandomSeed(seed);

            for (int i = 0; i < CHARACTERS_PER_SEED; i++) {
                final CharacterPlayer characterPlayer = new CharacterPlayer();
                final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
                randomizeCharacter.createCharacter();

                Assert.assertNotNull(characterPlayer.getSpecie());
                Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
                Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
                Assert.assertNotNull(characterPlayer.getFaction());
                Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
                Assert.assertFalse(characterPlayer.getInfo().getNames().isEmpty());
                Assert.assertNotNull(characterPlayer.getInfo().getSurname());

                try {
                    characterPlayer.validate();
                } catch (Exception e) {
                    RandomTestGenerationLog.severe(this.getClass(),
                            "FOUND FAILING SEED: " + seed + " at character " + i);
                    RandomTestGenerationLog.severe(this.getClass(),
                            "Error on character: " + characterPlayer);
                    RandomTestGenerationLog.errorMessage(this.getClass(), e);
                    System.err.println("\n\n========== FAILING SEED FOUND ==========");
                    System.err.println("FAILING_SEED=" + seed);
                    System.err.println("Character index: " + i);
                    System.err.println("Character: " + characterPlayer);
                    System.err.println("Error: " + e.getMessage());
                    System.err.println("========================================\n");
                    throw e;
                }
            }

            if (iteration % 1000 == 0 && iteration > 0) {
                System.out.println("Tested seeds: " + iteration + "/" + MAX_SEEDS_TO_TEST);
            }
        }

        System.out.println("No failing seeds found in range " + startSeed + "-"
                + (startSeed + MAX_SEEDS_TO_TEST - 1));
    }
}
