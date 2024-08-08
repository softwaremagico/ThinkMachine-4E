package com.softwaremagico.tm.character.upbringing;

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
import com.softwaremagico.tm.character.CharacterDefinitionStep;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Upbringing extends CharacterDefinitionStep<Upbringing> {
    @JsonIgnore
    private List<PerkOptions> finalPerkOptions;

    @Override
    public List<PerkOptions> getPerksOptions() {
        if (finalPerkOptions == null) {
            //No perks defined.
            finalPerkOptions = new ArrayList<>();
            for (PerkOptions perkOptions : super.getPerksOptions()) {
                if (perkOptions.isIncludeOpenPerks()) {
                    final PerkOptions completedPerkOption = perkOptions.copy();
                    //Add Open perks
                    try {
                        completedPerkOption.addPerks(PerkFactory.getInstance().getElements().stream().filter(perk -> perk.getRestrictions().isOpen())
                                .map(PerkOption::new).collect(Collectors.toList()));
                    } catch (InvalidXmlElementException e) {
                        MachineLog.errorMessage(this.getClass(), e);
                    }
                    finalPerkOptions.add(completedPerkOption);
                } else {
                    finalPerkOptions.add(perkOptions);
                }
            }
        }
        return finalPerkOptions;
    }
}
