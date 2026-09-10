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
import com.softwaremagico.tm.random.preferences.IRandomPreference;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomProfile extends Element implements IRandomPreference {
    private String mandatoryPerks;
    private String suggestedPerks;
    private String mandatoryCapabilities;
    private String suggestedCapabilities;
    private String mandatorySkills;
    private String suggestedSkills;

    @Override
    public String name() {
        return getId();
    }

    public Set<IRandomPreference> getPreferences() {
        return getRandomDefinition().getRecommendedPreferences().stream().map(IRandomPreference::valueOf)
                .collect(Collectors.toSet());
    }

    public Set<String> getMandatoryPerks() {
        return getValues(mandatoryPerks);
    }

    public void setMandatoryPerks(String mandatoryPerks) {
        this.mandatoryPerks = mandatoryPerks;
    }

    public Set<String> getSuggestedPerks() {
        return getValues(suggestedPerks);
    }

    public void setSuggestedPerks(String suggestedPerks) {
        this.suggestedPerks = suggestedPerks;
    }

    public Set<String> getMandatoryCapabilities() {
        return getValues(mandatoryCapabilities);
    }

    public void setMandatoryCapabilities(String mandatoryCapabilities) {
        this.mandatoryCapabilities = mandatoryCapabilities;
    }

    public Set<String> getSuggestedCapabilities() {
        return getValues(suggestedCapabilities);
    }

    public void setSuggestedCapabilities(String suggestedCapabilities) {
        this.suggestedCapabilities = suggestedCapabilities;
    }

    public Set<String> getMandatorySkills() {
        return getValues(mandatorySkills);
    }

    public void setMandatorySkills(String mandatorySkills) {
        this.mandatorySkills = mandatorySkills;
    }

    public Set<String> getSuggestedSkills() {
        return getValues(suggestedSkills);
    }

    public void setSuggestedSkills(String suggestedSkills) {
        this.suggestedSkills = suggestedSkills;
    }

    private Set<String> getValues(String values) {
        final Set<String> result = new HashSet<>();
        readCommaSeparatedTokens(result, values);
        return result;
    }
}
