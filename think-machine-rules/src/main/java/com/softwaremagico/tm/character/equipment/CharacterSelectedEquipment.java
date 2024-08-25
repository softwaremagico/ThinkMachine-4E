package com.softwaremagico.tm.character.equipment;

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

import com.softwaremagico.tm.character.CharacterSelectedElement;
import com.softwaremagico.tm.character.Selection;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CharacterSelectedEquipment extends CharacterSelectedElement {
    //If has been removed later. To not print it on the sheet.
    private Set<Selection> removed;

    public Set<Selection> getRemainder() {
        return getSelections().stream().filter(e -> !getRemoved().contains(e.getId())).collect(Collectors.toSet());
    }

    public Set<Selection> getRemoved() {
        if (this.removed == null) {
            this.removed = new HashSet<>();
        }
        return removed;
    }

    public void setRemoved(Set<Selection> removed) {
        this.removed = removed;
    }

    public void addRemoved(Selection removed) {
        if (this.removed == null) {
            this.removed = new HashSet<>();
        }
        this.removed.add(removed);
    }
}
