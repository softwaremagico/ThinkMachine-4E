package com.softwaremagico.tm.exceptions;

/*-
 * #%L
 * Think Machine 4E (Rules)
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


import com.softwaremagico.tm.character.Selection;

public class IncompleteSelectedElementException extends InvalidXmlElementException {
    private static final long serialVersionUID = 194333793074549262L;
    private final Selection selection;

    public IncompleteSelectedElementException(String message) {
        super(message);
        selection = null;
    }

    public IncompleteSelectedElementException(String message, Selection selection) {
        super(message);
        this.selection = selection;
    }

    public IncompleteSelectedElementException(String message, Exception e, Selection selection) {
        super(message, e);
        this.selection = selection;
    }

    public Selection getSelection() {
        return selection;
    }
}
