package com.softwaremagico.tm.pdf.complete.equipment;

import com.lowagie.text.Chunk;
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

public class FirebirdsFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final int ROWS = 2;
    private static final String GAP = "_______________________________________________";
    private static final int COLUMN_WIDTH = 174;

    public static PdfPTable getFirebirdsTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(getFirebirdsValueTable(characterPlayer));

        return table;
    }

    private static PdfPTable getFirebirdsValueTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("firebirds").getName().getTranslatedText(), widths.length));

        final PdfPCell separator = createWhiteSeparator();
        separator.setColspan(widths.length);
        table.addCell(separator);

        final PdfPCell cashCell = new PdfPCell(getCashValue(characterPlayer));
        cashCell.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
        cashCell.setBorderWidth(0);
        table.addCell(cashCell);

        for (int i = 0; i < ROWS; i++) {
            table.addCell(createEmptyElementLine(GAP, COLUMN_WIDTH));
        }

        table.addCell(separator);

        final PdfPCell assetsCell = new PdfPCell(getAssetsValue(characterPlayer));
        assetsCell.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
        assetsCell.setBorderWidth(0);
        table.addCell(assetsCell);

        for (int i = 0; i < ROWS; i++) {
            table.addCell(createEmptyElementLine(GAP, COLUMN_WIDTH));
        }

        return table;
    }

    private static Paragraph getCashValue(CharacterPlayer characterPlayer) {
        return getMoneyValue(characterPlayer, "cash", characterPlayer != null ? characterPlayer.getCashMoney() : null);
    }

    private static Paragraph getAssetsValue(CharacterPlayer characterPlayer) {
        return getMoneyValue(characterPlayer, "assets", null);
    }

    private static Paragraph getMoneyValue(CharacterPlayer characterPlayer, String tag, Integer value) {
        final Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk(TextFactory.getInstance().getElement(tag).getName().getTranslatedText()
                + ": ", new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        String moneyText = "";
        float usedWidth = 0;
        if (characterPlayer != null) {
            moneyText = "  " + (value != null ? value + "- " : "");
            usedWidth = FadingSunsTheme.getHandwrittingFont().getWidthPoint(
                    TextFactory.getInstance().getElement(tag).getName().getTranslatedText() + moneyText,
                    FadingSunsTheme.LINE_FONT_SIZE - 1f);
            paragraph.add(new Chunk(moneyText, new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
                    .getHandWrittingFontSize(FadingSunsTheme.LINE_FONT_SIZE - 1))));
        }
        moneyText = CellUtils.getSubStringFitsIn(GAP,
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE, COLUMN_WIDTH - usedWidth);
        paragraph.add(new Chunk(moneyText, new Font(FadingSunsTheme.getLineFont(),
                FadingSunsTheme.LINE_FONT_SIZE)));
        return paragraph;
    }
}
