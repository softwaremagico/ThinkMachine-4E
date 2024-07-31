package com.softwaremagico.tm.character.factions.random;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.Element;

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
}
