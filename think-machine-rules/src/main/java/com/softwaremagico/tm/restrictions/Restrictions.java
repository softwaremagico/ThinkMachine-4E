package com.softwaremagico.tm.restrictions;

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
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.XmlData;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityWithSpecialization;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
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

    @JsonProperty("factionGroups")
    private Set<FactionGroup> restrictedToFactionGroups = new HashSet<>();

    @JsonProperty("callings")
    private Set<String> restrictedToCallings = new HashSet<>();

    @JsonProperty("capabilities")
    private Set<RestrictedCapability> restrictedToCapabilities = new HashSet<>();

    @JsonProperty("perks")
    private Set<String> restrictedPerks = new HashSet<>();

    @JsonProperty("perksGroups")
    private Set<String> restrictedToPerksGroups = new HashSet<>();

    @JsonProperty("capabilitiesGroups")
    private Set<String> restrictedToCapabilitiesGroups = new HashSet<>();

    @JsonProperty("characteristics")
    private Set<RestrictedCharacteristic> restrictedCharacteristics = new HashSet<>();

    @JsonProperty("skills")
    private Set<RestrictedSkill> restrictedSkills = new HashSet<>();

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

    public Set<FactionGroup> getRestrictedToFactionGroups() {
        return restrictedToFactionGroups;
    }

    public void setRestrictedToFactionGroups(Set<FactionGroup> restrictedToFactionGroups) {
        this.restrictedToFactionGroups = restrictedToFactionGroups;
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

    public Set<String> getRestrictedToCapabilitiesGroups() {
        return restrictedToCapabilitiesGroups;
    }

    public void setRestrictedToCapabilitiesGroups(Set<String> restrictedToCapabilitiesGroups) {
        this.restrictedToCapabilitiesGroups = restrictedToCapabilitiesGroups;
    }

    public Set<RestrictedCapability> getRestrictedToCapabilities() {
        return restrictedToCapabilities;
    }

    public void setRestrictedToCapabilities(Set<RestrictedCapability> restrictedToCapabilities) {
        this.restrictedToCapabilities = restrictedToCapabilities;
    }

    public Set<RestrictedCharacteristic> getRestrictedCharacteristics() {
        return restrictedCharacteristics;
    }

    public void setRestrictedCharacteristics(Set<RestrictedCharacteristic> restrictedCharacteristics) {
        this.restrictedCharacteristics = restrictedCharacteristics;
    }

    public Set<RestrictedSkill> getRestrictedSkills() {
        return restrictedSkills;
    }

    public void setRestrictedSkills(Set<RestrictedSkill> restrictedSkills) {
        this.restrictedSkills = restrictedSkills;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public boolean isRestricted(CharacterPlayer characterPlayer) {
        switch (mode) {
            case ANY_FROM_GROUP:
                return !accomplishAnyRestrictionFromEachGroup(characterPlayer);
            case ALL:
                return !accomplishAllRestriction(characterPlayer);
            case ANY_CHARACTER_DEFINITION:
                return !accomplishAnyCharacterDefinitionRestriction(characterPlayer);
            case ANY:
            default:
                return !accomplishAnyRestriction(characterPlayer);
        }
    }

    public boolean isOpen() {
        return !this.restricted
                && (restrictedToSpecies == null || restrictedToSpecies.isEmpty())
                && (restrictedToUpbringing == null || restrictedToUpbringing.isEmpty())
                && (restrictedToFactions == null || restrictedToFactions.isEmpty())
                && (restrictedToFactionGroups == null || restrictedToFactionGroups.isEmpty())
                && (restrictedToCallings == null || restrictedToCallings.isEmpty())
                && (restrictedToCapabilities == null || restrictedToCapabilities.isEmpty())
                && (restrictedPerks == null || restrictedPerks.isEmpty())
                && (restrictedToPerksGroups == null || restrictedToPerksGroups.isEmpty())
                && (restrictedToCapabilitiesGroups == null || restrictedToCapabilitiesGroups.isEmpty())
                && (restrictedCharacteristics == null || restrictedCharacteristics.isEmpty())
                && (restrictedSkills == null || restrictedSkills.isEmpty());
    }

    public boolean isRestrictedByAllCharacteristics(CharacterPlayer characterPlayer) {
        if (getRestrictedCharacteristics() != null && !getRestrictedCharacteristics().isEmpty()) {
            for (RestrictedCharacteristic restrictedCharacteristic : getRestrictedCharacteristics()) {
                CharacteristicsDefinitionFactory.getInstance().getElement(restrictedCharacteristic.getCharacteristic());
                if (characterPlayer.getCharacteristicValue(restrictedCharacteristic.getCharacteristic())
                        < restrictedCharacteristic.getValue()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean isRestrictedByAllSkills(CharacterPlayer characterPlayer) {
        if (getRestrictedSkills() != null && !getRestrictedSkills().isEmpty()) {
            for (RestrictedSkill restrictedSkill : getRestrictedSkills()) {
                final Skill skill = SkillFactory.getInstance().getElement(restrictedSkill.getSkill());
                if (characterPlayer.getSkillValue(skill) < restrictedSkill.getValue()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean isRestrictedByAnyCharacteristic(CharacterPlayer characterPlayer) {
        if (getRestrictedCharacteristics() != null && !getRestrictedCharacteristics().isEmpty()) {
            for (RestrictedCharacteristic restrictedCharacteristic : getRestrictedCharacteristics()) {
                CharacteristicsDefinitionFactory.getInstance().getElement(restrictedCharacteristic.getCharacteristic());
                if (characterPlayer.getCharacteristicValue(restrictedCharacteristic.getCharacteristic())
                        >= restrictedCharacteristic.getValue()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isRestrictedByAnySkill(CharacterPlayer characterPlayer) {
        if (getRestrictedSkills() != null && !getRestrictedSkills().isEmpty()) {
            for (RestrictedSkill restrictedSkill : getRestrictedSkills()) {
                final Skill skill = SkillFactory.getInstance().getElement(restrictedSkill.getSkill());
                if (characterPlayer.getSkillValue(skill) >= restrictedSkill.getValue()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    public boolean isRestrictedToSpecie(CharacterPlayer characterPlayer) {
        return !getRestrictedToSpecies().isEmpty() && (characterPlayer.getSpecie() != null && getRestrictedToSpecies()
                .contains(characterPlayer.getSpecie().getId()));
    }

    public boolean isRestrictedToFaction(CharacterPlayer characterPlayer) {
        return !getRestrictedToFactions().isEmpty() && (characterPlayer.getFaction() != null && getRestrictedToFactions()
                .contains(characterPlayer.getFaction().getId()));
    }

    public boolean isRestrictedToFactionGroup(CharacterPlayer characterPlayer) {
        return !getRestrictedToFactionGroups().isEmpty() && (characterPlayer.getFaction() != null && getRestrictedToFactionGroups()
                .contains(FactionGroup.get(characterPlayer.getFaction().getGroup())));
    }


    private boolean accomplishAnyRestriction(CharacterPlayer characterPlayer) {
        if (characterPlayer == null) {
            return false;
        }
        if (!characterPlayer.getSettings().isRestrictionsChecked()) {
            return true;
        }
        try {
            return isOpen()
                    //Check Specie
                    || isRestrictedToSpecie(characterPlayer)
                    // Check Faction
                    || isRestrictedToFaction(characterPlayer)
                    // Check characteristics
                    || isRestrictedByAnyCharacteristic(characterPlayer)
                    // Check skills
                    || isRestrictedByAnySkill(characterPlayer)
                    // Check upbringing
                    || (!getRestrictedToUpbringing().isEmpty() && (characterPlayer.getUpbringing() != null && getRestrictedToUpbringing()
                    .contains(characterPlayer.getUpbringing().getId())))
                    //Check Callings
                    || (!getRestrictedToCallings().isEmpty() && (characterPlayer.getCalling() != null && getRestrictedToCallings()
                    .contains(characterPlayer.getCalling().getId())))
                    // Check Perks
                    || (!getRestrictedPerks().isEmpty() && (characterPlayer.getPerks() != null && !Collections.disjoint(
                    getRestrictedPerks(), (characterPlayer.getPerks().stream().map(Element::getId).collect(Collectors.toList())))))
                    // Check perks Groups
                    || (!getRestrictedToPerksGroups().isEmpty() && (characterPlayer.getPerks() != null && getRestrictedToPerksGroups().stream()
                    .anyMatch(characterPlayer.getPerks().stream().map(Element::getGroup).collect(Collectors.toList())::contains)))
                    // Check Capabilities Groups
                    || (!getRestrictedToCapabilitiesGroups().isEmpty() && (characterPlayer.getCapabilitiesWithSpecialization() != null
                    && getRestrictedToCapabilitiesGroups().stream().anyMatch(characterPlayer.getCapabilitiesWithSpecialization().stream()
                    .map(Element::getGroup).collect(Collectors.toList())::contains)))
                    //Check capabilities
                    || (!getRestrictedToCapabilities().isEmpty() && (characterPlayer.getCapabilitiesWithSpecialization() != null
                    && !Collections.disjoint(getRestrictedToCapabilities().stream().map(RestrictedCapability::getComparisonId)
                            .collect(Collectors.toList()),
                    (characterPlayer.getCapabilitiesWithSpecialization().stream().map(CapabilityWithSpecialization::getComparisonId)
                            .collect(Collectors.toList())))));
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
            return isOpen()
                    //Check Specie
                    || ((getRestrictedToSpecies().isEmpty() || (characterPlayer.getSpecie() != null && getRestrictedToSpecies()
                    .contains(characterPlayer.getSpecie().getId())))
                    // Check Faction
                    && (getRestrictedToFactions().isEmpty() || (characterPlayer.getFaction() != null && getRestrictedToFactions()
                    .contains(characterPlayer.getFaction().getId())))
                    // Check characteristics
                    && !isRestrictedByAnyCharacteristic(characterPlayer)
                    // Check skills
                    && !isRestrictedByAnySkill(characterPlayer)
                    // Check upbringing
                    && (getRestrictedToUpbringing().isEmpty() || (characterPlayer.getUpbringing() != null && getRestrictedToUpbringing()
                    .contains(characterPlayer.getUpbringing().getId())))
                    //Check Callings
                    && (getRestrictedToCallings().isEmpty() || (characterPlayer.getCalling() != null && getRestrictedToCallings()
                    .contains(characterPlayer.getCalling().getId())))
                    // Check Perks
                    && (getRestrictedPerks().isEmpty() || (characterPlayer.getPerks() != null && !Collections.disjoint(
                    getRestrictedPerks(), (characterPlayer.getPerks().stream().map(Element::getId).collect(Collectors.toList())))))
                    // Check perks Groups
                    && (getRestrictedToPerksGroups().isEmpty() || (characterPlayer.getPerks() != null && getRestrictedToPerksGroups().stream()
                    .anyMatch(characterPlayer.getPerks().stream().map(Element::getGroup).collect(Collectors.toList())::contains)))
                    // Check capabilities Groups
                    && (getRestrictedToCapabilitiesGroups().isEmpty() || (characterPlayer.getCapabilitiesWithSpecialization() != null
                    && getRestrictedToCapabilitiesGroups().stream().anyMatch(characterPlayer.getCapabilitiesWithSpecialization().stream()
                    .map(Element::getGroup).collect(Collectors.toList())::contains)))
                    //Check capabilities
                    && (getRestrictedToCapabilities().isEmpty() || (characterPlayer.getCapabilitiesWithSpecialization() != null
                    && !Collections.disjoint(getRestrictedToCapabilities().stream().map(RestrictedCapability::getComparisonId).collect(Collectors.toList()),
                    (characterPlayer.getCapabilitiesWithSpecialization().stream().map(CapabilityWithSpecialization::getComparisonId)
                            .collect(Collectors.toList()))))));
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage("Is restricted!", e);
        }
        return false;
    }

    private boolean accomplishAllRestriction(CharacterPlayer characterPlayer) {
        if (characterPlayer == null) {
            return false;
        }
        if (!characterPlayer.getSettings().isRestrictionsChecked()) {
            return true;
        }
        try {
            return isOpen()
                    //Check Specie
                    || (((getRestrictedToSpecies().isEmpty() || (characterPlayer.getSpecie() != null && getRestrictedToSpecies()
                    .contains(characterPlayer.getSpecie().getId())))
                    // Check Faction
                    && (getRestrictedToFactions().isEmpty() || (characterPlayer.getFaction() != null && getRestrictedToFactions()
                    .contains(characterPlayer.getFaction().getId())))
                    // Check characteristics
                    && !isRestrictedByAllCharacteristics(characterPlayer)
                    // Check skills
                    && !isRestrictedByAllSkills(characterPlayer)
                    // Check upbringing. Only one can be present.
                    && (getRestrictedToUpbringing().isEmpty() || (characterPlayer.getUpbringing() != null && getRestrictedToUpbringing()
                    .contains(characterPlayer.getUpbringing().getId())))
                    //Check Callings. Only one can be present.
                    && (getRestrictedToCallings().isEmpty() || (characterPlayer.getCalling() != null && getRestrictedToCallings()
                    .contains(characterPlayer.getCalling().getId())))
                    // Check Perks
                    && (getRestrictedPerks().isEmpty() || (characterPlayer.getPerks() != null
                    && characterPlayer.getPerks().stream().map(Element::getId).collect(Collectors.toSet()).containsAll(getRestrictedPerks())))
                    // Check perks Groups
                    && (getRestrictedToPerksGroups().isEmpty() || (characterPlayer.getPerks() != null && new HashSet<>(characterPlayer.getPerks().stream()
                    .map(Element::getGroup).collect(Collectors.toList())).containsAll(getRestrictedToPerksGroups())))
                    // Check capabilities groups
                    && (getRestrictedToCapabilitiesGroups().isEmpty() || (characterPlayer.getCapabilitiesWithSpecialization() != null
                    && characterPlayer.getCapabilitiesWithSpecialization().stream().map(Element::getGroup).collect(Collectors.toSet())
                    .containsAll(getRestrictedToCapabilitiesGroups())))
                    //Check capabilities
                    && (getRestrictedToCapabilities().isEmpty() || (characterPlayer.getCapabilitiesWithSpecialization() != null
                    && characterPlayer.getCapabilitiesWithSpecialization().stream().map(CapabilityWithSpecialization::getComparisonId)
                    .collect(Collectors.toSet())
                    .containsAll(getRestrictedToCapabilities().stream().map(RestrictedCapability::getComparisonId).collect(Collectors.toList()))))));
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage("Is restricted!", e);
        }
        return false;
    }

    private boolean accomplishAnyCharacterDefinitionRestriction(CharacterPlayer characterPlayer) {
        if (characterPlayer == null) {
            return false;
        }
        if (!characterPlayer.getSettings().isRestrictionsChecked()) {
            return true;
        }
        try {
            return isOpen()
                    //Must much specie or upbringing or calling or faction.
                    || ((((getRestrictedToSpecies().isEmpty() || (characterPlayer.getSpecie() != null && getRestrictedToSpecies()
                    .contains(characterPlayer.getSpecie().getId())))
                    // Check upbringing. Only one can be present.
                    || (getRestrictedToUpbringing().isEmpty() || (characterPlayer.getUpbringing() != null && getRestrictedToUpbringing()
                    .contains(characterPlayer.getUpbringing().getId())))
                    //Check Callings. Only one can be present.
                    || (getRestrictedToCallings().isEmpty() || (characterPlayer.getCalling() != null && getRestrictedToCallings()
                    .contains(characterPlayer.getCalling().getId())))
                    // Check Faction
                    || (getRestrictedToFactions().isEmpty() || (characterPlayer.getFaction() != null && getRestrictedToFactions()
                    .contains(characterPlayer.getFaction().getId()))))
                    // Check characteristics
                    && !isRestrictedByAllCharacteristics(characterPlayer)
                    // Check skills
                    && !isRestrictedByAllSkills(characterPlayer)
                    // Check Perks
                    && (getRestrictedPerks().isEmpty() || (characterPlayer.getPerks() != null
                    && characterPlayer.getPerks().stream().map(Element::getId).collect(Collectors.toSet()).containsAll(getRestrictedPerks())))
                    // Check perks Groups
                    && (getRestrictedToPerksGroups().isEmpty() || (characterPlayer.getPerks() != null && new HashSet<>(characterPlayer.getPerks().stream()
                    .map(Element::getGroup).collect(Collectors.toList())).containsAll(getRestrictedToPerksGroups())))
                    // Check capabilities groups
                    && (getRestrictedToCapabilitiesGroups().isEmpty() || (characterPlayer.getCapabilitiesWithSpecialization() != null
                    && characterPlayer.getCapabilitiesWithSpecialization().stream().map(Element::getGroup).collect(Collectors.toSet())
                    .containsAll(getRestrictedToCapabilitiesGroups())))
                    //Check capabilities
                    && (getRestrictedToCapabilities().isEmpty() || (characterPlayer.getCapabilitiesWithSpecialization() != null
                    && characterPlayer.getCapabilitiesWithSpecialization().stream().map(CapabilityWithSpecialization::getComparisonId)
                    .collect(Collectors.toSet())
                    .containsAll(getRestrictedToCapabilities().stream().map(RestrictedCapability::getComparisonId).collect(Collectors.toList()))))));
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

    public boolean isRequiredCapability(String capability, String specialization) {
        return restrictedToCapabilities.stream().anyMatch(c -> Objects.equals(c.getComparisonId(),
                RestrictedCapability.getComparisonId(capability, specialization)));
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        for (String race : restrictedToSpecies) {
            SpecieFactory.getInstance().getElement(race);
        }
        for (String upbringing : restrictedToUpbringing) {
            UpbringingFactory.getInstance().getElement(upbringing);
        }
        for (String faction : restrictedToFactions) {
            FactionFactory.getInstance().getElement(faction);
        }
        for (String calling : restrictedToCallings) {
            CallingFactory.getInstance().getElement(calling);
        }
        for (RestrictedCapability capability : restrictedToCapabilities) {
            CapabilityFactory.getInstance().getElement(capability.getId());
        }
        for (String perk : restrictedPerks) {
            PerkFactory.getInstance().getElement(perk);
        }
        for (String perkGroup : restrictedToPerksGroups) {
            if (!PerkFactory.getInstance().getElementGroups().contains(perkGroup)) {
                throw new InvalidXmlElementException("Perk group '" + perkGroup + "' does not exists.");
            }
        }
        for (String capabilityGroup : restrictedToCapabilitiesGroups) {
            if (!CapabilityFactory.getInstance().getElementGroups().contains(capabilityGroup)) {
                throw new InvalidXmlElementException("Capability group '" + capabilityGroup + "' does not exists.");
            }
        }
    }

    @Override
    public String toString() {
        return "Restrictions{"
                + "restricted=" + restricted
                + ", restrictedToSpecies=" + restrictedToSpecies
                + ", restrictedToUpbringing=" + restrictedToUpbringing
                + ", restrictedToFactions=" + restrictedToFactions
                + ", restrictedToFactionGroups=" + restrictedToFactionGroups
                + ", restrictedToCallings=" + restrictedToCallings
                + ", restrictedToCapabilities=" + restrictedToCapabilities
                + ", restrictedToCapabilitiesGroups=" + restrictedToCapabilitiesGroups
                + ", restrictedPerks=" + restrictedPerks
                + ", restrictedToPerksGroups=" + restrictedToPerksGroups
                + ", mode=" + mode
                + '}';
    }
}
