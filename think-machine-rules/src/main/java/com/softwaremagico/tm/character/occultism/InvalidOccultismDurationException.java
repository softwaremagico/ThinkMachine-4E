package com.softwaremagico.tm.character.occultism;


import com.softwaremagico.tm.InvalidXmlElementException;

public class InvalidOccultismDurationException extends InvalidXmlElementException {
	private static final long serialVersionUID = 3558660253411869827L;

	public InvalidOccultismDurationException(String message) {
		super(message);
	}

	public InvalidOccultismDurationException(String message, Exception e) {
		super(message, e);
	}
}
