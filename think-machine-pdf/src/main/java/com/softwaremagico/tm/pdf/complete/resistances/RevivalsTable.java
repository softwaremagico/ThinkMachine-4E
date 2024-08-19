package com.softwaremagico.tm.pdf.complete.resistances;

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

public class RevivalsTable extends BaseElement {
    private static final float[] WIDTHS = {1f};

    public static PdfPTable getRevivalsTable(CharacterPlayer characterPlayer) {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createWhiteSeparator();
        table.addCell(separator);

        table.addCell(getRevivalsValue(characterPlayer));

        return table;
    }

    private static PdfPTable getRevivalsValue(CharacterPlayer characterPlayer) {
        final float[] widths = {1.1f, 0.7f, 1.1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("revivals").getName().getTranslatedText(), widths.length));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("quantity").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getRevivalsRating() : null));
        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("number").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getRevivalsNumber() : null));

        return table;
    }
}