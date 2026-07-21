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

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for random party name generation with proper gender agreement
 * in Spanish and standard concatenation in English.
 */
public class PartyNameGeneratorTest {

	private static final String LANGUAGE_SPANISH = "es";
	private static final String LANGUAGE_ENGLISH = "en";
	private static final String MODULE_NAME = "Fading Suns 4E";

	/**
	 * Tests that party name generator returns null for null inputs.
	 */
	@Test
	public void nullPartyNameReturnsNull() {
		final String result = RandomPartyFactory.getPartyName(null, null, LANGUAGE_SPANISH);

		Assert.assertNull(result, "Null inputs should return null");
	}

	/**
	 * Tests that English party names concatenate properly.
	 */
	@Test
	public void englishPartyNameConcatenation() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyName name = new PartyName("Guardians", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyAdjective adjective = new PartyAdjective("of Shadows", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);

		final String result = RandomPartyFactory.getPartyName(name, adjective, LANGUAGE_ENGLISH);

		Assert.assertEquals(result, "Guardians of Shadows", "English names should concatenate with space");
	}

	/**
	 * Tests Spanish feminine noun gender agreement.
	 */
	@Test
	public void spanishFeminineNounGenderAgreement() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_SPANISH, MODULE_NAME);
		final PartyName name = new PartyName("Sombras", randomParty, LANGUAGE_SPANISH, MODULE_NAME); // Ends with 'as'
		final PartyAdjective adjective = new PartyAdjective("Perdidos", randomParty, LANGUAGE_SPANISH, MODULE_NAME); // Ends with 'os'

		final String result = RandomPartyFactory.getPartyName(name, adjective, LANGUAGE_SPANISH);

		Assert.assertEquals(result, "Sombras Perdidas", "Feminine nouns should convert adjectives from 'os' to 'as'");
	}

	/**
	 * Tests Spanish adjectives with prepositions.
	 */
	@Test
	public void spanishAdjectiveWithPreposition() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_SPANISH, MODULE_NAME);
		final PartyName name = new PartyName("Guardianes", randomParty, LANGUAGE_SPANISH, MODULE_NAME);
		final PartyAdjective adjective = new PartyAdjective("de las Sombras", randomParty, LANGUAGE_SPANISH, MODULE_NAME);

		final String result = RandomPartyFactory.getPartyName(name, adjective, LANGUAGE_SPANISH);

		Assert.assertEquals(result, "Guardianes de las Sombras", "Adjectives with 'de' should remain unchanged");
	}

	/**
	 * Tests that masculine nouns in Spanish don't change adjective endings.
	 */
	@Test
	public void spanishMasculineNounNoAdjustment() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_SPANISH, MODULE_NAME);
		final PartyName name = new PartyName("Guardianes", randomParty, LANGUAGE_SPANISH, MODULE_NAME); // Masculine
		final PartyAdjective adjective = new PartyAdjective("Perdidos", randomParty, LANGUAGE_SPANISH, MODULE_NAME);

		final String result = RandomPartyFactory.getPartyName(name, adjective, LANGUAGE_SPANISH);

		Assert.assertEquals(result, "Guardianes Perdidos", "Masculine nouns should keep adjective unchanged");
	}

	/**
	 * Tests language case-insensitive matching.
	 */
	@Test
	public void languageCaseInsensitiveMatching() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_SPANISH, MODULE_NAME);
		final PartyName name = new PartyName("Sombras", randomParty, LANGUAGE_SPANISH, MODULE_NAME);
		final PartyAdjective adjective = new PartyAdjective("Perdidos", randomParty, LANGUAGE_SPANISH, MODULE_NAME);

		final String resultLower = RandomPartyFactory.getPartyName(name, adjective, "es");
		final String resultUpper = RandomPartyFactory.getPartyName(name, adjective, "ES");

		Assert.assertEquals(resultLower, resultUpper, "Language matching should be case-insensitive");
	}

	/**
	 * Tests combined English names preserve original capitalization.
	 */
	@Test
	public void englishNameCapitalization() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyName name = new PartyName("The Guardians", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyAdjective adjective = new PartyAdjective("of Light", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);

		final String result = RandomPartyFactory.getPartyName(name, adjective, LANGUAGE_ENGLISH);

		Assert.assertEquals(result, "The Guardians of Light", "English name capitalization should be preserved");
	}

	/**
	 * Tests that adjective with preposition in English is handled correctly.
	 */
	@Test
	public void englishAdjectiveWithPreposition() {
		final RandomParty randomParty = new RandomParty(LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyName name = new PartyName("Guardians", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);
		final PartyAdjective adjective = new PartyAdjective("of the Emperor", randomParty, LANGUAGE_ENGLISH, MODULE_NAME);

		final String result = RandomPartyFactory.getPartyName(name, adjective, LANGUAGE_ENGLISH);

		Assert.assertEquals(result, "Guardians of the Emperor", "English prepositional adjectives should work");
	}

}
