package com.softwaremagico.tm.character.equipment.weapons;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.equipment.item.Quality;

public class CustomizedWeapon extends Weapon {

    @JsonProperty("quality")
    private Quality quality;

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }
}
