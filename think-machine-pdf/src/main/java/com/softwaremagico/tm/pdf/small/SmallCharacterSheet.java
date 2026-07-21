
package com.softwaremagico.tm.pdf.small;

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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.PdfDocument;
import com.softwaremagico.tm.pdf.complete.info.CharacterBasicsCompleteTableFactory;

/**
 * Generates compact character sheets in A5 format.
 */
public class SmallCharacterSheet extends PdfDocument {
    private final CharacterPlayer character;

    /**
     * Creates a small character sheet for a character.
     *
     * @param character the character to generate sheet for.
     */
    public SmallCharacterSheet(CharacterPlayer character) {
        this.character = character;
    }

    @Override
    protected Rectangle getPageSize() {
        return PageSize.A5;
    }

    @Override
    protected void createContent(Document document) throws InvalidXmlElementException, DocumentException {
        createCharacterPDF(document, character);
    }

    @Override
    protected void createCharacterPDF(Document document, CharacterPlayer characterPlayer) throws InvalidXmlElementException, DocumentException {
        // Page 1: Basics
        document.add(CharacterBasicsCompleteTableFactory.getCharacterBasicsTable(characterPlayer));
    }

    @Override
    protected void addDocumentWriterEvents(PdfWriter writer) {
        // No special events for small sheet
    }
}
