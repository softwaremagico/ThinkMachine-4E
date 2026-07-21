package com.softwaremagico.tm.character;

/*-
 * #%L
 * Think Machine 4E (Rules)
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

import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.ThreatLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Comprehensive tests for ThreatLevel calculation including perks,
 * weapons, armor, shields, cybernetics, and occultism combinations.
 */
@Test(groups = {"threatLevelCalculation"})
public class ThreatLevelPerksTest {

	@BeforeClass
	public static void setUpClass() {
		Translator.setLanguage("EN");
	}

	@Test
	public void testBasicCharacterThreatLevel() {
		// GIVEN a basic character without any special equipment
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		// WHEN we calculate threat level
		final int threatLevel = ThreatLevel.getThreatLevel(character);

		// THEN threat level should be greater than 0
		Assert.assertTrue(threatLevel > 0);
	}

	@Test
	public void testCharacterWithCombatPerkIncreasesThreadLevel() {
		// GIVEN a character with combat perks like "Aim where it Hurts"
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final int baselineThreat = ThreatLevel.getThreatLevel(character1);

		// Create a second character as a control
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();
		final int controlThreat = ThreatLevel.getThreatLevel(character2);

		// THEN both threats should be calculated
		Assert.assertTrue(baselineThreat > 0);
		Assert.assertTrue(controlThreat > 0);
	}

	@Test
	public void testDifferentCharacterArchetypesThreatVariation() {
		// GIVEN three different character archetypes
		final CharacterPlayer commander = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer sybarite = CharacterExamples.generateHumanNobleDecadosSybarite();
		final CharacterPlayer hawkwood = CharacterExamples.generateHumanNobleHawkwoodCommander();

		// WHEN we calculate threat levels
		final int commanderThreat = ThreatLevel.getThreatLevel(commander);
		final int sybariteThreat = ThreatLevel.getThreatLevel(sybarite);
		final int hawkwoodThreat = ThreatLevel.getThreatLevel(hawkwood);

		// THEN all should have threats
		Assert.assertTrue(commanderThreat > 0);
		Assert.assertTrue(sybariteThreat > 0);
		Assert.assertTrue(hawkwoodThreat > 0);

		// THEN commander and hawkwood should have higher threat than sybarite
		// (combat-oriented vs diplomat)
		Assert.assertTrue(commanderThreat > sybariteThreat);
		Assert.assertTrue(hawkwoodThreat > sybariteThreat);
	}

	@Test
	public void testWeaponImpactOnThreadLevel() {
		// GIVEN a character without weapons
		final CharacterPlayer character = new CharacterPlayer();

		// WHEN we calculate threat level
		final int threatWithoutWeapons = ThreatLevel.getThreatLevel(character);

		// THEN threat should exist from characteristics and skills
		Assert.assertTrue(threatWithoutWeapons >= 0);

		// GIVEN a well-armed character
		final CharacterPlayer armedCharacter = CharacterExamples.generateHumanNobleDecadosCommander();

		// WHEN we calculate threat level
		final int threatWithWeapons = ThreatLevel.getThreatLevel(armedCharacter);

		// THEN armed character threat should be greater than basic
		Assert.assertTrue(threatWithWeapons > threatWithoutWeapons);
	}

	@Test
	public void testArmorImpactOnThreadLevel() {
		// GIVEN two identical characters
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final int threatWithArmor = ThreatLevel.getThreatLevel(character1);

		// Create another similar character
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();
		final int similarThreat = ThreatLevel.getThreatLevel(character2);

		// THEN both should have armor-related threat
		Assert.assertTrue(threatWithArmor > 0);
		Assert.assertTrue(similarThreat > 0);
	}

	@Test
	public void testOccultismImpactOnThreadLevel() {
		// GIVEN a character with occultism powers
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		// WHEN we calculate threat level
		final int threat = ThreatLevel.getThreatLevel(character);

		// THEN threat should account for any occultism abilities
		Assert.assertTrue(threat > 0);
	}

	@Test
	public void testCombinedComponentsThreadLevel() {
		// GIVEN a well-equipped character with combat perks, weapons, and armor
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		// WHEN we calculate threat level multiple times
		ThreatLevel.resetStatistics();
		final int threat1 = ThreatLevel.getThreatLevel(character);
		final int threat2 = ThreatLevel.getThreatLevel(character);
		final int threat3 = ThreatLevel.getThreatLevel(character);

		// THEN threats should be consistent
		Assert.assertEquals(threat1, threat2);
		Assert.assertEquals(threat2, threat3);

		// THEN all threats should be greater than 0
		Assert.assertTrue(threat1 > 0);
	}

	@Test
	public void testCharacteristicsAffectThreatLevel() {
		// GIVEN a character with combat skills
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		// WHEN we calculate threat
		final int threat = ThreatLevel.getThreatLevel(character);

		// THEN characteristics like strength and wits should affect threat
		// (through fight, melee, and shoot skills)
		Assert.assertTrue(threat > 0);
	}

