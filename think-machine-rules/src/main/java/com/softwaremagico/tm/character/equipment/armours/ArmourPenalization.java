package com.softwaremagico.tm.character.equipment.armours;

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

public class ArmourPenalization {
    @JsonProperty("dexterity")
    private int dexterityModification;
    @JsonProperty("strength")
    private int strengthModification;
    @JsonProperty("initiative")
    private int initiativeModification;
    @JsonProperty("endurance")
    private int enduranceModification;

    public ArmourPenalization() {
        super();
    }

    public ArmourPenalization(int dexterityModification, int strengthModification, int initiativeModification, int enduranceModification) {
        this.dexterityModification = dexterityModification;
        this.strengthModification = strengthModification;
        this.initiativeModification = initiativeModification;
        this.enduranceModification = enduranceModification;
    }

    public int getEnduranceModification() {
        return enduranceModification;
    }

    public int getDexterityModification() {
        return dexterityModification;
    }

    public int getStrengthModification() {
        return strengthModification;
    }

    public int getInitiativeModification() {
        return initiativeModification;
    }

    public void setDexterityModification(int dexterityModification) {
        this.dexterityModification = dexterityModification;
    }

    public void setStrengthModification(int strengthModification) {
        this.strengthModification = strengthModification;
    }

    public void setInitiativeModification(int initiativeModification) {
        this.initiativeModification = initiativeModification;
    }

    public void setEnduranceModification(int enduranceModification) {
        this.enduranceModification = enduranceModification;
    }
}
