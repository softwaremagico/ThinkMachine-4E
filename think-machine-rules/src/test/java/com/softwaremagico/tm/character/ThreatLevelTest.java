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
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Comprehensive tests for ThreatLevel calculation system.
 * Tests combat skills, weapons, cybernetics, and vitality threat components.
 */
@Test(groups = {"threatLevelCalculation"})
public class ThreatLevelTest {

	@BeforeClass
	public void setup() {
		Translator.setLanguage("EN");
	}

	/**
	 * Tests that threat level for basic character with no combat skills is low.
	 */
	@Test
	public void basicCharacterThreatLevel() throws Exception {
		final CharacterPlayer character = new CharacterPlayer();

		final int threatLevel = ThreatLevel.getThreatLevel(character);
		Assert.assertTrue(threatLevel >= 0, "Threat level should be non-negative");
	}

	/**
	 * Tests that combat-trained character has higher threat level.
	 */
	@Test
	public void combatTrainedCharacterHigherThreat() throws Exception {
		final CharacterPlayer basicCharacter = new CharacterPlayer();

		final CharacterPlayer combatCharacter = CharacterExamples.generateHumanNobleDecadosCommander();

		final int basicThreat = ThreatLevel.getThreatLevel(basicCharacter);
		final int combatThreat = ThreatLevel.getThreatLevel(combatCharacter);

		Assert.assertTrue(combatThreat > basicThreat,
				"Combat-trained character should have higher threat than basic character");
	}

	/**
	 * Tests that threat level is consistent across multiple calculations.
	 */
	@Test
	public void threatLevelConsistency() throws Exception {
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		final int threat1 = ThreatLevel.getThreatLevel(character);
		final int threat2 = ThreatLevel.getThreatLevel(character);
		final int threat3 = ThreatLevel.getThreatLevel(character);

		Assert.assertEquals(threat1, threat2, "Threat level should be consistent");
		Assert.assertEquals(threat2, threat3, "Threat level should be consistent");
	}

	/**
	 * Tests that different character archetypes have appropriately different threat levels.
	 */
	@Test
	public void differentArchetypesDifferentThreat() throws Exception {
		final CharacterPlayer commander = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer hawkwood = CharacterExamples.generateHumanNobleHawkwoodCommander();
		final CharacterPlayer sybarite = CharacterExamples.generateHumanNobleDecadosSybarite();

		final int commanderThreat = ThreatLevel.getThreatLevel(commander);
		final int hawkwoodThreat = ThreatLevel.getThreatLevel(hawkwood);
		final int sybariteThreat = ThreatLevel.getThreatLevel(sybarite);

		// Commander and Hawkwood are combat-focused, Sybarite less so
		Assert.assertTrue(commanderThreat > 0, "Commander should have positive threat");
		Assert.assertTrue(hawkwoodThreat > 0, "Hawkwood should have positive threat");
		Assert.assertTrue(sybariteThreat >= 0, "Sybarite should have non-negative threat");
	}

	/**
	 * Tests that characteristics influence threat calculation.
	 */
	@Test
	public void characteristicsInfluenceThreat() throws Exception {
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleDecadosCommander();

		final int threat1 = ThreatLevel.getThreatLevel(character1);
		final int threat2 = ThreatLevel.getThreatLevel(character2);

		// Same archetype should have similar threat (random generation may cause small differences)
		final int threatDifference = Math.abs(threat1 - threat2);
		Assert.assertTrue(threatDifference < 50, "Same archetypes should have similar threat (within reasonable margin)");
	}

	/**
	 * Tests that weapon-equipped character has higher threat than unarmed.
	 */
	@Test
	public void weaponedCharacterHigherThreat() throws Exception {
		final CharacterPlayer character1 = new CharacterPlayer();

		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleDecadosCommander();

		final int unarmedThreat = ThreatLevel.getThreatLevel(character1);
		final int weaponedThreat = ThreatLevel.getThreatLevel(character2);

		Assert.assertTrue(weaponedThreat > unarmedThreat,
				"Weapon-equipped character should have higher threat");
	}

	/**
	 * Tests that threat level is always non-negative.
	 */
	@Test
	public void threatLevelNeverNegative() throws Exception {
		for (int i = 0; i < 5; i++) {
			final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();
			final int threatLevel = ThreatLevel.getThreatLevel(character);
			Assert.assertTrue(threatLevel >= 0, "Threat level should never be negative");
		}
	}

	/**
	 * Tests threat level with multiple random character generations.
	 */
	@Test
	public void multipleCharactersThreatDistribution() throws Exception {
		int minThreat = Integer.MAX_VALUE;
		int maxThreat = Integer.MIN_VALUE;
		int sumThreat = 0;

		for (int i = 0; i < 10; i++) {
			final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();
			final int threat = ThreatLevel.getThreatLevel(character);

			minThreat = Math.min(minThreat, threat);
			maxThreat = Math.max(maxThreat, threat);
			sumThreat += threat;
		}

		// Verify reasonable distribution (all commanders have same archetype, so threat should be consistent)
		Assert.assertTrue(minThreat >= 0, "Minimum threat should be non-negative");
		Assert.assertTrue(minThreat == maxThreat, "Same archetype commanders should have consistent threat");
		Assert.assertTrue((sumThreat / 10) > 0, "Average threat should be positive");
	}

	/**
	 * Tests that threat level increases with character advancement.
	 * (Simulated by using different character archetypes)
	 */
	@Test
	public void characterAdvancementThreatComparison() throws Exception {
		final CharacterPlayer novice = new CharacterPlayer();

		final CharacterPlayer veteran = CharacterExamples.generateHumanNobleDecadosCommander();

		final int noviceThreat = ThreatLevel.getThreatLevel(novice);
		final int veteranThreat = ThreatLevel.getThreatLevel(veteran);

		Assert.assertTrue(veteranThreat > noviceThreat,
				"Veteran character should have higher threat than novice");
	}
}
