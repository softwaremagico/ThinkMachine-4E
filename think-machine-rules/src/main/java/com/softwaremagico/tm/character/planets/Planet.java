package com.softwaremagico.tm.character.planets;

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


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.random.RandomFactionFactory;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@JacksonXmlRootElement(localName = "planet")
public class Planet extends Element {
    @JsonProperty("factions")
    private String factionData;
    @JsonProperty("species")
    private String speciesData;
    @JsonIgnore
    private Set<String> factions;
    @JsonIgnore
    private Set<String> species;
    private Set<String> humanFactions;
    private Set<Name> names;
    private Set<Surname> surnames;

    public Planet() {
        super();
    }

    public Planet(String id, TranslatedText name, TranslatedText description, String language, String moduleName, Set<String> factions) {
        super(id, name, description, language, moduleName);
        this.factions = factions;
    }

    protected String getFactionData() {
        return factionData;
    }

    protected void setFactionData(String factionData) {
        this.factionData = factionData;
    }

    public String getSpeciesData() {
        return speciesData;
    }

    public void setSpeciesData(String speciesData) {
        this.speciesData = speciesData;
    }

    public void setFactions(Set<String> factions) {
        this.factions = factions;
    }

    public Set<String> getFactions() {
        if (factions == null) {
            factions = new HashSet<>();
            readCommaSeparatedTokens(factions, factionData);
        }
        return factions;
    }

    public Set<String> getSpecies() {
        if (species == null) {
            species = new HashSet<>();
            readCommaSeparatedTokens(species, speciesData);
        }
        return species;
    }

    public Set<String> getHumanFactions() {
        // Get only human factions from the planet. Ignore Xeno factions.
        if (humanFactions == null) {
            humanFactions = new HashSet<>();
            try {
                this.humanFactions = FactionFactory.getInstance().getElements(factions).stream().filter(Faction::isOnlyForHuman)
                .map(Faction::getId).collect(Collectors.toSet());
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass(), e);
            }
        }
        return humanFactions;
    }

    public Set<Name> getNames() {
        if (names == null) {
            names = new HashSet<>();
            for (final String faction : getHumanFactions()) {
                try {
                    if (RandomFactionFactory.getInstance().getElement(faction) != null) {
                        names.addAll(RandomFactionFactory.getInstance().getElement(faction).getAllNames());
                    }
                } catch (InvalidXmlElementException e) {
                    MachineXmlReaderLog.errorMessage(this.getName(), e);
                }
            }
        }
        return names;
    }

    public Set<Surname> getSurnames() {
        if (surnames == null) {
            surnames = new HashSet<>();
            for (final String faction : getHumanFactions()) {
                try {
                    if (RandomFactionFactory.getInstance().getElement(faction) != null) {
                        surnames.addAll(RandomFactionFactory.getInstance().getElement(faction).getSurnames());
                    }
                } catch (InvalidXmlElementException e) {
                    MachineXmlReaderLog.errorMessage(this.getName(), e);
                }
            }
        }
        return surnames;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
