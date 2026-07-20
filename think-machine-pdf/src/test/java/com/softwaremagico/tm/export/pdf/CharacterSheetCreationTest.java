package com.softwaremagico.tm.export.pdf;

/*-
 * #%L
 * Think Machine 4E (PDF Sheets)
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


import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Integration tests for PDF character sheet generation.
 */
@Test(groups = {"characterPdfGeneration"})
public class CharacterSheetCreationTest extends PdfGeneration {

    /**
     * Creates the output folder used by PDF tests.
     *
     * @throws IOException if the output folder cannot be created.
     */
    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    /**
     * Generates an empty Spanish PDF character sheet.
     */
    @Test
    public void emptyPdfSpanish() {
        Translator.setLanguage("ES");
        final CharacterSheet sheet = new CharacterSheet();
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "FadingSuns_ES.pdf"), 2);
    }

    /**
     * Generates an empty English PDF character sheet.
     */
    @Test
    public void emptyPdfEnglish() {
        Translator.setLanguage("EN");
        final CharacterSheet sheet = new CharacterSheet();
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "FadingSuns_EN.pdf"), 2);
    }

    /**
     * Generates a populated Spanish PDF character sheet.
     */
    @Test
    public void characterPdfSpanish() {
        Translator.setLanguage("ES");
        final CharacterSheet sheet = new CharacterSheet(CharacterExamples.generateHumanNobleDecadosCommander());
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "CharacterFS_ES.pdf"), 2);
    }

    /**
     * Generates a populated English PDF character sheet.
     */
    @Test
    public void characterPdfEnglish() {
        Translator.setLanguage("EN");
        final CharacterSheet sheet = new CharacterSheet(CharacterExamples.generateHumanNobleDecadosCommander());
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "CharacterFS_EN.pdf"), 2);
    }

    /**
     * Generates an English PDF for a level-2 character after changing calling at level up.
     */
    @Test
    public void characterPdfEnglishWithCallingCombinationAtLevel2() {
        Translator.setLanguage("EN");
        final var characterPlayer = CharacterExamples.generateHumanNobleDecadosCommander();
        characterPlayer.addLevel().setCalling("conspiracist");
        CharacterExamples.populateLevel(characterPlayer);

        Assert.assertEquals(characterPlayer.getLevel(), 2);
        Assert.assertEquals(characterPlayer.getCallingCombinationRepresentation(" / "), "Commander / Conspiracist");

        final CharacterSheet sheet = new CharacterSheet(characterPlayer);
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "CharacterFS_EN_callings_level2.pdf"), 2);
    }
}
