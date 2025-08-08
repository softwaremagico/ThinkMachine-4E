package com.softwaremagico.tm.random.character.selectors;

import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

public interface AssignableRandomSelector {

    void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException;
}
