package com.softwaremagico.tm.pdf.complete.resistances;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.CustomPdfTable;
import com.softwaremagico.tm.txt.TextFactory;

public class VictoryPointBankTableFactory extends BaseElement {

    public static PdfPTable getVictoryPointBankTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        final PdfPCell separator = createWhiteSeparator();
        separator.setColspan(widths.length);
        table.addCell(separator);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("victoryPointsBank").getName().getTranslatedText(), widths.length));
        table.addCell(new Paragraph(TextFactory.getInstance().getElement("capacity").getName().getTranslatedText(),
                new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(new Paragraph(TextFactory.getInstance().getElement("currentVP").getName().getTranslatedText(),
                new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(new Paragraph(TextFactory.getInstance().getElement("currentWP").getName().getTranslatedText(),
                new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));

        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getBank() : null));
        table.addCell(createRectangle(null));
        table.addCell(createRectangle(null));

        return table;
    }
}
