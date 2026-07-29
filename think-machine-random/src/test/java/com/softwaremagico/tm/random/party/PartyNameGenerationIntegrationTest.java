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

import com.softwaremagico.tm.language.Translator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Integration tests for party name generation combining PartyName and PartyAdjective
 * with multi-language support.
 */
public class PartyNameGenerationIntegrationTest {

	private static final String LANGUAGE_SPANISH = "es";
	private static final String LANGUAGE_ENGLISH = "en";
	private static final String MODULE_NAME = "Fading Suns 4E";

	@BeforeClass
	public void setup() {
		Translator.setLanguage(LANGUAGE_ENGLISH);
	}

	/**
	 * Tests Spanish party name generation with gender agreement.
	 */
	@Test
	public void spanishPartyNameWithGenderAgreement() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_SPANISH, MODULE_NAME);
		final PartyName name = new PartyName("Sombras", randomParty, LANGUAGE_SPANISH, MODULE_NAME);
		final PartyAdjective adjective = new PartyAdjective("Perdidos", randomParty, LANGUAGE_SPANISH, MODULE_NAME);

		final String result = RandomPartyFactory.getPartyName(name, adjective, LANGUAGE_SPANISH);

		Assert.assertNotNull(result, "Generated name should not be null");
		Assert.assertEquals(result, "Sombras Perdidas", "Should apply gender agreement");
	}

	/**
	 * Tests English party name generation.
	 */
	@Test
	public void englishPartyNameGeneration() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyName name = new PartyName("Guardians", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyAdjective adjective = new PartyAdjective("of Shadows", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);

		final String result = RandomPartyFactory.getPartyName(name, adjective, LANGUAGE_ENGLISH);

		Assert.assertNotNull(result, "Generated name should not be null");
		Assert.assertEquals(result, "Guardians of Shadows");
	}

	/**
	 * Tests party name creation preserves module information.
	 */
	@Test
	public void partyNamePreservesModuleInformation() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyName name = new PartyName("Warriors", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);

		Assert.assertEquals(name.getModuleName(), MODULE_NAME);
		Assert.assertEquals(name.getLanguage(), LANGUAGE_ENGLISH);
	}

	/**
	 * Tests party adjective creation preserves module information.
	 */
	@Test
	public void partyAdjectivePreservesModuleInformation() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyAdjective adjective = new PartyAdjective("of Light", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);

		Assert.assertEquals(adjective.getModuleName(), MODULE_NAME);
		Assert.assertEquals(adjective.getLanguage(), LANGUAGE_ENGLISH);
	}

	/**
	 * Tests multiple Spanish names with different gender endings.
	 */
	@Test
	public void multipleSpanishNamesWithDifferentEndings() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_SPANISH, MODULE_NAME);

		// Feminine noun
		final PartyName feminineNoun = new PartyName("Sombras", randomParty, LANGUAGE_SPANISH, MODULE_NAME);
		final PartyAdjective masculineAdj = new PartyAdjective("Perdidos", randomParty, LANGUAGE_SPANISH, MODULE_NAME);
		String result1 = RandomPartyFactory.getPartyName(feminineNoun, masculineAdj, LANGUAGE_SPANISH);
		Assert.assertEquals(result1, "Sombras Perdidas");

		// Masculine noun
		final PartyName masculineNoun = new PartyName("Guardianes", randomParty, LANGUAGE_SPANISH, MODULE_NAME);
		String result2 = RandomPartyFactory.getPartyName(masculineNoun, masculineAdj, LANGUAGE_SPANISH);
		Assert.assertEquals(result2, "Guardianes Perdidos");
	}

	/**
	 * Tests party name factory returns null for null inputs.
	 */
	@Test
	public void partyNameFactoryHandlesNullInputs() {
		final String result = RandomPartyFactory.getPartyName(null, null, LANGUAGE_ENGLISH);
		Assert.assertNull(result, "Null inputs should return null");
	}

	/**
	 * Tests party name hashCode and equality.
	 */
	@Test
	public void partyNameHashCodeAndEquality() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyName name1 = new PartyName("Warriors", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyName name2 = new PartyName("Warriors", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyName name3 = new PartyName("Guardians", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);

		// Same content should be equal
		Assert.assertEquals(name1, name2);
		Assert.assertEquals(name1.hashCode(), name2.hashCode());

		// Different content should not be equal
		Assert.assertNotEquals(name1, name3);
	}

	/**
	 * Tests party adjective hashCode and equality.
	 */
	@Test
	public void partyAdjectiveHashCodeAndEquality() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyAdjective adj1 = new PartyAdjective("of Shadows", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyAdjective adj2 = new PartyAdjective("of Shadows", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyAdjective adj3 = new PartyAdjective("of Light", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);

		// Same content should be equal
		Assert.assertEquals(adj1, adj2);
		Assert.assertEquals(adj1.hashCode(), adj2.hashCode());

		// Different content should not be equal
		Assert.assertNotEquals(adj1, adj3);
	}

	/**
	 * Tests Spanish adjective with preposition remains unchanged.
	 */
	@Test
	public void spanishAdjectiveWithPrepositionUnchanged() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_SPANISH, MODULE_NAME);
		final PartyName name = new PartyName("Guardianes", randomParty, LANGUAGE_SPANISH, MODULE_NAME);
		final PartyAdjective adjective = new PartyAdjective("de las Sombras", randomParty, LANGUAGE_SPANISH,
				MODULE_NAME);

		final String result = RandomPartyFactory.getPartyName(name, adjective, LANGUAGE_SPANISH);

		Assert.assertEquals(result, "Guardianes de las Sombras", "Prepositions should remain unchanged");
	}

	/**
	 * Tests language case-insensitive matching in factory.
	 */
	@Test
	public void languageCaseInsensitiveInFactory() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_SPANISH, MODULE_NAME);
		final PartyName name = new PartyName("Sombras", randomParty, LANGUAGE_SPANISH, MODULE_NAME);
		final PartyAdjective adjective = new PartyAdjective("Perdidos", randomParty, LANGUAGE_SPANISH, MODULE_NAME);

		final String resultLower = RandomPartyFactory.getPartyName(name, adjective, "es");
		final String resultUpper = RandomPartyFactory.getPartyName(name, adjective, "ES");
		final String resultMixed = RandomPartyFactory.getPartyName(name, adjective, "Es");

		Assert.assertEquals(resultLower, resultUpper);
		Assert.assertEquals(resultUpper, resultMixed);
	}

}
