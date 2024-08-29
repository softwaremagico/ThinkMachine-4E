package com.softwaremagico.tm.pdf.complete;

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


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.equipment.ItemsPackFactory;
import com.softwaremagico.tm.pdf.complete.events.SheetAlternatedBackgroundEvent;
import com.softwaremagico.tm.pdf.complete.info.CharacterBasicsCompleteTableFactory;
import com.softwaremagico.tm.pdf.complete.info.DescriptionAndAnnotationsTableFactory;
import com.softwaremagico.tm.pdf.complete.skills.CharacteristicsAndSkillsTableFactory;

public class CharacterSheet extends PdfDocument {
    private static final float[] REAR_TABLE_WIDTHS = {1f, 1f, 1f};
    private static final int MAX_WIDTH = 100;
    private static final int PSI_ROWS = 10;
    private static final int PSI_EXTENDED_ROWS = 21;
    private CharacterPlayer characterPlayer = null;

    public CharacterSheet() {
    }

    public CharacterSheet(CharacterPlayer characterPlayer) {
        this.characterPlayer = characterPlayer;
    }

    @Override
    protected void createContent(Document document) throws InvalidXmlElementException, DocumentException {
        createCharacterPDF(document, getCharacterPlayer());
    }

    @Override
    protected Rectangle getPageSize() {
        return PageSize.A4;
    }

    @Override
    protected void addEvent(PdfWriter writer) {
        super.addEvent(writer);
        writer.setPageEvent(new SheetAlternatedBackgroundEvent());
    }

    @Override
    protected void createCharacterPDF(Document document, CharacterPlayer characterPlayer) throws InvalidXmlElementException, DocumentException {

        document.add(CharacterBasicsCompleteTableFactory.getCharacterBasicsTable(characterPlayer));

        document.add(CharacteristicsAndSkillsTableFactory.getCharacteristicsAndSkillsBasicsTable(characterPlayer));

        document.add(ResistancesCapabilitiesAndProtectionsTable.getResistancesAndProtectionsBasicsTable(characterPlayer));

        document.newPage();

        document.add(DescriptionAndAnnotationsTableFactory.getDescriptionAndAnnotationsTable(characterPlayer));

        document.add(FirebirdsAndOthersTable.getFirebirdsAndOthersTable(characterPlayer));

        document.add(ItemsPackFactory.getItemsPack(characterPlayer));


    }

    private CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
    }

    @Override
    protected void addDocumentWriterEvents(PdfWriter writer) {
        // No background.
    }

}
