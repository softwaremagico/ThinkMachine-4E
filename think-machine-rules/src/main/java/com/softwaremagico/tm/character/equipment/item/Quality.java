package com.softwaremagico.tm.character.equipment.item;

public enum Quality {
    PREMIUM(1.3),
    MASTERWORK(1.2),
    SUPERIOR(1.1),
    STANDARD(1),
    POOR_WORKMANSHIP(0.9),
    UNRELIABLE(0.8),
    DISREPAIR(0.7);

    private final double cost;

    Quality(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }
}
