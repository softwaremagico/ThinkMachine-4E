package com.softwaremagico.tm.character.occultism;


import com.softwaremagico.tm.InvalidXmlElementException;

public class InvalidOccultismRangeException extends InvalidXmlElementException {
	private static final long serialVersionUID = 3558660253411869827L;

	public InvalidOccultismRangeException(String message) {
		super(message);
	}

	public InvalidOccultismRangeException(String message, Exception e) {
		super(message, e);
	}
}
