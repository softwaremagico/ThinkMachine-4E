package com.softwaremagico.tm.pdf.complete;

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
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.occultism.OccultismValuesTableFactory;
import com.softwaremagico.tm.pdf.complete.resistances.ResistanceTableFactory;
import com.softwaremagico.tm.pdf.complete.resistances.RevivalsTable;
import com.softwaremagico.tm.pdf.complete.resistances.VictoryPointBankTableFactory;
import com.softwaremagico.tm.pdf.complete.resistances.VitalityTable;

public class RightColumn extends BaseElement {
    private static final float[] WIDTHS = {1f};

    public static PdfPTable getElements(CharacterPlayer characterPlayer) {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);
        table.getDefaultCell().setBorder(0);

        final PdfPCell separator = createWhiteSeparator();
        table.addCell(separator);


        table.addCell(OccultismValuesTableFactory.getOccultismValuesTable(characterPlayer));
        table.addCell(ResistanceTableFactory.getResistancesTable(characterPlayer));
        table.addCell(VictoryPointBankTableFactory.getVictoryPointBankTable(characterPlayer));
        table.addCell(VitalityTable.getVitalityTable(characterPlayer));
        table.addCell(RevivalsTable.getRevivalsTable(characterPlayer));


        return table;
    }
}
