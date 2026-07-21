package com.softwaremagico.tm.export.pdf;

/*-
 * #%L
 * Think Machine 4E (PDF Sheets)
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
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.party.Party;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for Party threat level calculations.
 */
@Test(groups = {"smallPartyPdfGeneration"})
public class PartyThreatLevelTest {

	/**
	 * Tests threat level calculation for empty party.
	 */
	@Test
	public void emptyPartyThreatLevel() {
		Translator.setLanguage("EN");
		final Party party = new Party("EN", "Fading Suns 4E");
		Assert.assertEquals(party.getThreatLevel(), 0);
		Assert.assertEquals(party.getMemberCount(), 0);
	}

	/**
	 * Tests threat level for single character in party.
	 */
	@Test
	public void singleCharacterThreatLevel() throws Exception {
		Translator.setLanguage("EN");
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();
		party.addMember(character);

		// Should have a positive threat level (combat skills + vitality)
		Assert.assertTrue(party.getThreatLevel() > 0);
		Assert.assertEquals(party.getMemberCount(), 1);

		// Individual character threat should be available
		Assert.assertTrue(party.getThreatLevel(character) > 0);
	}

	/**
	 * Tests threat level for multiple characters in party (sorted by threat).
	 */
	@Test
	public void multipleCharactersThreatLevel() throws Exception {
		Translator.setLanguage("EN");
		final Party party = new Party("EN", "Fading Suns 4E");

		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();

		party.addMember(character1);
		party.addMember(character2);

		// Total threat should be sum of individuals
		final int totalThreat = party.getThreatLevel();
		final int character1Threat = party.getThreatLevel(character1);
		final int character2Threat = party.getThreatLevel(character2);

		Assert.assertEquals(totalThreat, character1Threat + character2Threat);
		Assert.assertEquals(party.getMemberCount(), 2);

		// Members should be sorted by threat (highest first)
		final CharacterPlayer firstMember = party.getMembers().get(0);
		final CharacterPlayer secondMember = party.getMembers().get(1);

		Assert.assertTrue(
			party.getThreatLevel(firstMember) >= party.getThreatLevel(secondMember)
		);
	}

	/**
	 * Tests that removing a character updates threat level.
	 */
	@Test
	public void removeCharacterUpdatesThreat() throws Exception {
		Translator.setLanguage("EN");
		final Party party = new Party("EN", "Fading Suns 4E");

		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();

		party.addMember(character1);
		party.addMember(character2);
		final int initialThreat = party.getThreatLevel();

		party.removeCharacter(character1);
		final int remainingThreat = party.getThreatLevel();

		// Remaining threat should be less
		Assert.assertTrue(remainingThreat < initialThreat);
		Assert.assertEquals(party.getMemberCount(), 1);
		Assert.assertEquals(remainingThreat, party.getThreatLevel(character2));
	}
}
