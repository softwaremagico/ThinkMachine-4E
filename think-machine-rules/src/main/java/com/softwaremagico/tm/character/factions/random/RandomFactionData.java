package com.softwaremagico.tm.character.factions.random;

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
    public List<Name> getNames(Gender gender) {
        if (gender == Gender.MALE) {
            return getMaleNames();
        }
        return getFemaleNames();
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
