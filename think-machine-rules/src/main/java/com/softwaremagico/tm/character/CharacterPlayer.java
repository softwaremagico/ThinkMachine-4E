package com.softwaremagico.tm.character;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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
import com.softwaremagico.tm.character.cache.CacheManager;
import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.callings.CallingGroup;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.character.capabilities.CapabilityWithSpecialization;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicReassign;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatActionRequirement;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.cybernetics.CyberdeviceFactory;
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
import com.softwaremagico.tm.character.factions.Faction;
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
import com.softwaremagico.tm.character.perks.Perk;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.perks.PerkType;
import com.softwaremagico.tm.character.perks.SpecializedPerk;
import com.softwaremagico.tm.character.resistances.Resistance;
import com.softwaremagico.tm.character.resistances.ResistanceType;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.skills.SkillsReassign;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.character.specie.ElementValues;
import com.softwaremagico.tm.character.specie.Specie;
import com.softwaremagico.tm.character.specie.SpecieCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.character.values.SpecialValue;
import com.softwaremagico.tm.exceptions.InvalidCallingException;
import com.softwaremagico.tm.exceptions.InvalidCharacteristicException;
import com.softwaremagico.tm.exceptions.InvalidFactionException;
import com.softwaremagico.tm.exceptions.InvalidLevelException;
import com.softwaremagico.tm.exceptions.InvalidOccultismPowerException;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.exceptions.InvalidSkillException;
import com.softwaremagico.tm.exceptions.InvalidUpbringingException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.MaxValueExceededException;
import com.softwaremagico.tm.exceptions.RestrictedElementException;
import com.softwaremagico.tm.exceptions.UnofficialCharacterException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.structures.SelectionSet;
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
    private static final int BANK_INITIAL_VALUE = 5;
    private static final int INITIAL_TECH_LEVEL = 4;
    private static final int INITIAL_CASH = 300;

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

    private SelectionSet<Equipment> equipmentPurchased;

    private final Settings settings;

    private Affliction affliction;

    private final Stack<LevelSelector> levels = new Stack<>();

    private final List<CharacteristicReassign> characteristicReassigns = new ArrayList<>();
    private final List<SkillsReassign> skillsReassigns = new ArrayList<>();

    private final CacheManager cacheManager;

    public CharacterPlayer() {
        settings = new Settings();
        cacheManager = new CacheManager();
        reset();
    }

    private void reset() {
        info = new CharacterInfo();
        occultism = new Occultism();
        specie = null;
        upbringing = null;
        faction = null;
        calling = null;
        equipmentPurchased = new SelectionSet<>();
        cacheManager.reset();
    }

    public SpecieCharacterDefinitionStepSelection getSpecie() {
        return specie;
    }

    private void validateSelections() throws InvalidSelectionException {
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
    }

    private void validateCharacteristics() throws MaxValueExceededException, InvalidXmlElementException {
        if (getPrimaryCharacteristic() == null || getSecondaryCharacteristic() == null) {
            throw new InvalidCharacteristicException("You must choose your primary and secondary characteristic.");
        }
        for (CharacteristicDefinition characteristicDefinition : CharacteristicsDefinitionFactory.getInstance().getElements()) {
            if (characteristicDefinition.getType() != CharacteristicType.OTHERS) {
                final int characteristicValue = getCharacteristicValue(characteristicDefinition.getCharacteristicName());
                if (characteristicValue > (SpecieFactory.getInstance().getElement(getSpecie().getId())
                        .getSpecieCharacteristic(characteristicDefinition.getCharacteristicName()).getMaximumValue())) {
                    throw new MaxValueExceededException("Characteristic '" + characteristicDefinition.getCharacteristicName()
                            + "' has exceeded its maximum value of '"
                            + (SpecieFactory.getInstance().getElement(getSpecie().getId())
                            .getSpecieCharacteristic(characteristicDefinition.getCharacteristicName()).getMaximumValue())
                            + "' by specie.", characteristicDefinition.getId(), characteristicValue,
                            (SpecieFactory.getInstance().getElement(getSpecie().getId())
                                    .getSpecieCharacteristic(characteristicDefinition.getCharacteristicName()).getMaximumValue()));
                }
            }
        }
    }

    private void validateSkill(Skill skill) throws InvalidSkillException {
        try {
            getSkillValue(skill);
        } catch (InvalidSkillException e) {
            throw new InvalidSkillException("Skill '" + skill.getId()
                    + "' has exceeded its maximum value at level '" + getLevel() + "'.");
        }
    }

    private void validateSkills() throws InvalidSkillException, InvalidXmlElementException {
        for (Skill skill : SkillFactory.getInstance().getElements()) {
            validateSkill(skill);
        }
    }

    public void validate() {
        try {
            validateSelections();
            validateCharacteristics();
            validateSkills();
            checkDuplicatedPerks();
            checkDuplicatedCapabilities();
            getLevels().forEach(LevelSelector::validate);
        } catch (InvalidSelectionException e) {
            throw new InvalidFactionException("Error on character '" + this + "'.", e);
        }
    }

    public void checkMaxValueByLevel(Element element, int value) throws MaxValueExceededException {
        if (element != null) {
            checkMaxValueByLevel(element.getId(), value);
        }
    }

    public void checkMaxValueByLevel(String element, int value) throws MaxValueExceededException {
        if ((getLevel() == ElementValues.INITIAL_LEVEL && value > getMaximumInitialValue(element))
                || (getLevel() == ElementValues.INTERMEDIATE_LEVEL && value > getMaximumIntermediateValue())
                || (getLevel() > ElementValues.INTERMEDIATE_LEVEL && getLevel() < ElementValues.ADVANCED_LEVEL
                && value > Math.min(ElementValues.LEVEL_MAX_VALUE, getMaximumValue(element)))
                || getLevel() > ElementValues.ADVANCED_LEVEL && value > getMaximumValue(element)) {
            String composition;
            try {
                composition = getCharacteristicComposition(element);
            } catch (Exception e) {
                composition = getSkillComposition(element);
            }
            final int maxValue = getLevel() == ElementValues.INITIAL_LEVEL ? getMaximumInitialValue(element)
                    : (getLevel() == ElementValues.INTERMEDIATE_LEVEL ? getMaximumIntermediateValue()
                    : Math.min(ElementValues.LEVEL_MAX_VALUE, getMaximumValue(element)));
            throw new MaxValueExceededException("Element '" + element + "' has exceeded its maximum value '" + value + "' at level '" + getLevel() + "'. "
                    + "Obtained by " + composition, element, value, maxValue);
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
            this.specie.selectDefaultOptions();
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
            if (UpbringingFactory.getInstance().getElement(this.upbringing.getId()).getRestrictions().isRestricted(this)) {
                this.upbringing = null;
                throw new InvalidUpbringingException("Upbrinfing '" + upbringing + "' is restricted to the character.");
            }
            this.upbringing.selectDefaultOptions();
        } else {
            this.upbringing = null;
        }
        if (this.faction != null && FactionFactory.getInstance().getElement(this.faction.getId()).getRestrictions().isRestricted(this)) {
            setFaction((String) null);
        }

        if (this.calling != null && CallingFactory.getInstance().getElement(this.calling.getId()).getRestrictions().isRestricted(this)) {
            setCalling((String) null);
        }
    }


    public void setFaction(FactionCharacterDefinitionStepSelection faction) {
        this.faction = faction;
    }

    public void setFaction(String faction) {
        if (faction != null) {
            this.faction = new FactionCharacterDefinitionStepSelection(this, faction);
            if (FactionFactory.getInstance().getElement(this.faction.getId()).getRestrictions().isRestricted(this)) {
                this.faction = null;
                throw new InvalidFactionException("Faction '" + faction + "' is restricted to the character.");
            }
            this.faction.selectDefaultOptions();
        } else {
            this.faction = null;
        }
        try {
            if (this.calling != null && CallingFactory.getInstance().getElement(this.calling.getId()).getRestrictions().isRestricted(this)) {
                setCalling((String) null);
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
            if (CallingFactory.getInstance().getElement(this.calling.getId()).getRestrictions().isRestricted(this)) {
                this.calling = null;
                throw new InvalidCallingException("Calling '" + calling + "' is restricted to the character.");
            }
            this.calling.selectDefaultOptions();
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

    public int getSkillValue(Skill skill) throws MaxValueExceededException {
        if (skill == null) {
            return 0;
        }
        return getSkillValue(skill.getId());
    }

    public int getSkillValue(String skill) throws MaxValueExceededException {
        if (cacheManager.getSkillValue(skill) == null) {
            int bonus = 0;
            if (SkillFactory.getInstance().getElement(skill).isNatural()) {
                bonus += Skill.NATURAL_SKILL_INITIAL_VALUE;
            }
            if (specie != null) {
                bonus += specie.getSkillBonus(skill);
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

            bonus -= getSkillReassignValueDecreased(skill);
            bonus += getSkillReassignValueIncreased(skill);

            for (LevelSelector levelSelector : getLevels()) {
                bonus += levelSelector.getSkillBonus(skill);
            }

            bonus += getCyberdevicesAlwaysBonification(skill);

            checkMaxValueByLevel(skill, bonus);

            cacheManager.setSkillValue(skill, bonus);
        }
        return cacheManager.getSkillValue(skill);
    }

    public String getSkillComposition(String skill) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (upbringing != null) {
            appendSkillBonusIfPresent(stringBuilder, "upbringing", upbringing.getSkillBonus(skill));
        }
        if (faction != null) {
            appendSkillBonusIfPresent(stringBuilder, "faction", faction.getSkillBonus(skill));
        }
        if (calling != null) {
            appendSkillBonusIfPresent(stringBuilder, "calling", calling.getSkillBonus(skill));
        }
        appendSkillBonusIfPresent(stringBuilder, "cyberdevices", getCyberdevicesAlwaysBonification(skill));
        return stringBuilder.toString();
    }

    private void appendSkillBonusIfPresent(StringBuilder stringBuilder, String source, int bonus) {
        if (bonus > 0) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(source).append(": ").append(bonus);
        }
    }

    private int getCyberdevicesAlwaysBonification(String valueId) {
        int bonus = 0;
        for (Cyberdevice cyberdevice : getCyberdevices()) {
            for (Bonification bonification : cyberdevice.getBonifications()) {
                if (bonification.getSituation() != null && !bonification.getSituation().isBlank()) {
                    continue;
                }
                if (affectsValue(bonification.getAffects(), valueId)) {
                    bonus += bonification.getValue() != null ? bonification.getValue() : 0;
                }
            }
        }
        return bonus;
    }

    private boolean affectsValue(IValue affects, String valueId) {
        if (affects == null || valueId == null) {
            return false;
        }

        if (Objects.equals(affects.getId(), valueId)) {
            return true;
        }

        if (affects instanceof SpecialValue specialValue) {
            if (specialValue.getAffects() == null) {
                return false;
            }
            return specialValue.getAffects().stream().anyMatch(value -> Objects.equals(value.getId(), valueId));
        }

        return false;
    }

    public int getSkillReassignValueDecreased(String skill) {
        int total = 0;
        for (SkillsReassign skillsReassign : skillsReassigns) {
            if (skillsReassign.getFrom().equals(skill)) {
                total++;
            }
        }
        return total;
    }

    public int getSkillReassignValueIncreased(String skill) {
        int total = 0;
        for (SkillsReassign skillsReassign : skillsReassigns) {
            if (skillsReassign.getTo().equals(skill)) {
                total++;
            }
        }
        return total;
    }

    public List<SkillsReassign> getSkillsReassigns() {
        return skillsReassigns;
    }

    public int getCharacteristicValue(CharacteristicName characteristic) throws MaxValueExceededException {
        if (characteristic == null) {
            return 0;
        }
        return getCharacteristicValue(characteristic.getId());
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

    private int getCharacteristicInitialValue(String characteristic) {
        return SpecieFactory.getInstance().getElement(getSpecie()).getSpecieCharacteristic(characteristic).getInitialValue();
    }


    private int getInitialValue(String element) {
        try {
            return getCharacteristicInitialValue(element);
        } catch (InvalidXmlElementException e) {
            //Not a characteristic
            return ElementValues.INITIAL_VALUE;
        }
    }

    public int getMaximumValue(String element) {
        try {
            return SpecieFactory.getInstance().getElement(getSpecie()).getSpecieCharacteristic(element).getMaximumValue();
        } catch (InvalidXmlElementException | NullPointerException e) {
            return ElementValues.LEVEL_MAX_VALUE;
        }
    }

    private int getMaximumIntermediateValue() {
        return ElementValues.MAX_INTERMEDIATE_VALUE;
    }

    private int getMaximumInitialValue(String element) {
        try {
            return SpecieFactory.getInstance().getElement(getSpecie()).getSpecieCharacteristic(element).getMaximumInitialValue();
        } catch (InvalidXmlElementException | NullPointerException e) {
            return ElementValues.MAX_INITIAL_VALUE;
        }
    }

    private int getBaseCharacteristicBonus(String characteristic, CharacteristicName characteristicName) {
        if (getPrimaryCharacteristic() != null && Objects.equals(getPrimaryCharacteristic(), characteristic)) {
            return CharacteristicDefinition.PRIMARY_CHARACTERISTIC_VALUE;
        } else if (getSecondaryCharacteristic() != null && Objects.equals(getSecondaryCharacteristic(), characteristic)) {
            return CharacteristicDefinition.SECONDARY_CHARACTERISTIC_VALUE;
        } else if (specie != null) {
            return getInitialValue(characteristic);
        } else if (isBasicCharacteristic(characteristicName)) {
            return CharacteristicDefinition.INITIAL_CHARACTERISTIC_VALUE;
        }
        return 0;
    }

    private boolean isBasicCharacteristic(CharacteristicName characteristicName) {
        return characteristicName.getCharacteristicType().equals(CharacteristicType.BODY)
                || characteristicName.getCharacteristicType().equals(CharacteristicType.MIND)
                || characteristicName.getCharacteristicType().equals(CharacteristicType.SPIRIT);
    }

    private int accumulateCharacteristicBonuses(String characteristic, int bonus) {
        if (upbringing != null) {
            bonus += upbringing.getCharacteristicBonus(characteristic);
        }
        if (faction != null) {
            bonus += faction.getCharacteristicBonus(characteristic);
        }
        if (calling != null) {
            bonus += calling.getCharacteristicBonus(characteristic);
        }
        for (LevelSelector levelSelector : getLevels()) {
            bonus += levelSelector.getCharacteristicBonus(characteristic);
        }
        bonus += getCyberdevicesAlwaysBonification(characteristic);
        bonus -= getCharacteristicReassignValueDecreased(characteristic);
        bonus += getCharacteristicReassignValueIncreased(characteristic);
        return bonus;
    }

    public int getCharacteristicValue(String characteristic) throws MaxValueExceededException {
        if (cacheManager.getCharacteristicValue(characteristic) == null) {
            final CharacteristicName characteristicName = CharacteristicName.get(characteristic);
            if (characteristicName == null) {
                throw new InvalidCharacteristicException("No characteristic '" + characteristic + "' exists.");
            }
            int bonus = getBaseCharacteristicBonus(characteristic, characteristicName);
            bonus = accumulateCharacteristicBonuses(characteristic, bonus);
            checkMaxValueByLevel(characteristic, bonus);
            if (specie != null && bonus > getMaximumValue(characteristic)) {
                throw new MaxValueExceededException("Characteristic '" + characteristic + "' has exceeded the maximum value of '"
                        + getMaximumValue(characteristic) + "' with '"
                        + bonus + "'. " + getCharacteristicComposition(characteristic), characteristic, bonus,
                        getMaximumValue(characteristic));
            }
            cacheManager.setCharacteristicValue(characteristic, bonus);
        }

        return cacheManager.getCharacteristicValue(characteristic);
    }

    private String getPrimaryCharacteristicComposition(String characteristic) {
        if (getPrimaryCharacteristic() != null && Objects.equals(getPrimaryCharacteristic(), characteristic)) {
            return "Primary characteristic: " + CharacteristicDefinition.PRIMARY_CHARACTERISTIC_VALUE;
        }
        return null;
    }

    private String getSecondaryCharacteristicComposition(String characteristic) {
        if (getSecondaryCharacteristic() != null && Objects.equals(getSecondaryCharacteristic(), characteristic)) {
            return "Secondary characteristic: " + CharacteristicDefinition.SECONDARY_CHARACTERISTIC_VALUE;
        }
        return null;
    }

    private String getSpecieCharacteristicComposition(String characteristic) {
        if (specie != null) {
            final int bonus = getInitialValue(characteristic);
            if (bonus > 0) {
                return "Specie bonus: " + bonus;
            }
        }
        return null;
    }

    private String getBasicCharacteristicComposition(CharacteristicName characteristicName) {
        if (isBasicCharacteristic(characteristicName)) {
            return "Basic bonus: " + CharacteristicDefinition.INITIAL_CHARACTERISTIC_VALUE;
        }
        return null;
    }

    private void appendBonusIfPresent(StringBuilder stringBuilder, String label, int bonus) {
        if (bonus > 0) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(label).append(": ").append(bonus);
        }
    }

    public String getCharacteristicComposition(String characteristic) {
        final CharacteristicName characteristicName = CharacteristicName.get(characteristic);
        if (characteristicName == null) {
            throw new InvalidCharacteristicException("No characteristic '" + characteristic + "' exists.");
        }
        final StringBuilder stringBuilder = new StringBuilder();

        final String primaryComp = getPrimaryCharacteristicComposition(characteristic);
        if (primaryComp != null) {
            stringBuilder.append(primaryComp);
        } else {
            final String secondaryComp = getSecondaryCharacteristicComposition(characteristic);
            if (secondaryComp != null) {
                stringBuilder.append(secondaryComp);
            } else {
                final String specieComp = getSpecieCharacteristicComposition(characteristic);
                if (specieComp != null) {
                    stringBuilder.append(specieComp);
                } else {
                    final String basicComp = getBasicCharacteristicComposition(characteristicName);
                    if (basicComp != null) {
                        stringBuilder.append(basicComp);
                    }
                }
            }
        }

        if (upbringing != null) {
            appendBonusIfPresent(stringBuilder, "upbringing", upbringing.getCharacteristicBonus(characteristic));
        }
        if (faction != null) {
            appendBonusIfPresent(stringBuilder, "faction", faction.getCharacteristicBonus(characteristic));
        }
        if (calling != null) {
            appendBonusIfPresent(stringBuilder, "calling", calling.getCharacteristicBonus(characteristic));
        }
        appendBonusIfPresent(stringBuilder, "cyberdevices", getCyberdevicesAlwaysBonification(characteristic));
        appendBonusIfPresent(stringBuilder, "balance", getCharacteristicReassignValueIncreased(characteristic));
        return stringBuilder.toString();
    }

    public CombatActionRequirement getCharacteristicCombatValue(String id) {
        // Keep null behavior for unknown/invalid ids to preserve existing combat checks.
        if (id == null || id.isBlank()) {
            return null;
        }
        try {
            final int characteristicValue = getCharacteristicValue(id);
            final CombatActionRequirement combatActionRequirement = new CombatActionRequirement();
            combatActionRequirement.setValue(characteristicValue);
            return combatActionRequirement;
        } catch (InvalidCharacteristicException | MaxValueExceededException e) {
            return null;
        }
    }

    public int getCharacteristicReassignValueDecreased(String characteristic) {
        int total = 0;
        for (CharacteristicReassign characteristicReassign : characteristicReassigns) {
            if (characteristicReassign.getFrom().equals(characteristic)) {
                total++;
            }
        }
        return total;
    }

    public int getCharacteristicReassignValueIncreased(String characteristic) {
        int total = 0;
        for (CharacteristicReassign characteristicReassign : characteristicReassigns) {
            if (characteristicReassign.getTo().equals(characteristic)) {
                total++;
            }
        }
        return total;
    }

    public List<CharacteristicReassign> getCharacteristicReassigns() {
        return characteristicReassigns;
    }

    /**
     * Gets the current rank of the status of a character.
     *
     * @return the status of the character.
     */
    public Rank getRank() {
        return null;
    }

    public Set<SpecializedPerk> getPerks() {
        if (cacheManager.getPerksWithSpecializations() == null) {
            cacheManager.setPerksWithSpecializations(getPerks((Integer) null));
        }
        return cacheManager.getPerksWithSpecializations();
    }

    public Set<SpecializedPerk> getPerks(Integer level) {
        final Set<SpecializedPerk> perks = new HashSet<>();
        if (upbringing != null) {
            perks.addAll(getSelectedPerks(upbringing));
        }
        if (faction != null) {
            perks.addAll(getSelectedPerks(faction));
        }
        if (calling != null) {
            perks.addAll(getSelectedPerks(calling));
        }
        for (LevelSelector levelSelector : getLevels()) {
            if (level == null || level <= levelSelector.getLevel()) {
                perks.addAll(getSelectedPerks(levelSelector));
            }
        }
        return perks;
    }

    public boolean hasPerk(String id) {
        for (SpecializedPerk specializedPerk : getPerks()) {
            if (specializedPerk.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private List<SpecializedPerk> getSelectedPerks(CharacterDefinitionStepSelection step) {
        final List<SpecializedPerk> perks = new ArrayList<>();
        step.getSelectedPerksOptions().forEach(perkOption ->
                perkOption.getSelections().forEach(selection -> {
                    try {
                        perks.add(new SpecializedPerk(PerkFactory.getInstance().getElement(selection), selection.getSpecialization()));
                    } catch (Exception e) {
                        MachineLog.warning(this.getClass(), e.getMessage());
                    }
                }));
        return perks;
    }

    public CharacterInfo getInfo() {
        return info;
    }

    public Integer getVitalityValue() throws InvalidXmlElementException {
        final AtomicInteger vitality = new AtomicInteger(getCharacteristicValue(CharacteristicName.ENDURANCE)
                + getCharacteristicValue(CharacteristicName.WILL)
                + getCharacteristicValue(CharacteristicName.FAITH)
                + (specie != null ? SpecieFactory.getInstance().getElement(specie).getSize() : 0)
                + (specie != null ? SpecieFactory.getInstance().getElement(specie).getVitalityBonus() : 0));
        getLevels().forEach(level -> vitality.addAndGet(level.getExtraVitality()));
        return vitality.get();
    }

    public Set<CapabilityWithSpecialization> getCapabilitiesWithSpecialization() {
        if (cacheManager.getCapabilityWithSpecializations() == null) {
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
            for (LevelSelector levelSelector : getLevels()) {
                levelSelector.getSelectedCapabilityOptions().forEach(capabilityOption ->
                        capabilityOption.getSelections().forEach(selection ->
                                capabilities.add(CapabilityWithSpecialization.from(selection))));
            }
            cacheManager.setCapabilityWithSpecializations(capabilities);
        }
        return cacheManager.getCapabilityWithSpecializations();
    }

    public boolean hasCapability(String capability, String specialization) {
        final String comparedCapability = ComparableUtils.getComparisonId(capability, specialization);
        return getCapabilitiesWithSpecialization().stream().map(CapabilityWithSpecialization::getComparisonId)
                .anyMatch(x -> Objects.equals(x, comparedCapability));
    }

    private boolean checkCapabilityInPhases(String comparedCapability, Phase phase, String stepId) {
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

    public boolean hasCapability(String capability, String specialization, Phase phase, String stepId) {
        final String comparedCapability = ComparableUtils.getComparisonId(capability, specialization);
        return checkCapabilityInPhases(comparedCapability, phase, stepId);
    }

    public boolean hasCapability(String comparedCapabilityId, CharacterDefinitionStepSelection step) {
        if (step != null) {
            return step.getSelectedCapabilities().stream().map(c -> ComparableUtils.getComparisonId(c.getId(), c.getSpecialization()))
                    .anyMatch(x -> Objects.equals(x, comparedCapabilityId));
        }
        return false;
    }


    private void removePerkSelectionsFromPhases(ArrayList<Selection> possibleSelections, Phase phase, Integer level) {
        if (Phase.SPECIE.isCheckedPhase(phase)) {
            possibleSelections.removeAll(specie.getSelectedPerks());
        }
        if (Phase.UPBRINGING.isCheckedPhase(phase) && upbringing != null) {
            possibleSelections.removeAll(upbringing.getSelectedPerks());
        }
        if (Phase.FACTION.isCheckedPhase(phase) && faction != null) {
            possibleSelections.removeAll(faction.getSelectedPerks());
        }
        if (Phase.CALLING.isCheckedPhase(phase) && calling != null) {
            possibleSelections.removeAll(calling.getSelectedPerks());
        }
        if ((Phase.LEVEL.isCheckedPhase(phase) || phase == Phase.LEVEL) && level != null) {
            for (int i = 0; i < getLevels().size() && i < level; i++) {
                possibleSelections.removeAll(getLevels().get(i).getSelectedPerks());
            }
        }
    }

    /**
     * A perk can be an option if at least has one specialization that is not selected.
     *
     * @param perk  perk to check.
     * @param phase until which phase is checked.
     * @return true if at least one option can be selected.
     */
    public boolean hasOption(PerkOption perk, Phase phase, Integer level) {
        final ArrayList<Selection> possibleSelections = new ArrayList<>();
        if (perk.getSpecializations() == null || perk.getSpecializations().isEmpty()) {
            possibleSelections.add(new Selection(perk));
        } else {
            for (Specialization specialization : perk.getSpecializations()) {
                possibleSelections.add(new Selection(perk, specialization));
            }
        }
        removePerkSelectionsFromPhases(possibleSelections, phase, level);
        return !possibleSelections.isEmpty();
    }

    public boolean hasSelection(Selection perkSelection, CharacterDefinitionStepSelection step) {
        if (step == null) {
            return false;
        }
        return hasSelection(perkSelection, step.getPhase(), step.getLevel());
    }

    public boolean hasSelection(Selection perkSelection, Phase phase, Integer level) {
        final List<CharacterDefinitionStepSelection> stepsToCheck = new ArrayList<>();
        if (phase != null && specie != null && phase.checkedUntilPhase(Phase.SPECIE)) {
            stepsToCheck.add(specie);
        }
        if (phase != null && upbringing != null && phase.checkedUntilPhase(Phase.UPBRINGING)) {
            stepsToCheck.add(upbringing);
        }
        if (phase != null && faction != null && phase.checkedUntilPhase(Phase.FACTION)) {
            stepsToCheck.add(faction);
        }
        if (phase != null && calling != null && phase.checkedUntilPhase(Phase.CALLING)) {
            stepsToCheck.add(calling);
        }
        //Levels always check previous levels.
        if (phase != null && phase.checkedUntilPhase(Phase.LEVEL) || phase == Phase.LEVEL) {
            for (int i = 0; i < getLevels().size() && (level == null || i < level - 1); i++) {
                stepsToCheck.add(getLevels().get(i));
            }
        }
        return hasSelection(perkSelection, stepsToCheck);
    }

    public boolean hasSelection(Selection selection, Collection<CharacterDefinitionStepSelection> steps) {
        if (steps != null) {
            for (CharacterDefinitionStepSelection step : steps) {
                if (step.getSelectedPerks().contains(selection)) {
                    MachineLog.debug(this.getClass().getName(), "Selection '{}' found on step '{}'.", selection, step);
                    return true;
                }
            }
        }
        return false;
    }


    public Collection<Selection> getPerks(CharacterDefinitionStepSelection step) {
        if (step != null) {
            return step.getSelectedPerks();
        }
        return new ArrayList<>();
    }


    private void checkDuplicatedPerksInPhase(Collection<Selection> phasePerks, Collection<Selection> completePerkList,
                                             String phaseName) {
        final Collection<Selection> nextPerks = new HashSet<>(phasePerks);
        phasePerks.retainAll(completePerkList);
        if (!phasePerks.isEmpty()) {
            for (Selection perk : phasePerks) {
                if (!isRepeatablePerk(perk)) {
                    throw new InvalidXmlElementException("Duplicated perks '" + phasePerks + "' on " + phaseName + ".");
                }
            }
        }
        completePerkList.addAll(nextPerks);
    }

    private void checkDuplicatedPerksInLevels(Collection<Selection> completePerkList) {
        for (LevelSelector levelSelector : getLevels()) {
            final Collection<Selection> levelPerks = getPerks(levelSelector);
            final Collection<Selection> nextPerks = new ArrayList<>(levelPerks);
            levelPerks.retainAll(completePerkList);
            if (!levelPerks.isEmpty()) {
                for (Selection perk : levelPerks) {
                    if (!isRepeatablePerk(perk)) {
                        throw new InvalidXmlElementException("Duplicated perk '" + levelPerks + "' on level '" + levelSelector + "'.");
                    }
                }
            }
            completePerkList.addAll(nextPerks);
        }
    }

    public void checkDuplicatedPerks() {
        final Collection<Selection> speciePerks = getPerks(specie);
        final Collection<Selection> upbringingPerks = getPerks(upbringing);
        final Collection<Selection> factionPerks = getPerks(faction);
        final Collection<Selection> callingPerks = getPerks(calling);

        final Collection<Selection> completePerkList = new HashSet<>(speciePerks);

        checkDuplicatedPerksInPhase(upbringingPerks, completePerkList, "upbringing '" + getUpbringing() + "'");
        checkDuplicatedPerksInPhase(factionPerks, completePerkList, "faction '" + getFaction() + "'");
        checkDuplicatedPerksInPhase(callingPerks, completePerkList, "calling '" + getCalling() + "'");
        checkDuplicatedPerksInLevels(completePerkList);
    }

    private boolean isRepeatablePerk(Selection perk) {
        return PerkFactory.getInstance().getElement(perk).isRepeatable();
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
                    .toList();
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
        return Resistance.getBonus(ResistanceType.BODY, this)
                + SpecieFactory.getInstance().getElement(getSpecie().getId()).getBodyResistance();
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

    public LevelSelector getLatestLevel() {
        return levels.get(levels.size() - 1);
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
        cacheManager.reset();
        return newLevel;
    }

    public void removeLevel(int level) {
        if (level == 0) {
            throw new InvalidLevelException("First level cannot be removed.");
        }
        try {
            levels.remove(level - 2);
            cacheManager.reset();
        } catch (IndexOutOfBoundsException e) {
            //Level not existing.
        }
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
        return getEquipmentPurchased().stream().filter(equipmentClass::isInstance).map(equipmentClass::cast).toList();
    }

    public Set<Equipment> getEquipmentPurchased() {
        if (equipmentPurchased == null) {
            equipmentPurchased = new SelectionSet<>();
            equipmentPurchased.addSelectionUpdatedListeners(() -> getCacheManager().equipmentPurchasedChanged());
        }
        return equipmentPurchased;
    }

    public void addEquipmentPurchased(Equipment equipmentPurchased) {
        getEquipmentPurchased().add(equipmentPurchased);
    }

    public void setEquipmentPurchased(SelectionSet<Equipment> equipmentPurchased) {
        this.equipmentPurchased = equipmentPurchased;
        this.equipmentPurchased.addSelectionUpdatedListeners(() -> getCacheManager().equipmentPurchasedChanged());
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

    public void setPurchasedArmor(Armor armor, boolean removeOld) throws UnofficialElementNotAllowedException {
        if (armor != null && !armor.isOfficial() && getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("Armor '" + armor + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        if (removeOld) {
            getEquipmentPurchased(Armor.class).forEach(e -> getEquipmentPurchased().remove(e));
        }
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

    public HandheldShield getPurchasedHandheldShield() {
        return getEquipmentPurchased(HandheldShield.class).stream().findFirst().orElse(null);
    }

    public void setPurchasedHandheldShield(HandheldShield handheldShield, boolean removeOld) throws UnofficialElementNotAllowedException {
        if (handheldShield != null && !handheldShield.isOfficial() && getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("HandheldShields shield '" + handheldShield + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        if (removeOld) {
            getEquipmentPurchased(HandheldShield.class).forEach(e -> getEquipmentPurchased().remove(e));
        }
        getEquipmentPurchased().add(handheldShield);
    }

    public Shield getPurchasedShield() {
        return getEquipmentPurchased(Shield.class).stream().findFirst().orElse(null);
    }

    public void setPurchasedShield(Shield shield, boolean removeOld) throws UnofficialElementNotAllowedException {
        if (shield != null && !shield.isOfficial() && getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("Shield '" + shield + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        if (removeOld) {
            getEquipmentPurchased(Shield.class).forEach(e -> getEquipmentPurchased().remove(e));
        }
        getEquipmentPurchased().add(shield);
    }

    public Set<String> getAllowedShields() {
        if (cacheManager.getAllowedShields() == null) {
            if (getBestHandHandledShield() != null) {
                return new HashSet<>();
            }
            if (getBestArmor() != null) {
                return getBestArmor().getAllowedShields();
            }
            cacheManager.setAllowedShields(ShieldFactory.getInstance().getElements().stream().map(Element::getId).collect(Collectors.toSet()));
        }
        return cacheManager.getAllowedShields();
    }

    public List<Weapon> getPurchasedMeleeWeapons() {
        return getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isMeleeWeapon).toList();
    }

    public void setPurchasedMeleeWeapons(List<Weapon> weapons, boolean removeOld) throws UnofficialElementNotAllowedException {
        if (getSettings().isOnlyOfficialAllowed()) {
            for (Weapon weapon : weapons) {
                if (!weapon.isOfficial()) {
                    throw new UnofficialElementNotAllowedException("Weapon '" + weapon + "' is not official and cannot be added due "
                            + "to configuration limitations.");
                }
            }
        }
        if (removeOld) {
            getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isMeleeWeapon).forEach(e -> getEquipmentPurchased().remove(e));
        }
        getEquipmentPurchased().addAll(weapons);
    }

    public List<Weapon> getPurchasedRangedWeapons() {
        return getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isRangedWeapon).toList();
    }

    public void setPurchasedRangedWeapons(List<Weapon> weapons, boolean removeOld) throws UnofficialElementNotAllowedException {
        if (getSettings().isOnlyOfficialAllowed()) {
            for (Weapon weapon : weapons) {
                if (!weapon.isOfficial()) {
                    throw new UnofficialElementNotAllowedException("Weapon '" + weapon + "' is not official and cannot be added due "
                            + "to configuration limitations.");
                }
            }
        }
        if (removeOld) {
            getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isRangedWeapon).forEach(e -> getEquipmentPurchased().remove(e));
        }
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
        return getEquipment().stream().filter(equipmentClass::isInstance).map(equipmentClass::cast).toList();
    }

    public String getRepresentation() {
        final CharacterSheet characterSheet = new CharacterSheet(this);
        return characterSheet.toString();
    }

    public int getTechgnosisLevel() {
        return getLevel();
    }

    public int getTechLevel() {
        if (cacheManager.getTechLevel() == null) {
            cacheManager.setTechLevel(INITIAL_TECH_LEVEL + (int) getCapabilitiesWithSpecialization().stream().filter(capability ->
                    capability.getId().startsWith("techLore") && Objects.equals(capability.getGroup(), "techLore")).count());
        }
        return cacheManager.getTechLevel();
    }

    public double getCashMoney() {
        if (cacheManager.getCash() == null) {
            double cash = 0;
            for (Perk perk : getPerks()) {
                final double perkCash = getCashValue(perk);
                if (perkCash > cash) {
                    cash = perkCash;
                }
            }
            cacheManager.setCash(cash + INITIAL_CASH);
        }
        return cacheManager.getCash();
    }

    private double getCashValue(Perk perk) {
        try {
            return Double.parseDouble(perk.getId().replace("cash", ""));
        } catch (Exception e) {
            return 0d;
        }
    }

    public double getRemainingCash() {
        return getCashMoney() - getSpentCash();
    }

    public double getSpentCash() {
        if (cacheManager.getSpentCash() == null) {
            double total = 0;
            for (Equipment equipment : getEquipmentPurchased()) {
                if (equipment != null) {
                    total += equipment.getCost();
                }
            }
            cacheManager.setSpentCash(total);
        }
        return cacheManager.getSpentCash();
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

    public int getOccultismPointsAvailable(CharacterDefinitionStepSelection characterDefinitionStepSelection) {
        return (int) getPerks(characterDefinitionStepSelection).stream().filter(perk -> Objects.equals(perk.getId(), PerkFactory.THEURGY_RITES_PERK)
                || Objects.equals(perk.getId(), PerkFactory.PSYCHIC_POWERS_PERK)).count();
    }

    public int getOccultismPointsAvailable() {
        return (int) getPerks().stream().filter(perk -> Objects.equals(perk.getId(), PerkFactory.THEURGY_RITES_PERK)
                || Objects.equals(perk.getId(), PerkFactory.PSYCHIC_POWERS_PERK)).count();
    }

    public int getOccultismPointsSpent() {
        return getTotalSelectedPaths();
    }

    public int getOccultismLevel(OccultismType occultismType) {
        if (occultismType == null) {
            return 0;
        }
        return getCharacteristicValue(occultismType.getId());
    }

    public int setOccultismLevel(OccultismType occultismType) {
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

    private OccultismType checkFactionOccultismType() {
        if (getFaction() != null && (FactionGroup.get(getFaction().getGroup()) == FactionGroup.CHURCH
                || FactionGroup.get(getFaction().getGroup()) == FactionGroup.MINOR_CHURCH || Objects.equals(getFaction().getId(), Faction.SIBANZI)
                || Objects.equals(getFaction().getId(), Faction.VAGABONDS) || Objects.equals(getFaction().getId(), Faction.SWORD_OF_LEXTIUS))) {
            return OccultismTypeFactory.getTheurgy();
        }
        return null;
    }

    private OccultismType checkCallingOccultismType() {
        if (getCalling() != null && Objects.equals(getCalling().getId(), Calling.DERVISH)) {
            return OccultismTypeFactory.getPsi();
        }
        if (getCalling() != null && CallingGroup.get(getCalling().getGroup()) == CallingGroup.PSI) {
            return OccultismTypeFactory.getPsi();
        }
        if (getCalling() != null && CallingGroup.get(getCalling().getGroup()) == CallingGroup.THEURGY) {
            return OccultismTypeFactory.getTheurgy();
        }
        return null;
    }

    private OccultismType checkSpecieOccultismType() {
        if (getSpecie() != null && Objects.equals(getSpecie().getId(), Specie.ASCORBITE)) {
            return OccultismTypeFactory.getPsi();
        }
        return null;
    }

    private OccultismType checkSelectedPowerOccultismType() throws InvalidXmlElementException {
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
        return null;
    }

    private int getDefaultOccultismLevel(OccultismType occultismType) {
        if (getSpecie() == null) {
            return 0;
        }
        if (occultismType.getId().equals(OccultismTypeFactory.PSI_TAG)) {
            return SpecieFactory.getInstance().getElement(getSpecie())
                    .getSpecieCharacteristic(CharacteristicName.PSI).getInitialValue();
        }
        if (occultismType.getId().equals(OccultismTypeFactory.THEURGY_TAG)) {
            return SpecieFactory.getInstance().getElement(getSpecie())
                    .getSpecieCharacteristic(CharacteristicName.THEURGY).getInitialValue();
        }
        return 0;
    }

    private OccultismType checkCharacteristicOccultismType() throws InvalidXmlElementException {
        for (final OccultismType occultismType : OccultismTypeFactory.getInstance().getElements()) {
            final int defaultOccultismLevel = getDefaultOccultismLevel(occultismType);
            if (getCharacteristicValue(occultismType.getId()) > defaultOccultismLevel) {
                return occultismType;
            }
        }
        return null;
    }

    /**
     * Returns the selected option by the character.
     *
     * @return an occultism type or null if nothing has been selected.
     */
    public OccultismType getOccultismType() {
        OccultismType result = checkFactionOccultismType();
        if (result != null) {
            return result;
        }
        result = checkCallingOccultismType();
        if (result != null) {
            return result;
        }
        result = checkSpecieOccultismType();
        if (result != null) {
            return result;
        }
        try {
            result = checkSelectedPowerOccultismType();
            if (result != null) {
                return result;
            }
            return checkCharacteristicOccultismType();
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
        if (power.getRestrictions().isRestricted(this)) {
            throw new InvalidOccultismPowerException("Occultism Power '" + power + "' is restricted to this character.");
        }
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
        getOccultism().addPower(this, path, power, getFaction() != null ? getFaction().getId() : null, getSpecie().getId(), getSettings());
    }

    public void removeOccultismPower(OccultismPower power) {
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
        if (path != null) {
            getOccultism().removePower(path, power);
        }
    }

    public boolean hasOccultismPath(OccultismPath path) {
        return getOccultism().hasPath(path);
    }

    public List<OccultismPower> getAllSelectedPowers() {
        return getOccultism().getSelectedPowers().values().stream().flatMap(Collection::stream).toList();
    }

    public List<Cyberdevice> getCyberdevices() {
        final List<SpecializedPerk> cyberdevices = getPerks().stream()
                .filter(perk -> Objects.equals(perk.getType(), PerkType.CYBERDEVICE)).toList();
        return CyberdeviceFactory.getInstance().getElements(cyberdevices.stream().map(Perk::getId).toList());
    }

    public Affliction getAffliction() {
        return affliction;
    }

    public void setAffliction(Affliction affliction) {
        this.affliction = affliction;
    }

    public boolean canUseCombatArmor() {
        if (cacheManager.getCombatArmor() == null) {
            cacheManager.setCombatArmor(hasCapability(Capability.COMBAT_ARMOR_CAPABILITY, (String) null));
        }
        return cacheManager.getCombatArmor();
    }

    public boolean canUseWarArmor() {
        if (cacheManager.getWarArmor() == null) {
            cacheManager.setWarArmor(hasCapability(Capability.WAR_ARMOR_CAPABILITY, (String) null));
        }
        return cacheManager.getWarArmor();
    }

    public boolean canUseMilitaryWeapons() {
        if (cacheManager.getMilitaryWeapons() == null) {
            cacheManager.setMilitaryWeapons(hasCapability(Capability.MILITARY_WEAPONS_CAPABILITY, (String) null));
        }
        return cacheManager.getMilitaryWeapons();
    }

    public CacheManager getCacheManager() {
        return cacheManager;
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

    public void setRaisedInSpace(boolean raisedInSpace) {
        if (getUpbringing() != null) {
            getUpbringing().setRaisedInSpace(raisedInSpace);
            getCacheManager().reset();
        }
    }

    public boolean isRaisedInSpace() {
        if (getUpbringing() != null) {
            return getUpbringing().isRaisedInSpace();
        }
        return false;
    }
}
