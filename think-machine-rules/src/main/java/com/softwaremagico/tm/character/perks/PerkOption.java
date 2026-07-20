package com.softwaremagico.tm.character.perks;

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

import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.restrictions.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class PerkOption extends Option<Perk> {
    private final boolean repeatable;

    public PerkOption() {
        super();
        this.repeatable = false;
    }

    public PerkOption(Perk perk) {
        super();
        this.setId(perk.getId());
        this.setSpecializations(perk.getSpecializations());
        this.repeatable = perk.isRepeatable();
    }

    public List<PerkOption> expandGroup() {
        final List<PerkOption> finalPerkOptions = new ArrayList<>();
        if (this.getGroup() != null) {
            try {
                finalPerkOptions.addAll(PerkFactory.getInstance().getElementsByGroup(this.getGroup()).stream()
                        .map(PerkOption::new).toList());
            } catch (final InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        } else {
            finalPerkOptions.add(this);
        }
        return finalPerkOptions;
    }

    public List<Selection> getOptionsBySpecialization() {
        final List<PerkOption> finalPerkOptions = this.expandGroup();
        final List<Selection> specializedPerks = new ArrayList<>();
        for (final PerkOption perkOption : finalPerkOptions) {
            if (perkOption.getSpecializations() != null && !perkOption.getSpecializations().isEmpty()) {
                perkOption.getSpecializations().forEach(
                        specialization -> specializedPerks.add(new Selection(perkOption.getElement(), specialization)));
            } else {
                specializedPerks.add(new Selection(perkOption.getElement(), null));
            }
        }
        return specializedPerks;
    }

    @Override
    public Restrictions getRestrictions() {
        if (this.getId() != null) {
            return PerkFactory.getInstance().getElement(this.getId()).getRestrictions();
        }
        return super.getRestrictions();
    }

    public boolean isRepeatable() {
        return this.repeatable;
    }

    @Override
    public Perk getElement(String id) {
        return PerkFactory.getInstance().getElement(id);
    }

    @Override
    public String toString() {
        return this.getId() != null ? this.getId() : this.getGroup();
    }
}
