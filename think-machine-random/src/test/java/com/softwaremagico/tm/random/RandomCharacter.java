package com.softwaremagico.tm.random;


import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.random.character.RandomizeCharacter;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"randomCharacter"})
public class RandomCharacter {

    @Test
    public void createFullRandomTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertNotNull(characterPlayer.getInfo().getNames());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());
        Assert.assertNotNull(characterPlayer.getSpecie());
    }
}
