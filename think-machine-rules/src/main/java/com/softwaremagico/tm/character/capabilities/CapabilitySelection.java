package com.softwaremagico.tm.character.capabilities;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;

public class CapabilitySelection extends Element<CharacterDefinitionStepSelection> {
    private String specialization;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
