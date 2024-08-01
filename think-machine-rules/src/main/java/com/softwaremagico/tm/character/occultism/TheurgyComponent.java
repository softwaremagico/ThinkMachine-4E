package com.softwaremagico.tm.character.occultism;


import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

public class TheurgyComponent extends Element<TheurgyComponent> {
    private String abbreviation;
    private char code;

    public TheurgyComponent() {
        super();
    }

    public TheurgyComponent(String id, TranslatedText name, TranslatedText description, String language, String moduleName,
                            String abbreviation, char code) {
        super(id, name, description, language, moduleName);
        this.abbreviation = abbreviation;
        this.code = code;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public char getCode() {
        return code;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setCode(char code) {
        this.code = code;
    }
}
