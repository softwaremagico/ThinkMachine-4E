package com.softwaremagico.tm.character.cache;

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

public class CacheManager {


    private Double cash;

    private Integer techLevel;

    public void reset() {
        cash = null;
        techLevel = null;
    }

    public void capabilitiesChanged() {
        cash = null;
        techLevel = null;
    }

    public void perksChanged() {

    }

    public void skillsChanged() {

    }

    public void characteristicsChanged() {

    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public Integer getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(Integer techLevel) {
        this.techLevel = techLevel;
    }
}
