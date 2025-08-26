package com.softwaremagico.tm.random.character;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.character.perks.SpecializedPerk;
import com.softwaremagico.tm.random.character.level.RandomLevel;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.stream.Collectors;

@Test(groups = {"randomLevel"})
public class RandomLevelTest {
    private static final int LEVEL_TEST = 10;

    private static final int CHARACTERS_CREATED = 100;

    @Test
    public void testRandomLevel() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        final RandomLevel randomLevel = new RandomLevel(characterPlayer, 2, null);
        randomLevel.assign();
        characterPlayer.getLevels().forEach(LevelSelector::validate);
    }

    @Test
    public void createFullRandomCharacterTest() throws InvalidRandomElementSelectedException {
        for (int i = 0; i < CHARACTERS_CREATED; i++) {
            System.out.println("#######################################################");
            final CharacterPlayer characterPlayer = new CharacterPlayer();
            final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, LEVEL_TEST);
            randomizeCharacter.createCharacter();

            Assert.assertEquals(characterPlayer.getLevel(), LEVEL_TEST);
            characterPlayer.validate();
        }
    }

    @Test
    public void createHawkwoodCommanderRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("hawkwood");
        characterPlayer.setCalling("commander");

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, LEVEL_TEST);
        randomizeCharacter.createCharacter();

        Assert.assertEquals(characterPlayer.getLevel(), LEVEL_TEST);
        characterPlayer.validate();
    }

    @Test
    public void createScraverImperialCohortMerchantRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("scravers");
        characterPlayer.setCalling("imperialCohortMerchant");

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, LEVEL_TEST);
        randomizeCharacter.createCharacter();

        Assert.assertEquals(characterPlayer.getLevel(), LEVEL_TEST);
        characterPlayer.validate();
    }

    @Test
    public void createObunPriestRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("obun");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("avestites");
        characterPlayer.setCalling("inquisitor");

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, LEVEL_TEST);
        randomizeCharacter.createCharacter();

        Assert.assertEquals(characterPlayer.getLevel(), LEVEL_TEST);
        characterPlayer.validate();
    }

    @Test
    public void createDervishRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("yeoman");
        characterPlayer.setFaction("societyOfStPaulus");
        characterPlayer.setCalling("dervish");

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, LEVEL_TEST);
        randomizeCharacter.createCharacter();

        Assert.assertEquals(characterPlayer.getLevel(), LEVEL_TEST);
        characterPlayer.validate();
    }

    @Test
    public void createTwoRanksPerksRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("priest");
        characterPlayer.setFaction("orthodox");
        characterPlayer.setCalling("clergy");

        //If I select Novitiate, on orthodox must add the next rank
        characterPlayer.getUpbringing().getSelectedPerksOptions().get(1).getSelections().add(new Selection("churchOrdinationNovitiate"));

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertTrue(characterPlayer.getPerks().stream().map(Element::getId).collect(Collectors.toSet())
                .contains("churchOrdinationCanon"));
        characterPlayer.validate();
    }
}
