package com.softwaremagico.tm.character.characteristics;


import com.softwaremagico.tm.InvalidXmlElementException;

public class InvalidCharacteristicException extends InvalidXmlElementException {
    private static final long serialVersionUID = 1243337930745480002L;

    public InvalidCharacteristicException(String message) {
        super(message);
    }
}
