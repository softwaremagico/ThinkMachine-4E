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
 * Selections for a single character-creation step (specie / upbringing / faction / calling).
 * Each field is a list of option-slots; every slot holds the IDs chosen for that slot.
 * IDs with specialisations are encoded as "id:specializationId".
 * Empty or null slots are omitted from JSON to keep the payload compact.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StepSelectionData {

    /** Capability option slots. */
    @JsonProperty("caps")
    private List<List<String>> capabilities;

    /** Characteristic option slots. */
    @JsonProperty("chars")
    private List<List<String>> characteristics;

    /** Skill option slots. */
    @JsonProperty("skills")
    private List<List<String>> skills;

    /** Perk option slots. */
    @JsonProperty("perks")
    private List<List<String>> perks;

    /** Material-award option slots (each slot may also carry removed IDs). */
    @JsonProperty("mat")
    private List<EquipmentSlotData> materialAwards;

    public List<List<String>> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<List<String>> capabilities) {
        this.capabilities = capabilities;
    }

    public List<List<String>> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<List<String>> characteristics) {
        this.characteristics = characteristics;
    }

    public List<List<String>> getSkills() {
        return skills;
    }

    public void setSkills(List<List<String>> skills) {
        this.skills = skills;
    }

    public List<List<String>> getPerks() {
        return perks;
    }

    public void setPerks(List<List<String>> perks) {
        this.perks = perks;
    }

    public List<EquipmentSlotData> getMaterialAwards() {
        return materialAwards;
    }

    public void setMaterialAwards(List<EquipmentSlotData> materialAwards) {
        this.materialAwards = materialAwards;
    }
}
