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

import com.softwaremagico.tm.XmlData;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.factions.FactionGroup;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class RandomElementDefinition extends XmlData {
    private Integer staticProbability;
    private Integer minimumTechLevel;
    private Integer maximumTechLevel;
    private Double probabilityMultiplier;
    private Set<String> restrictedFactions = new HashSet<>();
    private Set<String> recommendedFactions = new HashSet<>();
    private Set<String> forbiddenSpecies = new HashSet<>();
    private Set<String> restrictedSpecies = new HashSet<>();
    private Set<String> recommendedSpecies = new HashSet<>();
    private Set<FactionGroup> restrictedFactionGroups = new HashSet<>();
    private Set<FactionGroup> recommendedFactionGroups = new HashSet<>();
    private RandomProbabilityDefinition probability;

    public RandomElementDefinition() {

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
        if (randomDefinition.getRestrictedFactionGroups() != null && !randomDefinition.getRestrictedFactionGroups().isEmpty()) {
            restrictedFactionGroups.clear();
            restrictedFactionGroups.addAll(randomDefinition.getRestrictedFactionGroups());
        }
        if (randomDefinition.getRecommendedFactionsGroups() != null && !randomDefinition.getRecommendedFactionsGroups().isEmpty()) {
            recommendedFactionGroups.clear();
            recommendedFactionGroups.addAll(randomDefinition.getRecommendedFactionsGroups());
        }
        if (randomDefinition.getProbability() != null) {
            setProbability(randomDefinition.getProbability());
        }
    }

    public Integer getMinimumTechLevel() {
        if (minimumTechLevel == null) {
            return 0;
        }
        return minimumTechLevel;
    }

    public void setMinimumTechLevel(Integer minimumTechLevel) {
        this.minimumTechLevel = minimumTechLevel;
    }

    public Set<String> getRecommendedFactions() {
        return recommendedFactions;
    }

    public void addRecommendedFactionGroup(FactionGroup recommendedFactionGroup) {
        if (recommendedFactionGroup != null) {
            recommendedFactionGroups.add(recommendedFactionGroup);
        }
    }

    public Set<FactionGroup> getRecommendedFactionsGroups() {
        return recommendedFactionGroups;
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
        if (probabilityMultiplier == null) {
            return 1d;
        }
        return probabilityMultiplier;
    }

    public void setProbabilityMultiplier(Double probabilityMultiplier) {
        this.probabilityMultiplier = probabilityMultiplier;
    }

    public Set<String> getRestrictedFactions() {
        return restrictedFactions;
    }

    public void addRestrictedFactionGroup(FactionGroup restrictedFactionGroup) {
        if (restrictedFactionGroup != null) {
            restrictedFactionGroups.add(restrictedFactionGroup);
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

    public Set<FactionGroup> getRestrictedFactionGroups() {
        return restrictedFactionGroups;
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

    public void setRestrictedFactionGroups(String restrictedFactionGroupsContent) {
        restrictedFactionGroups = new HashSet<>();
        final StringTokenizer restrictedFactionGroupsTokenizer = new StringTokenizer(restrictedFactionGroupsContent, ",");
        while (restrictedFactionGroupsTokenizer.hasMoreTokens()) {
            restrictedFactionGroups.add(FactionGroup.get(restrictedFactionGroupsTokenizer.nextToken().trim()));
        }
    }

    public void setRestrictedFactionGroups(Set<FactionGroup> restrictedFactionGroups) {
        this.restrictedFactionGroups = restrictedFactionGroups;
    }

    public Set<FactionGroup> getRecommendedFactionGroups() {
        return recommendedFactionGroups;
    }

    public void setRecommendedFactionGroups(String recommendedFactionGroupsContent) {
        recommendedFactionGroups = new HashSet<>();
        final StringTokenizer recommendedFactionGroupsTokenizer = new StringTokenizer(recommendedFactionGroupsContent, ",");
        while (recommendedFactionGroupsTokenizer.hasMoreTokens()) {
            recommendedFactionGroups.add(FactionGroup.get(recommendedFactionGroupsTokenizer.nextToken().trim()));
        }
    }

    public void setRecommendedFactionGroups(Set<FactionGroup> recommendedFactionGroups) {
        this.recommendedFactionGroups = recommendedFactionGroups;
    }

    @Override
    public String toString() {
        return minimumTechLevel + "";
    }
}
