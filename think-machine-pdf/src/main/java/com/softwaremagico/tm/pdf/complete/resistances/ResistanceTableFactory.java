package com.softwaremagico.tm.pdf.complete.resistances;

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

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

public class ResistanceTableFactory extends BaseElement {

    public static PdfPTable getResistancesAndProtectionsBasicsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        final PdfPCell separator = createBigWhiteSeparator();
        separator.setColspan(widths.length);
        table.addCell(separator);

        table.addCell(getResistancesTable(characterPlayer));

        final PdfPTable vitalityTable = new PdfPTable(new float[]{1f});
        setTableProperties(vitalityTable);
        vitalityTable.addCell(getVitalityTable(characterPlayer));
        vitalityTable.addCell(getRevivalsTable(characterPlayer));

        table.addCell(vitalityTable);

        return table;
    }

    public static PdfPTable getResistancesTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 0.9f, 1.1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("resistances").getName().getTranslatedText(), widths.length));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("bodyResistance").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getBodyResistance() : null));
        table.addCell(createRectangle(null));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("mindResistance").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getMindResistance() : null));
        table.addCell(createRectangle(null));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("spiritResistance").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getSpiritResistance() : null));
        table.addCell(createRectangle(null));

        return table;
    }

    public static PdfPTable getVitalityTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1.1f, 0.7f, 1.1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("vitality").getName().getTranslatedText(), widths.length));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("maximumAbbreviation").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getBodyResistance() : null));
        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("current").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle(null));

        return table;
    }

    public static PdfPTable getRevivalsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1.1f, 0.7f, 1.1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("revivals").getName().getTranslatedText(), widths.length));

        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("quantity").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle(characterPlayer != null ? characterPlayer.getBodyResistance() : null));
        table.addCell(new Paragraph(BaseElement.getChunk(TextFactory.getInstance().getElement("number").getName().getTranslatedText(),
                FadingSunsTheme.getLineFont(), FadingSunsTheme.LINE_FONT_SIZE)));
        table.addCell(createRectangle(null));

        return table;
    }
}
