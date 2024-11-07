package com.softwaremagico.tm.character.occultism;

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


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.Time;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OccultismPath extends Element {

    @JsonProperty("occultismType")
    private String occultismType;
    @JsonProperty("powers")
    private List<OccultismPower> occultismPowersElements;
    @JsonIgnore
    private Map<String, OccultismPower> occultismPowers;
    @JsonProperty("factions")
    private Set<String> factionsAllowed;
    @JsonAlias({"elementalUse", "charismata"})
    private TranslatedText elementalUse;
    @JsonProperty("time")
    private Time time;
    @JsonProperty("cost")
    private TranslatedText cost;
    @JsonProperty("components")
    private List<String> componentsCodes;

    public OccultismPath() {
        super();
    }

    public String getOccultismType() {
        return occultismType;
    }

    public Map<String, OccultismPower> getOccultismPowers() {
        if (occultismPowers == null) {
            if (occultismPowersElements != null) {
                occultismPowers = occultismPowersElements.stream().collect(Collectors.toMap(OccultismPower::getId, item -> item));
            } else {
                occultismPowers = new HashMap<>();
            }
        }
        return occultismPowers;
    }

    /**
     * Gets the previous level powers form a power. At least one of them must be
     * acquired to purchase this power if it is a psi path.
     *
     * @param power the power that has one level more than the previous one
     * @return A set with one or more powers for one level.
     */
    public Set<OccultismPower> getPreviousLevelPowers(OccultismPower power) {
        final Integer previousLevel = getPreviousLevelWithPowers(power);
        if (previousLevel != null) {
            return getPowersOfLevel(previousLevel);
        }
        return new HashSet<>();
    }

    private Integer getPreviousLevelWithPowers(OccultismPower power) {
        final List<OccultismPower> powersOfPath = new ArrayList<>(getOccultismPowers().values());

        // Sort by level inverse.
        powersOfPath.sort((power0, power1) -> {
            if (power0.getLevel() != power1.getLevel()) {
                return power1.getLevel() - power0.getLevel();
            }
            return power1.compareTo(power0);
        });

        // From up to down.
        for (final OccultismPower next : powersOfPath) {
            if (next.getLevel() < power.getLevel()) {
                return next.getLevel();
            }
        }
        return null;
    }

    public Set<OccultismPower> getPowersOfLevel(int level) {
        final Set<OccultismPower> powersOfLevel = new HashSet<>();
        for (final OccultismPower power : getOccultismPowers().values()) {
            if (power.getLevel() == level) {
                powersOfLevel.add(power);
            }
        }
        return powersOfLevel;
    }

    public Set<String> getFactionsAllowed() {
        return factionsAllowed;
    }

    public void setOccultismType(String occultismType) {
        this.occultismType = occultismType;
    }

    public void setOccultismPowers(Map<String, OccultismPower> occultismPowers) {
        this.occultismPowers = occultismPowers;
    }

    public void setFactionsAllowed(String factionsAllowedContent) {
        factionsAllowed = new HashSet<>();
        readCommaSeparatedTokens(factionsAllowed, factionsAllowedContent);
    }

    public void setFactionsAllowed(Set<String> factionsAllowed) {
        this.factionsAllowed = factionsAllowed;
    }

    public List<OccultismPower> getOccultismPowersElements() {
        return occultismPowersElements;
    }

    public void setOccultismPowersElements(List<OccultismPower> occultismPowersElements) {
        this.occultismPowersElements = occultismPowersElements;
    }

    public void setRestrictedToSpecies(String restrictedToSpeciesContent) {
        readCommaSeparatedTokens(super.getRestrictions().getRestrictedToSpecies(), restrictedToSpeciesContent);
    }

    public void setRestrictedToSpecies(Set<String> restrictedToSpecies) {
        super.getRestrictions().setRestrictedToSpecies(restrictedToSpecies);
    }

    public void setRestrictedToFactions(String restrictedToFactionsContent) {
        readCommaSeparatedTokens(super.getRestrictions().getRestrictedToFactions(), restrictedToFactionsContent);
    }


    public void setRestrictedToFactions(Set<String> restrictedToFactions) {
        super.getRestrictions().setRestrictedToFactions(restrictedToFactions);
    }


    public void setRestrictedToFactionGroup(Set<String> upbringings) {
        super.getRestrictions().setRestrictedToUpbringing(upbringings);
    }

    public TranslatedText getElementalUse() {
        return elementalUse;
    }

    public void setElementalUse(TranslatedText elementalUse) {
        this.elementalUse = elementalUse;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public TranslatedText getCost() {
        return cost;
    }

    public void setCost(TranslatedText cost) {
        this.cost = cost;
    }

    public List<String> getComponentsCodes() {
        return componentsCodes;
    }

    public void setComponentsCodes(List<String> componentsCodes) {
        this.componentsCodes = componentsCodes;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        OccultismTypeFactory.getInstance().getElement(occultismType);
        occultismPowersElements.forEach(OccultismPower::validate);
        FactionFactory.getInstance().getElements(factionsAllowed);
    }
}
