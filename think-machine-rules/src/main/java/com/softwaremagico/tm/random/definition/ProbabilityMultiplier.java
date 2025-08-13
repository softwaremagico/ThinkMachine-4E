package com.softwaremagico.tm.random.definition;

public enum ProbabilityMultiplier {
    EXOTIC(0.1),
    RARE(1),
    NORMAL(10),
    COMMON(30),
    PREFERRED(100);

    private final double value;

    ProbabilityMultiplier(double value) {
        this.value = value;
    }

    public static RandomProbabilityDefinition get(String probabilityName) {
        for (final RandomProbabilityDefinition probability : RandomProbabilityDefinition.values()) {
            if (probability.name().equalsIgnoreCase(probabilityName)) {
                return probability;
            }
        }
        return null;
    }

    public double getValue() {
        return value;
    }
}
