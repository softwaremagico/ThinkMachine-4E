package com.softwaremagico.tm.pdf.complete.elements;

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

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;

public class CellDottedBorderEvent implements PdfPCellEvent {
    private static final float DASH = 3f;

    public CellDottedBorderEvent() {
        super();
    }

    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        final PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
        canvas.setLineDash(DASH, DASH);
        canvas.moveTo(position.getLeft(), position.getTop());
        canvas.lineTo(position.getRight(), position.getTop());
        canvas.moveTo(position.getLeft(), position.getBottom());
        canvas.lineTo(position.getRight(), position.getBottom());
        canvas.stroke();
    }
}
