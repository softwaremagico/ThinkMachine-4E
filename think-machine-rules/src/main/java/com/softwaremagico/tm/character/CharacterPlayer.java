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
import com.softwaremagico.tm.character.callings.CallingGroup;
import com.softwaremagico.tm.character.capabilities.CapabilityWithSpecialization;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.combat.CombatActionRequirement;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.cybernetics.Cybernetics;
import com.softwaremagico.tm.character.equipment.CharacterSelectedEquipment;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.equipment.EquipmentOption;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShield;
import com.softwaremagico.tm.character.equipment.item.Item;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.FactionCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.occultism.Occultism;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.perks.Affliction;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.perks.SpecializedPerk;
import com.softwaremagico.tm.character.resistances.Resistance;
import com.softwaremagico.tm.character.resistances.ResistanceType;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.specie.SpecieCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidCharacteristicException;
import com.softwaremagico.tm.exceptions.InvalidCyberdeviceException;
import com.softwaremagico.tm.exceptions.InvalidOccultismPowerException;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.MaxInitialValueExceededException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialCharacterException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.txt.CharacterSheet;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CharacterPlayer {
    public static final int MAX_INITIAL_VALUE = 8;
    private static final int BANK_INITIAL_VALUE = 5;
    private static final int INITIAL_TECH_LEVEL = 4;

    // Basic description of the character.
    private CharacterInfo info;

    private int level = 1;

    private String primaryCharacteristic;
    private String secondaryCharacteristic;

    private SpecieCharacterDefinitionStepSelection specie;
    private UpbringingCharacterDefinitionStepSelection upbringing;
    private FactionCharacterDefinitionStepSelection faction;
    private CallingCharacterDefinitionStepSelection calling;

    // All Psi/Theurgy powers
    private Occultism occultism;

    private Cybernetics cybernetics;

    private Set<Equipment> equipmentPurchased;

    private final Settings settings;

    private Affliction affliction;

    public CharacterPlayer() {
        settings = new Settings();
        reset();
    }

    private void reset() {
        info = new CharacterInfo();
        occultism = new Occultism();
        cybernetics = new Cybernetics();
        specie = null;
        upbringing = null;
        faction = null;
        calling = null;
        equipmentPurchased = new HashSet<>();
    }

    public SpecieCharacterDefinitionStepSelection getSpecie() {
        return specie;
    }

    public void validate() {
        if (specie != null) {
            this.specie.validate();
        }
        if (upbringing != null) {
            this.upbringing.validate();
        }
        if (faction != null) {
            this.faction.validate();
        }
        if (calling != null) {
            this.calling.validate();
        }
        if (getPrimaryCharacteristic() == null || getSecondaryCharacteristic() == null) {
            throw new InvalidCharacteristicException("You must choose your primary and secondary characteristic.");
        }
        //Check one affliction max only.
        //coas = getPerks();
    }

    public void setSpecie(String specie) {
        if (specie != null) {
            this.specie = new SpecieCharacterDefinitionStepSelection(this, specie);
            try {
                this.specie.validate();
            } catch (InvalidSelectionException e) {
                this.specie = null;
                throw e;
            }
        } else {
            this.specie = null;
        }
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
        if (upbringing != null) {
            bonus += upbringing.getSkillBonus(skill);
        }
        if (faction != null) {
            bonus += faction.getSkillBonus(skill);
        }
        if (calling != null) {
            bonus += calling.getSkillBonus(skill);
        }
        if (bonus > MAX_INITIAL_VALUE) {
            throw new MaxInitialValueExceededException("Skill '" + skill + "' has exceeded the maximum value of '" + MAX_INITIAL_VALUE + "' .",
                    bonus, MAX_INITIAL_VALUE);
        }
        return bonus;
    }

    public int getCharacteristicValue(CharacteristicName characteristic) throws MaxInitialValueExceededException {
        if (characteristic == null) {
            return 0;
        }
        try {
            return getCharacteristicValue(characteristic.getId());
        } catch (MaxInitialValueExceededException e) {
            MachineLog.warning(this.getClass(), e.getMessage());
            return MAX_INITIAL_VALUE;
        }
    }

    public String getPrimaryCharacteristic() {
        return primaryCharacteristic;
    }

    public void setPrimaryCharacteristic(String primaryCharacteristic) {
        this.primaryCharacteristic = primaryCharacteristic;
    }

    public String getSecondaryCharacteristic() {
        return secondaryCharacteristic;
    }

    public void setSecondaryCharacteristic(String secondaryCharacteristic) {
        this.secondaryCharacteristic = secondaryCharacteristic;
    }

    public int getCharacteristicValue(String characteristic) throws MaxInitialValueExceededException {
        final CharacteristicName characteristicName = CharacteristicName.get(characteristic);
        if (characteristicName == null) {
            throw new InvalidCharacteristicException("No characteristic '" + characteristic + "' exists.");
        }
        int bonus;
        if (getPrimaryCharacteristic() != null && Objects.equals(getPrimaryCharacteristic(), characteristic)) {
            bonus = CharacteristicDefinition.PRIMARY_CHARACTERISTIC_VALUE;
        } else if (getSecondaryCharacteristic() != null && Objects.equals(getSecondaryCharacteristic(), characteristic)) {
            bonus = CharacteristicDefinition.SECONDARY_CHARACTERISTIC_VALUE;
        } else if (specie != null) {
            bonus = SpecieFactory.getInstance().getElement(getSpecie()).getSpecieCharacteristic(characteristic).getInitialValue();
        } else {
            bonus = Characteristic.INITIAL_VALUE;
        }
        if (upbringing != null) {
            bonus += upbringing.getCharacteristicBonus(characteristic);
        }
        if (faction != null) {
            bonus += faction.getCharacteristicBonus(characteristic);
        }
        if (calling != null) {
            bonus += calling.getCharacteristicBonus(characteristic);
        }
        if (getLevel() < 2 && bonus > MAX_INITIAL_VALUE) {
            throw new MaxInitialValueExceededException("Characteristic '" + characteristic + "' has exceeded the maximum value of '"
                    + bonus + "' with '" + MAX_INITIAL_VALUE + "'.", bonus, MAX_INITIAL_VALUE);
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
    public Rank getRank() {
        return null;
    }

    public List<SpecializedPerk> getPerks() {
        final List<SpecializedPerk> perks = new ArrayList<>();
        if (upbringing != null) {
            upbringing.getSelectedPerksOptions().forEach(perkOption ->
                    perkOption.getSelections().forEach(selection -> {
                        try {
                            perks.add(new SpecializedPerk(PerkFactory.getInstance().getElement(selection), selection.getSpecialization()));
                        } catch (Exception e) {
                            MachineLog.warning(this.getClass(), e.getMessage());
                        }
                    }));
        }
        if (faction != null) {
            faction.getSelectedPerksOptions().forEach(perkOption ->
                    perkOption.getSelections().forEach(selection -> {
                        try {
                            perks.add(new SpecializedPerk(PerkFactory.getInstance().getElement(selection), selection.getSpecialization()));
                        } catch (Exception e) {
                            MachineLog.warning(this.getClass(), e.getMessage());
                        }
                    }));
        }
        if (calling != null) {
            calling.getSelectedPerksOptions().forEach(perkOption ->
                    perkOption.getSelections().forEach(selection -> {
                        try {
                            perks.add(new SpecializedPerk(PerkFactory.getInstance().getElement(selection), selection.getSpecialization()));
                        } catch (Exception e) {
                            MachineLog.warning(this.getClass(), e.getMessage());
                        }
                    }));
        }
        return perks;
    }

    public CharacterInfo getInfo() {
        return info;
    }

    public Integer getVitalityValue() throws InvalidXmlElementException {
        return getCharacteristicValue(CharacteristicName.ENDURANCE)
                + getCharacteristicValue(CharacteristicName.WILL)
                + getCharacteristicValue(CharacteristicName.FAITH)
                + (specie != null ? SpecieFactory.getInstance().getElement(specie).getSize() : 0)
                + getLevel();
    }

    public Set<CapabilityWithSpecialization> getCapabilitiesWithSpecialization() {
        final Set<CapabilityWithSpecialization> capabilities = new HashSet<>();
        if (upbringing != null) {
            upbringing.getSelectedCapabilityOptions().forEach(capabilityOption ->
                    capabilityOption.getSelections().forEach(selection ->
                            capabilities.add(CapabilityWithSpecialization.from(selection))));
        }
        if (faction != null) {
            faction.getSelectedCapabilityOptions().forEach(capabilityOption ->
                    capabilityOption.getSelections().forEach(selection ->
                            capabilities.add(CapabilityWithSpecialization.from(selection))));
        }
        if (calling != null) {
            calling.getSelectedCapabilityOptions().forEach(capabilityOption ->
                    capabilityOption.getSelections().forEach(selection ->
                            capabilities.add(CapabilityWithSpecialization.from(selection))));
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
        return Resistance.getBonus(ResistanceType.BODY, this);
    }

    public int getMindResistance() {
        return Resistance.getBonus(ResistanceType.MIND, this);
    }

    public int getSpiritResistance() {
        return Resistance.getBonus(ResistanceType.SPIRIT, this);
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

    private Set<EquipmentOption> getSelectedMaterialAwards(CharacterDefinitionStepSelection definitionStepSelection, XmlFactory<?> factory,
                                                           boolean ignoreRemoved) {
        if (definitionStepSelection == null) {
            return new HashSet<>();
        }
        final Set<Selection> selected;
        if (ignoreRemoved) {
            selected = definitionStepSelection.getSelectedMaterialAwards().stream().map(CharacterSelectedEquipment::getSelections)
                    .flatMap(Collection::stream).collect(Collectors.toSet());
        } else {
            selected = definitionStepSelection.getSelectedMaterialAwards().stream().map(CharacterSelectedEquipment::getRemainder)
                    .flatMap(Collection::stream).collect(Collectors.toSet());
        }
        return ((CharacterDefinitionStep<?>) factory.getElement(definitionStepSelection.getId())).getMaterialAwards(selected);
    }

    public List<EquipmentOption> getMaterialAwardsSelected() {
        return getMaterialAwardsSelected(false);
    }

    public List<EquipmentOption> getMaterialAwardsSelected(boolean ignoreRemoved) {
        final List<EquipmentOption> materialAwards = new ArrayList<>();
        materialAwards.addAll(getSelectedMaterialAwards(upbringing, UpbringingFactory.getInstance(), ignoreRemoved));
        materialAwards.addAll(getSelectedMaterialAwards(faction, FactionFactory.getInstance(), ignoreRemoved));
        materialAwards.addAll(getSelectedMaterialAwards(calling, CallingFactory.getInstance(), ignoreRemoved));
        return materialAwards;
    }

    public <T extends Equipment> List<T> getEquipmentPurchased(Class<T> equipmentClass) {
        return getEquipmentPurchased().stream().filter(equipmentClass::isInstance).map(equipmentClass::cast).collect(Collectors.toList());
    }

    public Set<Equipment> getEquipmentPurchased() {
        if (equipmentPurchased == null) {
            equipmentPurchased = new HashSet<>();
        }
        return equipmentPurchased;
    }

    public void addEquipmentPurchased(Equipment equipmentPurchased) {
        if (this.equipmentPurchased == null) {
            this.equipmentPurchased = new HashSet<>();
        }
        this.equipmentPurchased.add(equipmentPurchased);
    }

    public void setEquipmentPurchased(Set<Equipment> equipmentPurchased) {
        this.equipmentPurchased = equipmentPurchased;
    }

    /**
     * Gets best armor purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public Armor getBestArmor() {
        final List<Armor> armors = getEquipment(Armor.class);
        if (armors.isEmpty()) {
            return null;
        }
        return Collections.max(armors, Comparator.comparing(Armor::getCost));
    }

    public Armor getPurchasedArmor() {
        return getEquipmentPurchased(Armor.class).stream().findFirst().orElse(null);
    }

    public void setPurchasedArmor(Armor armor) throws UnofficialElementNotAllowedException {
        if (armor != null && !armor.isOfficial() && getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("Armor '" + armor + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        getEquipmentPurchased(Armor.class).forEach(e -> getEquipmentPurchased().remove(e));
        getEquipmentPurchased().add(armor);
    }

    /**
     * Gets best shield purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public Shield getBestShield() {
        final List<Shield> shields = getEquipment(Shield.class);
        if (shields.isEmpty()) {
            return null;
        }
        return Collections.max(shields, Comparator.comparing(Shield::getCost));
    }

    /**
     * Gets best shield purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public HandheldShield getBestHandHandledShield() {
        final List<HandheldShield> handheldShields = getEquipment(HandheldShield.class);
        if (handheldShields.isEmpty()) {
            return null;
        }
        return Collections.max(handheldShields, Comparator.comparing(HandheldShield::getCost));
    }

    public void setPurchasedHandheldShield(HandheldShield handheldShield) throws UnofficialElementNotAllowedException {
        if (handheldShield != null && !handheldShield.isOfficial() && getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("HandheldShields shield '" + handheldShield + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        getEquipmentPurchased(HandheldShield.class).forEach(e -> getEquipmentPurchased().remove(e));
        getEquipmentPurchased().add(handheldShield);
    }

    public Shield getPurchasedShield() {
        return getEquipmentPurchased(Shield.class).stream().findFirst().orElse(null);
    }

    public void setPurchasedShield(Shield shield) throws UnofficialElementNotAllowedException {
        if (shield != null && !shield.isOfficial() && getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("Shield '" + shield + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        getEquipmentPurchased(Shield.class).forEach(e -> getEquipmentPurchased().remove(e));
        getEquipmentPurchased().add(shield);
    }

    public List<Weapon> getPurchasedMeleeWeapons() {
        return getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isMeleeWeapon).collect(Collectors.toList());
    }

    public void setPurchasedMeleeWeapons(List<Weapon> weapons) throws UnofficialElementNotAllowedException {
        if (getSettings().isOnlyOfficialAllowed()) {
            for (Weapon weapon : weapons) {
                if (!weapon.isOfficial()) {
                    throw new UnofficialElementNotAllowedException("Weapon '" + weapon + "' is not official and cannot be added due "
                            + "to configuration limitations.");
                }
            }
        }
        getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isMeleeWeapon).forEach(e -> getEquipmentPurchased().remove(e));
        getEquipmentPurchased().addAll(weapons);
    }

    public List<Weapon> getPurchasedRangedWeapons() {
        return getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isRangedWeapon).collect(Collectors.toList());
    }

    public void setPurchasedRangedWeapons(List<Weapon> weapons) throws UnofficialElementNotAllowedException {
        if (getSettings().isOnlyOfficialAllowed()) {
            for (Weapon weapon : weapons) {
                if (!weapon.isOfficial()) {
                    throw new UnofficialElementNotAllowedException("Weapon '" + weapon + "' is not official and cannot be added due "
                            + "to configuration limitations.");
                }
            }
        }
        getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isRangedWeapon).forEach(e -> getEquipmentPurchased().remove(e));
        getEquipmentPurchased().addAll(weapons);
    }

    public boolean hasWeapon(Weapon weapon) {
        return getEquipmentPurchased(Weapon.class).stream().anyMatch(w -> Objects.equals(w, weapon));
    }

    public List<Equipment> getEquipment() {
        final List<Equipment> totalEquipment = new ArrayList<>();
        totalEquipment.addAll(getMaterialAwardsSelected().stream().map(EquipmentOption::getElement).collect(Collectors.toSet()));
        totalEquipment.addAll(getEquipmentPurchased());
        return totalEquipment;
    }

    public <T extends Equipment> List<T> getEquipment(Class<T> equipmentClass) {
        return getEquipment().stream().filter(equipmentClass::isInstance).map(equipmentClass::cast).collect(Collectors.toList());
    }

    public String getRepresentation() {
        final CharacterSheet characterSheet = new CharacterSheet(this);
        return characterSheet.toString();
    }

    public int getTechgnosisLevel() {
        return getLevel();
    }

    public int getTechLevel() {
        return INITIAL_TECH_LEVEL + (int) getCapabilitiesWithSpecialization().stream().filter(capability ->
                capability.getId().startsWith("techLore") && Objects.equals(capability.getGroup(), "techLore")).count();
    }

    public int getStartingValue(CharacteristicName characteristicName) {
        return Characteristic.INITIAL_VALUE;
    }

    public float getCashMoney() {
        return 0;
    }

    public float getRemainingCash() {
        return getCashMoney() - getSpentCash();
    }

    public float getSpentCash() {
        float total = 0;
        for (Equipment equipment : getEquipmentPurchased()) {
            if (equipment != null) {
                total += equipment.getCost();
            }
        }
        return total;
    }

    public void checkIsOfficial() throws UnofficialCharacterException {
        if ((getFaction() != null && !FactionFactory.getInstance().getElement(getFaction().getId()).isOfficial())) {
            throw new UnofficialCharacterException("Faction '" + getFaction() + "' is not official.");
        }
        if ((getSpecie() != null && !SpecieFactory.getInstance().getElement(getSpecie()).isOfficial())) {
            throw new UnofficialCharacterException("Specie '" + getSpecie() + "' is not official.");
        }
        if ((getBestArmor() != null && !getBestArmor().isOfficial())) {
            throw new UnofficialCharacterException("Armor '" + getBestArmor() + "' is not official.");
        }
        if ((getBestShield() != null && !getBestShield().isOfficial())) {
            throw new UnofficialCharacterException("Shield '" + getBestShield() + "' is not official.");
        }
        if (!getWeapons().stream().allMatch(Equipment::isOfficial)) {
            throw new UnofficialCharacterException("Equipment '" + getWeapons() + "' are not all official.");
        }
//
//        if (!cybernetics.getElements().stream().allMatch(SelectedCyberneticDevice::isOfficial)) {
//            throw new UnofficialCharacterException("Cybernetics '" + cybernetics + "' are not all official.");
//        }
//
        for (final String occultismPathId : occultism.getSelectedPowers().keySet()) {
            try {
                if (!OccultismPathFactory.getInstance().getElement(occultismPathId).isOfficial()) {
                    throw new UnofficialCharacterException("Occultism path '" + occultismPathId + "' is not official.");
                }
            } catch (InvalidXmlElementException e) {
                // Ignore.
            }
        }
    }

    public void checkIsNotRestricted() throws RestrictedElementException {
        if ((getFaction() != null && FactionFactory.getInstance().getElement(getFaction().getId()).getRestrictions().isRestricted(this))) {
            throw new RestrictedElementException("Faction '" + getFaction() + "' is restricted.");
        }
        if ((getSpecie() != null && SpecieFactory.getInstance().getElement(getSpecie()).getRestrictions().isRestricted(this))) {
            throw new RestrictedElementException("Specie '" + getSpecie() + "' is restricted.");
        }

        if ((getBestArmor() != null && ArmorFactory.getInstance().getElement(getBestArmor()).getRestrictions().isRestricted(this))) {
            throw new RestrictedElementException("Armor '" + getBestArmor() + "' is restricted.");
        }
        if ((getBestShield() != null && ShieldFactory.getInstance().getElement(getBestShield()).getRestrictions().isRestricted(this))) {
            throw new RestrictedElementException("Shield '" + getBestShield() + "' is restricted.");
        }

        if (!getWeapons().stream().allMatch(w -> w.getRestrictions().isRestricted(this))) {
            throw new RestrictedElementException("Weapons '" + getWeapons() + "' have some restricted element.");
        }
//
//        if (!blessings.stream().allMatch(b -> b.isRestricted(this))) {
//            throw new RestrictedElementException("Blessings '" + blessings + "' have some restricted element.");
//        }
//
//        if (!benefices.stream().allMatch(b -> b.isRestricted(this))) {
//            throw new RestrictedElementException("Benefices '" + benefices + "' have some restricted element.");
//        }
//
//        if (!cybernetics.getElements().stream().allMatch(c -> c.isRestricted(this))) {
//            throw new RestrictedElementException("Cybernetics '" + cybernetics + "' have some restricted element.");
//        }
//
        for (final String occultismPathId : occultism.getSelectedPowers().keySet()) {
            try {
                if (!OccultismPathFactory.getInstance().getElement(occultismPathId).getRestrictions().isRestricted(this)) {
                    throw new RestrictedElementException("Occultism path '" + occultismPathId + "' is restricted.");
                }
            } catch (InvalidXmlElementException e) {
                // Ignore.
            }
        }
    }


    private Occultism getOccultism() {
        return occultism;
    }

    public int getOccultismPointsAvailable() {
        return (int) getPerks().stream().filter(perk -> Objects.equals(perk.getId(), "theurgicRites")
                || Objects.equals(perk.getId(), "psychicPowers")).count();
    }

    public int getOccultismPointsSpent() {
        return getTotalSelectedPaths();
    }

    public int getOccultismLevel(OccultismType occultismType) {
        return getCharacteristicValue(occultismType.getId());
    }

    public int setOccultismLevel(OccultismType occultismType, int newValue) {
        return getCharacteristicValue(occultismType.getId());
    }

    public int getOccultismLevel() {
        try {
            int level = 0;
            for (final OccultismType occultismType : OccultismTypeFactory.getInstance().getElements()) {
                final int typeLevel = getOccultismLevel(occultismType);
                if (typeLevel > level) {
                    level = typeLevel;
                }
            }
            return level;
        } catch (InvalidXmlElementException e) {
            //Ignore
        }
        return 0;
    }

    public int getDarkSideLevel(OccultismType occultismType) {
        return getOccultism().getDarkSideLevel(occultismType);
    }

    public void setDarkSideLevel(OccultismType occultismType, int darkSideValue) {
        getOccultism().setDarkSideLevel(occultismType, darkSideValue);
    }

    public Map<String, List<OccultismPower>> getSelectedPowers() {
        return getOccultism().getSelectedPowers();
    }

    public int getTotalSelectedPowers() {
        return getOccultism().getTotalSelectedPowers();
    }

    public int getTotalSelectedPaths() {
        return getOccultism().getTotalSelectedPaths();
    }

    /**
     * Returns the selected option by the character.
     *
     * @return an occultism type or null if nothing has been selected.
     */
    public OccultismType getOccultismType() {
        if (getFaction() != null && (FactionGroup.get(getFaction().getGroup()) == FactionGroup.CHURCH
                || FactionGroup.get(getFaction().getGroup()) == FactionGroup.MINOR_CHURCH || Objects.equals(getFaction().getId(), "sibanzi")
                || Objects.equals(getFaction().getId(), "vagabonds") || Objects.equals(getFaction().getId(), "swordsOfLextius"))) {
            return OccultismTypeFactory.getTheurgy();
        }
        if (getFaction() != null && (Objects.equals(getFaction().getId(), "dervishes"))) {
            return OccultismTypeFactory.getPsi();
        }
        if (getSpecie() != null && (Objects.equals(getSpecie().getId(), "ascorbite"))) {
            return OccultismTypeFactory.getPsi();
        }
        if (getCalling() != null && (CallingGroup.get(getCalling().getGroup()) == CallingGroup.PSI)) {
            return OccultismTypeFactory.getPsi();
        }
        if (getCalling() != null && (CallingGroup.get(getCalling().getGroup()) == CallingGroup.THEURGY)) {
            return OccultismTypeFactory.getTheurgy();
        }
        try {
            //Check if has some path purchased already. Get its occultismType;
            if (!getOccultism().getSelectedPowers().isEmpty()) {
                final Map.Entry<String, List<OccultismPower>> occultismPowers = getOccultism().getSelectedPowers().entrySet().iterator().next();
                if (occultismPowers.getValue() != null && !occultismPowers.getValue().isEmpty()) {
                    final OccultismPower occultismPower = occultismPowers.getValue().iterator().next();
                    if (OccultismPathFactory.getInstance().getOccultismPath(occultismPower) != null) {
                        return OccultismTypeFactory.getInstance().getElement(OccultismPathFactory.getInstance()
                                .getOccultismPath(occultismPower).getOccultismType());
                    }
                }
            }
            //Check if has some occultism level added already.
            for (final OccultismType occultismType : OccultismTypeFactory.getInstance().getElements()) {
                int defaultOccultismLevel = 0;
                if (getSpecie() != null) {
                    if (occultismType.getId().equals(OccultismTypeFactory.PSI_TAG)) {
                        defaultOccultismLevel = SpecieFactory.getInstance().getElement(getSpecie())
                                .getSpecieCharacteristic(CharacteristicName.PSI).getInitialValue();
                    }
                    if (occultismType.getId().equals(OccultismTypeFactory.THEURGY_TAG)) {
                        defaultOccultismLevel = SpecieFactory.getInstance().getElement(getSpecie())
                                .getSpecieCharacteristic(CharacteristicName.THEURGY).getInitialValue();
                    }
                }
                if (getCharacteristicValue(occultismType.getId()) > defaultOccultismLevel) {
                    return occultismType;
                }
            }
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return null;
    }

    public boolean hasOccultismPower(OccultismPower power) {
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
        return getOccultism().hasPower(path, power);
    }

    public boolean canAddOccultismPower(OccultismPower power) {
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
        try {
            getOccultism().canAddPower(this, path, power,
                    getFaction() != null ? getFaction().getId() : null,
                    getSpecie() != null ? getSpecie().getId() : null, getSettings());
            return true;
        } catch (InvalidOccultismPowerException e) {
            return false;
        }
    }

    public void addOccultismPower(OccultismPower power) throws InvalidOccultismPowerException, UnofficialElementNotAllowedException {
        if (power == null) {
            throw new InvalidOccultismPowerException("Null value not allowed");
        }
        if (!power.isOfficial() && getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("Occultism Power '" + power + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        if (!power.getRestrictions().getRestrictedToSpecies().isEmpty() && getSettings().isRestrictionsChecked()
                && (getSpecie() == null || !power.getRestrictions().getRestrictedToSpecies().contains(getSpecie().getId()))) {
            throw new InvalidOccultismPowerException("Occultism Power '" + power + "' is limited to races '"
                    + power.getRestrictions().getRestrictedToSpecies() + "'.");
        }
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
        getOccultism().addPower(this, path, power, getFaction() != null ? getFaction().getId() : null, getSpecie().getId(), getSettings());
    }

    public void removeOccultismPower(OccultismPower power) {
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
        getOccultism().removePower(path, power);
    }

    public boolean hasOccultismPath(OccultismPath path) {
        return getOccultism().hasPath(path);
    }

    private Cybernetics getCybernetics() {
        return cybernetics;
    }

    public int getCyberneticsPointsAvailable() {
        return (int) getPerks().stream().filter(perk -> Objects.equals(perk.getId(), "cyberdevice"))
                .count();
    }

    public int getCyberneticsPointsSpent() {
        return cybernetics.getElements().size();
    }

    public boolean hasDevice(Cyberdevice cyberdevice) {
        return getCybernetics().hasDevice(cyberdevice);
    }

    public List<Cyberdevice> getCyberdevices() {
        return getCybernetics().getElements();
    }

    public boolean canAddCyberdevice(Cyberdevice cyberdevice) {
        try {
            getCybernetics().canAddDevice(this, cyberdevice, getSettings());
            return true;
        } catch (InvalidOccultismPowerException e) {
            return false;
        }
    }

    public void addCyberdevice(Cyberdevice cyberdevice) throws InvalidOccultismPowerException, UnofficialElementNotAllowedException {
        if (cyberdevice == null) {
            throw new InvalidCyberdeviceException("Null value not allowed");
        }
        if (!cyberdevice.isOfficial() && getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("Cyberdevice '" + cyberdevice + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        if (!cyberdevice.getRestrictions().getRestrictedToSpecies().isEmpty() && getSettings().isRestrictionsChecked()
                && (getSpecie() == null || !cyberdevice.getRestrictions().getRestrictedToSpecies().contains(getSpecie().getId()))) {
            throw new InvalidCyberdeviceException("Cyberdevice '" + cyberdevice + "' is limited to races '"
                    + cyberdevice.getRestrictions().getRestrictedToSpecies() + "'.");
        }
        getCybernetics().getElements().add(cyberdevice);
    }

    public void removeCyberdevice(Cyberdevice cyberdevice) {
        getCybernetics().getElements().remove(cyberdevice);
    }

    public Affliction getAffliction() {
        return affliction;
    }

    public void setAffliction(Affliction affliction) {
        this.affliction = affliction;
    }

    @Override
    public String toString() {
        final String name = getCompleteNameRepresentation();
        return "CharacterPlayer{"
                + (name != null && !name.isBlank() ? "name=" + name + ", " : "")
                + "faction=" + faction
                + ", upbringing=" + upbringing
                + ", specie=" + specie
                + ", calling=" + calling
                + ", level=" + level
                + '}';
    }
}
