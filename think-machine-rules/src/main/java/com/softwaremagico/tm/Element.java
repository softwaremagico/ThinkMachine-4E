package com.softwaremagico.tm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.resistances.Resistance;
import com.softwaremagico.tm.character.resistances.ResistanceType;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.exceptions.InvalidSpecializationException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.definition.RandomElementDefinition;
import com.softwaremagico.tm.restrictions.Restrictions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/*-
 * #%L
 * Think Machine (Core)
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

public class Element extends XmlData implements Comparable<Element> {
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

    @JsonProperty("restrictions")
    private Restrictions restrictions;

    @JsonProperty("resistances")
    private List<Resistance> resistances;

    private List<Specialization> specializations;

    @JsonProperty("official")
    private boolean official = true;

    @JsonProperty("group")
    private String group;

    //Only fort sheet representation.
    @JsonIgnore
    private Integer order;

    /**
     * For creating empty elements.
     */
    public Element() {
        this.name = new TranslatedText();
        this.description = new TranslatedText();
        this.moduleName = "";
        this.language = "";
        this.randomDefinition = new RandomElementDefinition();
    }

    public Element(String id) {
        this();
        setId(id);
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

    public String getNameRepresentation() {
        if (getName() != null) {
            return getName().getTranslatedText();
        }
        return "";
    }

    public String getDescriptionRepresentation() {
        if (getDescription() != null) {
            return getDescription().getTranslatedText();
        }
        return "";
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

    public List<Specialization> getSpecializations() {
        return specializations;
    }

    public Specialization getSpecialization(String specialization) {
        return specializations.stream().filter(s -> Objects.equals(s.getId(), specialization)).findFirst().orElseThrow(() ->
                new InvalidSpecializationException("Specialization '" + specialization + "' not found on capability '" + getId() + "'."));
    }

    public void setSpecializations(List<Specialization> specializations) {
        this.specializations = specializations;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public int compareTo(Element element) {
        if (getName() == null) {
            if (element.getName() == null) {
                if (getId() != null) {
                    return getId().compareTo(element.getId());
                } else {
                    return 0;
                }
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
        if (getSpecializations() == null || getSpecializations().isEmpty()) {
            return getId();
        }
        return getId() + " " + getSpecializations();
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
        @SuppressWarnings("unchecked") final Element other = (Element) obj;
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

    public Restrictions getRestrictions() {
        if (restrictions == null) {
            restrictions = new Restrictions();
        }
        return restrictions;
    }

    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public void copy(Element element) {
        if (element == null) {
            throw new InvalidXmlElementException("You cannot make a copy if there is no element defined.");
        }
        setId(element.getId());
        setName(element.getName());
        setDescription(element.getDescription());
        setModuleName(element.getModuleName());
        setRandomDefinition(element.getRandomDefinition());
        setRestrictions(element.getRestrictions());
        if (element.getSpecializations() != null) {
            setSpecializations(new ArrayList<>(element.getSpecializations()));
        }
        setOfficial(element.isOfficial());
        setGroup(element.getGroup());
        if (element.getResistances() != null) {
            setResistances(new ArrayList<>(element.getResistances()));
        }
    }

    public static boolean isNull(Element element) {
        if (element == null) {
            return true;
        }
        return element.getId() == null || Objects.equals(element.getId(), DEFAULT_NULL_ID);
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        if (specializations != null) {
            try {
                specializations.forEach(Element::validate);
            } catch (InvalidXmlElementException e) {
                throw new InvalidXmlElementException("Error on '" + getId() + "'.", e);
            }
        }
        if (restrictions != null) {
            try {
                restrictions.validate();
            } catch (InvalidXmlElementException e) {
                throw new InvalidXmlElementException("Error on '" + getId() + "'.", e);
            }
        }
        if (randomDefinition != null) {
            try {
                randomDefinition.validate();
            } catch (InvalidXmlElementException e) {
                throw new InvalidXmlElementException("Error on '" + getId() + "'.", e);
            }
        }
        if (getName() == null && getId() != null && !getId().isEmpty()) {
            throw new InvalidXmlElementException("Name not set on Element '" + getId() + "'.");
        }
        if (getName() != null) {
            try {
                getName().validate();
            } catch (InvalidXmlElementException e) {
                throw new InvalidXmlElementException("Error on item '" + getId() + "'", e);
            }
        }
    }

    public List<Resistance> getResistances() {
        if (resistances == null) {
            resistances = new ArrayList<>();
        }
        return resistances;
    }

    public void setResistances(List<Resistance> resistances) {
        this.resistances = resistances;
    }

    public int getResistanceValue(ResistanceType resistanceType) {
        final Optional<Resistance> resistance = getResistances().stream().filter(r -> r.getType() == resistanceType).findAny();
        return resistance.map(Resistance::getBonus).orElse(0);
    }

    public void copyRestrictions(Element element) {
        getRestrictions().setRestrictedToSpecies(new HashSet<>(element.getRestrictions().getRestrictedToSpecies()));
        getRestrictions().setRestrictedToFactions(new HashSet<>(element.getRestrictions().getRestrictedToFactions()));
        getRestrictions().setRestrictedToUpbringing(new HashSet<>(element.getRestrictions().getRestrictedToUpbringing()));
        getRestrictions().setRestrictedToCallings(new HashSet<>(element.getRestrictions().getRestrictedToCallings()));
        getRestrictions().setRestrictedToCapabilities(new HashSet<>(element.getRestrictions().getRestrictedToCapabilities()));
        getRestrictions().setRestrictedToPerksGroups(new HashSet<>(element.getRestrictions().getRestrictedToPerksGroups()));
        getRestrictions().setRestrictedPerks(new HashSet<>(element.getRestrictions().getRestrictedPerks()));
        setOfficial(element.isOfficial());
    }

    public boolean isOpen() {
        return getRestrictions().isOpen();
    }
}
