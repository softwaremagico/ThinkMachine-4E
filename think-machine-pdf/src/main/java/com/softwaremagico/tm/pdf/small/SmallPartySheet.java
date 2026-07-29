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
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.pdf.complete.PdfDocument;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;

/**
 * Generates compact party sheets showing 2 characters per page in A4 landscape.
 */
public class SmallPartySheet extends PdfDocument {
    private static final int CELL_PADDING = 5;

    private final Party party;
    private PdfPTable mainTable;

    /**
     * Creates a small party sheet with given party.
     *
     * @param party the party to display.
     */
    public SmallPartySheet(Party party) {
        this.party = party;
    }

    @Override
    protected Rectangle getPageSize() {
        return PageSize.A4.rotate();
    }

    private void initializeSmallPartyTableContent() {
        final float[] widths = {1f, 1f};
        mainTable = new PdfPTable(widths);
        BaseElement.setTableProperties(mainTable);
        mainTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
        mainTable.getDefaultCell().setBorderWidth(2);
        mainTable.getDefaultCell().setPadding(0);
    }

    @Override
    protected void createContent(Document document) throws DocumentException, InvalidXmlElementException {
        initializeSmallPartyTableContent();

        if (party != null && party.getMemberCount() > 0) {
            // Add each character in the table (2 per row)
            for (final CharacterPlayer character : party.getMembers()) {
                createCharacterPDF(document, character);
            }

            // Fill empty cell if odd number of characters
            if (party.getMemberCount() % 2 > 0) {
                mainTable.addCell(new PdfPCell());
            }
        } else {
            // Add empty cells for empty party
            mainTable.addCell(new PdfPCell());
            mainTable.addCell(new PdfPCell());
        }

        document.add(mainTable);
    }

    @Override
    protected void createCharacterPDF(Document document, CharacterPlayer character) throws InvalidXmlElementException {
        final PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorderWidth(1);
        cell.setPadding(CELL_PADDING);

        // Create a simple character summary cell with character name
        final StringBuilder content = new StringBuilder();
        if (character.getInfo() != null && character.getInfo().getNameRepresentation() != null) {
            content.append(character.getInfo().getNameRepresentation());
        } else {
            content.append("[No Name]");
        }

        cell.addElement(new com.lowagie.text.Phrase(content.toString()));
        mainTable.addCell(cell);
    }

    @Override
    protected void addDocumentWriterEvents(PdfWriter writer) {
        // No special events for small party sheet
    }
}
