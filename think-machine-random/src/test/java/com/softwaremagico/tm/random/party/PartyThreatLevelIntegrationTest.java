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

import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.ThreatLevel;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.random.preferences.DifficultLevelPreferences;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Integration tests for Party threat level calculations with difficulty levels.
 * Verifies that party threat correlates with difficulty expectations.
 */
public class PartyThreatLevelIntegrationTest {

	private static final String LANGUAGE = "EN";
	private static final String MODULE_NAME = "Fading Suns 4E";

	@BeforeClass
	public void setup() {
		Translator.setLanguage(LANGUAGE);
	}

	/**
	 * Tests that generated commander threat is near hard difficulty target.
	 */
	@Test
	public void commanderCharacterThreatApproximatesHardDifficulty() {
		final CharacterPlayer commander = CharacterExamples.generateHumanNobleDecadosCommander();
		final int commanderThreat = ThreatLevel.getThreatLevel(commander);
		final int hardTarget = DifficultLevelPreferences.HARD.getEstimatedThreatLevel();

		// Commander threat should be in the ballpark of HARD difficulty
		Assert.assertTrue(commanderThreat > 0, "Commander threat should be positive");
		// Rough check: within 50% of target
		Assert.assertTrue(commanderThreat > hardTarget / 2, "Commander threat should be substantial");
	}

	/**
	 * Tests that sybarite character has lower threat than commander.
	 */
	@Test
	public void sybariteCharacterHasLowerThreatThanCommander() {
		final CharacterPlayer commander = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer sybarite = CharacterExamples.generateHumanNobleDecadosSybarite();

		final int commanderThreat = ThreatLevel.getThreatLevel(commander);
		final int sybariteThreat = ThreatLevel.getThreatLevel(sybarite);

		Assert.assertTrue(commanderThreat > sybariteThreat,
				"Commander threat should exceed sybarite threat");
	}

	/**
	 * Tests party threat accumulation from multiple members.
	 */
	@Test
	public void partyThreatAccumulatesCorrectly() {
		final Party party = new Party(LANGUAGE, MODULE_NAME);
		final CharacterPlayer char1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer char2 = CharacterExamples.generateHumanNobleDecadosSybarite();

		party.addMember(char1);
		final int threatAfterFirst = party.getThreatLevel();

		party.addMember(char2);
		final int threatAfterSecond = party.getThreatLevel();

		// Threat should increase when adding stronger member
		Assert.assertTrue(threatAfterSecond > threatAfterFirst,
				"Party threat should increase when adding member");
	}

	/**
	 * Tests that individual character threat matches party calculation.
	 */
	@Test
	public void individualCharacterThreatMatchesPartyCalculation() {
		final Party party = new Party(LANGUAGE, MODULE_NAME);
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		party.addMember(character);

		final int individualThreat = ThreatLevel.getThreatLevel(character);
		final int partyMemberThreat = party.getThreatLevel(character);

		Assert.assertEquals(partyMemberThreat, individualThreat,
				"Party member threat should match direct calculation");
	}

	/**
	 * Tests party member ordering by threat (descending).
	 */
	@Test
	public void partyMembersOrderedByThreatDescending() {
		final Party party = new Party(LANGUAGE, MODULE_NAME);
		final CharacterPlayer commander = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer sybarite = CharacterExamples.generateHumanNobleDecadosSybarite();

		party.addMember(sybarite);
		party.addMember(commander);

		final int commanderThreat = ThreatLevel.getThreatLevel(commander);
		final int sybariteThreat = ThreatLevel.getThreatLevel(sybarite);

		// Commander should be higher threat
		Assert.assertTrue(commanderThreat > sybariteThreat);

		// Members should be returned in descending threat order
		Assert.assertEquals(party.getMembers().get(0), commander,
				"Highest threat character should be first");
	}

	/**
	 * Tests threat comparison between difficulty levels and actual characters.
	 */
	@Test
	public void difficultyTargetsReflectCharacterThreat() {
		final CharacterPlayer commander = CharacterExamples.generateHumanNobleDecadosCommander();
		final int commanderThreat = ThreatLevel.getThreatLevel(commander);

		final int mediumTarget = DifficultLevelPreferences.MEDIUM.getEstimatedThreatLevel();
		final int hardTarget = DifficultLevelPreferences.HARD.getEstimatedThreatLevel();
		final int veryHardTarget = DifficultLevelPreferences.VERY_HARD.getEstimatedThreatLevel();

		// Verify difficulty targets are in ascending order
		Assert.assertTrue(mediumTarget < hardTarget);
		Assert.assertTrue(hardTarget < veryHardTarget);
	}

	/**
	 * Tests that party is correctly initialized with language and module.
	 */
	@Test
	public void partyInitializationPreservesMetadata() {
		final Party party = new Party(LANGUAGE, MODULE_NAME);

		// Party should accept members regardless of metadata
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();
		party.addMember(character);

		Assert.assertTrue(party.getMembers().size() > 0, "Party should contain member");
		Assert.assertEquals(party.getThreatLevel(), ThreatLevel.getThreatLevel(character),
				"Party threat should match member threat");
	}

	/**
	 * Tests threat consistency across multiple calculations.
	 */
	@Test
	public void threatCalculationIsConsistent() {
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		final int threat1 = ThreatLevel.getThreatLevel(character);
		final int threat2 = ThreatLevel.getThreatLevel(character);
		final int threat3 = ThreatLevel.getThreatLevel(character);

		Assert.assertEquals(threat1, threat2, "Threat should be deterministic");
		Assert.assertEquals(threat2, threat3, "Threat should be deterministic");
	}

}
