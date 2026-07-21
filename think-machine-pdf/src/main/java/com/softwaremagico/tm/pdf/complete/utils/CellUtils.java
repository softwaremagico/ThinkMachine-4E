package com.softwaremagico.tm.pdf.complete.utils;

/*-
 * #%L
 * Think Machine 4E (PDF Sheets)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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

import com.lowagie.text.pdf.BaseFont;

/**
 * Utilities for measuring/trimming text in PDF cells.
 */
public final class CellUtils {

    /**
     * Utility class.
     */
    private CellUtils() {

    }

    /**
     * Returns the largest substring that fits in the given width.
     *
     * @param originalText text to trim.
     * @param font font used for measurement.
     * @param fontSize font size.
     * @param width available width.
     * @return substring that fits.
     */
    public static String getSubStringFitsIn(String originalText, BaseFont font, int fontSize, float width) {
        String text = originalText;

        while (!fitsIn(text, font, fontSize, width) && !text.isEmpty()) {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }

    /**
     * Checks whether a text fits in the given width.
     *
     * @param text text to test.
     * @param font font used for measurement.
     * @param fontSize font size.
     * @param width available width.
     * @return true if text fits.
     */
    public static boolean fitsIn(String text, BaseFont font, int fontSize, float width) {
        return font.getWidthPoint(text, fontSize) < width;
    }
}
