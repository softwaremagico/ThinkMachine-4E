package com.softwaremagico.tm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.random.definition.RandomElementDefinition;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

public class Element<T extends Element<?>> implements Comparable<T> {
    public static final String DEFAULT_NULL_ID = "null";

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private TranslatedText name;

    @JsonProperty("description")
    private TranslatedText description;

    private String moduleName;

    private String language;

    @JsonProperty("random")
    private RandomElementDefinition randomDefinition;

    private boolean restricted = false;

    private boolean official = true;

    private Set<String> restrictedToRaces = new HashSet<>();

    private FactionGroup restrictedToFactionGroup = null;

    private Set<String> restrictedToFactions = new HashSet<>();

    //Only fort sheet representation.
    @JsonIgnore
    private Integer order;

    /**
     * For creating empty elements.
     */
    public Element() {
        this.id = DEFAULT_NULL_ID;
        this.name = new TranslatedText();
        this.description = new TranslatedText();
        this.moduleName = "";
        this.language = "";
        this.randomDefinition = new RandomElementDefinition();
        this.restricted = false;
    }

    public Element(String id, TranslatedText name, TranslatedText description, String language, String moduleName) {
        this(id, name, description, language, new RandomElementDefinition(), moduleName);
    }

    public Element(String id, TranslatedText name, TranslatedText description, String language, RandomElementDefinition randomDefinition,
                   String moduleName) {
        this.id = id != null ? id.trim() : null;
        this.name = name;
        this.description = description;
        this.language = language;
        this.randomDefinition = randomDefinition;
        this.moduleName = moduleName;
    }

    public TranslatedText getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(TranslatedText name) {
        this.name = name;
    }

    public TranslatedText getDescription() {
        return description;
    }

    public TranslatedText getNameRepresentation() {
        return getName();
    }

    public String getId() {
        return id;
    }

    public void setDescription(TranslatedText description) {
        this.description = description;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setRandomDefinition(RandomElementDefinition randomDefinition) {
        this.randomDefinition = randomDefinition;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public int compareTo(T element) {
        if (getName() == null) {
            if (element.getName() == null) {
                return 0;
            }
            return -1;
        }
        if (element.getName() == null) {
            return 1;
        }
        return getName().compareTo(element.getName());
    }

    @Override
    public String toString() {
        return getId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked") final T other = (T) obj;
        if (id == null) {
            return other.getId() == null;
        } else {
            return id.equals(other.getId());
        }
    }

    public RandomElementDefinition getRandomDefinition() {
        return randomDefinition;
    }

    public String getLanguage() {
        return language;
    }

    public String getModuleName() {
        return moduleName;
    }

    public static boolean isNull(Element<?> element) {
        if (element == null) {
            return true;
        }
        return Objects.equals(element.getId(), DEFAULT_NULL_ID);
    }

    public boolean isRestricted() {
        return restricted;
    }

    public boolean isRestricted(CharacterPlayer characterPlayer) {
        return characterPlayer != null && characterPlayer.getSettings().isRestrictionsChecked()
                && ((!getRestrictedToRaces().isEmpty() && (characterPlayer.getRace() == null || !getRestrictedToRaces().contains(characterPlayer.getRace())))
                || (getRestrictedToFactionGroup() != null && (characterPlayer.getFaction() == null
                && !Objects.equals(getRestrictedToFactionGroup(), FactionFactory.getInstance().getElement(characterPlayer.getFaction()).getFactionGroup())))
                || (!getRestrictedToFactions().isEmpty() && (characterPlayer.getFaction() == null
                || !getRestrictedToFactions().contains(characterPlayer.getFaction()))));
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public Set<String> getRestrictedToRaces() {
        return restrictedToRaces;
    }

    public Set<String> getRestrictedToFactions() {
        return restrictedToFactions;
    }

    public void setRestrictedToRaces(String restrictedToRaces) {
        this.restrictedToRaces = Collections.singleton(restrictedToRaces);
    }

    public void setRestrictedToRaces(Set<String> restrictedToRaces) {
        this.restrictedToRaces = restrictedToRaces;
    }

    public void setRestrictedToFactions(String restrictedToFactions) {
        this.restrictedToFactions = Collections.singleton(restrictedToFactions);
    }

    public void setRestrictedToFactions(Set<String> restrictedToFactions) {
        this.restrictedToFactions = restrictedToFactions;
    }

    public FactionGroup getRestrictedToFactionGroup() {
        return restrictedToFactionGroup;
    }

    public void setRestrictedToFactionGroup(FactionGroup restrictedToFactionGroup) {
        this.restrictedToFactionGroup = restrictedToFactionGroup;
    }
}
