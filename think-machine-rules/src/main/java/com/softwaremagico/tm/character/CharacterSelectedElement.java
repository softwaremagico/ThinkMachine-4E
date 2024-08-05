package com.softwaremagico.tm.character;

import java.util.List;

public class CharacterSelectedElement {
    private List<String> selections;
    private Integer value;

    public List<String> getSelections() {
        return selections;
    }

    public void setSelections(List<String> selections) {
        this.selections = selections;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
