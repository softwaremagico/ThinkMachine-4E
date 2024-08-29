package com.softwaremagico.tm.pdf.complete;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.equipment.FirebirdsFactory;

public class FirebirdsAndOthersTable extends BaseElement {
    private static final float[] WIDTHS = {1f, 2f};

    public static PdfPTable getFirebirdsAndOthersTable(CharacterPlayer characterPlayer) {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);
        table.getDefaultCell().setBorder(0);

        final PdfPTable firebirdsTable = FirebirdsFactory.getFirebirdsTable(characterPlayer);
        final PdfPCell firebirdsCell = new PdfPCell(firebirdsTable);
        firebirdsCell.setBorder(0);
        table.addCell(firebirdsCell);

        //final PdfPTable rightColumn = RightColumn.getElements(characterPlayer);
        final PdfPCell rightColumnCell = new PdfPCell();
        rightColumnCell.setBorder(0);
        table.addCell(rightColumnCell);

        return table;
    }
}
