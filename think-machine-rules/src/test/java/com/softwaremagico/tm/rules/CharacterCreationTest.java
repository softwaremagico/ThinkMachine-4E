package com.softwaremagico.tm.rules;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.FactionFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "characterCreation")
public class CharacterCreationTest {
    private final static String OBUN_SPECIE = "obun";
    private CharacterPlayer characterPlayer;

    @BeforeClass
    public void setUpd() {
        characterPlayer = new CharacterPlayer();
    }

    @Test
    public void selectRace() {
        characterPlayer.setSpecie(OBUN_SPECIE);

    }

    @Test
    public void selectFaction() throws InvalidXmlElementException {
        characterPlayer.setFaction(new CharacterDefinitionStepSelection(FactionFactory.getInstance().getElement("")));
    }
}
