package com.softwaremagico.tm.character.cybernetics;

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
import com.softwaremagico.tm.ElementClassification;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.equipment.IElementWithTechnologyLevel;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.character.values.IElementWithBonification;
import com.softwaremagico.tm.character.values.StaticValue;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CyberneticDevice extends Element<CyberneticDevice> implements IElementWithBonification, ICyberneticDevice, IElementWithTechnologyLevel {
    private int points;
    private int incompatibility;
    private int cost;
    private int techLevel;
    @JsonProperty("requires")
    private String requirement;
    private String weapon;
    private List<String> traits;
    private Set<Bonification> bonifications;
    @JsonProperty("skillsStaticValues")
    private Set<StaticValue> staticValues;
    private ElementClassification classification;

    /**
     * For creating empty elements.
     */
    public CyberneticDevice() {
        super();
        this.points = 0;
        this.incompatibility = 0;
        this.cost = 0;
        this.techLevel = 0;
        this.traits = new ArrayList<>();
        this.requirement = null;
        this.weapon = null;
        this.bonifications = new HashSet<>();
        this.staticValues = new HashSet<>();
        this.classification = ElementClassification.OTHERS;
    }

    public CyberneticDevice(String id, TranslatedText name, TranslatedText description, String language, String moduleName, int points,
                            int incompatibility, int cost, int techLevel, String requirement, String weapon,
                            Set<String> traits, Set<Bonification> bonifications, Set<StaticValue> staticValues,
                            ElementClassification classification) {
        super(id, name, description, language, moduleName);
        this.points = points;
        this.incompatibility = incompatibility;
        this.cost = cost;
        this.techLevel = techLevel;
        this.traits = new ArrayList<>(traits);
        Collections.sort(this.traits);
        this.requirement = requirement;
        this.weapon = weapon;
        this.bonifications = bonifications;
        this.staticValues = staticValues;
        this.classification = classification;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public int getIncompatibility() {
        return incompatibility;
    }

    @Override
    public String getTrait(CyberneticDeviceTraitCategory category) {
        for (final String trait : traits) {
            try {
                if (CyberneticDeviceTraitFactory.getInstance().getElement(trait).getCategory().equals(category)) {
                    return trait;
                }
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass(), e);
            }
        }
        return null;
    }

    @Override
    public String getRequirement() {
        return requirement;
    }

    @Override
    public String getWeapon() {
        return weapon;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getTechLevel() {
        return techLevel;
    }

    @Override
    public Set<Bonification> getBonifications() {
        return bonifications;
    }

    @Override
    public Set<StaticValue> getStaticValues() {
        return staticValues;
    }

    @Override
    public List<String> getTraits() {
        return traits;
    }

    public ElementClassification getClassification() {
        return classification;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setIncompatibility(int incompatibility) {
        this.incompatibility = incompatibility;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setTechLevel(int techLevel) {
        this.techLevel = techLevel;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public void setTraits(String traitsContent) {
        this.traits = new ArrayList<>();
        readCommaSeparatedTokens(traits, traitsContent);
    }

    public void setTraits(List<String> traits) {
        this.traits = traits;
    }

    public void setBonifications(Set<Bonification> bonifications) {
        this.bonifications = bonifications;
    }

    public void setStaticValues(Set<StaticValue> staticValues) {
        this.staticValues = staticValues;
    }

    public void setClassification(ElementClassification classification) {
        this.classification = classification;
    }
}
