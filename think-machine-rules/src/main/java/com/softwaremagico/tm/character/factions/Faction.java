package com.softwaremagico.tm.character.factions;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.character.CharacterDefinitionStep;
import com.softwaremagico.tm.character.equipment.Equipment;

import java.util.List;

@JacksonXmlRootElement(localName = "faction")
public class Faction extends CharacterDefinitionStep<Faction> {
    private Boolean isOnlyForHuman;
    private Blessing blessing;
    private Curse curse;
    @JsonProperty("favoredCallings")
    private List<String> favoredCallings;
    private List<Equipment<?>> materialAwards;

    public Faction() {
    }

    public boolean isOnlyForHuman() {
        if (isOnlyForHuman == null) {
            isOnlyForHuman = getRestrictions().getRestrictedToSpecies().size() == 1
                    && getRestrictions().getRestrictedToSpecies().contains("human");

        }
        return isOnlyForHuman;
    }

    public Blessing getBlessing() {
        return blessing;
    }

    public void setBlessing(Blessing blessing) {
        this.blessing = blessing;
    }

    public Curse getCurse() {
        return curse;
    }

    public void setCurse(Curse curse) {
        this.curse = curse;
    }

    public List<String> getFavoredCallings() {
        return favoredCallings;
    }

    public void setFavoredCallings(List<String> favoredCallings) {
        this.favoredCallings = favoredCallings;
    }

    public List<Equipment<?>> getMaterialAwards() {
        return materialAwards;
    }

    public void setMaterialAwards(List<Equipment<?>> materialAwards) {
        this.materialAwards = materialAwards;
    }
}
