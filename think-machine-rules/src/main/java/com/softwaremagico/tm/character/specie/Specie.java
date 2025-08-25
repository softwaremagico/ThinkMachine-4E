package com.softwaremagico.tm.character.specie;

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
import com.softwaremagico.tm.character.CharacterDefinitionStep;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Specie extends CharacterDefinitionStep {

    @JsonProperty("specieCharacteristics")
    private List<SpecieCharacteristic> specieCharacteristics;

    private Set<String> planets = null;

    private List<PerkOption> perks;

    @JsonProperty("primaryCharacteristics")
    private List<String> primaryCharacteristics;

    private int cost;

    private int size;

    public SpecieCharacteristic getSpecieCharacteristic(CharacteristicName characteristicName) throws InvalidSpecieException {
        return specieCharacteristics.stream().filter(specieCharacteristic -> specieCharacteristic.getCharacteristic() == characteristicName).findFirst()
                .orElseThrow(() -> new InvalidSpecieException("Characteristic '" + characteristicName + "' does not exists on race '" + getId() + "'."));
    }

    public void setMaximumValue(CharacteristicName characteristicName, int maxValue) {
        try {
            getSpecieCharacteristic(characteristicName).setMaximumValue(maxValue);
        } catch (NullPointerException | InvalidSpecieException npe) {
            MachineLog.severe(this.getClass().getName(), "Invalid maximum parameter '{}'.", characteristicName);
        }
    }

    public void setMaximumInitialValue(CharacteristicName characteristicName, int maxValue) {
        try {
            getSpecieCharacteristic(characteristicName).setMaximumInitialValue(maxValue);
        } catch (NullPointerException | InvalidSpecieException npe) {
            MachineLog.severe(this.getClass().getName(), "Invalid maximum initial parameter '{}'.", characteristicName);
        }
    }

    public void setValue(CharacteristicName characteristicName, int value) {
        try {
            getSpecieCharacteristic(characteristicName).setInitialValue(value);
        } catch (NullPointerException | InvalidSpecieException npe) {
            MachineLog.severe(this.getClass().getName(), "Invalid value parameter '{}'.", characteristicName);
        }
    }

    public SpecieCharacteristic getSpecieCharacteristic(String characteristicName) throws InvalidSpecieException {
        return getSpecieCharacteristic(CharacteristicName.get(characteristicName));
    }

    public int getCost() {
        return cost;
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

    public List<SpecieCharacteristic> getSpecieCharacteristics() {
        return specieCharacteristics;
    }

    public void setSpecieCharacteristics(List<SpecieCharacteristic> specieCharacteristics) {
        this.specieCharacteristics = specieCharacteristics;
    }

    public List<PerkOption> getPerks() {
        return perks;
    }

    public void setPerks(List<PerkOption> perks) {
        this.perks = perks;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getPrimaryCharacteristics() {
        return primaryCharacteristics;
    }

    public void setPrimaryCharacteristics(List<String> primaryCharacteristics) {
        this.primaryCharacteristics = primaryCharacteristics;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        getFinalPerksOptions().forEach(PerkOptions::validate);
        if (getPrimaryCharacteristics() != null) {
            getPrimaryCharacteristics().forEach(characteristic ->
                    CharacteristicsDefinitionFactory.getInstance().getElement(characteristic));
        }
        if (planets != null) {
            planets.forEach(planet ->
                    PlanetFactory.getInstance().getElement(planet));
        }
        if (specieCharacteristics != null) {
            specieCharacteristics.forEach(SpecieCharacteristic::validate);
        }
    }
}
