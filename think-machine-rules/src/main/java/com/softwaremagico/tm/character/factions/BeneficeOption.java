package com.softwaremagico.tm.character.factions;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.Element;

@JacksonXmlRootElement(localName = "benefice")
public class BeneficeOption extends Element<BeneficeOption> {
    private int value;

    public BeneficeOption() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
