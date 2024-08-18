package com.softwaremagico.tm.pdf.complete.endurances;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

public class EnduranceTableFactory extends BaseElement {

    public static PdfPTable getEnduranceAndProtectionsBasicsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(widths.length);
        table.addCell(separator);

        table.addCell(getEnduranceTable(characterPlayer));
        table.addCell(getProtectionTable(characterPlayer));
        table.addCell(getVitalityTable(characterPlayer));

        return table;
    }

    public static PdfPTable getEnduranceTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("resistances").getName().getTranslatedText(), widths.length));

        return table;
    }

    public static PdfPTable getProtectionTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("armor").getName().getTranslatedText(), widths.length));

        return table;
    }

    public static PdfPTable getVitalityTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("vitality").getName().getTranslatedText(), widths.length));


        return table;
    }
}
