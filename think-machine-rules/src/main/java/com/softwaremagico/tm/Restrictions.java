package com.softwaremagico.tm;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Restrictions extends XmlData {

    //Cannot be chosen by the character. Is assigned by race or faction.
    @JsonProperty("restricted")
    private boolean restricted = false;

    @JsonProperty("species")
    private Set<String> restrictedToSpecies = new HashSet<>();

    @JsonProperty("factionGroups")
    private Set<FactionGroup> restrictedToFactionGroup = new HashSet<>();

    @JsonProperty("factions")
    private Set<String> restrictedToFactions = new HashSet<>();

    @JsonProperty("uprising")
    private Set<String> restrictedToUprising = new HashSet<>();

    @JsonProperty("callings")
    private Set<String> restrictedToCallings = new HashSet<>();

    @JsonProperty("capabilities")
    private Set<String> restrictedToCapabilities = new HashSet<>();

    public Set<String> getRestrictedToSpecies() {
        return restrictedToSpecies;
    }

    public void setRestrictedToSpecies(Set<String> restrictedToSpecies) {
        this.restrictedToSpecies = restrictedToSpecies;
    }

    public Set<FactionGroup> getRestrictedToFactionGroup() {
        return restrictedToFactionGroup;
    }

    public void setRestrictedToFactionGroup(Set<FactionGroup> restrictedToFactionGroup) {
        this.restrictedToFactionGroup = restrictedToFactionGroup;
    }

    public Set<String> getRestrictedToFactions() {
        return restrictedToFactions;
    }

    public void setRestrictedToFactions(Set<String> restrictedToFactions) {
        this.restrictedToFactions = restrictedToFactions;
    }

    public Set<String> getRestrictedToUprising() {
        return restrictedToUprising;
    }

    public void setRestrictedToUprising(Set<String> restrictedToUprising) {
        this.restrictedToUprising = restrictedToUprising;
    }

    public Set<String> getRestrictedToCallings() {
        return restrictedToCallings;
    }

    public void setRestrictedToCallings(Set<String> restrictedToCallings) {
        this.restrictedToCallings = restrictedToCallings;
    }

    public Set<String> getRestrictedToCapabilities() {
        return restrictedToCapabilities;
    }

    public void setRestrictedToCapabilities(Set<String> restrictedToCapabilities) {
        this.restrictedToCapabilities = restrictedToCapabilities;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public boolean isRestricted(CharacterPlayer characterPlayer) {
        try {
            return characterPlayer != null && characterPlayer.getSettings().isRestrictionsChecked()
                    && ((!getRestrictedToSpecies().isEmpty() && (characterPlayer.getSpecie() == null
                    || !getRestrictedToSpecies().contains(characterPlayer.getSpecie())))
                    || isRestricted()
                    || (getRestrictedToFactionGroup() != null && (characterPlayer.getFaction() == null
                    && !getRestrictedToFactionGroup().contains(FactionFactory.getInstance()
                    .getElement(characterPlayer.getFaction().getId()).getFactionGroup())))
                    || (!getRestrictedToFactions().isEmpty() && (characterPlayer.getFaction() == null
                    || !getRestrictedToFactions().contains(characterPlayer.getFaction().getId())))
                    || (!getRestrictedToUprising().isEmpty() && (characterPlayer.getUprising() == null
                    || !getRestrictedToUprising().contains(characterPlayer.getUprising().getId()))));
            //TODO(softwaremagico): include callings.
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage("Is restricted!", e);
        }
        return true;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public boolean isRequiredCapability(String capability) {
        return restrictedToCapabilities.stream().anyMatch(c -> Objects.equals(c, capability));
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        for (String race : restrictedToSpecies) {
            SpecieFactory.getInstance().getElement(race);
        }
        for (String faction : restrictedToFactions) {
            FactionFactory.getInstance().getElement(faction);
        }
        for (String calling : restrictedToCallings) {
            CallingFactory.getInstance().getElement(calling);
        }

        for (String capability : restrictedToCapabilities) {
            CapabilityFactory.getInstance().getElement(capability);
        }
    }
}
