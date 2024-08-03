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
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.HashSet;
import java.util.Set;

public class Restrictions extends XmlData {

    //Cannot be chosen by the character. Is assigned by race or faction.
    @JsonProperty("restricted")
    private boolean restricted = false;

    @JsonProperty("races")
    private Set<String> restrictedToRaces = new HashSet<>();

    @JsonProperty("factionGroups")
    private Set<FactionGroup> restrictedToFactionGroup = new HashSet<>();

    @JsonProperty("factions")
    private Set<String> restrictedToFactions = new HashSet<>();

    @JsonProperty("callings")
    private Set<String> restrictedToCallings = new HashSet<>();

    public Set<String> getRestrictedToRaces() {
        return restrictedToRaces;
    }

    public void setRestrictedToRaces(Set<String> restrictedToRaces) {
        this.restrictedToRaces = restrictedToRaces;
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

    public Set<String> getRestrictedToCallings() {
        return restrictedToCallings;
    }

    public void setRestrictedToCallings(Set<String> restrictedToCallings) {
        this.restrictedToCallings = restrictedToCallings;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public boolean isRestricted(CharacterPlayer characterPlayer) {
        try {
            return characterPlayer != null && characterPlayer.getSettings().isRestrictionsChecked()
                    && ((!getRestrictedToRaces().isEmpty() && (characterPlayer.getRace() == null
                    || !getRestrictedToRaces().contains(characterPlayer.getRace())))
                    || isRestricted()
                    || (getRestrictedToFactionGroup() != null && (characterPlayer.getFaction() == null
                    && !getRestrictedToFactionGroup().contains(FactionFactory.getInstance().getElement(characterPlayer.getFaction()).getFactionGroup())))
                    || (!getRestrictedToFactions().isEmpty() && (characterPlayer.getFaction() == null
                    || !getRestrictedToFactions().contains(characterPlayer.getFaction()))));
            //TODO(softwaremagico): include callings.
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage("Is restricted!", e);
        }
        return true;
    }

    public boolean isRestricted() {
        return restricted;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        for (String race : restrictedToRaces) {
            RaceFactory.getInstance().getElement(race);
        }
        for (String faction : restrictedToFactions) {
            FactionFactory.getInstance().getElement(faction);
        }
        for (String calling : restrictedToCallings) {
            CallingFactory.getInstance().getElement(calling);
        }
    }
}
