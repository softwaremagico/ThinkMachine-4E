package com.softwaremagico.tm.pdf.complete.occultism;

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

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.txt.TextFactory;

import java.util.List;
import java.util.Map;

public class OccultismPowerTable extends BaseElement {
    private static final float[] WIDTHS = {3f, 1f, 1f, 3f};
    private static final String GAP = "____________";
    private static final int NAME_COLUMN_WIDTH = 120;
    private static final int LEVEL_COLUMN_WIDTH = 30;
    private static final int ROLL_COLUMN_WIDTH = 30;
    private static final int TIME_COLUMN_WIDTH = 120;
    private static final int ROWS = 6;

    public static PdfPTable getOccultismTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final float[] widths = {1f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        final PdfPCell separator = createWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(getOccultismValueTable(characterPlayer));

        return table;
    }


    public static PdfPTable getOccultismValueTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);

        table.addCell(createSectionTitle(TextFactory.getInstance().getElement("occultismPowers").getName(), WIDTHS.length));

        final PdfPCell separator = createWhiteSeparator();
        separator.setColspan(WIDTHS.length);
        table.addCell(separator);

        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("occultismTablePower").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("occultismTableLevel").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("occultismTableGoal").getName()));
        table.addCell(createTableSubtitleElement(TextFactory.getInstance().getElement("occultismTableCost").getName()));

        int addedPowers = 0;
        if (characterPlayer != null) {
            for (final Map.Entry<String, List<OccultismPower>> occultismPathEntry : characterPlayer.getSelectedPowers()
                    .entrySet()) {
                if (addedPowers >= ROWS) {
                    break;
                }
                for (final OccultismPower occultismPower : occultismPathEntry.getValue()) {
                    table.addCell(createFirstElementLine(occultismPower.getName().getTranslatedText(), NAME_COLUMN_WIDTH,
                            FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine(occultismPower.getOccultismLevel() + "", LEVEL_COLUMN_WIDTH,
                            FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine(occultismPower.getGoal(characterPlayer), ROLL_COLUMN_WIDTH,
                            FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
                    table.addCell(createElementLine(occultismPower.getCost().getTranslatedText(), TIME_COLUMN_WIDTH,
                            FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
                    addedPowers++;
                    if (addedPowers >= ROWS) {
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < ROWS - addedPowers; i++) {
            table.addCell(createEmptyElementLine(GAP + GAP + GAP, NAME_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, LEVEL_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP, ROLL_COLUMN_WIDTH));
            table.addCell(createEmptyElementLine(GAP + GAP + GAP, TIME_COLUMN_WIDTH));
        }

        return table;
    }
}
