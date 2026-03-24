package com.softwaremagico.tm.random.character;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"randomOccultism"})
public class RandomOccultistTest {

    @Test
    public void ensureOneOccultismPowerTheurgyIfPerkIsSelected() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("brotherBattle");
        characterPlayer.setFaction("brotherBattle");
        characterPlayer.setCalling("brotherBattle");

        characterPlayer.getCalling().getSelectedPerksOptions().get(0).getSelections().add(new Selection(PerkFactory.getInstance().getElement(PerkFactory.THEURGY_RITES_PERK)));


        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getCharacteristicValue(CharacteristicName.THEURGY) > 0);
        Assert.assertFalse(characterPlayer.getAllSelectedPowers().isEmpty());
    }

    @Test
    public void ensureOneOccultismPowerPsiIfPerkIsSelected() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("liHalan");
        characterPlayer.setCalling("dervish");

        characterPlayer.getCalling().getSelectedPerksOptions().get(0).getSelections().add(new Selection(PerkFactory.getInstance().getElement(PerkFactory.PSYCHIC_POWERS_PERK)));


        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getCharacteristicValue(CharacteristicName.PSI) > 0);
        Assert.assertFalse(characterPlayer.getAllSelectedPowers().isEmpty());
    }
}
