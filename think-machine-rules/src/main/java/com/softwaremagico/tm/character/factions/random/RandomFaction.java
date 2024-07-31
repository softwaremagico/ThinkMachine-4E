package com.softwaremagico.tm.character.factions.random;

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
