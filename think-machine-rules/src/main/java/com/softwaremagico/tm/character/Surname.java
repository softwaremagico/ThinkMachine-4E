package com.softwaremagico.tm.character;

/*-
 * #%L
 * Think Machine (Core)
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

public class Surname extends Element {

    private String surname;

    @JsonIgnore
    private final String faction;

    public String getFaction() {
        return faction;
    }

    public Surname(String surname, String language, String moduleName, String faction) {
        super(getId(surname, faction, moduleName), new TranslatedText(surname), null, language, moduleName);
        this.faction = faction;
    }

    public Surname(String surname, String faction) {
        super(surname, new TranslatedText(surname), null, null, null);
        this.faction = faction;
    }

    private static String getId(String surname, String faction, String moduleName) {
        return surname.replaceAll("\\s+", "_").toLowerCase() + (faction != null ? "_" + faction : "")
                + (moduleName != null ? "_" + moduleName.replaceAll("\\s+", "_").toLowerCase() : "");
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}
