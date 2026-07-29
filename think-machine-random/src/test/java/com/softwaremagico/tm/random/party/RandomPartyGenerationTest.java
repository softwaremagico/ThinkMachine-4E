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

import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.party.Party;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Tests for random party generation.
 */
@Test(groups = {"randomParty"})
public class RandomPartyGenerationTest {

	/**
	 * Tests generation of empty party (no members defined).
	 */
	@Test
	public void emptyRandomPartyTemplate() throws InvalidXmlElementException {
		Translator.setLanguage("EN");
		final Collection<RandomPartyMember> memberTemplates = new ArrayList<>();
		final RandomPartyDefinition randomPartyDefinition = new RandomPartyDefinition("EN", "Fading Suns 4E", 100,
				memberTemplates);

		randomPartyDefinition.generate();

		final Party party = randomPartyDefinition.getParty();
		Assert.assertEquals(party.getMemberCount(), 0);
		Assert.assertEquals(party.getThreatLevel(), 0);
	}

	/**
	 * Tests generation of party with single member template.
	 */
	@Test
	public void singleMemberTypeParty() throws InvalidXmlElementException {
		Translator.setLanguage("EN");
		final Collection<RandomPartyMember> memberTemplates = new ArrayList<>();
		memberTemplates.add(new RandomPartyMember("Commander", "decadosCommander", 1, 3, 10));

		final RandomPartyDefinition randomPartyDefinition = new RandomPartyDefinition("EN", "Fading Suns 4E", 150,
				memberTemplates);

		randomPartyDefinition.generate();

		final Party party = randomPartyDefinition.getParty();
		Assert.assertTrue(party.getMemberCount() >= 1, "Should have at least 1 commander (mandatory)");
		Assert.assertTrue(party.getThreatLevel() > 0, "Party should have threat level > 0");
		Assert.assertTrue(party.getMemberCount() <= 3, "Should not exceed maximum 3 commanders");
	}

	/**
	 * Tests generation of party with multiple member templates.
	 */
	@Test
	public void multiMemberTypeParty() throws InvalidXmlElementException {
		Translator.setLanguage("EN");
		final Collection<RandomPartyMember> memberTemplates = new ArrayList<>();
		memberTemplates.add(new RandomPartyMember("Commando", "decadosCommander", 1, 2, 8));
		memberTemplates.add(new RandomPartyMember("Hacker", "hawkwoodCommander", 0, 2, 6));
		memberTemplates.add(new RandomPartyMember("Support", "sybarite", 0, 2, 5));

		final RandomPartyDefinition randomPartyDefinition = new RandomPartyDefinition("EN", "Fading Suns 4E", 200,
				memberTemplates);

		randomPartyDefinition.generate();

		final Party party = randomPartyDefinition.getParty();
		Assert.assertTrue(party.getMemberCount() >= 1, "Should have at least 1 mandatory commando");
		Assert.assertTrue(party.getThreatLevel() > 0, "Party should have threat level > 0");
		Assert.assertTrue(party.getMemberCount() <= 6, "Should not exceed maximum members (2+2+2)");
	}

	/**
	 * Tests that threat level respects target margin.
	 */
	@Test
	public void threatLevelControl() throws InvalidXmlElementException {
		Translator.setLanguage("ES");
		final Collection<RandomPartyMember> memberTemplates = new ArrayList<>();
		memberTemplates.add(new RandomPartyMember("Guerrero", "decadosCommander", 1, 5, 10));

		final int targetThreat = 50;
		final RandomPartyDefinition randomPartyDefinition = new RandomPartyDefinition("ES", "Fading Suns 4E",
				targetThreat, memberTemplates);

		randomPartyDefinition.generate();

		final Party party = randomPartyDefinition.getParty();
		final int threatMargin = 10;
		Assert.assertTrue(party.getThreatLevel() >= targetThreat - threatMargin,
				"Threat should be close to target");
		Assert.assertTrue(party.getThreatLevel() <= targetThreat + (threatMargin * 2),
				"Threat should not far exceed target+margin");
	}

}
