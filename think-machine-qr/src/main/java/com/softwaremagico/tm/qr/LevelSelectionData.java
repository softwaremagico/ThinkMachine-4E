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
 * Level selections extend step selections with level-specific perk slots
 * and optional calling change.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LevelSelectionData extends StepSelectionData {

    /** The calling chosen at this level (null if unchanged). */
    @JsonProperty("cl")
    private String callingId;

    /** Class perk option slots. */
    @JsonProperty("clp")
    private List<List<String>> classPerks;

    /** Calling perk option slots. */
    @JsonProperty("cap")
    private List<List<String>> callingPerks;

    public String getCallingId() {
        return callingId;
    }

    public void setCallingId(String callingId) {
        this.callingId = callingId;
    }

    public List<List<String>> getClassPerks() {
        return classPerks;
    }

    public void setClassPerks(List<List<String>> classPerks) {
        this.classPerks = classPerks;
    }

    public List<List<String>> getCallingPerks() {
        return callingPerks;
    }

    public void setCallingPerks(List<List<String>> callingPerks) {
        this.callingPerks = callingPerks;
    }
}
