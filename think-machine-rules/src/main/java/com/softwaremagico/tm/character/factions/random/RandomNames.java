package com.softwaremagico.tm.character.factions.random;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "names")
public class RandomNames {

    @JsonProperty("male")
    private String randomMaleNamesContent;
    @JsonProperty("female")
    private String randomFemaleNamesContent;

    public String getRandomMaleNamesContent() {
        return randomMaleNamesContent;
    }

    public void setRandomMaleNamesContent(String randomMaleNamesContent) {
        this.randomMaleNamesContent = randomMaleNamesContent;
    }

    public String getRandomFemaleNamesContent() {
        return randomFemaleNamesContent;
    }

    public void setRandomFemaleNamesContent(String randomFemaleNamesContent) {
        this.randomFemaleNamesContent = randomFemaleNamesContent;
    }
}
