package com.softwaremagico.tm.pdf.complete.info;


import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.utils.CellUtils;
import com.softwaremagico.tm.txt.TextFactory;

public class AnnotationsTableFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final float CELL_HEIGHT = 90;
    private static final int DESCRIPTION_WIDTH = 2500;

    public static PdfPTable getAnnotationsTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final PdfPTable table = new PdfPTable(WIDTHS);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(createAnnotationsValue(characterPlayer));

        return table;
    }

    public static PdfPTable createAnnotationsValue(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("annotationsTable").getName(), widths.length));

        final PdfPCell descriptionCell = new PdfPCell(getCharacterDescription(characterPlayer));
        descriptionCell.setMinimumHeight(CELL_HEIGHT);
        descriptionCell.setBorderWidth(0);
        table.addCell(descriptionCell);

        final PdfPCell backgroundCell = new PdfPCell(getCharacterBackground(characterPlayer));
        backgroundCell.setMinimumHeight(CELL_HEIGHT);
        backgroundCell.setBorderWidth(0);
        table.addCell(backgroundCell);
        return table;
    }

    private static Paragraph getCharacterDescription(CharacterPlayer characterPlayer) {
        final Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph(TextFactory.getInstance().getElement("characterAnnotations").getName().getTranslatedText()
                + ": ", new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.ANNOTATIONS_SUBTITLE_FONT_SIZE)));
        if (characterPlayer != null) {
            paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(characterPlayer.getInfo()
                    .getCharacterDescription(), FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
                    .getHandWrittingFontSize(FadingSunsTheme.CHARACTER_DESCRIPTION_FONT_SIZE), DESCRIPTION_WIDTH),
                    new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
                            .getHandWrittingFontSize(FadingSunsTheme.CHARACTER_DESCRIPTION_FONT_SIZE))));
        }
        return paragraph;
    }

    private static Paragraph getCharacterBackground(CharacterPlayer characterPlayer) {
        final Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph(TextFactory.getInstance().getElement("historyAnnotations").getName().getTranslatedText()
                + ": ", new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.ANNOTATIONS_SUBTITLE_FONT_SIZE)));
        if (characterPlayer != null) {
            paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(characterPlayer.getInfo()
                    .getBackgroundDescription(), FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
                    .getHandWrittingFontSize(FadingSunsTheme.CHARACTER_DESCRIPTION_FONT_SIZE), DESCRIPTION_WIDTH),
                    new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
                            .getHandWrittingFontSize(FadingSunsTheme.CHARACTER_DESCRIPTION_FONT_SIZE))));
        }
        return paragraph;
    }

}
