package com.softwaremagico.tm.txt;


import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.rules.CharacterCreationTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Test(groups = {"exportTxt"})
public class ExportTxtTests {
    private static final String LANGUAGE = "es";

    @Test
    public void checkCustomCharacter() throws URISyntaxException, IOException {
        Translator.setLanguage(LANGUAGE);
        final CharacterPlayer player = CharacterCreationTest.generateHumanNobleDecadosCommander();
        final CharacterSheet characterSheet = new CharacterSheet(player);

        final String text = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                .getResource("CustomCharacter.txt").toURI())));
        Assert.assertEquals(characterSheet.toString(), text);
    }
}
