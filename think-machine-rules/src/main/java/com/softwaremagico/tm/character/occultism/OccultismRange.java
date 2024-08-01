package com.softwaremagico.tm.character.occultism;


import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

public class OccultismRange extends Element<OccultismRange> {

    public OccultismRange() {
        super();
    }

    public OccultismRange(String id, TranslatedText name, TranslatedText description, String language, String moduleName) {
        super(id, name, description, language, moduleName);
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
