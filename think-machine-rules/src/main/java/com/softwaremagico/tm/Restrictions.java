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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Restrictions extends XmlData {

    //Cannot be chosen by the character. Is assigned by race or faction.
    @JsonProperty("restricted")
    private boolean restricted = false;

    @JsonProperty("species")
    private Set<String> restrictedToSpecies = new HashSet<>();

    @JsonProperty("upbringings")
    private Set<String> restrictedToUpbringing = new HashSet<>();

    @JsonProperty("factions")
    private Set<String> restrictedToFactions = new HashSet<>();

    @JsonProperty("uprising")
    private Set<String> restrictedToUprising = new HashSet<>();

    @JsonProperty("callings")
    private Set<String> restrictedToCallings = new HashSet<>();

    @JsonProperty("capabilities")
    private Set<String> restrictedToCapabilities = new HashSet<>();

    @JsonProperty("perks")
    private Set<String> restrictedPerks = new HashSet<>();

    @JsonProperty("perksGroups")
    private Set<String> restrictedToPerksGroups = new HashSet<>();

    @JacksonXmlProperty(isAttribute = true)
    private RestrictionMode mode = RestrictionMode.ANY;

    @JacksonXmlProperty(isAttribute = true)

    public Set<String> getRestrictedToSpecies() {
        return restrictedToSpecies;
    }

    public void setRestrictedToSpecies(Set<String> restrictedToSpecies) {
        this.restrictedToSpecies = restrictedToSpecies;
    }

    public Set<String> getRestrictedToUpbringing() {
        return restrictedToUpbringing;
    }

    public void setRestrictedToUpbringing(Set<String> restrictedToUpbringing) {
        this.restrictedToUpbringing = restrictedToUpbringing;
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

    public Set<String> getRestrictedToPerksGroups() {
        return restrictedToPerksGroups;
    }

    public void setRestrictedToPerksGroups(Set<String> restrictedToPerksGroups) {
        this.restrictedToPerksGroups = restrictedToPerksGroups;
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
        switch (mode) {
            case ANY_FROM_GROUP:
                return accomplishAnyRestrictionFromEachGroup(characterPlayer);
            case ANY:
            default:
                return accomplishAnyRestriction(characterPlayer);
        }
    }

    public boolean isOpen() {
        return !this.restricted
                && (restrictedToSpecies == null || restrictedToSpecies.isEmpty())
                && (restrictedToUpbringing == null || restrictedToUpbringing.isEmpty())
                && (restrictedToFactions == null || restrictedToFactions.isEmpty())
                && (restrictedToUprising == null || restrictedToUprising.isEmpty())
                && (restrictedToCallings == null || restrictedToCallings.isEmpty())
                && (restrictedToCapabilities == null || restrictedToCapabilities.isEmpty())
                && (restrictedPerks == null || restrictedPerks.isEmpty())
                && (restrictedToPerksGroups == null || restrictedToPerksGroups.isEmpty());
    }


    private boolean accomplishAnyRestriction(CharacterPlayer characterPlayer) {
        if (characterPlayer == null) {
            return false;
        }
        if (!characterPlayer.getSettings().isRestrictionsChecked()) {
            return true;
        }
        try {
            return
                    //Check Specie
                    (!getRestrictedToSpecies().isEmpty() && (characterPlayer.getSpecie() != null && getRestrictedToSpecies()
                            .contains(characterPlayer.getSpecie())))
                            // Check Faction Group
                            || (!getRestrictedToUprising().isEmpty() && (characterPlayer.getFaction() != null && getRestrictedToUprising()
                            .contains(characterPlayer.getUpbringing().getId())))
                            // Check Faction
                            || (!getRestrictedToFactions().isEmpty() && (characterPlayer.getFaction() != null && getRestrictedToFactions()
                            .contains(characterPlayer.getFaction().getId())))
                            // Check Uprising
                            || (!getRestrictedToUprising().isEmpty() && (characterPlayer.getUpbringing() != null && getRestrictedToUprising()
                            .contains(characterPlayer.getUpbringing().getId())))
                            //Check Callings
                            || (!getRestrictedToCallings().isEmpty() && (characterPlayer.getCallings() != null && !Collections.disjoint(
                            getRestrictedToCallings(), (characterPlayer.getCallings()))))
                            // Check Perks
                            || (!getRestrictedPerks().isEmpty() && (characterPlayer.getPerks() != null && !Collections.disjoint(
                            getRestrictedPerks(), (characterPlayer.getPerks()))))
                            // Check perks Groups
                            || (!getRestrictedToPerksGroups().isEmpty() && (characterPlayer.getPerks() != null && getRestrictedToPerksGroups().stream()
                            .anyMatch(PerkFactory.getInstance().getElements(characterPlayer.getPerks()).stream()
                                    .map(Element::getGroup).collect(Collectors.toList())::contains)))
                            //Check capabilities
                            || (!getRestrictedToCallings().isEmpty() && (characterPlayer.getCallings() != null && !Collections.disjoint(
                            getRestrictedToCallings(), (characterPlayer.getCallings()))));
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage("Is restricted!", e);
        }
        return false;
    }


    private boolean accomplishAnyRestrictionFromEachGroup(CharacterPlayer characterPlayer) {
        if (characterPlayer == null) {
            return false;
        }
        if (!characterPlayer.getSettings().isRestrictionsChecked()) {
            return true;
        }
        try {
            return
                    //Check Specie
                    (getRestrictedToSpecies().isEmpty() || (characterPlayer.getSpecie() != null && getRestrictedToSpecies()
                            .contains(characterPlayer.getSpecie())))
                            // Check Faction Group
                            && (getRestrictedToUprising().isEmpty() || (characterPlayer.getFaction() != null && getRestrictedToUprising()
                            .contains(characterPlayer.getUpbringing().getId())))
                            // Check Faction
                            && (getRestrictedToFactions().isEmpty() || (characterPlayer.getFaction() != null && getRestrictedToFactions()
                            .contains(characterPlayer.getFaction().getId())))
                            // Check Uprising
                            && (getRestrictedToUprising().isEmpty() || (characterPlayer.getUpbringing() != null && getRestrictedToUprising()
                            .contains(characterPlayer.getUpbringing().getId())))
                            //Check Callings
                            && (getRestrictedToCallings().isEmpty() || (characterPlayer.getCallings() != null && !Collections.disjoint(
                            getRestrictedToCallings(), (characterPlayer.getCallings()))))
                            // Check Perks
                            && (getRestrictedPerks().isEmpty() || (characterPlayer.getPerks() != null && !Collections.disjoint(
                            getRestrictedPerks(), (characterPlayer.getPerks()))))
                            // Check perks Groups
                            && (getRestrictedToPerksGroups().isEmpty() || (characterPlayer.getPerks() != null && getRestrictedToPerksGroups().stream()
                            .anyMatch(PerkFactory.getInstance().getElements(characterPlayer.getPerks()).stream()
                                    .map(Element::getGroup).collect(Collectors.toList())::contains)))
                            //Check capabilities
                            && (getRestrictedToCallings().isEmpty() || (characterPlayer.getCallings() != null && !Collections.disjoint(
                            getRestrictedToCallings(), (characterPlayer.getCallings()))));
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage("Is restricted!", e);
        }
        return false;
    }


    public RestrictionMode getMode() {
        return mode;
    }

    public void setMode(RestrictionMode mode) {
        this.mode = mode;
    }

    public Set<String> getRestrictedPerks() {
        return restrictedPerks;
    }

    public void setRestrictedPerks(Set<String> restrictedPerks) {
        this.restrictedPerks = restrictedPerks;
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
