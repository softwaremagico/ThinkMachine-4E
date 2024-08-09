package com.softwaremagico.tm.character.equipment.shields;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.equipment.item.Quality;

public class CustomizedShield extends Shield {

    @JsonProperty("quality")
    private Quality quality;

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }
}
