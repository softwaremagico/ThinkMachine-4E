package com.softwaremagico.tm.character.planets;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.random.RandomFactionFactory;
import com.softwaremagico.tm.log.MachineLog;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@JacksonXmlRootElement(localName = "planet")
public class Planet extends Element<Planet> {
    @JsonProperty("factions")
    private String factionData;
    @JsonIgnore
    private Set<String> factions;
    private Set<Faction> humanFactions;
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

    public void setFactions(Set<String> factions) {
        this.factions = factions;
    }

    public Set<String> getFactions() {
        if (factions == null) {
            factions = new HashSet<>();
            if (factionData != null) {
                final StringTokenizer femaleNamesTokenizer = new StringTokenizer(factionData, ",");
                while (femaleNamesTokenizer.hasMoreTokens()) {
                    factions.add(femaleNamesTokenizer.nextToken().trim());
                }
            }
        }
        return factions;
    }

    public Set<Faction> getHumanFactions() {
        // Get only human factions from the planet. Ignore Xeno factions.
        if (humanFactions == null) {
            humanFactions = new HashSet<>();
            try {
                this.humanFactions = FactionFactory.getInstance().getElements(factions).stream().filter(Faction::isOnlyForHuman).
                        collect(Collectors.toSet());
            } catch (IOException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        }
        return humanFactions;
    }

    public Set<Name> getNames() {
        if (names == null) {
            names = new HashSet<>();
            for (final Faction faction : getHumanFactions()) {
                if (RandomFactionFactory.getInstance().getElement(faction.getId()) != null) {
                    names.addAll(RandomFactionFactory.getInstance().getElement(faction.getId()).getAllNames());
                }
            }
        }
        return names;
    }

    public Set<Surname> getSurnames() {
        if (surnames == null) {
            surnames = new HashSet<>();
            for (final Faction faction : getHumanFactions()) {
                if (RandomFactionFactory.getInstance().getElement(faction.getId()) != null) {
                    surnames.addAll(RandomFactionFactory.getInstance().getElement(faction.getId()).getSurnames());
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
