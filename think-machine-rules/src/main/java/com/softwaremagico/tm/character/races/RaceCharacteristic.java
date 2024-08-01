package com.softwaremagico.tm.character.races;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;


@JacksonXmlRootElement(localName = "characteristic")
public class RaceCharacteristic extends Element<RaceCharacteristic> {

    @JsonAlias("id")
    private CharacteristicName characteristic;
    @JsonProperty("value")
    private int initialValue;
    @JsonProperty("maximumValue")
    private int maximumValue;
    @JsonProperty("maximumInitialValue")
    private int maximumInitialValue;

    public RaceCharacteristic() {
        super();
    }

    public CharacteristicName getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(CharacteristicName characteristic) {
        this.characteristic = characteristic;
    }

    public int getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(int value) {
        this.initialValue = value;
    }

    public int getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(int max) {
        this.maximumValue = max;
    }

    public int getMaximumInitialValue() {
        return maximumInitialValue;
    }

    public void setMaximumInitialValue(int maximumInitialValue) {
        this.maximumInitialValue = maximumInitialValue;
    }

}
