package com.softwaremagico.tm.pdf.complete.resistances;

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

public class VitalityTable extends BaseElement {
    private static final float[] WIDTHS = {1f};

    public static PdfPTable getVitalityTable(CharacterPlayer characterPlayer) {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createWhiteSeparator();
        table.addCell(separator);

        table.addCell(getVitalityValuesTable(characterPlayer));

        return table;
    }


    private static PdfPTable getVitalityValuesTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1.1f, 0.7f, 1.1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("vitality").getName().getTranslatedText(), widths.length));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("maximumAbbreviation").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getVitalityValue() : null));
        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("current").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle());

        return table;
    }
}
