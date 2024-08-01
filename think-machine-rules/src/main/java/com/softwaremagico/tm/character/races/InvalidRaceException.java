package com.softwaremagico.tm.character.races;


import com.softwaremagico.tm.InvalidXmlElementException;

import java.io.Serial;

public class InvalidRaceException extends InvalidXmlElementException {
    @Serial
    private static final long serialVersionUID = 1243337930745480002L;

    public InvalidRaceException(String message) {
        super(message);
    }

    public InvalidRaceException(String message, Exception e) {
        super(message, e);
    }
}
