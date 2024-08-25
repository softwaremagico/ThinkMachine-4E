package com.softwaremagico.tm.pdf.complete.skills;

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
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.CustomPdfTable;
import com.softwaremagico.tm.txt.TextFactory;

import java.util.List;

public final class CharacteristicsColumn extends BaseElement {
    private static final String GAP = "   ";

    private CharacteristicsColumn() {

    }

    public static PdfPCell createContent(CharacterPlayer characterPlayer, CharacteristicType characteristicType) {
        final float[] widths = {3f, 0.5f, 1.5f, 0.1f};
        final PdfPTable table = new PdfPTable(widths);
        BaseElement.setTableProperties(table);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

        if (characteristicType != null) {
            final List<CharacteristicName> characteristicNames = CharacteristicName.getCharacteristics(characteristicType);
            for (final CharacteristicName characteristicName : characteristicNames) {
                final Paragraph paragraph = new Paragraph();
                paragraph.add(new Paragraph(TextFactory.getInstance().getElement(characteristicName.getId()).getName().getTranslatedText(),
                        new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
                final PdfPCell characteristicTitle = new PdfPCell(paragraph);
                characteristicTitle.setBorder(0);
                characteristicTitle.setMinimumHeight(FadingSunsTheme.ROW_HEIGHT);
                characteristicTitle.setVerticalAlignment(Element.ALIGN_TOP);
                table.addCell(characteristicTitle);

                final PdfPCell characteristicTitleInitialValue = new PdfPCell(new Paragraph("(" + (characterPlayer == null ? GAP
                        : characterPlayer.getStartingValue(characteristicName)) + ")",
                        new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
                characteristicTitleInitialValue.setBorder(0);
                table.addCell(characteristicTitleInitialValue);

                // Rectangle
                if (characterPlayer == null) {
                    table.addCell(CustomPdfTable.createRectangle());
                } else {
                    table.addCell(CustomPdfTable.createRectangle(characterPlayer.getCharacteristicValue(characteristicName) + ""));
                }

                // Margin
                final PdfPCell margin = new PdfPCell();
                margin.setBorder(0);
                table.addCell(margin);
            }
        }

        final PdfPCell cell = new PdfPCell();
        cell.addElement(table);
        BaseElement.setCellProperties(cell);

        return cell;
    }

}
