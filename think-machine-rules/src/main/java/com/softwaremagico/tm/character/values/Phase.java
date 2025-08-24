package com.softwaremagico.tm.character.values;

public enum Phase {
    SPECIE(0),
    UPBRINGING(1),
    FACTION(2),
    CALLING(3),
    LEVEL(4);

    private final int index;

    Phase(int index) {
        this.index = index;
    }

    public boolean isCheckedPhase(Phase phase) {
        if (phase != null) {
            return index < phase.index;
        }
        return false;
    }
}
