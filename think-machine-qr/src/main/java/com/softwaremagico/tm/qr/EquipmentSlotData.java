package com.softwaremagico.tm.qr;

/*-
 * #%L
 * Think Machine 4E (QR)
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Single material-award option slot: which equipment IDs were selected and which were later removed.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EquipmentSlotData {

    /** IDs of equipment items that were granted (selected). */
    @JsonProperty("sel")
    private List<String> selected;

    /** IDs of equipment items that were subsequently removed from the slot. */
    @JsonProperty("rm")
    private List<String> removed;

    public List<String> getSelected() {
        return selected;
    }

    public void setSelected(List<String> selected) {
        this.selected = selected;
    }

    public List<String> getRemoved() {
        return removed;
    }

    public void setRemoved(List<String> removed) {
        this.removed = removed;
    }
}
