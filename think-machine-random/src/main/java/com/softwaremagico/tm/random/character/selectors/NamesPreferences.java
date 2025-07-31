package com.softwaremagico.tm.random.character.selectors;

import com.softwaremagico.tm.random.RandomSelector;

import java.util.Set;

public enum NamesPreferences implements IGaussianDistribution, IRandomPreference<NamesPreferences> {

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

    NamesPreferences(int minimumValue, int maximumValue, int mean, int variance) {
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

    public static NamesPreferences getSelected(Set<IRandomPreference<?>> preferences) {
        for (final IRandomPreference<?> preference : preferences) {
            if (preference instanceof NamesPreferences) {
                return (NamesPreferences) preference;
            }
        }
        return LOW;
    }

    @Override
    public int randomGaussian() {
        int selectedValue;
        do {
            selectedValue = (int) (RandomSelector.RANDOM.nextGaussian() * Math.sqrt(variance) + mean);
        } while (selectedValue < minimum() || selectedValue > maximum());
        return selectedValue;
    }

    public static NamesPreferences getByStatus(int statusCost) {
        int index = (statusCost / STATUS_COST_MODIFIER) - 1;

        if (index < 0) {
            index = 0;
        }
        if (index > NamesPreferences.values().length - 1) {
            index = NamesPreferences.values().length - 1;
        }
        return NamesPreferences.values()[index];
    }

    @Override
    public IRandomPreference<NamesPreferences> getDefault() {
        return getDefaultOption();
    }

    public static IRandomPreference<NamesPreferences> getDefaultOption() {
        return null;
    }
}
