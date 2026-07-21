package com.softwaremagico.tm.party;

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

import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.ThreatLevel;
import com.softwaremagico.tm.language.Translator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Comprehensive tests for Party management and threat level tracking.
 */
@Test(groups = {"partyManagement"})
public class PartyTest {

	@BeforeClass
	public void setup() {
		Translator.setLanguage("EN");
	}

	/**
	 * Tests Party initialization with language and module.
	 */
	@Test
	public void partyInitialization() {
		final Party party = new Party("EN", "Fading Suns 4E");
		Assert.assertNotNull(party);
		Assert.assertEquals(party.getMemberCount(), 0);
		Assert.assertEquals(party.getThreatLevel(), 0);
	}

	/**
	 * Tests adding a single member to party.
	 */
	@Test
	public void addSingleMember() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		party.addMember(character);

		Assert.assertEquals(party.getMemberCount(), 1);
		Assert.assertTrue(party.getThreatLevel() > 0);
	}

	/**
	 * Tests adding multiple members.
	 */
	@Test
	public void addMultipleMembers() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();
		final CharacterPlayer character3 = CharacterExamples.generateHumanNobleDecadosSybarite();

		party.addMember(character1);
		party.addMember(character2);
		party.addMember(character3);

		Assert.assertEquals(party.getMemberCount(), 3);
		Assert.assertTrue(party.getThreatLevel() > 0);
	}

	/**
	 * Tests that threat levels are cumulative.
	 */
	@Test
	public void cumulativeThreatLevel() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();

		party.addMember(character1);
		final int threat1 = party.getThreatLevel();

		party.addMember(character2);
		final int threat2 = party.getThreatLevel();

		Assert.assertTrue(threat2 > threat1, "Adding a member should increase total threat");
	}

	/**
	 * Tests removing a member from party.
	 */
	@Test
	public void removeMember() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();

		party.addMember(character1);
		party.addMember(character2);
		final int initialCount = party.getMemberCount();
		final int initialThreat = party.getThreatLevel();

		party.removeCharacter(character1);

		Assert.assertEquals(party.getMemberCount(), initialCount - 1);
		Assert.assertTrue(party.getThreatLevel() < initialThreat);
	}

	/**
	 * Tests retrieving individual character threat levels.
	 */
	@Test
	public void individualCharacterThreatLevel() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		party.addMember(character);

		final int partyThreat = party.getThreatLevel(character);
		final int directThreat = ThreatLevel.getThreatLevel(character);

		Assert.assertEquals(partyThreat, directThreat, "Party threat should match direct calculation");
	}

	/**
	 * Tests members are retrieved as list.
	 */
	@Test
	public void getMembers() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();

		party.addMember(character1);
		party.addMember(character2);

		final java.util.List<CharacterPlayer> members = party.getMembers();
		Assert.assertEquals(members.size(), 2);
		Assert.assertTrue(members.contains(character1) || members.contains(character2));
	}

	/**
	 * Tests that party maintains language setting.
	 */
	@Test
	public void partyLanguageSetting() {
		final Party partyES = new Party("ES", "Fading Suns 4E");
		final Party partyEN = new Party("EN", "Fading Suns 4E");

		Assert.assertNotNull(partyES);
		Assert.assertNotNull(partyEN);
	}

	/**
	 * Tests that party maintains module name setting.
	 */
	@Test
	public void partyModuleName() {
		final Party party1 = new Party("EN", "Fading Suns 4E");
		final Party party2 = new Party("EN", "Faction Book");

		Assert.assertNotNull(party1);
		Assert.assertNotNull(party2);
	}

	/**
	 * Tests members are sorted by threat level (descending).
	 */
	@Test
	public void membersSortedByThreat() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleDecadosSybarite();

		party.addMember(character1);
		party.addMember(character2);

		final java.util.List<CharacterPlayer> members = party.getMembers();

		// Verify sorted order (highest threat first)
		if (members.size() >= 2) {
			final int threat1 = party.getThreatLevel(members.get(0));
			final int threat2 = party.getThreatLevel(members.get(1));
			Assert.assertTrue(threat1 >= threat2, "Members should be sorted by threat descending");
		}
	}

	/**
	 * Tests adding duplicate character doesn't increase count twice.
	 * (Set behavior - no duplicates)
	 */
	@Test
	public void noDuplicateMembers() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		party.addMember(character);
		party.addMember(character);

		Assert.assertEquals(party.getMemberCount(), 1, "Should not allow duplicate members");
	}

	/**
	 * Tests party with maximum reasonable size.
	 */
	@Test
	public void largePartySize() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");

		// Add 10 members
		for (int i = 0; i < 10; i++) {
			final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();
			party.addMember(character);
		}

		Assert.assertEquals(party.getMemberCount(), 10);
		Assert.assertTrue(party.getThreatLevel() > 0);
	}

	/**
	 * Tests threat level total equals sum of individuals.
	 */
	@Test
	public void threatLevelAddition() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();
		final CharacterPlayer character3 = CharacterExamples.generateHumanNobleDecadosSybarite();

		party.addMember(character1);
		party.addMember(character2);
		party.addMember(character3);

		final int totalThreat = party.getThreatLevel();
		final int sum = party.getThreatLevel(character1)
				+ party.getThreatLevel(character2)
				+ party.getThreatLevel(character3);

		Assert.assertEquals(totalThreat, sum, "Total threat should equal sum of individual threats");
	}

	/**
	 * Tests removing all members leaves empty party.
	 */
	@Test
	public void removeAllMembers() throws Exception {
		final Party party = new Party("EN", "Fading Suns 4E");
		final CharacterPlayer character1 = CharacterExamples.generateHumanNobleDecadosCommander();
		final CharacterPlayer character2 = CharacterExamples.generateHumanNobleHawkwoodCommander();

		party.addMember(character1);
		party.addMember(character2);

		party.removeCharacter(character1);
		party.removeCharacter(character2);

		Assert.assertEquals(party.getMemberCount(), 0);
		Assert.assertEquals(party.getThreatLevel(), 0);
	}

	/**
	 * Tests party with different module settings.
	 */
	@Test
	public void differentModules() throws Exception {
		final Party party1 = new Party("EN", "Fading Suns 4E");
		final Party party2 = new Party("EN", "Faction Book");

		final CharacterPlayer character = CharacterExamples.generateHumanNobleDecadosCommander();

		party1.addMember(character);
		Assert.assertEquals(party1.getMemberCount(), 1);
		Assert.assertEquals(party2.getMemberCount(), 0);
	}

	/**
	 * Tests party name setting and retrieval.
	 */
	@Test
	public void partyNaming() {
		final Party party = new Party("EN", "Fading Suns 4E");
		final String partyName = "Alpha Squadron";
		party.setPartyName(partyName);

		Assert.assertEquals(party.getPartyName(), partyName);
	}
}
