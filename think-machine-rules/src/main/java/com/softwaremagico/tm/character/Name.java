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

public class Name extends Element {

    @JsonIgnore
    private final Gender gender;

    @JsonIgnore
    private final String faction;

    @JsonIgnore
    private final String specie;

    public Name(String name, String language, String moduleName, Gender gender, String faction, String specie) {
        super(getId(name, faction, specie, moduleName), new TranslatedText(name.trim()), null, language, moduleName);
        this.gender = gender;
        this.faction = faction;
        this.specie = specie;
    }

    public Name(String name, Gender gender, String faction, String specie) {
        super(name, new TranslatedText(name.trim()), null, null, null);
        this.gender = gender;
        this.faction = faction;
        this.specie = specie;
    }

    public String getFaction() {
        return faction;
    }

    public Gender getGender() {
        return gender;
    }

    public String getSpecie() {
        return specie;
    }

    private static String getId(String name, String faction, String specie, String moduleName) {
        return name.replaceAll("\\s+", "_").toLowerCase() + (faction != null ? "_" + faction : "")
                + (specie != null ? "_" + specie : "")
                + (moduleName != null ? "_" + moduleName.replaceAll("\\s+", "_").toLowerCase() : "");
    }

}
