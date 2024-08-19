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

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

public class ShieldTableFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final String GAP = "______";
    private static final int COLUMN_WIDTH = 90;

    public static PdfPTable getShieldTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final PdfPTable table = new PdfPTable(WIDTHS);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(createShieldValues(characterPlayer));

        return table;
    }

    private static PdfPTable createShieldValues(CharacterPlayer characterPlayer) {
        final float[] widths = {1.5f, 1f, 1.5f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("shield").getName().getTranslatedText(), widths.length));

        final PdfPCell nameCell;
        if (characterPlayer == null || characterPlayer.getShield() == null) {
            nameCell = createEmptyElementLine(GAP + GAP + GAP, COLUMN_WIDTH);
        } else {
            nameCell = createElementLine(characterPlayer.getShield().getName().getTranslatedText(), COLUMN_WIDTH,
                    FadingSunsTheme.SHIELD_CONTENT_FONT_SIZE);
        }
        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(nameCell);

        table.addCell(getShieldRange(characterPlayer));
        table.addCell(getShieldDetails(characterPlayer));

        final Paragraph paragraph = new Paragraph();
        paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getElement("shieldHits").getName() + ": "));
        if (characterPlayer == null || characterPlayer.getShield() == null) {
            BaseElement.getChunk(GAP);
        } else {
            paragraph.add(BaseElement.getChunk(characterPlayer.getShield().getHits() + " - ", FadingSunsTheme
                    .getHandwrittingFont(), FadingSunsTheme.SHIELD_CONTENT_FONT_SIZE));
        }
        final PdfPCell protectionCell = new PdfPCell();
        protectionCell.setPhrase(paragraph);
        protectionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        protectionCell.setColspan(widths.length);
        protectionCell.setBorder(0);
        table.addCell(protectionCell);

        return table;
    }

    private static PdfPTable getShieldDetails(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        BaseElement.setTableProperties(table);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);


        Paragraph paragraph = new Paragraph();
        paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getElement("burnOut").getName() + ": "));
        if (characterPlayer == null || characterPlayer.getShield() == null) {
            BaseElement.getChunk(GAP);
        } else {
            paragraph.add(BaseElement.getChunk(characterPlayer.getShield().getBurnOut() + "   ", FadingSunsTheme
                    .getHandwrittingFont(), FadingSunsTheme.SHIELD_CONTENT_FONT_SIZE));
        }
        table.addCell(paragraph);

        paragraph = new Paragraph();
        paragraph.add(BaseElement.getChunk(TextFactory.getInstance().getElement("distortion").getName() + ": "));
        if (characterPlayer == null || characterPlayer.getShield() == null) {
            BaseElement.getChunk(GAP);
        } else {
            paragraph.add(BaseElement.getChunk(characterPlayer.getShield().getDistortion() + "   ", FadingSunsTheme
                    .getHandwrittingFont(), FadingSunsTheme.SHIELD_CONTENT_FONT_SIZE));
        }
        table.addCell(paragraph);

        return table;
    }


    private static PdfPTable getShieldRange(CharacterPlayer characterPlayer) {
        final float[] widths = {3f, 1f, 3f};
        final PdfPTable table = new PdfPTable(widths);
        BaseElement.setTableProperties(table);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setPadding(0);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        if (characterPlayer == null || characterPlayer.getShield() == null) {
            table.addCell(createRectangle(""));
        } else {
            table.addCell(createRectangle(characterPlayer.getShield().getImpact()));
        }
        table.addCell(createEmptyElementLine("/"));
        final PdfPCell rectangle;
        if (characterPlayer == null || characterPlayer.getShield() == null) {
            rectangle = createRectangle("");
        } else {
            rectangle = createRectangle(characterPlayer.getShield().getForce());
        }
        rectangle.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
        table.addCell(rectangle);

        return table;
    }
}