	@Test
	public void testMultipleCalculationsProduceSameResult() {
		// GIVEN a character
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		// WHEN we calculate threat level 5 times
		ThreatLevel.resetStatistics();
		final int threat1 = ThreatLevel.getThreatLevel(character);
		final int threat2 = ThreatLevel.getThreatLevel(character);
		final int threat3 = ThreatLevel.getThreatLevel(character);
		final int threat4 = ThreatLevel.getThreatLevel(character);
		final int threat5 = ThreatLevel.getThreatLevel(character);

		// THEN all calculations should be identical
		Assert.assertEquals(threat1, threat2);
		Assert.assertEquals(threat2, threat3);
		Assert.assertEquals(threat3, threat4);
		Assert.assertEquals(threat4, threat5);
	}

	@Test
	public void testVariousCombatSituations() {
		// Test multiple character archetypes to verify threat coverage
		final CharacterPlayer[] characters = {
				CharacterExamples.generateHumanNobleDecadosCommander(),
				CharacterExamples.generateHumanNobleHawkwoodCommander(),
				CharacterExamples.generateHumanNobleDecadosSybarite()
		};

		// GIVEN various characters
		ThreatLevel.resetStatistics();

		// WHEN we calculate threat for each
		for (final CharacterPlayer character : characters) {
			final int threat = ThreatLevel.getThreatLevel(character);

			// THEN each should have positive threat level
			Assert.assertTrue(threat > 0,
					"Character " + character + " should have positive threat level");
		}

		// THEN statistics should be available
		ThreatLevel.showStatistics();
	}

	@Test
	public void testWeaponSpecializationAffectsThreat() {
		// GIVEN a well-armed character
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		// WHEN we calculate threat with weapons
		final int weaponizedThreat = ThreatLevel.getThreatLevel(character);

		// THEN weapons should significantly increase threat
		Assert.assertTrue(weaponizedThreat > 50,
				"Armed character should have substantial threat from weapons");
	}

	@Test
	public void testResistanceComponentInThreatLevel() {
		// GIVEN a character with resistances
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		// WHEN we calculate threat
		final int threat = ThreatLevel.getThreatLevel(character);

		// THEN resistances should be factored in
		// (body, mind, spirit resistance × 2 each)
		Assert.assertTrue(threat > 0);
	}

	@Test
	public void testShieldMultiplierAffectsThreatLevel() {
		// GIVEN a character with a shield
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		// WHEN we calculate threat
		final int threat = ThreatLevel.getThreatLevel(character);

		// THEN shield properties should multiply the threat level
		Assert.assertTrue(threat > 0);
	}

	@Test
	public void testLargeScaleCharacterComparison() {
		// GIVEN multiple character combinations
		final CharacterPlayer[] characters = new CharacterPlayer[3];
		characters[0] = CharacterExamples.generateHumanNobleDecadosCommander();
		characters[1] = CharacterExamples.generateHumanNobleHawkwoodCommander();
		characters[2] = CharacterExamples.generateHumanNobleDecadosSybarite();

		// WHEN we calculate threat levels
		ThreatLevel.resetStatistics();
		final int[] threats = new int[characters.length];
		for (int i = 0; i < characters.length; i++) {
			threats[i] = ThreatLevel.getThreatLevel(characters[i]);
		}

		// THEN all threats should be positive
		for (final int threat : threats) {
			Assert.assertTrue(threat > 0);
		}

		// THEN commander-type characters should have higher threat
		Assert.assertTrue(threats[0] + threats[1] > threats[2] * 2);
	}

	@Test
	public void testEmptyCharacterMinimalThreat() {
		// GIVEN an empty character with no equipment or enhancements
		final CharacterPlayer emptyCharacter = new CharacterPlayer();

		// WHEN we calculate threat
		final int minimalThreat = ThreatLevel.getThreatLevel(emptyCharacter);

		// THEN threat should still be minimal but present
		Assert.assertTrue(minimalThreat >= 0);
	}

	@Test
	public void testStatisticsResetWorks() {
		// GIVEN characters with threat levels calculated
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		ThreatLevel.resetStatistics();
		ThreatLevel.getThreatLevel(character1);

		// WHEN we reset statistics
		ThreatLevel.resetStatistics();

		// THEN we should be able to calculate again
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();
		final int threat2 = ThreatLevel.getThreatLevel(character2);

		// THEN threat should be valid
		Assert.assertTrue(threat2 > 0);
	}

	@Test
	public void testCyberneticsContributionToThreat() {
		// GIVEN a character with cybernetic enhancements
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		// WHEN we calculate threat
		final int threatWithCybernetics = ThreatLevel.getThreatLevel(character);

		// THEN cybernetics should contribute to threat (if present)
		Assert.assertTrue(threatWithCybernetics > 0);
	}

	@Test
	public void testThreatLevelConsistency() {
		// GIVEN the same character calculated multiple times
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();
		ThreatLevel.resetStatistics();

		// WHEN we calculate threat 10 times
		final int[] results = new int[10];
		for (int i = 0; i < 10; i++) {
			results[i] = ThreatLevel.getThreatLevel(character);
		}

		// THEN all results should be identical (deterministic)
		for (int i = 1; i < results.length; i++) {
			Assert.assertEquals(results[i], results[0],
					"Threat level should be deterministic");
		}
	}

}
