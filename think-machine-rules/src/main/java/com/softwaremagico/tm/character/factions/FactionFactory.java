package com.softwaremagico.tm.character.factions;

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

import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FactionFactory extends XmlFactory<Faction> {
    private static final String XML_FILE = "factions.xml";

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
        return readXml(Faction.class);
    }

    private void setNames() {
        if (namesByFaction == null) {
            namesByFaction = new HashMap<>();
            for (Gender gender : Gender.values()) {
                getElements().forEach(f -> {
                    namesByFaction.computeIfAbsent(f.getId(), k -> new EnumMap<>(Gender.class));
                    namesByFaction.get(f.getId()).put(gender, f.getRandomDefinition().getNames(f.getId(), gender));
                });
            }
        }
    }

    private void setSurnames() {
        if (surnamesByFaction == null) {
            surnamesByFaction = new HashMap<>();
            getElements().forEach(f ->
                    surnamesByFaction.put(f.getId(), f.getRandomDefinition().getSurnames(f.getId())));
        }
    }

    public Set<Name> getAllNames(String faction, Gender gender) {
        if (namesByFaction == null) {
            setNames();
        }
        return namesByFaction.get(faction).get(gender);
    }

    public Set<Name> getAllNames() {
        final Set<Name> names = new HashSet<>();
        for (final Faction faction : getElements()) {
            names.addAll(getAllNames(faction.getId()));
        }
        return names;
    }

    public Set<Name> getAllNames(String faction) {
        final Set<Name> names = new HashSet<>();
        for (final Gender gender : Gender.values()) {
            try {
                names.addAll(getAllNames(faction, gender));
            } catch (NullPointerException e) {
                //No names defined.
            }
        }
        return names;
    }

    public Set<Surname> getAllSurnames(String faction) {
        if (surnamesByFaction == null) {
            setSurnames();
        }
        return surnamesByFaction.get(faction);
    }

    public Set<Surname> getAllSurnames() {
        final Set<Surname> surnames = new HashSet<>();
        for (final Faction faction : getElements()) {
            surnames.addAll(getAllSurnames(faction.getId()));
        }
        return surnames;
    }
}
