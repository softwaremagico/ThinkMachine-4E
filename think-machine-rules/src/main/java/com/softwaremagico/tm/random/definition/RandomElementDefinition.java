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
    private Set<String> forbiddenRaces = new HashSet<>();
    private Set<String> restrictedRaces = new HashSet<>();
    private Set<String> recommendedRaces = new HashSet<>();
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
        if (randomDefinition.getRecommendedRaces() != null && !randomDefinition.getRecommendedRaces().isEmpty()) {
            recommendedRaces.clear();
            recommendedRaces.addAll(randomDefinition.getRecommendedRaces());
        }
        if (randomDefinition.getForbiddenRaces() != null && !randomDefinition.getForbiddenRaces().isEmpty()) {
            forbiddenRaces.clear();
            forbiddenRaces.addAll(randomDefinition.getForbiddenRaces());
        }
        if (randomDefinition.getRestrictedRaces() != null && !randomDefinition.getRestrictedRaces().isEmpty()) {
            restrictedRaces.clear();
            restrictedRaces.addAll(randomDefinition.getRestrictedRaces());
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

    public void addRecommendedRaces(String race) {
        if (race != null) {
            restrictedRaces.add(race);
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
            recommendedRaces.add(race);
        }
    }

    public Set<String> getRecommendedRaces() {
        return recommendedRaces;
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
            restrictedRaces.add(restrictedRace);
        }
    }

    public Set<String> getRestrictedRaces() {
        return restrictedRaces;
    }

    public void addForbiddenRace(String forbiddenRace) {
        if (forbiddenRace != null) {
            forbiddenRaces.add(forbiddenRace);
        }
    }

    public Set<String> getForbiddenRaces() {
        return forbiddenRaces;
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

    public void setForbiddenRaces(String forbiddenRacesContent) {
        forbiddenRaces = new HashSet<>();
        readCommaSeparatedTokens(forbiddenRaces, forbiddenRacesContent);
    }

    public void setForbiddenRaces(Set<String> forbiddenRaces) {
        this.forbiddenRaces = forbiddenRaces;
    }

    public void setRestrictedRaces(String restrictedRacesContent) {
        restrictedRaces = new HashSet<>();
        readCommaSeparatedTokens(restrictedRaces, restrictedRacesContent);
    }

    public void setRestrictedRaces(Set<String> restrictedRaces) {
        this.restrictedRaces = restrictedRaces;
    }

    public void setRecommendedRaces(String recommendedRacesContent) {
        recommendedRaces = new HashSet<>();
        readCommaSeparatedTokens(recommendedRaces, recommendedRacesContent);
    }

    public void setRecommendedRaces(Set<String> recommendedRaces) {
        this.recommendedRaces = recommendedRaces;
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
