package com.softwaremagico.tm.character.factions;


import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

public class FactionRankTranslation extends Element<FactionRankTranslation> {

    public FactionRankTranslation() {
        super();
    }

    public FactionRankTranslation(String id, TranslatedText name, String language, String moduleName) {
        super(id, name, null, language, moduleName);
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
