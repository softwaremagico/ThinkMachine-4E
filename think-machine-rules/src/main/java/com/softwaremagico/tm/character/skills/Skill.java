package com.softwaremagico.tm.character.skills;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2024 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.values.IValue;

public class Skill extends SkillDefinition<Skill> implements ISkillRandomDefintions, IValue {
    public static final int NATURAL_SKILL_INITIAL_VALUE = 3;
    public static final int SKILL_MAX_VALUE = 10;

    public Skill() {
        super();
    }

    public Skill(String id, TranslatedText name, TranslatedText description, String language, String moduleName) {
        super(id, name, description, language, moduleName);
    }

    public boolean isNatural() {
        return getRestrictions().isOpen();
    }

}
