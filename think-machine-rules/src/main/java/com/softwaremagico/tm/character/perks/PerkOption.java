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

import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.restrictions.Restrictions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerkOption extends Option<Perk> {

    public PerkOption() {
        super();
    }

    public PerkOption(Perk perk) {
        this();
        setId(perk.getId());
        setSpecializations(perk.getSpecializations());
    }

    public List<PerkOption> expandGroup() {
        final List<PerkOption> finalPerkOptions = new ArrayList<>();
        if (getGroup() != null) {
            try {
                finalPerkOptions.addAll(PerkFactory.getInstance().getElementsByGroup(getGroup()).stream()
                        .map(PerkOption::new).collect(Collectors.toList()));
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        } else {
            finalPerkOptions.add(this);
        }
        return finalPerkOptions;
    }

    @Override
    public Restrictions getRestrictions() {
        if (getId() != null) {
            return PerkFactory.getInstance().getElement(getId()).getRestrictions();
        }
        return super.getRestrictions();
    }

    @Override
    public Perk getElement(String id) {
        return PerkFactory.getInstance().getElement(id);
    }

    @Override
    public String toString() {
        return getId() != null ? getId() : getGroup();
    }
}
