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

import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;

public class DescriptionAndAnnotationsTableFactory extends BaseElement {

    public static PdfPTable getDescriptionAndAnnotationsTable(CharacterPlayer characterPlayer) {
        final float[] widths = {1.1f, 2f};
        final PdfPTable table = new PdfPTable(widths);
        setTableProperties(table);

        table.addCell(DescriptionTableFactory.getDescriptionTable(characterPlayer));
        table.addCell(AnnotationsTableFactory.getAnnotationsTable(characterPlayer));

        return table;
    }
}
