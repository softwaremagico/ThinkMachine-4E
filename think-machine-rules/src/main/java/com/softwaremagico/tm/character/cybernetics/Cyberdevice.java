package com.softwaremagico.tm.character.cybernetics;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.equipment.TechCompulsionFactory;
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.character.values.IElementWithBonification;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.Collections;
import java.util.Set;

public class Cyberdevice extends Perk implements IElementWithBonification {
    @JsonProperty("techLevel")
    private Integer techLevel;
    @JsonProperty("techCompulsion")
    private String techCompulsion;
    @JsonProperty("size")
    private Size size;
    @JsonProperty("bonifications")
    private Set<Bonification> bonifications;

    public Integer getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(Integer techLevel) {
        this.techLevel = techLevel;
    }

    public String getTechCompulsion() {
        return techCompulsion;
    }

    public void setTechCompulsion(String techCompulsion) {
        this.techCompulsion = techCompulsion;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public Set<Bonification> getBonifications() {
        if (bonifications == null) {
            return Collections.emptySet();
        }
        return bonifications;
    }

    public void setBonifications(Set<Bonification> bonifications) {
        this.bonifications = bonifications;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (techLevel == null) {
            throw new InvalidXmlElementException("cyberDeviceTechLevel is required");
        }
        if (techCompulsion == null) {
            throw new InvalidXmlElementException("techCompulsion is required");
        }
        TechCompulsionFactory.getInstance().getElement(getTechCompulsion());
    }
}
