package com.softwaremagico.tm.random.profile;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.Set;

public class RandomProfile extends Element {
    private Set<String> mandatoryPerks = Set.of();
    private Set<String> suggestedPerks = Set.of();
    private Set<String> mandatoryCapabilities = Set.of();
    private Set<String> suggestedCapabilities = Set.of();
    private Set<String> mandatorySkills = Set.of();
    private Set<String> suggestedSkills = Set.of();
    private Set<String> recommendedUpbringings = Set.of();
    private Set<String> recommendedFactions = Set.of();
    private Set<String> recommendedCallings = Set.of();

    public Set<String> getMandatoryPerks() {
        return mandatoryPerks;
    }

    public void setMandatoryPerks(Set<String> mandatoryPerks) {
        this.mandatoryPerks = mandatoryPerks;
    }

    public Set<String> getSuggestedPerks() {
        return suggestedPerks;
    }

    public void setSuggestedPerks(Set<String> suggestedPerks) {
        this.suggestedPerks = suggestedPerks;
    }

    public Set<String> getMandatoryCapabilities() {
        return mandatoryCapabilities;
    }

    public void setMandatoryCapabilities(Set<String> mandatoryCapabilities) {
        this.mandatoryCapabilities = mandatoryCapabilities;
    }

    public Set<String> getSuggestedCapabilities() {
        return suggestedCapabilities;
    }

    public void setSuggestedCapabilities(Set<String> suggestedCapabilities) {
        this.suggestedCapabilities = suggestedCapabilities;
    }

    public Set<String> getMandatorySkills() {
        return mandatorySkills;
    }

    public void setMandatorySkills(Set<String> mandatorySkills) {
        this.mandatorySkills = mandatorySkills;
    }

    public Set<String> getSuggestedSkills() {
        return suggestedSkills;
    }

    public void setSuggestedSkills(Set<String> suggestedSkills) {
        this.suggestedSkills = suggestedSkills;
    }

    public Set<String> getRecommendedUpbringings() {
        return recommendedUpbringings;
    }

    public void setRecommendedUpbringings(Set<String> recommendedUpbringings) {
        this.recommendedUpbringings = recommendedUpbringings;
    }

    public Set<String> getRecommendedFactions() {
        return recommendedFactions;
    }

    public void setRecommendedFactions(Set<String> recommendedFactions) {
        this.recommendedFactions = recommendedFactions;
    }

    public Set<String> getRecommendedCallings() {
        return recommendedCallings;
    }

    public void setRecommendedCallings(Set<String> recommendedCallings) {
        this.recommendedCallings = recommendedCallings;
    }


    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        validateElements(mandatoryPerks, PerkFactory.getInstance(), "mandatory perk");
        validateElements(suggestedPerks, PerkFactory.getInstance(), "suggested perk");
        validateElements(mandatoryCapabilities, CapabilityFactory.getInstance(), "mandatory capability");
        validateElements(suggestedCapabilities, CapabilityFactory.getInstance(), "suggested capability");
        validateElements(mandatorySkills, SkillFactory.getInstance(), "mandatory skill");
        validateElements(suggestedSkills, SkillFactory.getInstance(), "suggested skill");
        validateElements(recommendedUpbringings, UpbringingFactory.getInstance(), "recommended upbringing");
        validateElements(recommendedFactions, FactionFactory.getInstance(), "recommended faction");
        validateElements(recommendedCallings, CallingFactory.getInstance(), "recommended calling");
    }

    private void validateElements(Set<String> elementIds, com.softwaremagico.tm.xml.XmlFactory<?> factory,
                                  String elementType) throws InvalidXmlElementException {
        for (final String elementId : elementIds) {
            try {
                factory.getElement(elementId);
            } catch (InvalidXmlElementException e) {
                throw new InvalidXmlElementException("Unknown " + elementType + " '" + elementId
                        + "' in profile '" + getId() + "'.", e);
            }
        }
    }
}
