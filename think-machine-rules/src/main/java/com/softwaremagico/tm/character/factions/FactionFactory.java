package com.softwaremagico.tm.character.factions;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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

import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FactionFactory extends XmlFactory<Faction> {
    private static final String XML_FILE = "factions.xml";
    private static final String HUMAN = "human";

    private Map<String, Map<Gender, Set<Name>>> namesByFaction;
    private Map<String, Set<Surname>> surnamesByFaction;

    private static final class FactionFactoryInit {
        public static final FactionFactory INSTANCE = new FactionFactory();
    }

    public static FactionFactory getInstance() {
        return FactionFactoryInit.INSTANCE;
    }

    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<Faction> getElements() throws InvalidXmlElementException {
        return this.readXml(Faction.class);
    }

    private void setNames() {
        if (this.namesByFaction == null) {
          this.namesByFaction = new HashMap<>();
          this.getElements().forEach(f -> {
                for (final Gender gender : Gender.values()) {
                  this.namesByFaction.computeIfAbsent(f.getId(), k -> new EnumMap<>(Gender.class));
                  this.namesByFaction.get(f.getId()).computeIfAbsent(gender, k -> new HashSet<>());
                  this.namesByFaction.get(f.getId()).put(gender,
                            f.getRandomDefinition().getNames(f.getId(), HUMAN, gender));
                }
            });
        }
    }

    private void setSurnames() {
        if (this.surnamesByFaction == null) {
          this.surnamesByFaction = new HashMap<>();
          this.getElements().forEach(
                    f -> this.surnamesByFaction.put(f.getId(), f.getRandomDefinition().getSurnames(f.getId(), HUMAN)));
        }
    }

    public Set<Name> getAllNames(String faction, Gender gender) {
        if (this.namesByFaction == null) {
          this.setNames();
        }
        return this.namesByFaction.get(faction).get(gender);
    }

    public Set<Name> getAllNames() {
        final Set<Name> names = new HashSet<>();
        for (final Faction faction : this.getElements()) {
            names.addAll(this.getAllNames(faction.getId()));
        }
        return names;
    }

    public Set<Name> getAllNames(String faction) {
        final Set<Name> names = new HashSet<>();
        for (final Gender gender : Gender.values()) {
            try {
                names.addAll(this.getAllNames(faction, gender));
            } catch (final NullPointerException e) {
                // No names defined.
            }
        }
        return names;
    }

    public Set<Surname> getAllSurnames(String faction) {
        if (this.surnamesByFaction == null) {
          this.setSurnames();
        }
        return this.surnamesByFaction.get(faction);
    }

    public Set<Surname> getAllSurnames() {
        final Set<Surname> surnames = new HashSet<>();
        for (final Faction faction : this.getElements()) {
            surnames.addAll(this.getAllSurnames(faction.getId()));
        }
        return surnames;
    }

    public List<Faction> getByFactionGroups(Collection<FactionGroup> factionGroups) {
        return this.getElements().stream().filter(faction -> factionGroups.contains(FactionGroup.get(faction.getGroup())))
                .toList();
    }
}
