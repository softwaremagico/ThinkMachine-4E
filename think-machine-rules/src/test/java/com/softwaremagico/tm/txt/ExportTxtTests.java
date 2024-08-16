package com.softwaremagico.tm.txt;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2024 Softwaremagico
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
