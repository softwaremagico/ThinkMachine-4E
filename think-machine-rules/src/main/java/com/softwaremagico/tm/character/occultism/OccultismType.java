package com.softwaremagico.tm.character.occultism;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.values.IValue;


public class OccultismType extends Element<OccultismType> implements IValue {
    private String darksideName;

    public OccultismType() {
        super();
    }

    public OccultismType(String id, TranslatedText name, TranslatedText description, String language, String moduleName, String darksideName) {
        super(id, name, description, language, moduleName);
        this.darksideName = darksideName;
    }

    public String getDarkSideName() {
        return darksideName;
    }

    public void setDarksideName(String darksideName) {
        this.darksideName = darksideName;
    }
}
