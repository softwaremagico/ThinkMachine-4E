package com.softwaremagico.tm.character;

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

import com.softwaremagico.tm.character.callings.CallingCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.combat.CombatActionRequirement;
import com.softwaremagico.tm.character.equipment.CharacterSelectedEquipment;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.item.Item;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.FactionCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidCharacteristicException;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.MaxInitialValueExceededException;
import com.softwaremagico.tm.txt.CharacterSheet;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CharacterPlayer {
    private static final int MAX_INITIAL_VALUE = 8;
    private static final int BANK_INITIAL_VALUE = 5;

    // Basic description of the character.
    private CharacterInfo info;

    private String specie;
    private int level = 1;

    private UpbringingCharacterDefinitionStepSelection upbringing;
    private FactionCharacterDefinitionStepSelection faction;
    private CallingCharacterDefinitionStepSelection calling;

    private Set<Equipment<?>> equipmentPurchased;


    private final Settings settings;

    public CharacterPlayer() {
        settings = new Settings();
        reset();
    }

    private void reset() {
        info = new CharacterInfo();
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
        try {
            if (this.upbringing != null) {
                this.upbringing.validate();
            }
        } catch (InvalidSelectionException e) {
            setUpbringing((String) null);
        }
    }

    public FactionCharacterDefinitionStepSelection getFaction() {
        return faction;
    }


    public UpbringingCharacterDefinitionStepSelection getUpbringing() {
        return upbringing;
    }

    public void setUpbringing(UpbringingCharacterDefinitionStepSelection upbringing) {
        this.upbringing = upbringing;
    }

    public void setUpbringing(String upbringing) {
        if (upbringing != null) {
            this.upbringing = new UpbringingCharacterDefinitionStepSelection(this, upbringing);
            try {
                this.upbringing.validate();
            } catch (InvalidSelectionException e) {
                this.upbringing = null;
                throw e;
            }
        } else {
            this.upbringing = null;
        }
        try {
            if (this.faction != null) {
                this.faction.validate();
            }
        } catch (InvalidSelectionException e) {
            setFaction((String) null);
        }

        try {
            if (this.calling != null) {
                this.calling.validate();
            }
        } catch (InvalidSelectionException e) {
            setCalling((String) null);
        }
    }


    public void setFaction(FactionCharacterDefinitionStepSelection faction) {
        this.faction = faction;
    }

    public void setFaction(String faction) {
        if (faction != null) {
            this.faction = new FactionCharacterDefinitionStepSelection(this, faction);
            try {
                this.faction.validate();
            } catch (InvalidSelectionException e) {
                this.faction = null;
                throw e;
            }
        } else {
            this.faction = null;
        }
        try {
            if (this.calling != null) {
                this.calling.validate();
            }
        } catch (InvalidSelectionException e) {
            setCalling((String) null);
        }
    }

    public CallingCharacterDefinitionStepSelection getCalling() {
        return calling;
    }

    public void setCalling(CallingCharacterDefinitionStepSelection calling) {
        this.calling = calling;
    }

    public void setCalling(String calling) {
        if (calling != null) {
            this.calling = new CallingCharacterDefinitionStepSelection(this, calling);
            try {
                this.calling.validate();
            } catch (InvalidSelectionException e) {
                this.calling = null;
                throw e;
            }
        } else {
            this.calling = null;
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public int getSkillValue(Skill skill) throws MaxInitialValueExceededException {
        if (skill == null) {
            return 0;
        }
        return getSkillValue(skill.getId());
    }

    public int getSkillValue(String skill) throws MaxInitialValueExceededException {
        int bonus = 0;
        if (SkillFactory.getInstance().getElement(skill).isNatural()) {
            bonus += Skill.NATURAL_SKILL_INITIAL_VALUE;
        }
        bonus += upbringing.getSkillBonus(skill);
        bonus += faction.getSkillBonus(skill);
        bonus += calling.getSkillBonus(skill);
        if (bonus > MAX_INITIAL_VALUE) {
            throw new MaxInitialValueExceededException("Skill '" + skill + "' has exceeded the maximum value of .",
                    bonus, MAX_INITIAL_VALUE);
        }
        return bonus;
    }

    public int getCharacteristicValue(CharacteristicName characteristic) throws MaxInitialValueExceededException {
        if (characteristic == null) {
            return 0;
        }
        return getCharacteristicValue(characteristic.getId());
    }

    public int getCharacteristicValue(String characteristic) throws MaxInitialValueExceededException {
        final CharacteristicName characteristicName = CharacteristicName.get(characteristic);
        if (characteristicName == null) {
            throw new InvalidCharacteristicException("No characteristic '" + characteristic + "' exists.");
        }
        int bonus = characteristicName.getInitialValue();
        //bonus += specie.getCharacteristicBonus(characteristic);
        bonus += upbringing.getCharacteristicBonus(characteristic);
        bonus += faction.getCharacteristicBonus(characteristic);
        bonus += calling.getCharacteristicBonus(characteristic);
        if (bonus > MAX_INITIAL_VALUE) {
            throw new MaxInitialValueExceededException("Characteristic '" + characteristic + "' has exceeded the maximum value of .",
                    bonus, MAX_INITIAL_VALUE);
        }
        return bonus;
    }

    public CombatActionRequirement getCharacteristicCombatValue(String id) {
        return null;
    }

    /**
     * Gets the current rank of the status of a character.
     *
     * @return the status of the character.
     */
    public String getRank() {
        return null;
    }

    public List<String> getPerks() {
        return new ArrayList<>();
    }

    public CharacterInfo getInfo() {
        return info;
    }

    public Integer getVitalityValue() throws InvalidXmlElementException {
        return getCharacteristicValue(CharacteristicName.ENDURANCE)
                + getCharacteristicValue(CharacteristicName.WILL)
                + getCharacteristicValue(CharacteristicName.FAITH)
                + SpecieFactory.getInstance().getElement(specie).getSize()
                + getLevel();
    }

    public Set<Capability> getCapabilities() {
        final Set<Capability> capabilities = new HashSet<>();
        if (upbringing != null) {
            upbringing.getCapabilityOptions().forEach(capabilityOption -> {
                capabilityOption.getSelections().forEach(selection -> {
                    capabilities.add(CapabilityFactory.getInstance().getElement(selection));
                });
            });
        }
        return capabilities;
    }


    public String getCompleteNameRepresentation() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (getInfo() != null) {
            stringBuilder.append(getInfo().getNameRepresentation());
            stringBuilder.append(" ");
            if (getInfo() != null && getInfo().getSurname() != null) {
                stringBuilder.append(getInfo().getSurname().getName());
            }
        }
        return stringBuilder.toString().trim();
    }

    public int getBodyResistance() {
        if (getArmor() != null) {
            return getArmor().getProtection();
        }
        return 0;
    }

    public int getMindResistance() {
        //return perks.
        return 0;
    }

    public int getSpiritResistance() {
        //return perks.
        return 0;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBank() throws InvalidXmlElementException {
        return BANK_INITIAL_VALUE;
    }

    public int getSurgesRating() throws InvalidXmlElementException {
        return Math.max(Math.max(getCharacteristicValue(CharacteristicName.STRENGTH),
                        getCharacteristicValue(CharacteristicName.WITS)),
                getCharacteristicValue(CharacteristicName.FAITH))
                + getLevel();
    }

    public int getSurgesNumber() throws InvalidXmlElementException {
        return 1;
    }

    public int getRevivalsRating() throws InvalidXmlElementException {
        return Math.max(Math.max(getCharacteristicValue(CharacteristicName.STRENGTH),
                        getCharacteristicValue(CharacteristicName.WITS)),
                getCharacteristicValue(CharacteristicName.FAITH))
                + getLevel();
    }

    public int getRevivalsNumber() throws InvalidXmlElementException {
        return 1;
    }

    /**
     * Gets best shield purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public Shield getShield() {
        final List<Shield> shields = getEquipment(Shield.class);
        if (shields.isEmpty()) {
            return null;
        }
        return Collections.max(shields, Comparator.comparing(Shield::getCost));
    }

    /**
     * Gets best armor purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public Armor getArmor() {
        final List<Armor> armors = getEquipment(Armor.class);
        if (armors.isEmpty()) {
            return null;
        }
        return Collections.max(armors, Comparator.comparing(Armor::getCost));
    }

    /**
     * Gets all weapons purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public List<Weapon> getWeapons() {
        return getEquipment(Weapon.class);
    }

    /**
     * Gets all items purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public List<Item> getItems() {
        return getEquipment(Item.class);
    }

    private Set<Equipment<?>> getSelectedMaterialAwards(CharacterDefinitionStepSelection<?> definitionStepSelection, XmlFactory<?> factory,
                                                        boolean ignoreRemoved) {
        final Set<String> selected;
        if (ignoreRemoved) {
            selected = definitionStepSelection.getMaterialAwards().stream().map(CharacterSelectedEquipment::getSelections)
                    .flatMap(Collection::stream).collect(Collectors.toSet());
        } else {
            selected = definitionStepSelection.getMaterialAwards().stream().map(CharacterSelectedEquipment::getRemainder)
                    .flatMap(Collection::stream).collect(Collectors.toSet());
        }
        return ((CharacterDefinitionStep<?>) factory.getElement(definitionStepSelection.getId())).getMaterialAwards(selected);
    }

    public List<Equipment<?>> getMaterialAwardsSelected() {
        return getMaterialAwardsSelected(false);
    }

    public List<Equipment<?>> getMaterialAwardsSelected(boolean ignoreRemoved) {
        final List<Equipment<?>> materialAwards = new ArrayList<>();
        materialAwards.addAll(getSelectedMaterialAwards(upbringing, UpbringingFactory.getInstance(), ignoreRemoved));
        materialAwards.addAll(getSelectedMaterialAwards(faction, FactionFactory.getInstance(), ignoreRemoved));
        materialAwards.addAll(getSelectedMaterialAwards(calling, CallingFactory.getInstance(), ignoreRemoved));
        return materialAwards;
    }

    public <T extends Equipment<?>> List<T> getEquipmentPurchased(Class<T> equipmentClass) {
        return getEquipmentPurchased().stream().filter(equipmentClass::isInstance).map(equipmentClass::cast).collect(Collectors.toList());
    }

    public Set<Equipment<?>> getEquipmentPurchased() {
        if (equipmentPurchased == null) {
            return new HashSet<>();
        }
        return equipmentPurchased;
    }

    public void addEquipmentPurchased(Equipment<?> equipmentPurchased) {
        if (this.equipmentPurchased == null) {
            this.equipmentPurchased = new HashSet<>();
        }
        this.equipmentPurchased.add(equipmentPurchased);
    }

    public void setEquipmentPurchased(Set<Equipment<?>> equipmentPurchased) {
        this.equipmentPurchased = equipmentPurchased;
    }

    public List<Equipment<?>> getEquipment() {
        final List<Equipment<?>> totalEquipment = new ArrayList<>();
        totalEquipment.addAll(getMaterialAwardsSelected());
        totalEquipment.addAll(getEquipmentPurchased());
        return totalEquipment;
    }

    public <T extends Equipment<?>> List<T> getEquipment(Class<T> equipmentClass) {
        return getEquipment().stream().filter(equipmentClass::isInstance).map(equipmentClass::cast).collect(Collectors.toList());
    }

    public String getRepresentation() {
        final CharacterSheet characterSheet = new CharacterSheet(this);
        return characterSheet.toString();
    }

    public int getTechgnosisLevel() {
        return getLevel();
    }

    @Override
    public String toString() {
        final String name = getCompleteNameRepresentation();
        if (!name.isEmpty()) {
            return name;
        }
        return super.toString();
    }

    public int getStartingValue(CharacteristicName characteristicName) {
        return Characteristic.INITIAL_VALUE;
    }
}
