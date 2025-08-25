package com.softwaremagico.tm.random.character.names;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
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

import com.softwaremagico.tm.character.Rank;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.character.selectors.IGaussianDistribution;

public enum NamesProbability implements IGaussianDistribution {

    // Gaussian distribution.
    LOW(1, 1, 1, 1),

    FAIR(1, 2, 1, 1),

    HIGH(2, 3, 1, 1),

    VERY_HIGH(2, 4, 3, 1);

    private static final int STATUS_COST_MODIFIER = 4;

    private final int minimum;
    private final int maximum;
    private final int mean;
    private final int variance;

    NamesProbability(int minimumValue, int maximumValue, int mean, int variance) {
        this.maximum = maximumValue;
        this.minimum = minimumValue;
        this.variance = variance;
        this.mean = mean;
    }

    @Override
    public int maximum() {
        return maximum;
    }

    @Override
    public int minimum() {
        return minimum;
    }

    @Override
    public int variance() {
        return variance;
    }

    @Override
    public int mean() {
        return mean;
    }

    @Override
    public int randomGaussian() {
        int selectedValue;
        do {
            selectedValue = (int) (RandomSelector.RANDOM.nextGaussian() * Math.sqrt(variance) + mean);
        } while (selectedValue < minimum() || selectedValue > maximum());
        return selectedValue;
    }

    public static NamesProbability getByStatus(Rank rank) {
        int index = rank != null ? rank.getLevel() : 0;

        if (index < 0) {
            index = 0;
        }
        if (index > NamesProbability.values().length - 1) {
            index = NamesProbability.values().length - 1;
        }
        return NamesProbability.values()[index];
    }
}
