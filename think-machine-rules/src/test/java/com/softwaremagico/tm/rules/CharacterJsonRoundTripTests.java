package com.softwaremagico.tm.rules;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwaremagico.tm.ObjectMapperFactory;
import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.txt.CharacterSheet;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

@Test(groups = {"jsonCharacterRoundTrip"})
public class CharacterJsonRoundTripTests extends RulesTest {

    private final ObjectMapper mapper = ObjectMapperFactory.getJsonObjectMapper();

    @Test
    public void roundTripLevelOneCommanderWithEquipmentAndRemovedAward() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        final CharacterPlayer decoded = roundTrip(original);

        assertCharactersEqual(original, decoded);
    }

    @Test
    public void roundTripLevelOneHawkwoodWithCharacteristicReassign() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleHawkwoodCommander();
        final CharacterPlayer decoded = roundTrip(original);

        assertCharactersEqual(original, decoded);
    }

    @Test
    public void roundTripTwoLevelsWithChangedCalling() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();
        original.addLevel().setCalling("conspiracist");
        CharacterExamples.populateLevel(original);

        final CharacterPlayer decoded = roundTrip(original);

        assertCharactersEqual(original, decoded);
    }

    @Test
    public void roundTripThreeLevelsWithAlternatingCallings() throws IOException {
        final CharacterPlayer original = CharacterExamples.generateHumanNobleDecadosCommander();

        original.addLevel().setCalling("conspiracist");
        CharacterExamples.populateLevel(original);

        original.addLevel().setCalling("commander");
        CharacterExamples.populateLevel(original);

        final CharacterPlayer decoded = roundTrip(original);

        assertCharactersEqual(original, decoded);
    }

    private CharacterPlayer roundTrip(CharacterPlayer original) throws IOException {
        final String json = mapper.writeValueAsString(original);
        Assert.assertNotNull(json);
        Assert.assertFalse(json.isBlank());
        return mapper.readValue(json, CharacterPlayer.class);
    }

    private void assertCharactersEqual(CharacterPlayer expected, CharacterPlayer actual) {
        Assert.assertNotNull(actual);
        Assert.assertEquals(
                new CharacterSheet(actual).toString().trim(),
                new CharacterSheet(expected).toString().trim(),
                "Character sheet TXT"
        );
    }
}
