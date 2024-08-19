package com.softwaremagico.tm.pdf.complete;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.occultism.OccultismValuesTableFactory;
import com.softwaremagico.tm.pdf.complete.resistances.ResistanceTableFactory;
import com.softwaremagico.tm.pdf.complete.resistances.RevivalsTable;
import com.softwaremagico.tm.pdf.complete.resistances.VictoryPointBankTableFactory;
import com.softwaremagico.tm.pdf.complete.resistances.VitalityTable;

public class RightColumn extends BaseElement {
    private static final float[] WIDTHS = {1f};

    public static PdfPTable getElements(CharacterPlayer characterPlayer) {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createWhiteSeparator();
        table.addCell(separator);


        table.addCell(OccultismValuesTableFactory.getOccultismValuesTable(characterPlayer));
        table.addCell(ResistanceTableFactory.getResistancesTable(characterPlayer));
        table.addCell(VictoryPointBankTableFactory.getVictoryPointBankTable(characterPlayer));
        table.addCell(VitalityTable.getVitalityTable(characterPlayer));
        table.addCell(RevivalsTable.getRevivalsTable(characterPlayer));


        return table;
    }
}
