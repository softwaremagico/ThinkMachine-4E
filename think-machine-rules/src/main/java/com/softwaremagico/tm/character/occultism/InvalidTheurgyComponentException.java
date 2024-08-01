package com.softwaremagico.tm.character.occultism;


import com.softwaremagico.tm.InvalidXmlElementException;

public class InvalidTheurgyComponentException extends InvalidXmlElementException {
	private static final long serialVersionUID = -3441804641445713841L;

	public InvalidTheurgyComponentException(String message) {
		super(message);
	}

	public InvalidTheurgyComponentException(String message, Exception e) {
		super(message, e);
	}
}
