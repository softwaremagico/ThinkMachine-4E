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
import com.softwaremagico.tm.pdf.complete.equipment.FirebirdsFactory;

public class FirebirdsAndOthersTable extends BaseElement {
    private static final float[] WIDTHS = {1f, 2f};

    public static PdfPTable getFirebirdsAndOthersTable(CharacterPlayer characterPlayer) {
        final PdfPTable table = new PdfPTable(WIDTHS);
        setTableProperties(table);
        table.getDefaultCell().setBorder(0);

        final PdfPTable firebirdsTable = FirebirdsFactory.getFirebirdsTable(characterPlayer);
        final PdfPCell firebirdsCell = new PdfPCell(firebirdsTable);
        firebirdsCell.setBorder(0);
        table.addCell(firebirdsCell);

        final PdfPCell rightColumnCell = new PdfPCell();
        rightColumnCell.setBorder(0);
        table.addCell(rightColumnCell);

        return table;
    }
}
