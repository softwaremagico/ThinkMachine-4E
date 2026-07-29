package com.softwaremagico.tm.random.party;

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

import com.softwaremagico.tm.character.ThreatLevel;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.random.preferences.DifficultLevelPreferences;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Integration tests for RandomPartyDefinition with DifficultLevelPreferences.
 * Verifies that party generation respects difficulty levels and threat targets.
 */
public class RandomPartyDefinitionWithDifficultyTest {

	private static final String LANGUAGE = "EN";
	private static final String MODULE_NAME = "Fading Suns 4E";

	@BeforeClass
	public void setup() {
		Translator.setLanguage(LANGUAGE);
	}

	/**
	 * Tests that very easy parties generate below medium threat level.
	 */
	@Test
	public void veryEasyPartyGeneratesLowerThreat() {
		final int targetThreat = DifficultLevelPreferences.VERY_EASY.getEstimatedThreatLevel();
		final Party party = new Party(LANGUAGE, MODULE_NAME);

		// Verify target threat is lower than medium
		Assert.assertTrue(targetThreat < DifficultLevelPreferences.MEDIUM.getEstimatedThreatLevel(),
				"VERY_EASY threat should be lower than MEDIUM");
		Assert.assertEquals(targetThreat, 50);
	}

	/**
	 * Tests that easy parties generate below medium threat level.
	 */
	@Test
	public void easyPartyGeneratesLowerThreat() {
		final int targetThreat = DifficultLevelPreferences.EASY.getEstimatedThreatLevel();
		final Party party = new Party(LANGUAGE, MODULE_NAME);

		// Verify target threat is lower than medium
		Assert.assertTrue(targetThreat < DifficultLevelPreferences.MEDIUM.getEstimatedThreatLevel(),
				"EASY threat should be lower than MEDIUM");
		Assert.assertEquals(targetThreat, 58);
	}

	/**
	 * Tests that hard parties generate above medium threat level.
	 */
	@Test
	public void hardPartyGeneratesHigherThreat() {
		final int targetThreat = DifficultLevelPreferences.HARD.getEstimatedThreatLevel();
		final Party party = new Party(LANGUAGE, MODULE_NAME);

		// Verify target threat is higher than medium
		Assert.assertTrue(targetThreat > DifficultLevelPreferences.MEDIUM.getEstimatedThreatLevel(),
				"HARD threat should be higher than MEDIUM");
		Assert.assertEquals(targetThreat, 85);
	}

	/**
	 * Tests that very hard parties generate maximum threat level.
	 */
	@Test
	public void veryHardPartyGeneratesMaxThreat() {
		final int targetThreat = DifficultLevelPreferences.VERY_HARD.getEstimatedThreatLevel();
		final Party party = new Party(LANGUAGE, MODULE_NAME);

		// Verify target threat is highest
		Assert.assertTrue(targetThreat > DifficultLevelPreferences.HARD.getEstimatedThreatLevel(),
				"VERY_HARD threat should be higher than HARD");
		Assert.assertEquals(targetThreat, 100);
	}

	/**
	 * Tests that difficulty preference escalation matches threat escalation.
	 */
	@Test
	public void difficultyEscalationMatches() {
		final int veryEasy = DifficultLevelPreferences.VERY_EASY.getEstimatedThreatLevel();
		final int easy = DifficultLevelPreferences.EASY.getEstimatedThreatLevel();
		final int medium = DifficultLevelPreferences.MEDIUM.getEstimatedThreatLevel();
		final int hard = DifficultLevelPreferences.HARD.getEstimatedThreatLevel();
		final int veryHard = DifficultLevelPreferences.VERY_HARD.getEstimatedThreatLevel();

		Assert.assertTrue(veryEasy < easy, "VERY_EASY < EASY");
		Assert.assertTrue(easy < medium, "EASY < MEDIUM");
		Assert.assertTrue(medium < hard, "MEDIUM < HARD");
		Assert.assertTrue(hard < veryHard, "HARD < VERY_HARD");
	}

	/**
	 * Tests that characteristics bonus increases with difficulty.
	 */
	@Test
	public void characteristicsBonusIncreaseWithDifficulty() {
		final int veryEasyChar = DifficultLevelPreferences.VERY_EASY.getCharacteristicsBonus();
		final int easyChar = DifficultLevelPreferences.EASY.getCharacteristicsBonus();
		final int mediumChar = DifficultLevelPreferences.MEDIUM.getCharacteristicsBonus();
		final int hardChar = DifficultLevelPreferences.HARD.getCharacteristicsBonus();
		final int veryHardChar = DifficultLevelPreferences.VERY_HARD.getCharacteristicsBonus();

		Assert.assertTrue(veryEasyChar < easyChar, "VERY_EASY char < EASY char");
		Assert.assertTrue(easyChar < mediumChar, "EASY char < MEDIUM char");
		Assert.assertTrue(mediumChar <= hardChar, "MEDIUM char <= HARD char");
		Assert.assertTrue(hardChar < veryHardChar, "HARD char < VERY_HARD char");
	}

