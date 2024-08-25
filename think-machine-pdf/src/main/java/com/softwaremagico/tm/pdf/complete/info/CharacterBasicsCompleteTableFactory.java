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


import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;

import java.io.IOException;

public class CharacterBasicsCompleteTableFactory extends CharacterBasicsTableFactory {
    private static final int MAX_VALUE_LENGTH = 13;

    public static PdfPTable getCharacterBasicsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f, 1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        final PdfPCell separator = createWhiteSeparator();
        separator.setColspan(widths.length);
        table.addCell(separator);

        table.addCell(getFirstColumnTable(characterPlayer));
        table.addCell(getSecondColumnTable(characterPlayer));
        table.addCell(getLogoColumnTable());
        return table;
    }

    private static PdfPCell getFirstColumnTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createField(characterPlayer, "name", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE, MAX_VALUE_LENGTH));
        table.addCell(createField(characterPlayer, "gender", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE, MAX_VALUE_LENGTH));
        table.addCell(createField(characterPlayer, "specie", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE,
                MAX_VALUE_LENGTH));
        table.addCell(createField(characterPlayer, "planet", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE,
                MAX_VALUE_LENGTH));
        table.addCell(createField(characterPlayer, "age", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE, MAX_VALUE_LENGTH));

        final PdfPCell cell = new PdfPCell();
        setCellProperties(cell);

        cell.addElement(table);

        return cell;
    }

    private static PdfPCell getSecondColumnTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(createField(characterPlayer, "level", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE, MAX_VALUE_LENGTH));
        table.addCell(createField(characterPlayer, "upbringing", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE, MAX_VALUE_LENGTH));
        table.addCell(createField(characterPlayer, "faction", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE, MAX_VALUE_LENGTH));
        table.addCell(createField(characterPlayer, "calling", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE, MAX_VALUE_LENGTH));
        table.addCell(createField(characterPlayer, "rank", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE, MAX_VALUE_LENGTH));

        final PdfPCell cell = new PdfPCell();
        setCellProperties(cell);

        cell.addElement(table);

        return cell;
    }

    private static PdfPCell getLogoColumnTable() {
        try {
            return createLogoCell();
        } catch (DocumentException | IOException e) {
            PdfExporterLog.errorMessage(CharacterBasicsCompleteTableFactory.class.getName(), e);
        }
        return null;
    }

}
