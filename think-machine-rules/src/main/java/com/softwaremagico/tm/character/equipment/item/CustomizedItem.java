package com.softwaremagico.tm.character.equipment.item;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomizedItem extends Item {

    @JsonProperty("quality")
    private Quality quality;

}
