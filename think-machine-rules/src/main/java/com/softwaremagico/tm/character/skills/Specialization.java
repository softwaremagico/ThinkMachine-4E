package com.softwaremagico.tm.character.skills;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

public class Specialization extends Element<Specialization> implements ISkillRandomDefintions {

    public Specialization() {
        super();
    }

    public Specialization(String id, TranslatedText name, TranslatedText description, String language, String moduleName) {
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
