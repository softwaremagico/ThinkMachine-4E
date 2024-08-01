package com.softwaremagico.tm.character.skills;

/**
 * A Skill that has been chosen by a player. It has rank values and in some
 * cases a specific generalization.
 */
public class SelectedSkill extends Skill<SelectedSkill> {
    private final int value;
    // Special are represented with a '*' in the character sheet. Does not count
    // for cost calculation.
    private final boolean cost;
    private final AvailableSkill availableSkill;

    public SelectedSkill(AvailableSkill availableSkill, int value, boolean cost) {
        super(availableSkill.getUniqueId(), availableSkill.getCompleteName(), availableSkill.getDescription(),
                availableSkill.getLanguage(), availableSkill.getModuleName());
        this.availableSkill = availableSkill;
        this.value = value;
        this.cost = cost;
    }

    public int getValue() {
        return value;
    }

    public AvailableSkill getAvailableSkill() {
        return availableSkill;
    }

    public boolean hasCost() {
        return cost;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + value + ")";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public boolean isOfficial() {
        return availableSkill.isOfficial();
    }
}
