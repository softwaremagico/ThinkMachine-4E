package com.softwaremagico.tm.character.occultism;


import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

public class Wyrd extends Element<Wyrd> {
    public static final String WYRD_ID = "wyrd";
    private static final String WYRD_NAME = "Wyrd";
    private int value = 0;

    public Wyrd(String language, String moduleName) {
        super(WYRD_ID, new TranslatedText(WYRD_NAME), null, language, moduleName);
    }

    public Wyrd(String language, String moduleName, int value) {
        this(language, moduleName);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
