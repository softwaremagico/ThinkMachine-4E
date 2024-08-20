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
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.equipment.ItemsPackFactory;
import com.softwaremagico.tm.pdf.complete.events.SheetAlternatedBackgroundEvent;
import com.softwaremagico.tm.pdf.complete.info.CharacterBasicsCompleteTableFactory;
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

        document.add(ItemsPackFactory.getItemsPack(characterPlayer));


//        final PdfPTable skillsTable = MainSkillsTableFactory.getSkillsTable(characterPlayer);
//        document.add(skillsTable);
//        final PdfPTable perksTable = MainPerksTableFactory.getPerksTable(characterPlayer);
//        document.add(perksTable);
//        document.newPage();
//        if (characterPlayer == null || characterPlayer.getTotalSelectedPowers() < PSI_ROWS) {
//            document.add(createRearTable());
//        } else if (characterPlayer.getCybernetics().isEmpty()) {
//            document.add(createRearTablePsiExtended());
//        } else {
//            document.add(createRearTable());
//        }
//        document.add(FightingManeuvers.getFightingManoeuvresTable(characterPlayer));
//        document.add(WeaponsAndArmours.getWeaponsAndArmoursTable(characterPlayer));
    }

    private PdfPTable createRearTable() throws InvalidXmlElementException {
        final PdfPTable mainTable = new PdfPTable(REAR_TABLE_WIDTHS);
        mainTable.getDefaultCell().setBorder(0);
        mainTable.setWidthPercentage(MAX_WIDTH);

//        mainTable.addCell(new DescriptionTable(characterPlayer));
//        final PdfPCell cell = new PdfPCell(new AnnotationsTable(characterPlayer));
//        cell.setBorderWidth(0);
//        cell.setColspan(2);
//        mainTable.addCell(cell);
//
//        final PdfPCell blackSeparator = BaseElement.createBigSeparator(90);
//        mainTable.addCell(blackSeparator);
//
//        final PdfPCell separatorCell = new PdfPCell(BaseElement.createWhiteSeparator());
//        separatorCell.setColspan(2);
//        mainTable.addCell(separatorCell);
//
//        mainTable.addCell(new PropertiesTable(characterPlayer));
//
//        final PdfPCell psiCell = new PdfPCell(new OccultismsPowerTable(characterPlayer, PSI_ROWS));
//        psiCell.setColspan(2);
//        mainTable.addCell(psiCell);
//
//        mainTable.addCell(BaseElement.createBigSeparator(90));
//
//        mainTable.addCell(separatorCell);
//
//        final PdfPTable othersTable = new OthersTable();
//        mainTable.addCell(othersTable);
//
//        final PdfPCell cyberneticsCell = new PdfPCell(new CyberneticsTable(getCharacterPlayer()));
//        cyberneticsCell.setColspan(2);
//        mainTable.addCell(cyberneticsCell);

        return mainTable;
    }

    private PdfPTable createRearTablePsiExtended() throws InvalidXmlElementException {
        final PdfPTable mainTable = new PdfPTable(REAR_TABLE_WIDTHS);
//        mainTable.getDefaultCell().setBorder(0);
//        mainTable.setWidthPercentage(100);
//
//        mainTable.addCell(new DescriptionTable(characterPlayer));
//        final PdfPCell cell = new PdfPCell(new AnnotationsTable(characterPlayer));
//        cell.setBorderWidth(0);
//        cell.setColspan(2);
//        mainTable.addCell(cell);
//
//        final PdfPCell blackSeparator = BaseElement.createBigSeparator(90);
//        mainTable.addCell(blackSeparator);
//
//        final PdfPCell separatorCell = new PdfPCell(BaseElement.createWhiteSeparator());
//        separatorCell.setColspan(2);
//        mainTable.addCell(separatorCell);
//
//        mainTable.addCell(new PropertiesTable(characterPlayer));
//
//        final PdfPCell psiCell = new PdfPCell(new OccultismsPowerTable(characterPlayer,
//                PSI_EXTENDED_ROWS));
//        psiCell.setColspan(2);
//        psiCell.setRowspan(3);
//        mainTable.addCell(psiCell);
//
//        mainTable.addCell(BaseElement.createBigSeparator(90));
//
//        final PdfPTable othersTable = new OthersTable();
//        mainTable.addCell(othersTable);

        return mainTable;
    }

    private CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
    }

    @Override
    protected void addDocumentWriterEvents(PdfWriter writer) {
        // No background.
    }

}
