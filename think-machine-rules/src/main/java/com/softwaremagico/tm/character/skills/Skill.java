package com.softwaremagico.tm.character.skills;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

public class Skill<T extends Skill<?>> extends Element<T> {

    public Skill() {
        super();
    }

    public Skill(String id, TranslatedText name, TranslatedText description, String language, String moduleName) {
        super(id, name, description, language, moduleName);
    }

    public String getUniqueId() {
        return getId();
    }
}
