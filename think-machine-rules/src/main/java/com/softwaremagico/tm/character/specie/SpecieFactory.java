package com.softwaremagico.tm.character.specie;

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

public class SpecieFactory extends XmlFactory<Specie> {
    private static final String XML_FILE = "species.xml";

    private Map<String, Map<Gender, Set<Name>>> namesByXeno;
    private Map<String, Set<Surname>> surnamesByXeno;

    private static final class RaceFactoryInit {
        public static final SpecieFactory INSTANCE = new SpecieFactory();
    }

    public static SpecieFactory getInstance() {
        return RaceFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<Specie> getElements() throws InvalidXmlElementException {
        return readXml(Specie.class);
    }

    private void setNames() {
        if (namesByXeno == null) {
            namesByXeno = new HashMap<>();
            getElements().forEach(s -> {
                for (Gender gender : Gender.values()) {
                    namesByXeno.computeIfAbsent(s.getId(), k -> new EnumMap<>(Gender.class));
                    namesByXeno.get(s.getId()).computeIfAbsent(gender, k -> new HashSet<>());
                    namesByXeno.get(s.getId()).put(gender, s.getRandomDefinition().getNames(null, s.getId(), gender));
                }
            });
        }
    }

    private void setSurnames() {
        if (surnamesByXeno == null) {
            surnamesByXeno = new HashMap<>();
            getElements().forEach(s ->
                    surnamesByXeno.put(s.getId(), s.getRandomDefinition().getSurnames(null, s.getId())));
        }
    }

    public Set<Name> getAllNames(String specie, Gender gender) {
        if (namesByXeno == null) {
            setNames();
        }
        return namesByXeno.get(specie).get(gender);
    }

    public Set<Name> getAllNames() {
        final Set<Name> names = new HashSet<>();
        for (final Specie specie : getElements()) {
            names.addAll(getAllNames(specie.getId()));
        }
        return names;
    }

    public Set<Name> getAllNames(String specie) {
        final Set<Name> names = new HashSet<>();
        for (final Gender gender : Gender.values()) {
            try {
                names.addAll(getAllNames(specie, gender));
            } catch (NullPointerException e) {
                //No names defined.
            }
        }
        return names;
    }

    public Set<Surname> getAllSurnames(String specie) {
        if (surnamesByXeno == null) {
            setSurnames();
        }
        return surnamesByXeno.get(specie);
    }

    public Set<Surname> getAllSurnames() {
        final Set<Surname> surnames = new HashSet<>();
        for (final Specie specie : getElements()) {
            surnames.addAll(getAllSurnames(specie.getId()));
        }
        return surnames;
    }
}
