package com.softwaremagico.tm.export.pdf;

/*-
 * #%L
 * Think Machine 4E (PDF Sheets)
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


import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Test(groups = {"characterPdfGeneration"})
public class CharacterSheetCreationTest extends PdfGeneration {

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }


    @Test
    public void emptyPdfSpanish() {
        final CharacterSheet sheet = new CharacterSheet();
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "FadingSuns_ES.pdf"), 2);
    }

    @Test
    public void emptyPdfEnglish() {
        final CharacterSheet sheet = new CharacterSheet();
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "FadingSuns_EN.pdf"), 2);
    }


    @Test
    public void characterPdfSpanish() {
        final CharacterSheet sheet = new CharacterSheet(generateHumanNobleDecadosCommander());
        Assert.assertEquals(sheet.createFile(OUTPUT_FOLDER + "CharacterFS_ES.pdf"), 2);
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
