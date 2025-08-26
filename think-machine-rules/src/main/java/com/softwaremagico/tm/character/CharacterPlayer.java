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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.callings.CallingCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.callings.CallingGroup;
import com.softwaremagico.tm.character.capabilities.CapabilityWithSpecialization;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
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
import com.softwaremagico.tm.character.level.LevelSelector;
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
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.InvalidCharacteristicException;
import com.softwaremagico.tm.exceptions.InvalidCyberdeviceException;
import com.softwaremagico.tm.exceptions.InvalidFactionException;
import com.softwaremagico.tm.exceptions.InvalidLevelException;
import com.softwaremagico.tm.exceptions.InvalidOccultismPowerException;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.InvalidSkillException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.MaxInitialValueExceededException;
import com.softwaremagico.tm.exceptions.MaxValueExceededException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialCharacterException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.txt.CharacterSheet;
import com.softwaremagico.tm.utils.ComparableUtils;
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
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CharacterPlayer {
    public static final int MAX_INITIAL_VALUE = 8;
    public static final int MAX_INTERMEDIAL_VALUE = 9;
    public static final int LEVEL_MAX_VALUE = 10;
    private static final int BANK_INITIAL_VALUE = 5;
    private static final int INITIAL_TECH_LEVEL = 4;

    // Basic description of the character.
    private CharacterInfo info;

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

    private final Stack<LevelSelector> levels = new Stack<>();

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
        try {
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
            //Check characteristics values
            for (CharacteristicDefinition characteristicDefinition : CharacteristicsDefinitionFactory.getInstance().getElements()) {
                if (characteristicDefinition.getType() != CharacteristicType.OTHERS) {
                    final int characteristicValue = getCharacteristicValue(characteristicDefinition.getCharacteristicName());
                    try {
                        checkMaxValueByLevel(characteristicDefinition, characteristicValue);
                    } catch (InvalidXmlElementException e) {
                        throw new InvalidCharacteristicException("Characteristic '" + characteristicDefinition.getCharacteristicName()
                                + "' has exceeded its maximum value at level '" + getLevel() + "'.", e);
                    }
                    if (characteristicValue > (SpecieFactory.getInstance().getElement(getSpecie().getId())
                            .getSpecieCharacteristic(characteristicDefinition.getCharacteristicName()).getMaximumValue())) {
                        throw new InvalidCharacteristicException("Characteristic '" + characteristicDefinition.getCharacteristicName()
                                + "' has exceeded its maximum value of '"
                                + (SpecieFactory.getInstance().getElement(getSpecie().getId())
                                .getSpecieCharacteristic(characteristicDefinition.getCharacteristicName()).getMaximumValue())
                                + "' by specie.");
                    }
                }
            }

            //Check skills values
            for (Skill skill : SkillFactory.getInstance().getElements()) {
                final int skillValue = getSkillValue(skill);
                try {
                    checkMaxValueByLevel(skill, skillValue);
                } catch (InvalidSkillException e) {
                    throw new InvalidSkillException("Skill '" + skill.getId()
                            + "' has exceeded its maximum value at level '" + getLevel() + "'.");
                }
            }

            checkDuplicatedPerks();
            checkDuplicatedCapabilities();

            //Check levels.
            getLevels().forEach(LevelSelector::validate);
        } catch (InvalidSelectionException e) {
            throw new InvalidFactionException("Error on character '" + this + "'.", e);
        }
    }

    public void checkMaxValueByLevel(Element element, int value) {
        if (element != null) {
            checkMaxValueByLevel(element.getId(), value);
        }
    }

    public void checkMaxValueByLevel(String element, int value) {
        if ((getLevel() < 2 && value > MAX_INITIAL_VALUE)
                || (getLevel() < LEVEL_MAX_VALUE && value > MAX_INTERMEDIAL_VALUE)) {
            String composition;
            try {
                composition = getCharacteristicComposition(element);
            } catch (Exception e) {
                composition = getSkillComposition(element);
            }
            throw new InvalidXmlElementException("Element '" + element + "' has exceeded its maximum value '" + value + "' at level '" + getLevel() + "'. "
                    + composition);
        }
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
                UpbringingFactory.getInstance().getElement(this.upbringing.getId()).getRestrictions().isRestricted(this);
            } catch (InvalidSelectionException e) {
                this.upbringing = null;
                throw e;
            }
        } else {
            this.upbringing = null;
        }
        try {
            if (this.faction != null) {
                FactionFactory.getInstance().getElement(this.faction.getId()).getRestrictions().isRestricted(this);
            }
        } catch (InvalidSelectionException e) {
            setFaction((String) null);
        }

        try {
            if (this.calling != null) {
                CallingFactory.getInstance().getElement(this.calling.getId()).getRestrictions().isRestricted(this);
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
                FactionFactory.getInstance().getElement(this.faction.getId()).getRestrictions().isRestricted(this);
            } catch (InvalidSelectionException e) {
                this.faction = null;
                throw e;
            }
        } else {
            this.faction = null;
        }
        try {
            if (this.calling != null) {
                CallingFactory.getInstance().getElement(this.calling.getId()).getRestrictions().isRestricted(this);
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
                CallingFactory.getInstance().getElement(this.calling.getId()).getRestrictions().isRestricted(this);
            } catch (InvalidSelectionException e) {
                this.calling = null;
                throw e;
            }
        } else {
            this.calling = null;
        }
    }

    public boolean isFavoredCalling() {
        return (getFaction() != null && getCalling() != null
                && FactionFactory.getInstance().getElement(getFaction()).getFavoredCallings().contains(getCalling().getId()));
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
            final int skillBonus = upbringing.getSkillBonus(skill);
            bonus += skillBonus;
        }
        if (faction != null) {
            final int skillBonus = faction.getSkillBonus(skill);
            bonus += skillBonus;
        }
        if (calling != null) {
            final int skillBonus = calling.getSkillBonus(skill);
            bonus += skillBonus;
        }
        try {
            checkMaxValueByLevel(skill, bonus);
        } catch (InvalidXmlElementException e) {
            throw new InvalidXmlElementException("Invalid skill total. " + getSkillComposition(skill), e);
        }
        return bonus;
    }

    public String getSkillComposition(String skill) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (upbringing != null) {
            final int skillBonus = upbringing.getSkillBonus(skill);
            if (skillBonus > 0) {
                stringBuilder.append("upbringing: ").append(skillBonus);
            }
        }
        if (faction != null) {
            final int skillBonus = faction.getSkillBonus(skill);
            if (skillBonus > 0) {
                stringBuilder.append((stringBuilder.length() > 0 ? ", " : "")).append("faction: ").append(skillBonus);
            }
        }
        if (calling != null) {
            final int skillBonus = calling.getSkillBonus(skill);
            if (skillBonus > 0) {
                stringBuilder.append((stringBuilder.length() > 0 ? ", " : "")).append("calling: ").append(skillBonus);
            }
        }
        return stringBuilder.toString();
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
        int bonus = 0;
        if (getPrimaryCharacteristic() != null && Objects.equals(getPrimaryCharacteristic(), characteristic)) {
            bonus = CharacteristicDefinition.PRIMARY_CHARACTERISTIC_VALUE;
        } else if (getSecondaryCharacteristic() != null && Objects.equals(getSecondaryCharacteristic(), characteristic)) {
            bonus = CharacteristicDefinition.SECONDARY_CHARACTERISTIC_VALUE;
        } else if (specie != null) {
            bonus = SpecieFactory.getInstance().getElement(getSpecie()).getSpecieCharacteristic(characteristic).getInitialValue();
        } else if (characteristicName.getCharacteristicType().equals(CharacteristicType.BODY)
                || characteristicName.getCharacteristicType().equals(CharacteristicType.MIND)
                || characteristicName.getCharacteristicType().equals(CharacteristicType.SPIRIT)) {
            bonus = Characteristic.INITIAL_VALUE;
        }
        if (upbringing != null) {
            final int upbringingBonus = upbringing.getCharacteristicBonus(characteristic);
            bonus += upbringingBonus;
        }
        if (faction != null) {
            final int factionBonus = faction.getCharacteristicBonus(characteristic);
            bonus += factionBonus;
        }
        if (calling != null) {
            final int callingBonus = calling.getCharacteristicBonus(characteristic);
            bonus += callingBonus;
        }
        if (bonus > MAX_INITIAL_VALUE + getLevel() - 1) {
            throw new MaxInitialValueExceededException("Characteristic '" + characteristic + "' has exceeded the maximum value of '"
                    + (MAX_INITIAL_VALUE + getLevel() - 1) + "' with '" + bonus + "'. " + getCharacteristicComposition(characteristic),
                    bonus, (MAX_INITIAL_VALUE + getLevel() - 1));
        }
        if (specie != null && bonus > SpecieFactory.getInstance().getElement(specie.getId()).getSpecieCharacteristic(characteristic).getMaximumValue()) {
            throw new MaxValueExceededException("Characteristic '" + characteristic + "' has exceeded the maximum value of '"
                    + SpecieFactory.getInstance().getElement(specie.getId()).getSpecieCharacteristic(characteristic).getMaximumValue() + "' with '"
                    + bonus + "'. " + getCharacteristicComposition(characteristic), bonus,
                    SpecieFactory.getInstance().getElement(specie.getId()).getSpecieCharacteristic(characteristic).getMaximumValue());
        }
        return bonus;
    }

    public String getCharacteristicComposition(String characteristic) {
        final CharacteristicName characteristicName = CharacteristicName.get(characteristic);
        if (characteristicName == null) {
            throw new InvalidCharacteristicException("No characteristic '" + characteristic + "' exists.");
        }
        final StringBuilder stringBuilder = new StringBuilder();
        if (getPrimaryCharacteristic() != null && Objects.equals(getPrimaryCharacteristic(), characteristic)) {
            stringBuilder.append("Primary characteristic: ").append(CharacteristicDefinition.PRIMARY_CHARACTERISTIC_VALUE);
        } else if (getSecondaryCharacteristic() != null && Objects.equals(getSecondaryCharacteristic(), characteristic)) {
            stringBuilder.append("Secondary characteristic: ").append(CharacteristicDefinition.SECONDARY_CHARACTERISTIC_VALUE);
        } else if (specie != null) {
            final int bonus = SpecieFactory.getInstance().getElement(getSpecie()).getSpecieCharacteristic(characteristic).getInitialValue();
            if (bonus > 0) {
                stringBuilder.append("Specie bonus: ").append(bonus);
            }
        } else if (characteristicName.getCharacteristicType().equals(CharacteristicType.BODY)
                || characteristicName.getCharacteristicType().equals(CharacteristicType.MIND)
                || characteristicName.getCharacteristicType().equals(CharacteristicType.SPIRIT)) {
            stringBuilder.append("Basic bonus: ").append(Characteristic.INITIAL_VALUE);
        }
        if (upbringing != null) {
            final int upbringingBonus = upbringing.getCharacteristicBonus(characteristic);
            if (upbringingBonus > 0) {
                stringBuilder.append((stringBuilder.length() > 0 ? ", " : "")).append("upbringing: ").append(upbringingBonus);
            }
        }
        if (faction != null) {
            final int factionBonus = faction.getCharacteristicBonus(characteristic);
            if (factionBonus > 0) {
                stringBuilder.append((stringBuilder.length() > 0 ? ", " : "")).append("faction: ").append(factionBonus);
            }
        }
        if (calling != null) {
            final int callingBonus = calling.getCharacteristicBonus(characteristic);
            if (callingBonus > 0) {
                stringBuilder.append((stringBuilder.length() > 0 ? ", " : "")).append("calling: ").append(callingBonus);
            }
        }
        return stringBuilder.toString();
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
        final AtomicInteger vitality = new AtomicInteger(getCharacteristicValue(CharacteristicName.ENDURANCE)
                + getCharacteristicValue(CharacteristicName.WILL)
                + getCharacteristicValue(CharacteristicName.FAITH)
                + (specie != null ? SpecieFactory.getInstance().getElement(specie).getSize() : 0));
        getLevels().forEach(level -> {
            vitality.addAndGet(level.getExtraVitality());
        });
        return vitality.get();
    }

    public Set<CapabilityWithSpecialization> getCapabilitiesWithSpecialization() {
        final Set<CapabilityWithSpecialization> capabilities = new HashSet<>();
        if (specie != null) {
            specie.getSelectedCapabilityOptions().forEach(capabilityOption ->
                    capabilityOption.getSelections().forEach(selection ->
                            capabilities.add(CapabilityWithSpecialization.from(selection))));
        }
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


    public boolean hasCapability(String capability, String specialization, Phase phase, String stepId) {
        final String comparedCapability = ComparableUtils.getComparisonId(capability, specialization);
        if (Phase.SPECIE.isCheckedPhase(phase) && hasCapability(comparedCapability, specie)) {
            return true;
        }
        if (Phase.UPBRINGING.isCheckedPhase(phase) && hasCapability(comparedCapability, upbringing)) {
            return true;
        }
        if (Phase.FACTION.isCheckedPhase(phase) && hasCapability(comparedCapability, faction)) {
            return true;
        }
        if (Phase.CALLING.isCheckedPhase(phase) && hasCapability(comparedCapability, calling)) {
            return true;
        }
        //Levels always check other levels.
        if (Phase.LEVEL.isCheckedPhase(phase) || phase == Phase.LEVEL) {
            for (LevelSelector levelSelector : getLevels()) {
                if (Objects.equals(levelSelector.getId(), stepId)) {
                    break;
                }
                if (hasCapability(comparedCapability, levelSelector)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasCapability(String comparedCapabilityId, CharacterDefinitionStepSelection step) {
        if (step != null) {
            return step.getSelectedCapabilities().stream().map(c -> ComparableUtils.getComparisonId(c.getId(), c.getSpecialization()))
                    .anyMatch(x -> Objects.equals(x, comparedCapabilityId));
        }
        return false;
    }


    public boolean hasPerk(String perk, Phase phase) {
        if (Phase.SPECIE.isCheckedPhase(phase) && hasPerk(perk, specie)) {
            return true;
        }
        if (Phase.UPBRINGING.isCheckedPhase(phase) && hasPerk(perk, upbringing)) {
            return true;
        }
        if (Phase.FACTION.isCheckedPhase(phase) && hasPerk(perk, faction)) {
            return true;
        }
        if (Phase.CALLING.isCheckedPhase(phase) && hasPerk(perk, calling)) {
            return true;
        }
        //Levels always check other levels.
        if (Phase.LEVEL.isCheckedPhase(phase) || phase == Phase.LEVEL) {
            for (LevelSelector levelSelector : getLevels()) {
                if (hasPerk(perk, levelSelector)) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean hasPerk(String perk, CharacterDefinitionStepSelection step) {
        if (step != null) {
            return step.getSelectedPerks().stream().map(Selection::getId)
                    .anyMatch(x -> Objects.equals(x, perk));
        }
        return false;
    }


    public Collection<String> getPerks(CharacterDefinitionStepSelection step) {
        if (step != null) {
            return step.getSelectedPerks().stream().map(p -> ComparableUtils.getComparisonId(p.getId(), p.getSpecialization()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    public void checkDuplicatedPerks() {
        //Check duplicate perks.
        final Collection<String> speciePerks = getPerks(specie);
        final Collection<String> upbringingPerks = getPerks(upbringing);
        final Collection<String> factionPerks = getPerks(faction);
        final Collection<String> callingPerks = getPerks(calling);

        final Collection<String> completePerkList = new HashSet<>(speciePerks);
        Collection<String> nextPerks = new HashSet<>(upbringingPerks);

        upbringingPerks.retainAll(completePerkList);
        if (!upbringingPerks.isEmpty()) {
            throw new InvalidXmlElementException("Duplicated perks '" + upbringingPerks + "' on upbringing '" + getUpbringing() + "'.");
        }
        completePerkList.addAll(nextPerks);

        nextPerks = new ArrayList<>(factionPerks);
        factionPerks.retainAll(completePerkList);
        if (!factionPerks.isEmpty()) {
            throw new InvalidXmlElementException("Duplicated perks '" + factionPerks + "' on faction '" + getFaction() + "'.");
        }
        completePerkList.addAll(nextPerks);

        nextPerks = new ArrayList<>(callingPerks);
        callingPerks.retainAll(completePerkList);
        if (!callingPerks.isEmpty()) {
            throw new InvalidXmlElementException("Duplicated perks '" + callingPerks + "' on calling '" + getCalling() + "'.");
        }
        completePerkList.addAll(nextPerks);

        for (LevelSelector levelSelector : getLevels()) {
            final Collection<String> levelPerks = getPerks(levelSelector);
            nextPerks = new ArrayList<>(levelPerks);
            levelPerks.retainAll(completePerkList);
            if (!levelPerks.isEmpty()) {
                throw new InvalidXmlElementException("Duplicated perk '" + levelPerks + "' on level '" + levelSelector + "'.");
            }
            completePerkList.addAll(nextPerks);
        }

    }

    public void checkDuplicatedCapabilities() {
        //Check duplicate capabilities.
        final Collection<String> specieCapabilities = getCapabilities(specie);
        final Collection<String> upbringingCapabilities = getCapabilities(upbringing);
        final Collection<String> factionCapabilities = getCapabilities(faction);
        final Collection<String> callingCapabilities = getCapabilities(calling);

        final Collection<String> completeCapabilitiesList = new HashSet<>(specieCapabilities);
        Collection<String> nextCapabilities = new HashSet<>(upbringingCapabilities);

        upbringingCapabilities.retainAll(completeCapabilitiesList);
        if (!upbringingCapabilities.isEmpty()) {
            throw new InvalidXmlElementException("Duplicated capability '" + upbringingCapabilities + "' on upbringing '" + getUpbringing() + "'.");
        }
        completeCapabilitiesList.addAll(nextCapabilities);

        nextCapabilities = new ArrayList<>(factionCapabilities);
        factionCapabilities.retainAll(completeCapabilitiesList);
        if (!factionCapabilities.isEmpty()) {
            throw new InvalidXmlElementException("Duplicated capability '" + upbringingCapabilities + "' on faction '" + getFaction() + "'.");
        }
        completeCapabilitiesList.addAll(nextCapabilities);

        nextCapabilities = new ArrayList<>(callingCapabilities);
        callingCapabilities.retainAll(completeCapabilitiesList);
        if (!callingCapabilities.isEmpty()) {
            throw new InvalidXmlElementException("Duplicated capability '" + callingCapabilities + "' on calling '" + getCalling() + "'.");
        }
        completeCapabilitiesList.addAll(nextCapabilities);

        for (LevelSelector levelSelector : getLevels()) {
            final Collection<String> levelCapabilities = getCapabilities(levelSelector);
            nextCapabilities = new ArrayList<>(levelCapabilities);
            levelCapabilities.retainAll(completeCapabilitiesList);
            if (!levelCapabilities.isEmpty()) {
                throw new InvalidXmlElementException("Duplicated capability '" + levelCapabilities + "' on level '" + levelSelector + "'.");
            }
            completeCapabilitiesList.addAll(nextCapabilities);
        }
    }


    public Collection<String> getCapabilities(CharacterDefinitionStepSelection step) {
        if (step != null) {
            return step.getSelectedCapabilities().stream().map(c -> ComparableUtils.getComparisonId(c.getId(), c.getSpecialization()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
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
        return 1 + levels.size();
    }

    public LevelSelector getLevel(int index) {
        return levels.get(index - 1);
    }

    public Stack<LevelSelector> getLevels() {
        return levels;
    }

    public LevelSelector addLevel() {
        if (getFaction() == null || getSpecie() == null || getCalling() == null) {
            throw new InvalidLevelException("Error on character '" + this + "'. Please, finalize or correct level 1 first.");
        }
        try {
            validate();
        } catch (InvalidXmlElementException e) {
            throw new InvalidLevelException("Error on character '" + this + "'. Please, finalize or correct previous level first.", e);
        }
        final LevelSelector newLevel = new LevelSelector(this, getLevel() + 1);
        levels.add(newLevel);
        return newLevel;
    }

    public int getBank() throws InvalidXmlElementException {
        final AtomicInteger bank = new AtomicInteger(BANK_INITIAL_VALUE);
        getLevels().forEach(level ->
                bank.addAndGet(level.getExtraVPBank()));
        return bank.get();
    }

    public int getSurgesRating() throws InvalidXmlElementException {
        final AtomicInteger surge = new AtomicInteger(Math.max(Math.max(getCharacteristicValue(CharacteristicName.STRENGTH),
                        getCharacteristicValue(CharacteristicName.WITS)),
                getCharacteristicValue(CharacteristicName.FAITH)));
        getLevels().forEach(level ->
                surge.addAndGet(level.getExtraSurgeRating()));
        return surge.get();
    }

    public int getSurgesNumber() throws InvalidXmlElementException {
        final AtomicInteger surge = new AtomicInteger(1);
        getLevels().forEach(level ->
                surge.addAndGet(level.getExtraSurgeNumber()));
        return surge.get();
    }

    public int getRevivalsRating() throws InvalidXmlElementException {
        final AtomicInteger revivals = new AtomicInteger(
                (specie != null ? SpecieFactory.getInstance().getElement(specie).getSize() : 0));
        getLevels().forEach(level ->
                revivals.addAndGet(level.getExtraRevivalRating()));
        return revivals.get();
    }

    public int getRevivalsNumber() throws InvalidXmlElementException {
        final AtomicInteger revivals = new AtomicInteger(1);
        getLevels().forEach(level ->
                revivals.addAndGet(level.getExtraRevivalNumber()));
        return revivals.get();
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
        return ((CharacterDefinitionStep) factory.getElement(definitionStepSelection.getId())).getMaterialAwards(selected);
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

    public boolean isOccultist() {
        if (getCharacteristicValue(CharacteristicName.PSI) > 0) {
            return true;
        }
        if (getCharacteristicValue(CharacteristicName.THEURGY) > 0) {
            return true;
        }
        if (getCalling() != null) {
            final String callingGroup = CallingFactory.getInstance().getElement(getCalling().getId()).getGroup();
            return CharacteristicName.PSI.name().equalsIgnoreCase(callingGroup) || CharacteristicName.THEURGY.name().equalsIgnoreCase(callingGroup);
        }
        return false;
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
        if (getCalling() != null && (Objects.equals(getCalling().getId(), "dervish"))) {
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
            //Check if it has some path purchased already. Get its occultismType;
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
            //Check if it has some occultism level added already.
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
        if (path == null) {
            return false;
        }
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
                + "specie=" + specie
                + ", upbringing=" + upbringing
                + ", faction=" + faction
                + ", calling=" + calling
                + ", level=" + getLevel()
                + '}';
    }
}
