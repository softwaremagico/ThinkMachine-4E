package com.softwaremagico.tm.character.cybernetics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.equipment.TechCompulsionFactory;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

public class Cyberdevice extends Perk {
    @JsonProperty("techLevel")
    private Integer techLevel;
    @JsonProperty("techCompulsion")
    private String techCompulsion;
    @JsonProperty("size")
    private Size size;

    public Integer getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(Integer techLevel) {
        this.techLevel = techLevel;
    }

    public String getTechCompulsion() {
        return techCompulsion;
    }

    public void setTechCompulsion(String techCompulsion) {
        this.techCompulsion = techCompulsion;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (techLevel == null) {
            throw new InvalidXmlElementException("cyberDeviceTechLevel is required");
        }
        if (techCompulsion == null) {
            throw new InvalidXmlElementException("techCompulsion is required");
        }
        TechCompulsionFactory.getInstance().getElement(getTechCompulsion());
    }
}
