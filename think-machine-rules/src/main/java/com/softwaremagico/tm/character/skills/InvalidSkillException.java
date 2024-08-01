package com.softwaremagico.tm.character.skills;

import com.softwaremagico.tm.InvalidXmlElementException;

public class InvalidSkillException extends InvalidXmlElementException {
    private static final long serialVersionUID = 9001638102276858558L;

    public InvalidSkillException(String message) {
        super(message);
    }

    public InvalidSkillException(String message, Exception e) {
        super(message, e);
    }
}
