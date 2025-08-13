package com.softwaremagico.tm.random.definition;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.XmlData;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.equipment.Agora;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RandomElementDefinition extends XmlData {

    private ElementClassification classification;
    private Integer staticProbability;
    private Integer minimumTechLevel;
    private Integer maximumTechLevel;
    private ProbabilityMultiplier probabilityMultiplier = ProbabilityMultiplier.NORMAL;
    private Set<String> recommendedFactions = new HashSet<>();
    private Set<String> recommendedFactionsGroups = new HashSet<>();
    private Set<String> recommendedSpecies = new HashSet<>();
    private Set<String> recommendedUpbringings = new HashSet<>();
    private Set<String> forbiddenPreferences = new HashSet<>();
    private Set<String> restrictedPreferences = new HashSet<>();
    private Set<String> recommendedPreferences = new HashSet<>();
    private RandomProbabilityDefinition probability;

    @JsonProperty("names")
    private Names namesElements;
    @JsonProperty("surnames")
    private String surnameElements;

    @JsonIgnore
    private Set<Name> names;
    @JsonIgnore
    private Set<Surname> surnames;

    public RandomElementDefinition() {
        super();
    }

    public RandomElementDefinition(RandomElementDefinition... randomDefinitions) {
        this();
        for (final RandomElementDefinition randomDefinition : randomDefinitions) {
            update(randomDefinition);
        }
    }

    private void update(RandomElementDefinition randomDefinition) {
        if (randomDefinition.getStaticProbability() != null) {
            setStaticProbability(randomDefinition.getStaticProbability());
        }
        if (randomDefinition.getMinimumTechLevel() != null) {
            setMinimumTechLevel(randomDefinition.getMinimumTechLevel());
        }
        if (randomDefinition.getMaximumTechLevel() != null) {
            setMaximumTechLevel(randomDefinition.getMaximumTechLevel());
        }
        if (randomDefinition.getProbabilityMultiplier() != null) {
            setProbabilityMultiplier(randomDefinition.getProbabilityMultiplier());
        }
        if (randomDefinition.getRecommendedFactions() != null && !randomDefinition.getRecommendedFactions().isEmpty()) {
            recommendedFactions.clear();
            recommendedFactions.addAll(randomDefinition.getRecommendedFactions());
        }
        if (randomDefinition.getRecommendedFactionsGroups() != null && !randomDefinition.getRecommendedFactionsGroups().isEmpty()) {
            recommendedFactionsGroups.clear();
            recommendedFactionsGroups.addAll(randomDefinition.getRecommendedFactions());
        }
        if (randomDefinition.getRecommendedSpecies() != null && !randomDefinition.getRecommendedSpecies().isEmpty()) {
            recommendedSpecies.clear();
            recommendedSpecies.addAll(randomDefinition.getRecommendedSpecies());
        }
        if (randomDefinition.getRecommendedUpbringings() != null && !randomDefinition.getRecommendedUpbringings().isEmpty()) {
            recommendedUpbringings.clear();
            recommendedUpbringings.addAll(randomDefinition.getRecommendedUpbringings());
        }
        if (randomDefinition.getForbiddenPreferences() != null && !randomDefinition.getForbiddenPreferences().isEmpty()) {
            forbiddenPreferences.clear();
            forbiddenPreferences.addAll(randomDefinition.getForbiddenPreferences());
        }
        if (randomDefinition.getRestrictedPreferences() != null && !randomDefinition.getRestrictedPreferences().isEmpty()) {
            restrictedPreferences.clear();
            restrictedPreferences.addAll(randomDefinition.getRestrictedPreferences());
        }
        if (randomDefinition.getRecommendedPreferences() != null && !randomDefinition.getRecommendedPreferences().isEmpty()) {
            recommendedPreferences.clear();
            recommendedPreferences.addAll(randomDefinition.getRecommendedPreferences());
        }
        if (randomDefinition.getProbability() != null) {
            setProbability(randomDefinition.getProbability());
        }
    }

    public Integer getMinimumTechLevel() {
        return Objects.requireNonNullElse(minimumTechLevel, 0);
    }

    public void setMinimumTechLevel(Integer minimumTechLevel) {
        this.minimumTechLevel = minimumTechLevel;
    }

    public Set<String> getRecommendedFactions() {
        return recommendedFactions;
    }

    public void addRecommendedFactionGroup(String recommendedFactionGroup) {
        if (recommendedFactionGroup != null) {
            recommendedUpbringings.add(recommendedFactionGroup);
        }
    }

    public void addRecommendedFaction(String faction) {
        if (faction != null) {
            recommendedFactions.add(faction);
        }
    }

    public Set<String> getRecommendedFactionsGroups() {
        return recommendedFactionsGroups;
    }

    public void setRecommendedFactionsGroups(Set<String> recommendedFactionsGroups) {
        this.recommendedFactionsGroups = recommendedFactionsGroups;
    }

    public RandomProbabilityDefinition getProbability() {
        return probability;
    }

    public void setProbability(RandomProbabilityDefinition probability) {
        this.probability = probability;
    }

    public void addRecommendedSpecie(String specie) {
        if (specie != null) {
            recommendedSpecies.add(specie);
        }
    }

    public Set<String> getRecommendedSpecies() {
        return recommendedSpecies;
    }

    public Integer getMaximumTechLevel() {
        return Objects.requireNonNullElse(maximumTechLevel, Characteristic.MAX_VALUE);
    }

    public void setMaximumTechLevel(Integer maximumTechLevel) {
        this.maximumTechLevel = maximumTechLevel;
    }

    public Integer getStaticProbability() {
        return staticProbability;
    }

    public void setStaticProbability(Integer staticProbability) {
        this.staticProbability = staticProbability;
    }

    public ProbabilityMultiplier getProbabilityMultiplier() {
        return probabilityMultiplier;
    }

    public Set<String> getForbiddenPreferences() {
        return forbiddenPreferences;
    }

    public void setForbiddenPreferences(Set<String> forbiddenPreferences) {
        this.forbiddenPreferences = forbiddenPreferences;
    }

    public Set<String> getRestrictedPreferences() {
        return restrictedPreferences;
    }

    public void setRestrictedPreferences(Set<String> restrictedPreferences) {
        this.restrictedPreferences = restrictedPreferences;
    }

    public Set<String> getRecommendedPreferences() {
        return recommendedPreferences;
    }

    public void setRecommendedPreferences(Set<String> recommendedPreferences) {
        this.recommendedPreferences = recommendedPreferences;
    }

    public void setAgoraProbabilityMultiplier(Agora agora) {
        if (agora != null && getProbabilityMultiplier() != ProbabilityMultiplier.NORMAL) {
            switch (agora) {
                case COMMON:
                case KNOWN_WORLDS:
                    this.probabilityMultiplier = ProbabilityMultiplier.COMMON;
                    break;
                case RARE:
                    this.probabilityMultiplier = ProbabilityMultiplier.RARE;
                    break;
                case EXOTIC:
                    this.probabilityMultiplier = ProbabilityMultiplier.EXOTIC;
                    break;
                default:
                    this.probabilityMultiplier = ProbabilityMultiplier.NORMAL;
                    break;
            }
        }
    }

    public void setProbabilityMultiplier(ProbabilityMultiplier probabilityMultiplier) {
        if (probabilityMultiplier != null) {
            this.probabilityMultiplier = probabilityMultiplier;
        }
    }

    public void setRecommendedFactions(String recommendedFactionsContent) {
        recommendedFactions = new HashSet<>();
        readCommaSeparatedTokens(recommendedFactions, recommendedFactionsContent);
    }

    public void setRecommendedFactions(Set<String> recommendedFactions) {
        this.recommendedFactions = recommendedFactions;
    }

    public void setRecommendedSpecies(String recommendedSpeciesContent) {
        recommendedSpecies = new HashSet<>();
        readCommaSeparatedTokens(recommendedSpecies, recommendedSpeciesContent);
    }

    public void setRecommendedSpecies(Set<String> recommendedSpecies) {
        this.recommendedSpecies = recommendedSpecies;
    }

    public Set<String> getRecommendedUpbringings() {
        return recommendedUpbringings;
    }

    public void setRecommendedUpbringings(Set<String> recommendedUpbringings) {
        this.recommendedUpbringings = recommendedUpbringings;
    }

    public ElementClassification getClassification() {
        return classification;
    }

    public void setClassification(ElementClassification classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return minimumTechLevel + "";
    }

    public Set<Surname> getSurnames(String faction) {
        if (surnames == null) {
            surnames = new HashSet<>();
            if (surnameElements != null) {
                Arrays.stream(surnameElements.split(",")).forEach(s ->
                        surnames.add(new Surname(s.trim(), faction))
                );
            }
        }
        return surnames;
    }

    public Set<Name> getNames(String faction, Gender gender) {
        if (names == null) {
            names = new HashSet<>();
            if (gender == Gender.MALE && namesElements != null) {
                Arrays.stream(namesElements.getMaleNames().split(",")).forEach(s ->
                        names.add(new Name(s.trim(), gender, faction))
                );
            }

        }
        return names;
    }
}
