package com.softwaremagico.tm.pdf.complete.equipment;

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

import java.text.DecimalFormat;

public class FirebirdsFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final int ROWS = 2;
    private static final String GAP = "_______________________________________________";
    private static final int COLUMN_WIDTH = 174;
    private static final int TITLE_MARGIN = 10;
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#.#");

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

    private static Paragraph getMoneyValue(CharacterPlayer characterPlayer, String tag, Float value) {
        final Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk(TextFactory.getInstance().getElement(tag).getName().getTranslatedText()
                + ": ", new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        String moneyText = "";
        float usedWidth = FadingSunsTheme.getTitleFont().getWidthPoint(TextFactory.getInstance().getElement(tag).getName().getTranslatedText(),
                FadingSunsTheme.LINE_FONT_SIZE) + TITLE_MARGIN;
        if (characterPlayer != null) {
            moneyText = "  " + (value != null ? MONEY_FORMAT.format(value) + "- " : "");
            usedWidth += FadingSunsTheme.getHandwrittingFont().getWidthPoint(
                    TextFactory.getInstance().getElement(tag).getName().getTranslatedText() + moneyText,
                    FadingSunsTheme.LINE_FONT_SIZE);
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