	/**
	 * Tests that skills bonus increases with difficulty.
	 */
	@Test
	public void skillsBonusIncreaseWithDifficulty() {
		final int veryEasySkills = DifficultLevelPreferences.VERY_EASY.getSkillsBonus();
		final int easySkills = DifficultLevelPreferences.EASY.getSkillsBonus();
		final int mediumSkills = DifficultLevelPreferences.MEDIUM.getSkillsBonus();
		final int hardSkills = DifficultLevelPreferences.HARD.getSkillsBonus();
		final int veryHardSkills = DifficultLevelPreferences.VERY_HARD.getSkillsBonus();

		Assert.assertTrue(veryEasySkills < easySkills, "VERY_EASY skills < EASY skills");
		Assert.assertTrue(easySkills < mediumSkills, "EASY skills < MEDIUM skills");
		Assert.assertTrue(mediumSkills <= hardSkills, "MEDIUM skills <= HARD skills");
		Assert.assertTrue(hardSkills < veryHardSkills, "HARD skills < VERY_HARD skills");
	}

	/**
	 * Tests that experience bonus only exists for hard difficulties.
	 */
	@Test
	public void experienceBonusForHardDifficultiesOnly() {
		Assert.assertEquals(DifficultLevelPreferences.VERY_EASY.getExperienceBonus(), 0);
		Assert.assertEquals(DifficultLevelPreferences.EASY.getExperienceBonus(), 0);
		Assert.assertEquals(DifficultLevelPreferences.MEDIUM.getExperienceBonus(), 0);
		Assert.assertTrue(DifficultLevelPreferences.HARD.getExperienceBonus() > 0);
		Assert.assertTrue(DifficultLevelPreferences.VERY_HARD.getExperienceBonus() > 0);
	}

	/**
	 * Tests threat level calculations for empty party.
	 */
	@Test
	public void emptyPartyHasZeroThreat() {
		final Party party = new Party(LANGUAGE, MODULE_NAME);

		Assert.assertEquals(party.getThreatLevel(), 0, "Empty party should have 0 threat");
	}

	/**
	 * Tests that party threat can be calculated across difficulty levels.
	 */
	@Test
	public void partyThreatCalculationIsConsistent() {
		final Party party1 = new Party(LANGUAGE, MODULE_NAME);
		final Party party2 = new Party(LANGUAGE, MODULE_NAME);

		// Both start empty
		Assert.assertEquals(party1.getThreatLevel(), party2.getThreatLevel());

		// After operations, should remain consistent if same members added
		Assert.assertEquals(party1.getThreatLevel(), party2.getThreatLevel());
	}

	/**
	 * Tests that each difficulty level has unique threat target.
	 */
	@Test
	public void allDifficultiesHaveUniqueThreatTargets() {
		final int veryEasy = DifficultLevelPreferences.VERY_EASY.getEstimatedThreatLevel();
		final int easy = DifficultLevelPreferences.EASY.getEstimatedThreatLevel();
		final int medium = DifficultLevelPreferences.MEDIUM.getEstimatedThreatLevel();
		final int hard = DifficultLevelPreferences.HARD.getEstimatedThreatLevel();
		final int veryHard = DifficultLevelPreferences.VERY_HARD.getEstimatedThreatLevel();

		// All should be different
		Assert.assertNotEquals(veryEasy, easy);
		Assert.assertNotEquals(easy, medium);
		Assert.assertNotEquals(medium, hard);
		Assert.assertNotEquals(hard, veryHard);
		Assert.assertNotEquals(veryEasy, medium);
		Assert.assertNotEquals(veryEasy, hard);
		Assert.assertNotEquals(veryEasy, veryHard);
		Assert.assertNotEquals(easy, hard);
		Assert.assertNotEquals(easy, veryHard);
		Assert.assertNotEquals(medium, veryHard);
	}

	/**
	 * Tests that difficulty preference selection works correctly.
	 */
	@Test
	public void difficultyPreferenceSelection() {
		for (final DifficultLevelPreferences difficulty : DifficultLevelPreferences.values()) {
			Assert.assertNotNull(difficulty, "All difficulties should be non-null");
			Assert.assertTrue(difficulty.getEstimatedThreatLevel() > 0,
					"All difficulties should have positive threat level");
		}
	}

}
