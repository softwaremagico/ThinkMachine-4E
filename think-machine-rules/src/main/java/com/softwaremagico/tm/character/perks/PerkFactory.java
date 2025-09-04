package com.softwaremagico.tm.character.perks;

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

import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PerkFactory extends XmlFactory<Perk> {
    private static final String XML_FILE = "perks.xml";
    private Set<Perk> classPrivilegePerks = null;
    private Set<Selection> classPrivilegeSelections = null;

    private static final class PerkFactoryInit {
        public static final PerkFactory INSTANCE = new PerkFactory();
    }

    public static PerkFactory getInstance() {
        return PerkFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<Perk> getElements() throws InvalidXmlElementException {
        return readXml(Perk.class);
    }

    public Set<Perk> getClassPrivilegePerks() {
        if (classPrivilegePerks == null) {
            classPrivilegePerks = getElements().stream().filter(perk ->
                    perk.getSource() == PerkSource.CLASS && perk.getType() == PerkType.PRIVILEGE).collect(Collectors.toSet());
        }
        return classPrivilegePerks;
    }

    public Set<Selection> getClassPrivilegeSelections() {
        if (classPrivilegeSelections == null) {
            classPrivilegeSelections = new HashSet<>();
            getClassPrivilegeSelections().forEach(perk -> {
                if (perk.getSpecializations() != null && !perk.getSpecializations().isEmpty()) {
                    for (Specialization specialization : perk.getSpecializations()) {
                        classPrivilegeSelections.add(new Selection(perk, specialization));
                    }
                } else {
                    classPrivilegeSelections.add(new Selection(perk, null));
                }
            });
        }
        return classPrivilegeSelections;
    }
}
