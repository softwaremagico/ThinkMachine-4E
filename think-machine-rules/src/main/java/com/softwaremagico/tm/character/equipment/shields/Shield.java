package com.softwaremagico.tm.character.equipment.shields;

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


import com.softwaremagico.tm.character.equipment.Equipment;

public class Shield extends Equipment {
    private int impact;
    private int force;
    private int hits;
    private int burnOut;
    private int distortion;

    /**
     * For creating empty elements.
     */
    public Shield() {
        super();
        this.impact = 0;
        this.force = 0;
        this.hits = 0;
    }

    public int getImpact() {
        return impact;
    }

    public int getForce() {
        return force;
    }

    public int getHits() {
        return hits;
    }

    public void setImpact(int impact) {
        this.impact = impact;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getBurnOut() {
        return burnOut;
    }

    public void setBurnOut(int burnOut) {
        this.burnOut = burnOut;
    }

    public int getDistortion() {
        return distortion;
    }

    public void setDistortion(int distortion) {
        this.distortion = distortion;
    }
}
