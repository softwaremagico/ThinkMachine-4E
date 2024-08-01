package com.softwaremagico.tm.character.occultism;


import com.softwaremagico.tm.InvalidXmlElementException;

public class InvalidOccultismPowerException extends InvalidXmlElementException {
	private static final long serialVersionUID = 3558660253411869827L;

	public InvalidOccultismPowerException(String message) {
		super(message);
	}

	public InvalidOccultismPowerException(String message, Exception e) {
		super(message, e);
	}
}
