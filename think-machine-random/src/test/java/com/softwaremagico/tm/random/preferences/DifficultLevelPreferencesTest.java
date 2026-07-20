package com.softwaremagico.tm.random.preferences;

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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests for DifficultLevelPreferences enum with threat level mappings
 * and character generation bonuses.
 */
public class DifficultLevelPreferencesTest {

	/**
	 * Tests that all difficulty levels have defined threat levels.
	 */
	@Test
	public void allDifficultLevelsHaveThreatLevels() {
		for (final DifficultLevelPreferences level : DifficultLevelPreferences.values()) {
			Assert.assertNotNull(level, "DifficultLevelPreferences should not be null");
			Assert.assertTrue(level.getEstimatedThreatLevel() > 0,
					"Threat level should be positive for " + level.name());
		}
	}

	/**
	 * Tests that threat levels increase with difficulty.
	 */
	@Test
	public void threatLevelIncreaseWithDifficulty() {
		Assert.assertTrue(
				DifficultLevelPreferences.VERY_EASY.getEstimatedThreatLevel() < DifficultLevelPreferences.EASY
						.getEstimatedThreatLevel(),
				"VERY_EASY threat < EASY threat");

		Assert.assertTrue(DifficultLevelPreferences.EASY.getEstimatedThreatLevel() < DifficultLevelPreferences.MEDIUM
				.getEstimatedThreatLevel(), "EASY threat < MEDIUM threat");

		Assert.assertTrue(DifficultLevelPreferences.MEDIUM.getEstimatedThreatLevel() < DifficultLevelPreferences.HARD
				.getEstimatedThreatLevel(), "MEDIUM threat < HARD threat");

		Assert.assertTrue(DifficultLevelPreferences.HARD.getEstimatedThreatLevel() < DifficultLevelPreferences.VERY_HARD
				.getEstimatedThreatLevel(), "HARD threat < VERY_HARD threat");
	}

	/**
	 * Tests that MEDIUM is the default when no preference selected.
	 */
	@Test
	public void mediumIsDefaultDifficulty() {
		final Set<IRandomPreference> emptyPreferences = new HashSet<>();
		final DifficultLevelPreferences selected = DifficultLevelPreferences.getSelected(emptyPreferences);

		Assert.assertEquals(selected, DifficultLevelPreferences.MEDIUM, "Default difficulty should be MEDIUM");
	}

	/**
	 * Tests null preference set returns MEDIUM.
	 */
	@Test
	public void nullPreferencesReturnsDefault() {
		final DifficultLevelPreferences selected = DifficultLevelPreferences.getSelected(null);

		Assert.assertEquals(selected, DifficultLevelPreferences.MEDIUM, "Null preferences should return MEDIUM");
	}

	/**
	 * Tests selection of specific difficulty from preference set.
	 */
	@Test
	public void selectSpecificDifficulty() {
		final Set<IRandomPreference> preferences = new HashSet<>();
		preferences.add(DifficultLevelPreferences.HARD);

		final DifficultLevelPreferences selected = DifficultLevelPreferences.getSelected(preferences);

		Assert.assertEquals(selected, DifficultLevelPreferences.HARD, "Should select HARD difficulty");
	}

	/**
	 * Tests characteristics bonus decreases then stays stable.
	 */
	@Test
	public void characteristicsBonusProgression() {
		Assert.assertTrue(
				DifficultLevelPreferences.VERY_EASY.getCharacteristicsBonus() < DifficultLevelPreferences.EASY
						.getCharacteristicsBonus(),
				"VERY_EASY char bonus < EASY char bonus");

		Assert.assertEquals(DifficultLevelPreferences.MEDIUM.getCharacteristicsBonus(), 0,
				"MEDIUM should have 0 characteristics bonus");

		Assert.assertTrue(
				DifficultLevelPreferences.HARD.getCharacteristicsBonus() < DifficultLevelPreferences.VERY_HARD
						.getCharacteristicsBonus(),
				"HARD char bonus < VERY_HARD char bonus");
	}

	/**
	 * Tests skills bonus progression.
	 */
	@Test
	public void skillsBonusProgression() {
		Assert.assertTrue(DifficultLevelPreferences.VERY_EASY.getSkillsBonus() < DifficultLevelPreferences.EASY
				.getSkillsBonus(), "VERY_EASY skills < EASY skills");

		Assert.assertEquals(DifficultLevelPreferences.MEDIUM.getSkillsBonus(), 0,
				"MEDIUM should have 0 skills bonus");

		Assert.assertTrue(DifficultLevelPreferences.HARD.getSkillsBonus() < DifficultLevelPreferences.VERY_HARD
				.getSkillsBonus(), "HARD skills < VERY_HARD skills");
	}

	/**
	 * Tests experience bonus is only for hard difficulties.
	 */
	@Test
	public void experienceBonusOnlyForHardDifficulties() {
		Assert.assertEquals(DifficultLevelPreferences.VERY_EASY.getExperienceBonus(), 0,
				"VERY_EASY should have no experience bonus");
		Assert.assertEquals(DifficultLevelPreferences.EASY.getExperienceBonus(), 0,
				"EASY should have no experience bonus");
		Assert.assertEquals(DifficultLevelPreferences.MEDIUM.getExperienceBonus(), 0,
				"MEDIUM should have no experience bonus");

		Assert.assertTrue(DifficultLevelPreferences.HARD.getExperienceBonus() > 0,
				"HARD should have experience bonus");
		Assert.assertTrue(DifficultLevelPreferences.VERY_HARD.getExperienceBonus() > 0,
				"VERY_HARD should have experience bonus");
	}

	/**
	 * Tests valueOf method for enum conversion.
	 */
	@Test
	public void valueOfEnumConversion() {
		final DifficultLevelPreferences easy = DifficultLevelPreferences.valueOf("EASY");
		Assert.assertEquals(easy, DifficultLevelPreferences.EASY);

		final DifficultLevelPreferences hard = DifficultLevelPreferences.valueOf("HARD");
		Assert.assertEquals(hard, DifficultLevelPreferences.HARD);
	}

	/**
	 * Tests threat level estimates match expected ranges.
	 */
	@Test
	public void threatLevelEstimates() {
		Assert.assertEquals(DifficultLevelPreferences.VERY_EASY.getEstimatedThreatLevel(), 50);
		Assert.assertEquals(DifficultLevelPreferences.EASY.getEstimatedThreatLevel(), 58);
		Assert.assertEquals(DifficultLevelPreferences.MEDIUM.getEstimatedThreatLevel(), 65);
		Assert.assertEquals(DifficultLevelPreferences.HARD.getEstimatedThreatLevel(), 85);
		Assert.assertEquals(DifficultLevelPreferences.VERY_HARD.getEstimatedThreatLevel(), 100);
	}

}
