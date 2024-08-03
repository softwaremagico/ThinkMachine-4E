package com.softwaremagico.tm.character.factions;

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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@JacksonXmlRootElement(localName = "faction")
public class Faction extends Element<Faction> {
    private FactionGroup factionGroup;
    private final Set<FactionRankTranslation> ranksTranslations = new HashSet<>();
    private Set<String> blessings = null;
    private Set<String> benefices = null;
    @JsonProperty("suggestedBenefices")
    private List<BeneficeOption> suggestedBenefices = null;
    @JsonProperty("restrictedBenefices")
    private List<BeneficeOption> restrictedBenefices = null;
    private Boolean isOnlyForHuman;

    public Faction() {
    }

    public Faction(String id, TranslatedText name, TranslatedText description, FactionGroup factionGroup, String language,
                   String moduleName) {
        super(id, name, description, language, moduleName);
        this.factionGroup = factionGroup;
    }

    public FactionGroup getFactionGroup() {
        return factionGroup;
    }

    public void setFactionGroup(FactionGroup factionGroup) {
        this.factionGroup = factionGroup;
    }

    public void addRankTranslation(FactionRankTranslation factionRank) {
        ranksTranslations.add(factionRank);
    }

    public Set<FactionRankTranslation> getRanksTranslations() {
        return ranksTranslations;
    }

    public FactionRankTranslation getRankTranslation(String rankId) {
        for (final FactionRankTranslation factionRankTranslation : getRanksTranslations()) {
            if (Objects.equals(factionRankTranslation.getId(), rankId)) {
                return factionRankTranslation;
            }
        }
        return null;
    }

    public Set<String> getBlessings() {
        return this.blessings;
    }


    public Set<String> getBenefices() {
        return this.benefices;
    }

    public List<BeneficeOption> getSuggestedBenefices() {
        return this.suggestedBenefices;
    }

    public List<BeneficeOption> getRestrictedBenefices() {
        return this.restrictedBenefices;
    }

    public void setBlessings(String blessings) {
        this.blessings =  Collections.singleton(blessings);
    }

    public void setBlessings(Set<String> blessings) {
        this.blessings = blessings;
    }

    public void setBenefices(String benefices) {
        this.benefices =  Collections.singleton(benefices);
    }

    public void setBenefices(Set<String> benefices) {
        this.benefices = benefices;
    }

    public void setSuggestedBenefices(List<BeneficeOption> suggestedBenefices) {
        this.suggestedBenefices = suggestedBenefices;
    }


    public void setRestrictedBenefices(List<BeneficeOption> restrictedBenefices) {
        this.restrictedBenefices = restrictedBenefices;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public boolean isOnlyForHuman() {
        if (isOnlyForHuman == null) {
            isOnlyForHuman = getRestrictions().getRestrictedToRaces().size() == 1
                    && getRestrictions().getRestrictedToRaces().contains("human");

        }
        return isOnlyForHuman;
    }
}
