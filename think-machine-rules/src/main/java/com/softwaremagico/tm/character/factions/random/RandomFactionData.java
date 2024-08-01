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
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@JacksonXmlRootElement(localName = "random")
public class RandomFactionData {

    @JsonProperty("names")
    private RandomNames randomNames;

    @JsonProperty("surnames")
    private String randomSurnamesContent;

    private String faction;

    @JsonIgnore
    private List<Name> maleNames;

    @JsonIgnore
    private List<Name> femaleNames;

    @JsonIgnore
    private List<Surname> surnames;


    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public RandomNames getRandomNames() {
        return randomNames;
    }

    public void setRandomNames(RandomNames randomNames) {
        this.randomNames = randomNames;
    }

    public String getRandomSurnamesContent() {
        return randomSurnamesContent;
    }

    public void setRandomSurnamesContent(String randomSurnamesContent) {
        this.randomSurnamesContent = randomSurnamesContent;
    }

    @JsonIgnore
    public List<Name> getMaleNames() {
        if (maleNames == null) {
            maleNames = new ArrayList<>();
            if (getRandomNames().getRandomMaleNamesContent() != null && !getRandomNames().getRandomMaleNamesContent().isEmpty()) {
                final StringTokenizer maleNamesTokenizer = new StringTokenizer(getRandomNames().getRandomMaleNamesContent(), ",");
                while (maleNamesTokenizer.hasMoreTokens()) {
                    maleNames.add(new Name(maleNamesTokenizer.nextToken().trim(), null, null, Gender.MALE,
                            faction));
                }
            }
        }
        return maleNames;
    }

    @JsonIgnore
    public List<Name> getFemaleNames() {
        if (femaleNames == null) {
            femaleNames = new ArrayList<>();
            if (getRandomNames().getRandomFemaleNamesContent() != null && !getRandomNames().getRandomFemaleNamesContent().isEmpty()) {
                final StringTokenizer femaleNamesTokenizer = new StringTokenizer(getRandomNames().getRandomFemaleNamesContent(), ",");
                while (femaleNamesTokenizer.hasMoreTokens()) {
                    femaleNames.add(new Name(femaleNamesTokenizer.nextToken().trim(), null, null, Gender.FEMALE,
                            faction));
                }
            }
        }
        return femaleNames;
    }

    @JsonIgnore
    public List<Surname> getSurnames() {
        if (surnames == null) {
            surnames = new ArrayList<>();
            if (randomSurnamesContent != null && !randomSurnamesContent.isEmpty()) {
                final StringTokenizer surnamesTokenizer = new StringTokenizer(randomSurnamesContent, ",");
                while (surnamesTokenizer.hasMoreTokens()) {
                    surnames.add(new Surname(surnamesTokenizer.nextToken().trim(), null, null,
                            faction));
                }
            }
        }
        return surnames;
    }
}
