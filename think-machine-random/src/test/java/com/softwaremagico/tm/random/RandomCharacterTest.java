package com.softwaremagico.tm.random;


import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.random.character.RandomizeCharacter;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"randomCharacter"})
public class RandomCharacterTest {

    @Test
    public void createFullRandomCharacterTest() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();

        Assert.assertNotNull(characterPlayer.getSpecie());
        Assert.assertNotNull(characterPlayer.getPrimaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getSecondaryCharacteristic());
        Assert.assertNotNull(characterPlayer.getFaction());
        Assert.assertNotNull(characterPlayer.getInfo().getPlanet());
        Assert.assertNotNull(characterPlayer.getInfo().getNames());
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());
    }
}
