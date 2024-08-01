package com.softwaremagico.tm.character.occultism;


import com.softwaremagico.tm.InvalidXmlElementException;

public class InvalidOccultismTypeException extends InvalidXmlElementException {
	private static final long serialVersionUID = -3441804641445713841L;

	public InvalidOccultismTypeException(String message) {
		super(message);
	}

	public InvalidOccultismTypeException(String message, Exception e) {
		super(message, e);
	}
}
