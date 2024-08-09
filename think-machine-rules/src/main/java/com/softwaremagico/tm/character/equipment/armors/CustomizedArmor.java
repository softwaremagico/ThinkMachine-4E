package com.softwaremagico.tm.character.equipment.armors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.equipment.item.Quality;

public class CustomizedArmor extends Armor {

    @JsonProperty("quality")
    private Quality quality;

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }
}
