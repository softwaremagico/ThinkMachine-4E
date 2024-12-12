package com.softwaremagico.tm.character.cybernetics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.equipment.TechCompulsionFactory;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

public class Cyberdevice extends Perk {
    @JsonProperty("cyberdevice")
    private Integer cyberDeviceTechLevel;
    @JsonProperty("techCompulsion")
    private String techCompulsion;

    public Integer getCyberDeviceTechLevel() {
        return cyberDeviceTechLevel;
    }

    public void setCyberDeviceTechLevel(Integer cyberDeviceTechLevel) {
        this.cyberDeviceTechLevel = cyberDeviceTechLevel;
    }

    public String getTechCompulsion() {
        return techCompulsion;
    }

    public void setTechCompulsion(String techCompulsion) {
        this.techCompulsion = techCompulsion;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (cyberDeviceTechLevel == null) {
            throw new InvalidXmlElementException("cyberDeviceTechLevel is required");
        }
        if (techCompulsion == null) {
            throw new InvalidXmlElementException("techCompulsion is required");
        }
        TechCompulsionFactory.getInstance().getElement(getTechCompulsion());
    }
}
