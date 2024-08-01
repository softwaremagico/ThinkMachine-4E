package com.softwaremagico.tm.character.factions.random;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JacksonXmlRootElement(localName = "faction")
public class RandomFaction extends Element<RandomFaction> {

    @JsonProperty("random")
    private RandomFactionData data;

    public RandomFactionData getData() {
        return data;
    }

    public void setData(RandomFactionData data) {
        data.setFaction(getId());
        this.data = data;
    }

    @JsonIgnore
    public List<Name> getNames(Gender gender) {
        if (data != null) {
            if (gender == Gender.MALE) {
                return data.getMaleNames();
            }
            return data.getFemaleNames();
        }
        return new ArrayList<>();
    }

    @JsonIgnore
    public Set<Name> getAllNames() {
        final Set<Name> names = new HashSet<>();
        for (final Gender gender : Gender.values()) {
            try {
                names.addAll(getNames(gender));
            } catch (NullPointerException e) {
                //No names defined.
            }
        }
        return names;
    }

    @JsonIgnore
    public List<Surname> getSurnames() {
        if (data != null) {
            return data.getSurnames();
        }
        return new ArrayList<>();
    }
}
