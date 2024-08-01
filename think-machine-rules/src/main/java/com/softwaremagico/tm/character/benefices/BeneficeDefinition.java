package com.softwaremagico.tm.character.benefices;

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


import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class BeneficeDefinition extends Element<BeneficeDefinition> {
    private List<Integer> cost = new ArrayList<>();
    private Set<BeneficeSpecialization> specializations = new HashSet<>();
    private Set<String> incompatibleWith = new HashSet<>();
    private BeneficeGroup group;
    private boolean affliction;

    public BeneficeDefinition() {
        super();
    }

    public BeneficeDefinition(String id, TranslatedText name, TranslatedText description, String language, String moduleName,
                              List<Integer> cost, BeneficeGroup group, boolean affliction, Set<String> incompatibleWith) {
        super(id, name, description, language, moduleName);
        this.cost = cost;
        this.group = group;
        this.affliction = affliction;
        this.incompatibleWith.addAll(incompatibleWith);
    }

    public List<Integer> getCost() {
        return cost;
    }

    public Set<BeneficeSpecialization> getSpecializations() {
        return specializations;
    }

    public BeneficeGroup getGroup() {
        return group;
    }

    public void addSpecializations(Set<BeneficeSpecialization> specializations) {
        this.specializations.addAll(specializations);
    }

    public BeneficeClassification getBeneficeClassification() {
        if (affliction) {
            return BeneficeClassification.AFFLICTION;
        }
        return BeneficeClassification.BENEFICE;
    }

    public void setCost(String costRange) {
        final List<Integer> costs = new ArrayList<>();
        if (costRange != null) {
            if (costRange.contains("-")) {
                final int minValue = Integer.parseInt(costRange.substring(0, costRange.indexOf('-')));
                final int maxValue = Integer.parseInt(costRange.substring(costRange.indexOf('-') + 1));
                for (int i = minValue; i <= maxValue; i++) {
                    costs.add(i);
                }
            } else if (costRange.contains(",")) {
                final StringTokenizer costsOfBenefice = new StringTokenizer(costRange, ",");
                while (costsOfBenefice.hasMoreTokens()) {
                    costs.add(Integer.parseInt(costsOfBenefice.nextToken().trim()));
                }
            } else {
                costs.add(Integer.parseInt(costRange));
            }
        }

        this.cost = costs;
    }

    public void setCosts(List<Integer> costs) {
        this.cost = costs;
    }

    public void setSpecializations(Set<BeneficeSpecialization> specializations) {
        this.specializations = specializations;
    }

    public void setIncompatibleWith(String incompatibleWithContent) {
        incompatibleWith = new HashSet<>();
        readCommaSeparatedTokens(incompatibleWith, incompatibleWithContent);
    }

    public void setIncompatibleWith(Set<String> incompatibleWith) {
        this.incompatibleWith = incompatibleWith;
    }

    public void setGroup(BeneficeGroup group) {
        this.group = group;
    }

    public void setAffliction(boolean affliction) {
        this.affliction = affliction;
    }

    public boolean isAffliction() {
        return affliction;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Set<String> getIncompatibleWith() {
        return incompatibleWith;
    }


}
