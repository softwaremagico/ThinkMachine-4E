package com.softwaremagico.tm.character.perks;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Element;

public class Perk extends Element {
    private boolean isAffliction = false;
    @JsonProperty("cyberDevice")
    private Integer cyberDeviceTechLevel;

    public boolean isAffliction() {
        return isAffliction;
    }

    public void setAffliction(boolean affliction) {
        isAffliction = affliction;
    }

    public Integer getCyberDeviceTechLevel() {
        return cyberDeviceTechLevel;
    }

    public void setCyberDeviceTechLevel(Integer cyberDeviceTechLevel) {
        this.cyberDeviceTechLevel = cyberDeviceTechLevel;
    }
}
