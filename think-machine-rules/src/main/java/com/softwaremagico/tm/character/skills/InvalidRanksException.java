package com.softwaremagico.tm.character.skills;

public class InvalidRanksException extends Exception {
    private static final long serialVersionUID = 9001638112276858558L;

    public InvalidRanksException(String message) {
        super(message);
    }

    public InvalidRanksException(String message, Exception e) {
        super(message, e);
    }
}
