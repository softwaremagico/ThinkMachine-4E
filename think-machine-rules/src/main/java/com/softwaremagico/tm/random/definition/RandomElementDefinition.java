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
    private static final double COMMON_PROBABILITY = 100d;
    private static final double RARE_PROBABILITY = 10d;
    private static final double EXOTIC_PROBABILITY = 1d;

    private ElementClassification classification;
    private Integer staticProbability;
    private Integer minimumTechLevel;
    private Integer maximumTechLevel;
    private Double probabilityMultiplier;
    private Set<String> restrictedFactions = new HashSet<>();
    private Set<String> recommendedFactions = new HashSet<>();
    private Set<String> recommendedFactionsGroups = new HashSet<>();
    private Set<String> restrictedFactionsGroups = new HashSet<>();
    private Set<String> forbiddenSpecies = new HashSet<>();
    private Set<String> restrictedSpecies = new HashSet<>();
    private Set<String> recommendedSpecies = new HashSet<>();
    private Set<String> restrictedUpbringing = new HashSet<>();
    private Set<String> recommendedUpbringings = new HashSet<>();
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
        if (randomDefinition.getRestrictedFactions() != null && !randomDefinition.getRestrictedFactions().isEmpty()) {
            restrictedFactions.clear();
            restrictedFactions.addAll(randomDefinition.getRestrictedFactions());
        }
        if (randomDefinition.getRecommendedFactions() != null && !randomDefinition.getRecommendedFactions().isEmpty()) {
            recommendedFactions.clear();
            recommendedFactions.addAll(randomDefinition.getRecommendedFactions());
        }
        if (randomDefinition.getRestrictedFactionsGroups() != null && !randomDefinition.getRestrictedFactionsGroups().isEmpty()) {
            restrictedFactionsGroups.clear();
            restrictedFactionsGroups.addAll(randomDefinition.getRestrictedFactionsGroups());
        }
        if (randomDefinition.getRecommendedFactionsGroups() != null && !randomDefinition.getRecommendedFactionsGroups().isEmpty()) {
            recommendedFactionsGroups.clear();
            recommendedFactionsGroups.addAll(randomDefinition.getRecommendedFactions());
        }
        if (randomDefinition.getRecommendedSpecies() != null && !randomDefinition.getRecommendedSpecies().isEmpty()) {
            recommendedSpecies.clear();
            recommendedSpecies.addAll(randomDefinition.getRecommendedSpecies());
        }
        if (randomDefinition.getForbiddenSpecies() != null && !randomDefinition.getForbiddenSpecies().isEmpty()) {
            forbiddenSpecies.clear();
            forbiddenSpecies.addAll(randomDefinition.getForbiddenSpecies());
        }
        if (randomDefinition.getRestrictedSpecies() != null && !randomDefinition.getRestrictedSpecies().isEmpty()) {
            restrictedSpecies.clear();
            restrictedSpecies.addAll(randomDefinition.getRestrictedSpecies());
        }
        if (randomDefinition.getRestrictedUpbringing() != null && !randomDefinition.getRestrictedUpbringing().isEmpty()) {
            restrictedUpbringing.clear();
            restrictedUpbringing.addAll(randomDefinition.getRestrictedUpbringing());
        }
        if (randomDefinition.getRecommendedUpbringings() != null && !randomDefinition.getRecommendedUpbringings().isEmpty()) {
            recommendedUpbringings.clear();
            recommendedUpbringings.addAll(randomDefinition.getRecommendedUpbringings());
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

    public void addRecommendedSpecies(String race) {
        if (race != null) {
            restrictedSpecies.add(race);
        }
    }

    public Set<String> getRecommendedFactionsGroups() {
        return recommendedFactionsGroups;
    }

    public void setRecommendedFactionsGroups(Set<String> recommendedFactionsGroups) {
        this.recommendedFactionsGroups = recommendedFactionsGroups;
    }

    public Set<String> getRestrictedFactionsGroups() {
        return restrictedFactionsGroups;
    }

    public void setRestrictedFactionsGroups(Set<String> restrictedFactionsGroups) {
        this.restrictedFactionsGroups = restrictedFactionsGroups;
    }

    public RandomProbabilityDefinition getProbability() {
        return probability;
    }

    public void setProbability(RandomProbabilityDefinition probability) {
        this.probability = probability;
    }

    public void addRecommendedRace(String race) {
        if (race != null) {
            recommendedSpecies.add(race);
        }
    }

    public Set<String> getRecommendedSpecies() {
        return recommendedSpecies;
    }

    public Integer getMaximumTechLevel() {
        if (maximumTechLevel == null) {
            return Characteristic.MAX_VALUE;
        }
        return maximumTechLevel;
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

    public Double getProbabilityMultiplier() {
        return Objects.requireNonNullElse(probabilityMultiplier, 1d);
    }

    public void setAgoraProbabilityMultiplier(Agora agora) {
        if (agora != null) {
            switch (agora) {
                case COMMON:
                case KNOWN_WORLDS:
                    this.probabilityMultiplier = COMMON_PROBABILITY;
                    break;
                case RARE:
                    this.probabilityMultiplier = RARE_PROBABILITY;
                    break;
                case EXOTIC:
                    this.probabilityMultiplier = EXOTIC_PROBABILITY;
                    break;
                default:
                    break;
            }
        }
    }

    public void setProbabilityMultiplier(Double probabilityMultiplier) {
        if (probabilityMultiplier != null) {
            this.probabilityMultiplier = probabilityMultiplier;
        }
    }

    public Set<String> getRestrictedFactions() {
        return restrictedFactions;
    }

    public void addRestrictedFactionGroup(String restrictedFactionGroup) {
        if (restrictedFactionGroup != null) {
            restrictedUpbringing.add(restrictedFactionGroup);
        }
    }

    public void addRestrictedRace(String restrictedRace) {
        if (restrictedRace != null) {
            restrictedSpecies.add(restrictedRace);
        }
    }

    public Set<String> getRestrictedSpecies() {
        return restrictedSpecies;
    }

    public void addForbiddenRace(String forbiddenRace) {
        if (forbiddenRace != null) {
            forbiddenSpecies.add(forbiddenRace);
        }
    }

    public Set<String> getForbiddenSpecies() {
        return forbiddenSpecies;
    }

    public Set<String> getRestrictedUpbringing() {
        return restrictedUpbringing;
    }

    public void setRestrictedFactions(String restrictedFactionsContent) {
        restrictedFactions = new HashSet<>();
        readCommaSeparatedTokens(restrictedFactions, restrictedFactionsContent);
    }

    public void setRestrictedFactions(Set<String> restrictedFactions) {
        this.restrictedFactions = restrictedFactions;
    }

    public void setRecommendedFactions(String recommendedFactionsContent) {
        recommendedFactions = new HashSet<>();
        readCommaSeparatedTokens(recommendedFactions, recommendedFactionsContent);
    }

    public void setRecommendedFactions(Set<String> recommendedFactions) {
        this.recommendedFactions = recommendedFactions;
    }

    public void setForbiddenSpecies(String forbiddenSpeciesContent) {
        forbiddenSpecies = new HashSet<>();
        readCommaSeparatedTokens(forbiddenSpecies, forbiddenSpeciesContent);
    }

    public void setForbiddenSpecies(Set<String> forbiddenSpecies) {
        this.forbiddenSpecies = forbiddenSpecies;
    }

    public void setRestrictedSpecies(String restrictedSpeciesContent) {
        restrictedSpecies = new HashSet<>();
        readCommaSeparatedTokens(restrictedSpecies, restrictedSpeciesContent);
    }

    public void setRestrictedSpecies(Set<String> restrictedSpecies) {
        this.restrictedSpecies = restrictedSpecies;
    }

    public void setRecommendedSpecies(String recommendedSpeciesContent) {
        recommendedSpecies = new HashSet<>();
        readCommaSeparatedTokens(recommendedSpecies, recommendedSpeciesContent);
    }

    public void setRecommendedSpecies(Set<String> recommendedSpecies) {
        this.recommendedSpecies = recommendedSpecies;
    }

    public void setRestrictedFactionGroups(Set<String> restrictedUpbringing) {
        this.restrictedUpbringing = restrictedUpbringing;
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
            Arrays.stream(surnameElements.split(",")).forEach(s ->
                    surnames.add(new Surname(s.trim(), faction))
            );
        }
        return surnames;
    }

    public Set<Name> getNames(String faction, Gender gender) {
        if (names == null) {
            names = new HashSet<>();
            if (gender == Gender.MALE) {
                Arrays.stream(namesElements.getMaleNames().split(",")).forEach(s ->
                        names.add(new Name(s.trim(), gender, faction))
                );
            }
        }
        return names;
    }
}
