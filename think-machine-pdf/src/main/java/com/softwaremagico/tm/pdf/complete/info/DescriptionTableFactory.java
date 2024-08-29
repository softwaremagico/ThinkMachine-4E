package com.softwaremagico.tm.pdf.complete.info;

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

public class DescriptionTableFactory extends BaseElement {
    private static final float[] WIDTHS = {1f};
    private static final String GAP = "_______________________________________________";
    private static final int COLUMN_WIDTH = 174;

    public static PdfPTable getDescriptionTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final PdfPTable table = new PdfPTable(WIDTHS);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(createDescriptionValue(characterPlayer));

        return table;
    }

    private static PdfPTable createDescriptionValue(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("description").getName(), widths.length));
        table.addCell(createLine(characterPlayer, "birthdate", (characterPlayer == null
                || characterPlayer.getInfo().getBirthdate() == null) ? null : characterPlayer.getInfo().getBirthdate()));
        table.addCell(createLine(characterPlayer, "hair", (characterPlayer == null
                || characterPlayer.getInfo().getHair() == null) ? null : characterPlayer.getInfo().getHair()));
        table.addCell(createLine(characterPlayer, "eyes", (characterPlayer == null
                || characterPlayer.getInfo().getEyes() == null) ? null : characterPlayer.getInfo().getEyes()));
        table.addCell(createLine(characterPlayer, "complexion", (characterPlayer == null
                || characterPlayer.getInfo().getComplexion() == null) ? null : characterPlayer.getInfo().getComplexion()));
        table.addCell(createLine(characterPlayer, "weight", (characterPlayer == null
                || characterPlayer.getInfo().getWeight() == null) ? null : characterPlayer.getInfo().getWeight()));
        table.addCell(createLine(characterPlayer, "appearance", null));
        table.addCell(createEmptyElementLine(GAP, COLUMN_WIDTH));
        table.addCell(createEmptyElementLine(GAP, COLUMN_WIDTH));

        return table;
    }

    private static PdfPCell createLine(CharacterPlayer characterPlayer, String tag, String value) {
        final Paragraph paragraph = new Paragraph();

        final String text = getTranslatedTag(tag);
        // Spaces at the end are eliminated. For calculating width we can put
        // the characters in different order.
        final float textWidth = FadingSunsTheme.getLineFont().getWidthPoint(text + " :",
                FadingSunsTheme.LINE_FONT_SIZE);

        paragraph.add(BaseElement.getChunk(text + ": "));
        if (characterPlayer == null || value == null) {
            paragraph.add(BaseElement.getChunk(GAP, (int) (COLUMN_WIDTH - textWidth)));
        } else {
            paragraph.add(BaseElement.getChunk(value, (int) (COLUMN_WIDTH - textWidth)));
        }

        final PdfPCell cell = createEmptyElementLine("");
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPhrase(paragraph);

        return cell;
    }

    private static String getTranslatedTag(String tag) {
        return TextFactory.getInstance().getTranslatedText(tag);
    }
}
