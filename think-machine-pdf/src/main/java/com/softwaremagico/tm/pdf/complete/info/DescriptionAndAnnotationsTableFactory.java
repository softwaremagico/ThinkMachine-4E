package com.softwaremagico.tm.pdf.complete.info;

import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;

public class DescriptionAndAnnotationsTableFactory extends BaseElement {

    public static PdfPTable getDescriptionAndAnnotationsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1.1f, 2f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(DescriptionTableFactory.getDescriptionTable(characterPlayer));
        table.addCell(AnnotationsTableFactory.getAnnotationsTable(characterPlayer));

        return table;
    }
}
