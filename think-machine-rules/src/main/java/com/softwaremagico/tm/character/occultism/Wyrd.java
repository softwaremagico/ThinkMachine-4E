package com.softwaremagico.tm.character.occultism;

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


import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

public class Wyrd extends Element {
    public static final String WYRD_ID = "wyrd";
    private static final String WYRD_NAME = "Wyrd";
    private int value = 0;

    public Wyrd(String language, String moduleName) {
        super(WYRD_ID, new TranslatedText(WYRD_NAME), null, language, moduleName);
    }

    public Wyrd(String language, String moduleName, int value) {
        this(language, moduleName);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
