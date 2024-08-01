package com.softwaremagico.tm.character.occultism;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.ElementClassification;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.factions.FactionGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OccultismPath extends Element<OccultismPath> {

    @JsonProperty("occultismType")
    private String occultismType;
    @JsonProperty("powers")
    private Set<OccultismPower> occultismPowersElements;
    @JsonIgnore
    private Map<String, OccultismPower> occultismPowers;
    @JsonProperty("factions")
    private Set<String> factionsAllowed;
    @JsonProperty("classification")
    private ElementClassification classification;

    public OccultismPath() {
        super();
    }

    public OccultismPath(String id, TranslatedText name, TranslatedText description, String language, String moduleName, String occultismType,
                         Set<String> allowedFactions, ElementClassification classification) {
        super(id, name, description, language, moduleName);
        this.occultismType = occultismType;
        this.factionsAllowed = allowedFactions;
        this.classification = classification;
    }

    public String getOccultismType() {
        return occultismType;
    }

    public Map<String, OccultismPower> getOccultismPowers() {
        if (occultismPowers == null) {
            occultismPowers = occultismPowersElements.stream().collect(Collectors.toMap(OccultismPower::getId, item -> item));
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
        final List<OccultismPower> powersOfPath = new ArrayList<>(occultismPowers.values());

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

    public ElementClassification getClassification() {
        return classification;
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

    public void setClassification(ElementClassification classification) {
        this.classification = classification;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void setRestrictedToRaces(String restrictedToRacesContent) {
        readCommaSeparatedTokens(super.getRestrictedToRaces(), restrictedToRacesContent);
    }

    @Override
    public void setRestrictedToRaces(Set<String> restrictedToRaces) {
        super.setRestrictedToRaces(restrictedToRaces);
        occultismPowers.forEach((s, occultismPower) -> occultismPower.setRestrictedToRaces(restrictedToRaces));
    }

    public void setRestrictedToFactions(String restrictedToFactionsContent) {
        readCommaSeparatedTokens(super.getRestrictedToFactions(), restrictedToFactionsContent);
    }

    @Override
    public void setRestrictedToFactions(Set<String> restrictedToFactions) {
        super.setRestrictedToFactions(restrictedToFactions);
        occultismPowers.forEach((s, occultismPower) -> occultismPower.setRestrictedToFactions(restrictedToFactions));
    }

    @Override
    public void setRestricted(boolean restricted) {
        super.setRestricted(restricted);
        occultismPowers.forEach((s, occultismPower) -> occultismPower.setRestricted(restricted));
    }

    @Override
    public void setRestrictedToFactionGroup(FactionGroup factionGroup) {
        super.setRestrictedToFactionGroup(factionGroup);
        occultismPowers.forEach((s, occultismPower) -> occultismPower.setRestrictedToFactionGroup(factionGroup));
    }

    @Override
    public void setOfficial(boolean official) {
        super.setOfficial(official);
        occultismPowers.forEach((s, occultismPower) -> occultismPower.setOfficial(official));
    }
}
