package com.softwaremagico.tm.character.values;

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


import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillDefinitionFactory;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

public class Bonification {
    private Integer value;
    private IValue affects;
    private String situation;

    public Bonification() {
    }

    public Bonification(Integer value, IValue affects, String situation) {
        this.value = value;
        this.affects = affects;
        this.situation = situation;
    }

    public Integer getValue() {
        return value;
    }

    public String getSituation() {
        return situation;
    }

    public IValue getAffects() {
        return affects;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setAffects(String affectsContent) {
        try {
            if (AvailableSkillsFactory.getInstance().getElement(affectsContent) != null) {
                this.affects = AvailableSkillsFactory.getInstance().getElement(affectsContent);
            } else if (SkillDefinitionFactory.getInstance().getElement(affectsContent) != null) {
                this.affects = SkillDefinitionFactory.getInstance().getElement(affectsContent);
            } else if (CharacteristicsDefinitionFactory.getInstance().getElement(affectsContent) != null) {
                this.affects = CharacteristicsDefinitionFactory.getInstance().getElement(affectsContent);
            }
            //TODO(softwaremagico): add new IValues types when implemented.
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(this.getClass().getName(), e.getMessage());
        }
    }

    public void setAffects(IValue affects) {
        this.affects = affects;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    @Override
    public String toString() {
        return value + " " + affects + ": " + situation;
    }
}
