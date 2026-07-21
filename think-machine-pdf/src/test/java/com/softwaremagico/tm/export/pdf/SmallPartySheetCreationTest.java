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
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.pdf.small.SmallPartySheet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Integration tests for PDF small party sheet generation.
 */
@Test(groups = {"smallPartyPdfGeneration"})
public class SmallPartySheetCreationTest extends PdfGeneration {

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
     * Generates an empty Spanish PDF party sheet.
     */
    @Test
    public void emptySmallPartyPdfSpanish() {
        Translator.setLanguage("ES");
        final Party party = new Party("ES", "Fading Suns 4E");
        final SmallPartySheet sheet = new SmallPartySheet(party);
        // Empty party generates 1 page
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "FadingSuns_SmallParty_ES.pdf"), 1);
    }

    /**
     * Generates an empty English PDF party sheet.
     */
    @Test
    public void emptySmallPartyPdfEnglish() {
        Translator.setLanguage("EN");
        final Party party = new Party("EN", "Fading Suns 4E");
        final SmallPartySheet sheet = new SmallPartySheet(party);
        // Empty party generates 1 page
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "FadingSuns_SmallParty_EN.pdf"), 1);
    }

    /**
     * Generates a populated Spanish PDF party sheet with multiple characters.
     */
    @Test
    public void populatedSmallPartyPdfSpanish() throws Exception {
        Translator.setLanguage("ES");
        final Party party = new Party("ES", "Fading Suns 4E");
        party.addMember(CharacterExamples.generateHumanNobleDecadosCommander());
        party.addMember(CharacterExamples.generateHumanNobleHawkwoodCommander());
        final SmallPartySheet sheet = new SmallPartySheet(party);
        // 2 characters in landscape → 1 page
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "FadingSuns_SmallParty_Populated_ES.pdf"), 1);
    }

    /**
     * Generates a populated English PDF party sheet.
     */
    @Test
    public void populatedSmallPartyPdfEnglish() throws Exception {
        Translator.setLanguage("EN");
        final Party party = new Party("EN", "Fading Suns 4E");
        party.addMember(CharacterExamples.generateHumanNobleDecadosCommander());
        party.addMember(CharacterExamples.generateHumanNobleHawkwoodCommander());
        final SmallPartySheet sheet = new SmallPartySheet(party);
        // 2 characters in landscape → 1 page
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "FadingSuns_SmallParty_Populated_EN.pdf"), 1);
    }
}
