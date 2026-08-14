package com.softwaremagico.tm.rules;

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
