package com.softwaremagico.tm.random.exceptions;


public class InvalidRandomElementSelectedException extends Exception {

    private static final long serialVersionUID = 1700452257397096508L;

    public InvalidRandomElementSelectedException(String message) {
        super(message);
    }

    public InvalidRandomElementSelectedException(String message, Throwable e) {
        super(message, e);
    }
}
