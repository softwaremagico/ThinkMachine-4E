package com.softwaremagico.tm.character.characteristics;


public class CharacteristicImprovement {
    private CharacteristicDefinition characteristic;
    private int bonus;
    private boolean always;

    public CharacteristicImprovement(CharacteristicDefinition characteristic, int bonus, boolean always) {
        super();
        this.characteristic = characteristic;
        this.bonus = bonus;
        this.always = always;
    }

    public CharacteristicDefinition getCharacteristic() {
        return characteristic;
    }

    public int getBonus() {
        return bonus;
    }

    public boolean isAlways() {
        return always;
    }
}
