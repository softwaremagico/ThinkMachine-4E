package com.softwaremagico.tm.character.races;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.log.MachineLog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Race extends Element<Race> {

    private List<RaceCharacteristic> raceCharacteristics;

    private Set<String> blessings = null;
    private Set<String> benefices = null;
    private Set<String> planets = null;

    private int psi;
    private int theurgy;
    private int urge;
    private int hubris;

    private int cost;

    public RaceCharacteristic getParameter(CharacteristicName characteristicName) throws InvalidRaceException {
        return raceCharacteristics.stream().filter(raceCharacteristic -> raceCharacteristic.getCharacteristic() == characteristicName).findFirst()
                .orElseThrow(() -> new InvalidRaceException("Characteristic '" + characteristicName + "' does not exists on race '" + getId() + "'."));
    }

    public void setMaximumValue(CharacteristicName characteristicName, int maxValue) {
        try {
            getParameter(characteristicName).setMaximumValue(maxValue);
        } catch (NullPointerException | InvalidRaceException npe) {
            MachineLog.severe(this.getClass().getName(), "Invalid maximum parameter '{}'.", characteristicName);
        }
    }

    public void setMaximumInitialValue(CharacteristicName characteristicName, int maxValue) {
        try {
            getParameter(characteristicName).setMaximumInitialValue(maxValue);
        } catch (NullPointerException | InvalidRaceException npe) {
            MachineLog.severe(this.getClass().getName(), "Invalid maximum initial parameter '{}'.", characteristicName);
        }
    }

    public void setValue(CharacteristicName characteristicName, int value) {
        try {
            getParameter(characteristicName).setInitialValue(value);
        } catch (NullPointerException | InvalidRaceException npe) {
            MachineLog.severe(this.getClass().getName(), "Invalid value parameter '{}'.", characteristicName);
        }
    }

    public RaceCharacteristic get(CharacteristicName characteristicName) throws InvalidRaceException {
        return getParameter(characteristicName);
    }

    public int getPsi() {
        return psi;
    }

    public int getTheurgy() {
        return theurgy;
    }

    public int getUrge() {
        return urge;
    }

    public int getHubris() {
        return hubris;
    }

    public int getCost() {
        return cost;
    }

    public void setPsi(int psi) {
        this.psi = psi;
    }

    public void setTheurgy(int teurgy) {
        this.theurgy = teurgy;
    }

    public void setUrge(int urge) {
        this.urge = urge;
    }

    public void setHubris(int hubris) {
        this.hubris = hubris;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public boolean isXeno() {
        return !getId().equals("human");
    }

    public Set<String> getBlessings() {
        return blessings;
    }

    public Set<String> getBenefices() {
        return benefices;
    }

    public void setBlessings(String blessingContent) {
        blessings = new HashSet<>();
        readCommaSeparatedTokens(blessings, blessingContent);
    }

    public void setBlessings(Set<String> blessings) {
        this.blessings = blessings;
    }

    public void setBenefices(String beneficesContent) {
        benefices = new HashSet<>();
        readCommaSeparatedTokens(benefices, beneficesContent);
    }

    public void setBenefices(Set<String> benefices) {
        this.benefices = benefices;
    }

    public void setPlanets(String planetsContent) {
        planets = new HashSet<>();
        readCommaSeparatedTokens(planets, planetsContent);
    }

    public Set<String> getPlanets() {
        return planets;
    }

    public void setPlanets(Set<String> planets) {
        this.planets = planets;
    }
}
