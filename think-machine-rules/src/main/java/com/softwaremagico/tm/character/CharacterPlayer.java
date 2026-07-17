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
      this.settings = new Settings();
      this.cacheManager = new CacheManager();
      this.reset();
    }

    private void reset() {
      this.info = new CharacterInfo();
      this.occultism = new Occultism();
      this.specie = null;
      this.upbringing = null;
      this.faction = null;
      this.calling = null;
      this.equipmentPurchased = new SelectionSet<>();
      this.cacheManager.reset();
    }

    public SpecieCharacterDefinitionStepSelection getSpecie() {
        return this.specie;
    }

    private void validateSelections() throws InvalidSelectionException {
        if (this.specie != null) {
            this.specie.validate();
        }
        if (this.upbringing != null) {
            this.upbringing.validate();
        }
        if (this.faction != null) {
            this.faction.validate();
        }
        if (this.calling != null) {
            this.calling.validate();
        }
    }

    private void validateCharacteristics() throws MaxValueExceededException, InvalidXmlElementException {
        if (this.getPrimaryCharacteristic() == null || this.getSecondaryCharacteristic() == null) {
            throw new InvalidCharacteristicException("You must choose your primary and secondary characteristic.");
        }
        for (final CharacteristicDefinition characteristicDefinition : CharacteristicsDefinitionFactory.getInstance().getElements()) {
            if (characteristicDefinition.getType() != CharacteristicType.OTHERS) {
                final int characteristicValue = this.getCharacteristicValue(characteristicDefinition.getCharacteristicName());
                if (characteristicValue > (SpecieFactory.getInstance().getElement(this.getSpecie().getId())
                        .getSpecieCharacteristic(characteristicDefinition.getCharacteristicName()).getMaximumValue())) {
                    throw new MaxValueExceededException("Characteristic '" + characteristicDefinition.getCharacteristicName()
                            + "' has exceeded its maximum value of '"
                            + (SpecieFactory.getInstance().getElement(this.getSpecie().getId())
                            .getSpecieCharacteristic(characteristicDefinition.getCharacteristicName()).getMaximumValue())
                            + "' by specie.", characteristicDefinition.getId(), characteristicValue,
                            (SpecieFactory.getInstance().getElement(this.getSpecie().getId())
                                    .getSpecieCharacteristic(characteristicDefinition.getCharacteristicName()).getMaximumValue()));
                }
            }
        }
    }

    private void validateSkill(Skill skill) throws InvalidSkillException {
        try {
          this.getSkillValue(skill);
        } catch (final InvalidSkillException e) {
            throw new InvalidSkillException("Skill '" + skill.getId()
                    + "' has exceeded its maximum value at level '" + this.getLevel() + "'.");
        }
    }

    private void validateSkills() throws InvalidXmlElementException {
        for (final Skill skill : SkillFactory.getInstance().getElements()) {
          this.validateSkill(skill);
        }
    }

    public void validate() {
        try {
          this.validateSelections();
          this.validateCharacteristics();
          this.validateSkills();
          this.checkDuplicatedPerks();
          this.checkDuplicatedCapabilities();
          this.getLevels().forEach(LevelSelector::validate);
        } catch (final InvalidSelectionException e) {
            throw new InvalidFactionException("Error on character '" + this + "'.", e);
        }
    }

    public void checkMaxValueByLevel(Element element, int value) throws MaxValueExceededException {
        if (element != null) {
          this.checkMaxValueByLevel(element.getId(), value);
        }
    }

    public void checkMaxValueByLevel(String element, int value) throws MaxValueExceededException {
        if ((this.getLevel() == ElementValues.INITIAL_LEVEL && value > this.getMaximumInitialValue(element))
                || (this.getLevel() == ElementValues.INTERMEDIATE_LEVEL && value > this.getMaximumIntermediateValue())
                || (this.getLevel() > ElementValues.INTERMEDIATE_LEVEL && this.getLevel() < ElementValues.ADVANCED_LEVEL
                && value > Math.min(ElementValues.LEVEL_MAX_VALUE, this.getMaximumValue(element)))
                || this.getLevel() > ElementValues.ADVANCED_LEVEL && value > this.getMaximumValue(element)) {
            String composition;
            try {
                composition = this.getCharacteristicComposition(element);
            } catch (final Exception e) {
                composition = this.getSkillComposition(element);
            }
            final int maxValue = this.getLevel() == ElementValues.INITIAL_LEVEL ? this.getMaximumInitialValue(element)
                    : (this.getLevel() == ElementValues.INTERMEDIATE_LEVEL ? this.getMaximumIntermediateValue()
                    : Math.min(ElementValues.LEVEL_MAX_VALUE, this.getMaximumValue(element)));
            throw new MaxValueExceededException("Element '" + element + "' has exceeded its maximum value '" + value + "' at level '" + this.getLevel() + "'. "
                    + "Obtained by " + composition, element, value, maxValue);
        }
    }

    public void setSpecie(String specie) {
        if (specie != null) {
            this.specie = new SpecieCharacterDefinitionStepSelection(this, specie);
            try {
                this.specie.validate();
            } catch (final InvalidSelectionException e) {
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
        } catch (final InvalidSelectionException e) {
          this.setUpbringing((String) null);
        }
    }

    public FactionCharacterDefinitionStepSelection getFaction() {
        return this.faction;
    }

    public UpbringingCharacterDefinitionStepSelection getUpbringing() {
        return this.upbringing;
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
          this.setFaction((String) null);
        }

        if (this.calling != null && CallingFactory.getInstance().getElement(this.calling.getId()).getRestrictions().isRestricted(this)) {
          this.setCalling((String) null);
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
              this.setCalling((String) null);
            }
        } catch (final InvalidSelectionException e) {
          this.setCalling((String) null);
        }
    }

    public CallingCharacterDefinitionStepSelection getCalling() {
        return this.calling;
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
        return (this.getFaction() != null && this.getCalling() != null
                && FactionFactory.getInstance().getElement(this.getFaction()).getFavoredCallings().contains(this.getCalling().getId()));
    }

    public Settings getSettings() {
        return this.settings;
    }

    public int getSkillValue(Skill skill) throws MaxValueExceededException {
        if (skill == null) {
            return 0;
        }
        return this.getSkillValue(skill.getId());
    }

    public int getSkillValue(String skill) throws MaxValueExceededException {
        if (this.cacheManager.getSkillValue(skill) == null) {
            int bonus = 0;
            if (SkillFactory.getInstance().getElement(skill).isNatural()) {
                bonus += Skill.NATURAL_SKILL_INITIAL_VALUE;
            }
            if (this.specie != null) {
                bonus += this.specie.getSkillBonus(skill);
            }
            if (this.upbringing != null) {
                final int skillBonus = this.upbringing.getSkillBonus(skill);
                bonus += skillBonus;
            }
            if (this.faction != null) {
                final int skillBonus = this.faction.getSkillBonus(skill);
                bonus += skillBonus;
            }
            if (this.calling != null) {
                final int skillBonus = this.calling.getSkillBonus(skill);
                bonus += skillBonus;
            }

            bonus -= this.getSkillReassignValueDecreased(skill);
            bonus += this.getSkillReassignValueIncreased(skill);

            for (final LevelSelector levelSelector : this.getLevels()) {
                bonus += levelSelector.getSkillBonus(skill);
            }

            bonus += this.getCyberdevicesAlwaysBonification(skill);

          this.checkMaxValueByLevel(skill, bonus);

          this.cacheManager.setSkillValue(skill, bonus);
        }
        return this.cacheManager.getSkillValue(skill);
    }

    public String getSkillComposition(String skill) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (this.upbringing != null) {
          this.appendSkillBonusIfPresent(stringBuilder, "upbringing", this.upbringing.getSkillBonus(skill));
        }
        if (this.faction != null) {
          this.appendSkillBonusIfPresent(stringBuilder, "faction", this.faction.getSkillBonus(skill));
        }
        if (this.calling != null) {
          this.appendSkillBonusIfPresent(stringBuilder, "calling", this.calling.getSkillBonus(skill));
        }
      this.appendSkillBonusIfPresent(stringBuilder, "cyberdevices", this.getCyberdevicesAlwaysBonification(skill));
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
        for (final Cyberdevice cyberdevice : this.getCyberdevices()) {
            for (final Bonification bonification : cyberdevice.getBonifications()) {
                if (bonification.getSituation() != null && !bonification.getSituation().isBlank()) {
                    continue;
                }
                if (this.affectsValue(bonification.getAffects(), valueId)) {
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
        for (final SkillsReassign skillsReassign : this.skillsReassigns) {
            if (skillsReassign.getFrom().equals(skill)) {
                total++;
            }
        }
        return total;
    }

    public int getSkillReassignValueIncreased(String skill) {
        int total = 0;
        for (final SkillsReassign skillsReassign : this.skillsReassigns) {
            if (skillsReassign.getTo().equals(skill)) {
                total++;
            }
        }
        return total;
    }

    public List<SkillsReassign> getSkillsReassigns() {
        return this.skillsReassigns;
    }

    public int getCharacteristicValue(CharacteristicName characteristic) throws MaxValueExceededException {
        if (characteristic == null) {
            return 0;
        }
        return this.getCharacteristicValue(characteristic.getId());
    }

    public String getPrimaryCharacteristic() {
        return this.primaryCharacteristic;
    }

    public void setPrimaryCharacteristic(String primaryCharacteristic) {
        this.primaryCharacteristic = primaryCharacteristic;
    }

    public String getSecondaryCharacteristic() {
        return this.secondaryCharacteristic;
    }

    public void setSecondaryCharacteristic(String secondaryCharacteristic) {
        this.secondaryCharacteristic = secondaryCharacteristic;
    }

    private int getCharacteristicInitialValue(String characteristic) {
        return SpecieFactory.getInstance().getElement(this.getSpecie()).getSpecieCharacteristic(characteristic).getInitialValue();
    }


    private int getInitialValue(String element) {
        try {
            return this.getCharacteristicInitialValue(element);
        } catch (final InvalidXmlElementException e) {
            //Not a characteristic
            return ElementValues.INITIAL_VALUE;
        }
    }

    public int getMaximumValue(String element) {
        try {
            return SpecieFactory.getInstance().getElement(this.getSpecie()).getSpecieCharacteristic(element).getMaximumValue();
        } catch (final InvalidXmlElementException | NullPointerException e) {
            return ElementValues.LEVEL_MAX_VALUE;
        }
    }

    private int getMaximumIntermediateValue() {
        return ElementValues.MAX_INTERMEDIATE_VALUE;
    }

    private int getMaximumInitialValue(String element) {
        try {
            return SpecieFactory.getInstance().getElement(this.getSpecie()).getSpecieCharacteristic(element).getMaximumInitialValue();
        } catch (final InvalidXmlElementException | NullPointerException e) {
            return ElementValues.MAX_INITIAL_VALUE;
        }
    }

    private int getBaseCharacteristicBonus(String characteristic, CharacteristicName characteristicName) {
        if (this.getPrimaryCharacteristic() != null && Objects.equals(this.getPrimaryCharacteristic(), characteristic)) {
            return CharacteristicDefinition.PRIMARY_CHARACTERISTIC_VALUE;
        } else if (this.getSecondaryCharacteristic() != null && Objects.equals(this.getSecondaryCharacteristic(), characteristic)) {
            return CharacteristicDefinition.SECONDARY_CHARACTERISTIC_VALUE;
        } else if (this.specie != null) {
            return this.getInitialValue(characteristic);
        } else if (this.isBasicCharacteristic(characteristicName)) {
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
        if (this.upbringing != null) {
            bonus += this.upbringing.getCharacteristicBonus(characteristic);
        }
        if (this.faction != null) {
            bonus += this.faction.getCharacteristicBonus(characteristic);
        }
        if (this.calling != null) {
            bonus += this.calling.getCharacteristicBonus(characteristic);
        }
        for (final LevelSelector levelSelector : this.getLevels()) {
            bonus += levelSelector.getCharacteristicBonus(characteristic);
        }
        bonus += this.getCyberdevicesAlwaysBonification(characteristic);
        bonus -= this.getCharacteristicReassignValueDecreased(characteristic);
        bonus += this.getCharacteristicReassignValueIncreased(characteristic);
        return bonus;
    }

    public int getCharacteristicValue(String characteristic) throws MaxValueExceededException {
        if (this.cacheManager.getCharacteristicValue(characteristic) == null) {
            final CharacteristicName characteristicName = CharacteristicName.get(characteristic);
            if (characteristicName == null) {
                throw new InvalidCharacteristicException("No characteristic '" + characteristic + "' exists.");
            }
            int bonus = this.getBaseCharacteristicBonus(characteristic, characteristicName);
            bonus = this.accumulateCharacteristicBonuses(characteristic, bonus);
          this.checkMaxValueByLevel(characteristic, bonus);
            if (this.specie != null && bonus > this.getMaximumValue(characteristic)) {
                throw new MaxValueExceededException("Characteristic '" + characteristic + "' has exceeded the maximum value of '"
                        + this.getMaximumValue(characteristic) + "' with '"
                        + bonus + "'. " + this.getCharacteristicComposition(characteristic), characteristic, bonus,
                        this.getMaximumValue(characteristic));
            }
          this.cacheManager.setCharacteristicValue(characteristic, bonus);
        }

        return this.cacheManager.getCharacteristicValue(characteristic);
    }

    private String getPrimaryCharacteristicComposition(String characteristic) {
        if (this.getPrimaryCharacteristic() != null && Objects.equals(this.getPrimaryCharacteristic(), characteristic)) {
            return "Primary characteristic: " + CharacteristicDefinition.PRIMARY_CHARACTERISTIC_VALUE;
        }
        return null;
    }

    private String getSecondaryCharacteristicComposition(String characteristic) {
        if (this.getSecondaryCharacteristic() != null && Objects.equals(this.getSecondaryCharacteristic(), characteristic)) {
            return "Secondary characteristic: " + CharacteristicDefinition.SECONDARY_CHARACTERISTIC_VALUE;
        }
        return null;
    }

    private String getSpecieCharacteristicComposition(String characteristic) {
        if (this.specie != null) {
            final int bonus = this.getInitialValue(characteristic);
            if (bonus > 0) {
                return "Specie bonus: " + bonus;
            }
        }
        return null;
    }

    private String getBasicCharacteristicComposition(CharacteristicName characteristicName) {
        if (this.isBasicCharacteristic(characteristicName)) {
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

        final String primaryComp = this.getPrimaryCharacteristicComposition(characteristic);
        if (primaryComp != null) {
            stringBuilder.append(primaryComp);
        } else {
            final String secondaryComp = this.getSecondaryCharacteristicComposition(characteristic);
            if (secondaryComp != null) {
                stringBuilder.append(secondaryComp);
            } else {
                final String specieComp = this.getSpecieCharacteristicComposition(characteristic);
                if (specieComp != null) {
                    stringBuilder.append(specieComp);
                } else {
                    final String basicComp = this.getBasicCharacteristicComposition(characteristicName);
                    if (basicComp != null) {
                        stringBuilder.append(basicComp);
                    }
                }
            }
        }

        if (this.upbringing != null) {
          this.appendBonusIfPresent(stringBuilder, "upbringing", this.upbringing.getCharacteristicBonus(characteristic));
        }
        if (this.faction != null) {
          this.appendBonusIfPresent(stringBuilder, "faction", this.faction.getCharacteristicBonus(characteristic));
        }
        if (this.calling != null) {
          this.appendBonusIfPresent(stringBuilder, "calling", this.calling.getCharacteristicBonus(characteristic));
        }
      this.appendBonusIfPresent(stringBuilder, "cyberdevices", this.getCyberdevicesAlwaysBonification(characteristic));
      this.appendBonusIfPresent(stringBuilder, "balance", this.getCharacteristicReassignValueIncreased(characteristic));
        return stringBuilder.toString();
    }

    public CombatActionRequirement getCharacteristicCombatValue(String id) {
        // Keep null behavior for unknown/invalid ids to preserve existing combat checks.
        if (id == null || id.isBlank()) {
            return null;
        }
        try {
            final int characteristicValue = this.getCharacteristicValue(id);
            final CombatActionRequirement combatActionRequirement = new CombatActionRequirement();
            combatActionRequirement.setValue(characteristicValue);
            return combatActionRequirement;
        } catch (final InvalidCharacteristicException | MaxValueExceededException e) {
            return null;
        }
    }

    public int getCharacteristicReassignValueDecreased(String characteristic) {
        int total = 0;
        for (final CharacteristicReassign characteristicReassign : this.characteristicReassigns) {
            if (characteristicReassign.getFrom().equals(characteristic)) {
                total++;
            }
        }
        return total;
    }

    public int getCharacteristicReassignValueIncreased(String characteristic) {
        int total = 0;
        for (final CharacteristicReassign characteristicReassign : this.characteristicReassigns) {
            if (characteristicReassign.getTo().equals(characteristic)) {
                total++;
            }
        }
        return total;
    }

    public List<CharacteristicReassign> getCharacteristicReassigns() {
        return this.characteristicReassigns;
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
        if (this.cacheManager.getPerksWithSpecializations() == null) {
          this.cacheManager.setPerksWithSpecializations(this.getPerks((Integer) null));
        }
        return this.cacheManager.getPerksWithSpecializations();
    }

    public Set<SpecializedPerk> getPerks(Integer level) {
        final Set<SpecializedPerk> perks = new HashSet<>();
        if (this.upbringing != null) {
            perks.addAll(this.getSelectedPerks(this.upbringing));
        }
        if (this.faction != null) {
            perks.addAll(this.getSelectedPerks(this.faction));
        }
        if (this.calling != null) {
            perks.addAll(this.getSelectedPerks(this.calling));
        }
        for (final LevelSelector levelSelector : this.getLevels()) {
            if (level == null || level <= levelSelector.getLevel()) {
                perks.addAll(this.getSelectedPerks(levelSelector));
            }
        }
        return perks;
    }

    public boolean hasPerk(String id) {
        for (final SpecializedPerk specializedPerk : this.getPerks()) {
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
                    } catch (final Exception e) {
                        MachineLog.warning(this.getClass(), e.getMessage());
                    }
                }));
        return perks;
    }

    public CharacterInfo getInfo() {
        return this.info;
    }

    public Integer getVitalityValue() throws InvalidXmlElementException {
        final AtomicInteger vitality = new AtomicInteger(this.getCharacteristicValue(CharacteristicName.ENDURANCE)
                + this.getCharacteristicValue(CharacteristicName.WILL)
                + this.getCharacteristicValue(CharacteristicName.FAITH)
                + (this.specie != null ? SpecieFactory.getInstance().getElement(this.specie).getSize() : 0)
                + (this.specie != null ? SpecieFactory.getInstance().getElement(this.specie).getVitalityBonus() : 0));
      this.getLevels().forEach(level -> vitality.addAndGet(level.getExtraVitality()));
        return vitality.get();
    }

    public Set<CapabilityWithSpecialization> getCapabilitiesWithSpecialization() {
        if (this.cacheManager.getCapabilityWithSpecializations() == null) {
            final Set<CapabilityWithSpecialization> capabilities = new HashSet<>();
            if (this.specie != null) {
              this.specie.getSelectedCapabilityOptions().forEach(capabilityOption ->
                        capabilityOption.getSelections().forEach(selection ->
                                capabilities.add(CapabilityWithSpecialization.from(selection))));
            }
            if (this.upbringing != null) {
              this.upbringing.getSelectedCapabilityOptions().forEach(capabilityOption ->
                        capabilityOption.getSelections().forEach(selection ->
                                capabilities.add(CapabilityWithSpecialization.from(selection))));
            }
            if (this.faction != null) {
              this.faction.getSelectedCapabilityOptions().forEach(capabilityOption ->
                        capabilityOption.getSelections().forEach(selection ->
                                capabilities.add(CapabilityWithSpecialization.from(selection))));
            }
            if (this.calling != null) {
              this.calling.getSelectedCapabilityOptions().forEach(capabilityOption ->
                        capabilityOption.getSelections().forEach(selection ->
                                capabilities.add(CapabilityWithSpecialization.from(selection))));
            }
            for (final LevelSelector levelSelector : this.getLevels()) {
                levelSelector.getSelectedCapabilityOptions().forEach(capabilityOption ->
                        capabilityOption.getSelections().forEach(selection ->
                                capabilities.add(CapabilityWithSpecialization.from(selection))));
            }
          this.cacheManager.setCapabilityWithSpecializations(capabilities);
        }
        return this.cacheManager.getCapabilityWithSpecializations();
    }

    public boolean hasCapability(String capability, String specialization) {
        final String comparedCapability = ComparableUtils.getComparisonId(capability, specialization);
        return this.getCapabilitiesWithSpecialization().stream().map(CapabilityWithSpecialization::getComparisonId)
                .anyMatch(x -> Objects.equals(x, comparedCapability));
    }

    private boolean checkCapabilityInPhases(String comparedCapability, Phase phase, String stepId) {
        if (Phase.SPECIE.isCheckedPhase(phase) && this.hasCapability(comparedCapability, this.specie)) {
            return true;
        }
        if (Phase.UPBRINGING.isCheckedPhase(phase) && this.hasCapability(comparedCapability, this.upbringing)) {
            return true;
        }
        if (Phase.FACTION.isCheckedPhase(phase) && this.hasCapability(comparedCapability, this.faction)) {
            return true;
        }
        if (Phase.CALLING.isCheckedPhase(phase) && this.hasCapability(comparedCapability, this.calling)) {
            return true;
        }
        if (Phase.LEVEL.isCheckedPhase(phase) || phase == Phase.LEVEL) {
            for (final LevelSelector levelSelector : this.getLevels()) {
                if (Objects.equals(levelSelector.getId(), stepId)) {
                    break;
                }
                if (this.hasCapability(comparedCapability, levelSelector)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasCapability(String capability, String specialization, Phase phase, String stepId) {
        final String comparedCapability = ComparableUtils.getComparisonId(capability, specialization);
        return this.checkCapabilityInPhases(comparedCapability, phase, stepId);
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
            possibleSelections.removeAll(this.specie.getSelectedPerks());
        }
        if (Phase.UPBRINGING.isCheckedPhase(phase) && this.upbringing != null) {
            possibleSelections.removeAll(this.upbringing.getSelectedPerks());
        }
        if (Phase.FACTION.isCheckedPhase(phase) && this.faction != null) {
            possibleSelections.removeAll(this.faction.getSelectedPerks());
        }
        if (Phase.CALLING.isCheckedPhase(phase) && this.calling != null) {
            possibleSelections.removeAll(this.calling.getSelectedPerks());
        }
        if ((Phase.LEVEL.isCheckedPhase(phase) || phase == Phase.LEVEL) && level != null) {
            for (int i = 0; i < this.getLevels().size() && i < level; i++) {
                possibleSelections.removeAll(this.getLevels().get(i).getSelectedPerks());
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
            for (final Specialization specialization : perk.getSpecializations()) {
                possibleSelections.add(new Selection(perk, specialization));
            }
        }
      this.removePerkSelectionsFromPhases(possibleSelections, phase, level);
        return !possibleSelections.isEmpty();
    }

    public boolean hasSelection(Selection perkSelection, CharacterDefinitionStepSelection step) {
        if (step == null) {
            return false;
        }
        return this.hasSelection(perkSelection, step.getPhase(), step.getLevel());
    }

    public boolean hasSelection(Selection perkSelection, Phase phase, Integer level) {
        final List<CharacterDefinitionStepSelection> stepsToCheck = new ArrayList<>();
        if (phase != null && this.specie != null && phase.checkedUntilPhase(Phase.SPECIE)) {
            stepsToCheck.add(this.specie);
        }
        if (phase != null && this.upbringing != null && phase.checkedUntilPhase(Phase.UPBRINGING)) {
            stepsToCheck.add(this.upbringing);
        }
        if (phase != null && this.faction != null && phase.checkedUntilPhase(Phase.FACTION)) {
            stepsToCheck.add(this.faction);
        }
        if (phase != null && this.calling != null && phase.checkedUntilPhase(Phase.CALLING)) {
            stepsToCheck.add(this.calling);
        }
        //Levels always check previous levels.
        if (phase != null && phase.checkedUntilPhase(Phase.LEVEL) || phase == Phase.LEVEL) {
            for (int i = 0; i < this.getLevels().size() && (level == null || i < level - 1); i++) {
                stepsToCheck.add(this.getLevels().get(i));
            }
        }
        return this.hasSelection(perkSelection, stepsToCheck);
    }

    public boolean hasSelection(Selection selection, Collection<CharacterDefinitionStepSelection> steps) {
        if (steps != null) {
            for (final CharacterDefinitionStepSelection step : steps) {
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
            for (final Selection perk : phasePerks) {
                if (!this.isRepeatablePerk(perk)) {
                    throw new InvalidXmlElementException("Duplicated perks '" + phasePerks + "' on " + phaseName + ".");
                }
            }
        }
        completePerkList.addAll(nextPerks);
    }

    private void checkDuplicatedPerksInLevels(Collection<Selection> completePerkList) {
        for (final LevelSelector levelSelector : this.getLevels()) {
            final Collection<Selection> levelPerks = this.getPerks(levelSelector);
            final Collection<Selection> nextPerks = new ArrayList<>(levelPerks);
            levelPerks.retainAll(completePerkList);
            if (!levelPerks.isEmpty()) {
                for (final Selection perk : levelPerks) {
                    if (!this.isRepeatablePerk(perk)) {
                        throw new InvalidXmlElementException("Duplicated perk '" + levelPerks + "' on level '" + levelSelector + "'.");
                    }
                }
            }
            completePerkList.addAll(nextPerks);
        }
    }

    public void checkDuplicatedPerks() {
        final Collection<Selection> speciePerks = this.getPerks(this.specie);
        final Collection<Selection> upbringingPerks = this.getPerks(this.upbringing);
        final Collection<Selection> factionPerks = this.getPerks(this.faction);
        final Collection<Selection> callingPerks = this.getPerks(this.calling);

        final Collection<Selection> completePerkList = new HashSet<>(speciePerks);

      this.checkDuplicatedPerksInPhase(upbringingPerks, completePerkList, "upbringing '" + this.getUpbringing() + "'");
      this.checkDuplicatedPerksInPhase(factionPerks, completePerkList, "faction '" + this.getFaction() + "'");
      this.checkDuplicatedPerksInPhase(callingPerks, completePerkList, "calling '" + this.getCalling() + "'");
      this.checkDuplicatedPerksInLevels(completePerkList);
    }

    private boolean isRepeatablePerk(Selection perk) {
        return PerkFactory.getInstance().getElement(perk).isRepeatable();
    }

    public void checkDuplicatedCapabilities() {
        //Check duplicate capabilities.
        final Collection<String> specieCapabilities = this.getCapabilities(this.specie);
        final Collection<String> upbringingCapabilities = this.getCapabilities(this.upbringing);
        final Collection<String> factionCapabilities = this.getCapabilities(this.faction);
        final Collection<String> callingCapabilities = this.getCapabilities(this.calling);

        final Collection<String> completeCapabilitiesList = new HashSet<>(specieCapabilities);
        Collection<String> nextCapabilities = new HashSet<>(upbringingCapabilities);

        upbringingCapabilities.retainAll(completeCapabilitiesList);
        if (!upbringingCapabilities.isEmpty()) {
            throw new InvalidXmlElementException("Duplicated capability '" + upbringingCapabilities + "' on upbringing '" + this.getUpbringing() + "'.");
        }
        completeCapabilitiesList.addAll(nextCapabilities);

        nextCapabilities = new ArrayList<>(factionCapabilities);
        factionCapabilities.retainAll(completeCapabilitiesList);
        if (!factionCapabilities.isEmpty()) {
            throw new InvalidXmlElementException("Duplicated capability '" + upbringingCapabilities + "' on faction '" + this.getFaction() + "'.");
        }
        completeCapabilitiesList.addAll(nextCapabilities);

        nextCapabilities = new ArrayList<>(callingCapabilities);
        callingCapabilities.retainAll(completeCapabilitiesList);
        if (!callingCapabilities.isEmpty()) {
            throw new InvalidXmlElementException("Duplicated capability '" + callingCapabilities + "' on calling '" + this.getCalling() + "'.");
        }
        completeCapabilitiesList.addAll(nextCapabilities);

        for (final LevelSelector levelSelector : this.getLevels()) {
            final Collection<String> levelCapabilities = this.getCapabilities(levelSelector);
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
            // checkDuplicatedCapabilities() aplica retainAll(), por lo que esta colección debe ser mutable.
            return new ArrayList<>(step.getSelectedCapabilities().stream()
                    .map(c -> ComparableUtils.getComparisonId(c.getId(), c.getSpecialization()))
                    .toList());
        }
        return new ArrayList<>();
    }


    public String getCompleteNameRepresentation() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (this.getInfo() != null) {
            stringBuilder.append(this.getInfo().getNameRepresentation());
            stringBuilder.append(" ");
            if (this.getInfo() != null && this.getInfo().getSurname() != null) {
                stringBuilder.append(this.getInfo().getSurname().getName());
            }
        }
        return stringBuilder.toString().trim();
    }

    public int getBodyResistance() {
        return Resistance.getBonus(ResistanceType.BODY, this)
                + SpecieFactory.getInstance().getElement(this.getSpecie().getId()).getBodyResistance();
    }

    public int getMindResistance() {
        return Resistance.getBonus(ResistanceType.MIND, this);
    }

    public int getSpiritResistance() {
        return Resistance.getBonus(ResistanceType.SPIRIT, this);
    }

    public int getLevel() {
        return 1 + this.levels.size();
    }

    public LevelSelector getLevel(int index) {
        return this.levels.get(index - 1);
    }

    public LevelSelector getLatestLevel() {
        return this.levels.get(this.levels.size() - 1);
    }

    public Stack<LevelSelector> getLevels() {
        return this.levels;
    }

    public LevelSelector addLevel() {
        if (this.getFaction() == null || this.getSpecie() == null || this.getCalling() == null) {
            throw new InvalidLevelException("Error on character '" + this + "'. Please, finalize or correct level 1 first.");
        }
        try {
          this.validate();
        } catch (final InvalidXmlElementException e) {
            throw new InvalidLevelException("Error on character '" + this + "'. Please, finalize or correct previous level first.", e);
        }
        final LevelSelector newLevel = new LevelSelector(this, this.getLevel() + 1);
      this.levels.add(newLevel);
      this.cacheManager.reset();
        return newLevel;
    }

    public void removeLevel(int level) {
        if (level == 0) {
            throw new InvalidLevelException("First level cannot be removed.");
        }
        try {
          this.levels.remove(level - 2);
          this.cacheManager.reset();
        } catch (final IndexOutOfBoundsException e) {
            //Level not existing.
        }
    }

    public int getBank() throws InvalidXmlElementException {
        final AtomicInteger bank = new AtomicInteger(BANK_INITIAL_VALUE);
      this.getLevels().forEach(level ->
                bank.addAndGet(level.getExtraVPBank()));
        return bank.get();
    }

    public int getSurgesRating() throws InvalidXmlElementException {
        final AtomicInteger surge = new AtomicInteger(Math.max(Math.max(this.getCharacteristicValue(CharacteristicName.STRENGTH),
                        this.getCharacteristicValue(CharacteristicName.WITS)),
                this.getCharacteristicValue(CharacteristicName.FAITH)));
      this.getLevels().forEach(level ->
                surge.addAndGet(level.getExtraSurgeRating()));
        return surge.get();
    }

    public int getSurgesNumber() throws InvalidXmlElementException {
        final AtomicInteger surge = new AtomicInteger(1);
      this.getLevels().forEach(level ->
                surge.addAndGet(level.getExtraSurgeNumber()));
        return surge.get();
    }

    public int getRevivalsRating() throws InvalidXmlElementException {
        final AtomicInteger revivals = new AtomicInteger(
                (this.specie != null ? SpecieFactory.getInstance().getElement(this.specie).getSize() : 0));
      this.getLevels().forEach(level ->
                revivals.addAndGet(level.getExtraRevivalRating()));
        return revivals.get();
    }

    public int getRevivalsNumber() throws InvalidXmlElementException {
        final AtomicInteger revivals = new AtomicInteger(1);
      this.getLevels().forEach(level ->
                revivals.addAndGet(level.getExtraRevivalNumber()));
        return revivals.get();
    }


    /**
     * Gets all weapons purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public List<Weapon> getWeapons() {
        return this.getEquipment(Weapon.class);
    }

    /**
     * Gets all items purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public List<Item> getItems() {
        return this.getEquipment(Item.class);
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
        return this.getMaterialAwardsSelected(false);
    }

    public List<EquipmentOption> getMaterialAwardsSelected(boolean ignoreRemoved) {
        final List<EquipmentOption> materialAwards = new ArrayList<>();
        materialAwards.addAll(this.getSelectedMaterialAwards(this.upbringing, UpbringingFactory.getInstance(), ignoreRemoved));
        materialAwards.addAll(this.getSelectedMaterialAwards(this.faction, FactionFactory.getInstance(), ignoreRemoved));
        materialAwards.addAll(this.getSelectedMaterialAwards(this.calling, CallingFactory.getInstance(), ignoreRemoved));
        return materialAwards;
    }

    public <T extends Equipment> List<T> getEquipmentPurchased(Class<T> equipmentClass) {
        return this.getEquipmentPurchased().stream().filter(equipmentClass::isInstance).map(equipmentClass::cast).toList();
    }

    public Set<Equipment> getEquipmentPurchased() {
        if (this.equipmentPurchased == null) {
          this.equipmentPurchased = new SelectionSet<>();
          this.equipmentPurchased.addSelectionUpdatedListeners(() -> this.getCacheManager().equipmentPurchasedChanged());
        }
        return this.equipmentPurchased;
    }

    public void addEquipmentPurchased(Equipment equipmentPurchased) {
      this.getEquipmentPurchased().add(equipmentPurchased);
    }

    public void setEquipmentPurchased(SelectionSet<Equipment> equipmentPurchased) {
        this.equipmentPurchased = equipmentPurchased;
        this.equipmentPurchased.addSelectionUpdatedListeners(() -> this.getCacheManager().equipmentPurchasedChanged());
    }

    /**
     * Gets best armor purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public Armor getBestArmor() {
        final List<Armor> armors = this.getEquipment(Armor.class);
        if (armors.isEmpty()) {
            return null;
        }
        return Collections.max(armors, Comparator.comparing(Armor::getCost));
    }

    public Armor getPurchasedArmor() {
        return this.getEquipmentPurchased(Armor.class).stream().findFirst().orElse(null);
    }

    public void setPurchasedArmor(Armor armor, boolean removeOld) throws UnofficialElementNotAllowedException {
        if (armor != null && !armor.isOfficial() && this.getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("Armor '" + armor + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        if (removeOld) {
          this.getEquipmentPurchased(Armor.class).forEach(e -> this.getEquipmentPurchased().remove(e));
        }
      this.getEquipmentPurchased().add(armor);
    }

    /**
     * Gets best shield purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public Shield getBestShield() {
        final List<Shield> shields = this.getEquipment(Shield.class);
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
        final List<HandheldShield> handheldShields = this.getEquipment(HandheldShield.class);
        if (handheldShields.isEmpty()) {
            return null;
        }
        return Collections.max(handheldShields, Comparator.comparing(HandheldShield::getCost));
    }

    public HandheldShield getPurchasedHandheldShield() {
        return this.getEquipmentPurchased(HandheldShield.class).stream().findFirst().orElse(null);
    }

    public void setPurchasedHandheldShield(HandheldShield handheldShield, boolean removeOld) throws UnofficialElementNotAllowedException {
        if (handheldShield != null && !handheldShield.isOfficial() && this.getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("HandheldShields shield '" + handheldShield + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        if (removeOld) {
          this.getEquipmentPurchased(HandheldShield.class).forEach(e -> this.getEquipmentPurchased().remove(e));
        }
      this.getEquipmentPurchased().add(handheldShield);
    }

    public Shield getPurchasedShield() {
        return this.getEquipmentPurchased(Shield.class).stream().findFirst().orElse(null);
    }

    public void setPurchasedShield(Shield shield, boolean removeOld) throws UnofficialElementNotAllowedException {
        if (shield != null && !shield.isOfficial() && this.getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("Shield '" + shield + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        if (removeOld) {
          this.getEquipmentPurchased(Shield.class).forEach(e -> this.getEquipmentPurchased().remove(e));
        }
      this.getEquipmentPurchased().add(shield);
    }

    public Set<String> getAllowedShields() {
        if (this.cacheManager.getAllowedShields() == null) {
            if (this.getBestHandHandledShield() != null) {
                return new HashSet<>();
            }
            if (this.getBestArmor() != null) {
                return this.getBestArmor().getAllowedShields();
            }
          this.cacheManager.setAllowedShields(ShieldFactory.getInstance().getElements().stream().map(Element::getId).collect(Collectors.toSet()));
        }
        return this.cacheManager.getAllowedShields();
    }

    public List<Weapon> getPurchasedMeleeWeapons() {
        return this.getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isMeleeWeapon).toList();
    }

    public void setPurchasedMeleeWeapons(List<Weapon> weapons, boolean removeOld) throws UnofficialElementNotAllowedException {
        if (this.getSettings().isOnlyOfficialAllowed()) {
            for (final Weapon weapon : weapons) {
                if (!weapon.isOfficial()) {
                    throw new UnofficialElementNotAllowedException("Weapon '" + weapon + "' is not official and cannot be added due "
                            + "to configuration limitations.");
                }
            }
        }
        if (removeOld) {
          this.getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isMeleeWeapon).forEach(e -> this.getEquipmentPurchased().remove(e));
        }
      this.getEquipmentPurchased().addAll(weapons);
    }

    public List<Weapon> getPurchasedRangedWeapons() {
        return this.getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isRangedWeapon).toList();
    }

    public void setPurchasedRangedWeapons(List<Weapon> weapons, boolean removeOld) throws UnofficialElementNotAllowedException {
        if (this.getSettings().isOnlyOfficialAllowed()) {
            for (final Weapon weapon : weapons) {
                if (!weapon.isOfficial()) {
                    throw new UnofficialElementNotAllowedException("Weapon '" + weapon + "' is not official and cannot be added due "
                            + "to configuration limitations.");
                }
            }
        }
        if (removeOld) {
          this.getEquipmentPurchased(Weapon.class).stream().filter(Weapon::isRangedWeapon).forEach(e -> this.getEquipmentPurchased().remove(e));
        }
      this.getEquipmentPurchased().addAll(weapons);
    }

    public boolean hasWeapon(Weapon weapon) {
        return this.getEquipmentPurchased(Weapon.class).stream().anyMatch(w -> Objects.equals(w, weapon));
    }

    public List<Equipment> getEquipment() {
        final List<Equipment> totalEquipment = new ArrayList<>();
        totalEquipment.addAll(this.getMaterialAwardsSelected().stream().map(EquipmentOption::getElement).collect(Collectors.toSet()));
        totalEquipment.addAll(this.getEquipmentPurchased());
        return totalEquipment;
    }

    public <T extends Equipment> List<T> getEquipment(Class<T> equipmentClass) {
        return this.getEquipment().stream().filter(equipmentClass::isInstance).map(equipmentClass::cast).toList();
    }

    public String getRepresentation() {
        final CharacterSheet characterSheet = new CharacterSheet(this);
        return characterSheet.toString();
    }

    public int getTechgnosisLevel() {
        return this.getLevel();
    }

    public int getTechLevel() {
        if (this.cacheManager.getTechLevel() == null) {
          this.cacheManager.setTechLevel(INITIAL_TECH_LEVEL + (int) this.getCapabilitiesWithSpecialization().stream().filter(capability ->
                    capability.getId().startsWith("techLore") && Objects.equals(capability.getGroup(), "techLore")).count());
        }
        return this.cacheManager.getTechLevel();
    }

    public double getCashMoney() {
        if (this.cacheManager.getCash() == null) {
            double cash = 0;
            for (final Perk perk : this.getPerks()) {
                final double perkCash = this.getCashValue(perk);
                if (perkCash > cash) {
                    cash = perkCash;
                }
            }
          this.cacheManager.setCash(cash + INITIAL_CASH);
        }
        return this.cacheManager.getCash();
    }

    private double getCashValue(Perk perk) {
        try {
            return Double.parseDouble(perk.getId().replace("cash", ""));
        } catch (final Exception e) {
            return 0d;
        }
    }

    public double getRemainingCash() {
        return this.getCashMoney() - this.getSpentCash();
    }

    public double getSpentCash() {
        if (this.cacheManager.getSpentCash() == null) {
            double total = 0;
            for (final Equipment equipment : this.getEquipmentPurchased()) {
                if (equipment != null) {
                    total += equipment.getCost();
                }
            }
          this.cacheManager.setSpentCash(total);
        }
        return this.cacheManager.getSpentCash();
    }


    public void checkIsOfficial() throws UnofficialCharacterException {
        if ((this.getFaction() != null && !FactionFactory.getInstance().getElement(this.getFaction().getId()).isOfficial())) {
            throw new UnofficialCharacterException("Faction '" + this.getFaction() + "' is not official.");
        }
        if ((this.getSpecie() != null && !SpecieFactory.getInstance().getElement(this.getSpecie()).isOfficial())) {
            throw new UnofficialCharacterException("Specie '" + this.getSpecie() + "' is not official.");
        }
        if ((this.getBestArmor() != null && !this.getBestArmor().isOfficial())) {
            throw new UnofficialCharacterException("Armor '" + this.getBestArmor() + "' is not official.");
        }
        if ((this.getBestShield() != null && !this.getBestShield().isOfficial())) {
            throw new UnofficialCharacterException("Shield '" + this.getBestShield() + "' is not official.");
        }
        if (!this.getWeapons().stream().allMatch(Equipment::isOfficial)) {
            throw new UnofficialCharacterException("Equipment '" + this.getWeapons() + "' are not all official.");
        }

        for (final String occultismPathId : this.occultism.getSelectedPowers().keySet()) {
            try {
                if (!OccultismPathFactory.getInstance().getElement(occultismPathId).isOfficial()) {
                    throw new UnofficialCharacterException("Occultism path '" + occultismPathId + "' is not official.");
                }
            } catch (final InvalidXmlElementException e) {
                // Ignore.
            }
        }
    }

    public void checkIsNotRestricted() throws RestrictedElementException {
        if ((this.getFaction() != null && FactionFactory.getInstance().getElement(this.getFaction().getId()).getRestrictions().isRestricted(this))) {
            throw new RestrictedElementException("Faction '" + this.getFaction() + "' is restricted.");
        }
        if ((this.getSpecie() != null && SpecieFactory.getInstance().getElement(this.getSpecie()).getRestrictions().isRestricted(this))) {
            throw new RestrictedElementException("Specie '" + this.getSpecie() + "' is restricted.");
        }

        if ((this.getBestArmor() != null && ArmorFactory.getInstance().getElement(this.getBestArmor()).getRestrictions().isRestricted(this))) {
            throw new RestrictedElementException("Armor '" + this.getBestArmor() + "' is restricted.");
        }
        if ((this.getBestShield() != null && ShieldFactory.getInstance().getElement(this.getBestShield()).getRestrictions().isRestricted(this))) {
            throw new RestrictedElementException("Shield '" + this.getBestShield() + "' is restricted.");
        }

        if (!this.getWeapons().stream().allMatch(w -> w.getRestrictions().isRestricted(this))) {
            throw new RestrictedElementException("Weapons '" + this.getWeapons() + "' have some restricted element.");
        }
        for (final String occultismPathId : this.occultism.getSelectedPowers().keySet()) {
            try {
                if (!OccultismPathFactory.getInstance().getElement(occultismPathId).getRestrictions().isRestricted(this)) {
                    throw new RestrictedElementException("Occultism path '" + occultismPathId + "' is restricted.");
                }
            } catch (final InvalidXmlElementException e) {
                // Ignore.
            }
        }
    }

    public boolean isOccultist() {
        if (this.getCharacteristicValue(CharacteristicName.PSI) > 0) {
            return true;
        }
        if (this.getCharacteristicValue(CharacteristicName.THEURGY) > 0) {
            return true;
        }
        if (this.getCalling() != null) {
            final String callingGroup = CallingFactory.getInstance().getElement(this.getCalling().getId()).getGroup();
            return CharacteristicName.PSI.name().equalsIgnoreCase(callingGroup) || CharacteristicName.THEURGY.name().equalsIgnoreCase(callingGroup);
        }
        return false;
    }

    private Occultism getOccultism() {
        return this.occultism;
    }

    public int getOccultismPointsAvailable(CharacterDefinitionStepSelection characterDefinitionStepSelection) {
        return (int) this.getPerks(characterDefinitionStepSelection).stream().filter(perk -> Objects.equals(perk.getId(), PerkFactory.THEURGY_RITES_PERK)
                || Objects.equals(perk.getId(), PerkFactory.PSYCHIC_POWERS_PERK)).count();
    }

    public int getOccultismPointsAvailable() {
        return (int) this.getPerks().stream().filter(perk -> Objects.equals(perk.getId(), PerkFactory.THEURGY_RITES_PERK)
                || Objects.equals(perk.getId(), PerkFactory.PSYCHIC_POWERS_PERK)).count();
    }

    public int getOccultismPointsSpent() {
        return this.getTotalSelectedPaths();
    }

    public int getOccultismLevel(OccultismType occultismType) {
        if (occultismType == null) {
            return 0;
        }
        return this.getCharacteristicValue(occultismType.getId());
    }

    public int setOccultismLevel(OccultismType occultismType) {
        return this.getCharacteristicValue(occultismType.getId());
    }

    public int getOccultismLevel() {
        try {
            int level = 0;
            for (final OccultismType occultismType : OccultismTypeFactory.getInstance().getElements()) {
                final int typeLevel = this.getOccultismLevel(occultismType);
                if (typeLevel > level) {
                    level = typeLevel;
                }
            }
            return level;
        } catch (final InvalidXmlElementException e) {
            //Ignore
        }
        return 0;
    }

    public int getDarkSideLevel(OccultismType occultismType) {
        return this.getOccultism().getDarkSideLevel(occultismType);
    }

    public void setDarkSideLevel(OccultismType occultismType, int darkSideValue) {
      this.getOccultism().setDarkSideLevel(occultismType, darkSideValue);
    }

    public Map<String, List<OccultismPower>> getSelectedPowers() {
        return this.getOccultism().getSelectedPowers();
    }

    public int getTotalSelectedPowers() {
        return this.getOccultism().getTotalSelectedPowers();
    }

    public int getTotalSelectedPaths() {
        return this.getOccultism().getTotalSelectedPaths();
    }

    private OccultismType checkFactionOccultismType() {
        if (this.getFaction() != null && (FactionGroup.get(this.getFaction().getGroup()) == FactionGroup.CHURCH
                || FactionGroup.get(this.getFaction().getGroup()) == FactionGroup.MINOR_CHURCH || Objects.equals(this.getFaction().getId(), Faction.SIBANZI)
                || Objects.equals(this.getFaction().getId(), Faction.VAGABONDS) || Objects.equals(this.getFaction().getId(), Faction.SWORD_OF_LEXTIUS))) {
            return OccultismTypeFactory.getTheurgy();
        }
        return null;
    }

    private OccultismType checkCallingOccultismType() {
        if (this.getCalling() != null && Objects.equals(this.getCalling().getId(), Calling.DERVISH)) {
            return OccultismTypeFactory.getPsi();
        }
        if (this.getCalling() != null && CallingGroup.get(this.getCalling().getGroup()) == CallingGroup.PSI) {
            return OccultismTypeFactory.getPsi();
        }
        if (this.getCalling() != null && CallingGroup.get(this.getCalling().getGroup()) == CallingGroup.THEURGY) {
            return OccultismTypeFactory.getTheurgy();
        }
        return null;
    }

    private OccultismType checkSpecieOccultismType() {
        if (this.getSpecie() != null && Objects.equals(this.getSpecie().getId(), Specie.ASCORBITE)) {
            return OccultismTypeFactory.getPsi();
        }
        return null;
    }

    private OccultismType checkSelectedPowerOccultismType() throws InvalidXmlElementException {
        if (!this.getOccultism().getSelectedPowers().isEmpty()) {
            final Map.Entry<String, List<OccultismPower>> occultismPowers = this.getOccultism().getSelectedPowers().entrySet().iterator().next();
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
        if (this.getSpecie() == null) {
            return 0;
        }
        if (occultismType.getId().equals(OccultismTypeFactory.PSI_TAG)) {
            return SpecieFactory.getInstance().getElement(this.getSpecie())
                    .getSpecieCharacteristic(CharacteristicName.PSI).getInitialValue();
        }
        if (occultismType.getId().equals(OccultismTypeFactory.THEURGY_TAG)) {
            return SpecieFactory.getInstance().getElement(this.getSpecie())
                    .getSpecieCharacteristic(CharacteristicName.THEURGY).getInitialValue();
        }
        return 0;
    }

    private OccultismType checkCharacteristicOccultismType() throws InvalidXmlElementException {
        for (final OccultismType occultismType : OccultismTypeFactory.getInstance().getElements()) {
            final int defaultOccultismLevel = this.getDefaultOccultismLevel(occultismType);
            if (this.getCharacteristicValue(occultismType.getId()) > defaultOccultismLevel) {
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
        OccultismType result = this.checkFactionOccultismType();
        if (result != null) {
            return result;
        }
        result = this.checkCallingOccultismType();
        if (result != null) {
            return result;
        }
        result = this.checkSpecieOccultismType();
        if (result != null) {
            return result;
        }
        try {
            result = this.checkSelectedPowerOccultismType();
            if (result != null) {
                return result;
            }
            return this.checkCharacteristicOccultismType();
        } catch (final InvalidXmlElementException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return null;
    }

    public boolean hasOccultismPower(OccultismPower power) {
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
        if (path == null) {
            return false;
        }
        return this.getOccultism().hasPower(path, power);
    }

    public boolean canAddOccultismPower(OccultismPower power) {
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
        try {
          this.getOccultism().canAddPower(this, path, power,
                    this.getFaction() != null ? this.getFaction().getId() : null,
                    this.getSpecie() != null ? this.getSpecie().getId() : null, this.getSettings());
            return true;
        } catch (final InvalidOccultismPowerException e) {
            return false;
        }
    }

    public void addOccultismPower(OccultismPower power) throws InvalidOccultismPowerException, UnofficialElementNotAllowedException {
        if (power == null) {
            throw new InvalidOccultismPowerException("Null value not allowed");
        }
        if (!power.isOfficial() && this.getSettings().isOnlyOfficialAllowed()) {
            throw new UnofficialElementNotAllowedException("Occultism Power '" + power + "' is not official and cannot be added due "
                    + "to configuration limitations.");
        }
        if (power.getRestrictions().isRestricted(this)) {
            throw new InvalidOccultismPowerException("Occultism Power '" + power + "' is restricted to this character.");
        }
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
      this.getOccultism().addPower(this, path, power, this.getFaction() != null ? this.getFaction().getId()
              : null, this.getSpecie().getId(), this.getSettings());
    }

    public void removeOccultismPower(OccultismPower power) {
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
        if (path != null) {
          this.getOccultism().removePower(path, power);
        }
    }

    public boolean hasOccultismPath(OccultismPath path) {
        return this.getOccultism().hasPath(path);
    }

    public List<OccultismPower> getAllSelectedPowers() {
        return this.getOccultism().getSelectedPowers().values().stream().flatMap(Collection::stream).toList();
    }

    public List<Cyberdevice> getCyberdevices() {
        final List<SpecializedPerk> cyberdevices = this.getPerks().stream()
                .filter(perk -> Objects.equals(perk.getType(), PerkType.CYBERDEVICE)).toList();
        return CyberdeviceFactory.getInstance().getElements(cyberdevices.stream().map(Perk::getId).toList()).stream()
                .filter(cyberdevice -> !this.getSettings().isOnlyOfficialAllowed() || cyberdevice.isOfficial())
                .toList();
    }

    public Affliction getAffliction() {
        return this.affliction;
    }

    public void setAffliction(Affliction affliction) {
        this.affliction = affliction;
    }

    public boolean canUseCombatArmor() {
        if (this.cacheManager.getCombatArmor() == null) {
          this.cacheManager.setCombatArmor(this.hasCapability(Capability.COMBAT_ARMOR_CAPABILITY, (String) null));
        }
        return this.cacheManager.getCombatArmor();
    }

    public boolean canUseWarArmor() {
        if (this.cacheManager.getWarArmor() == null) {
          this.cacheManager.setWarArmor(this.hasCapability(Capability.WAR_ARMOR_CAPABILITY, (String) null));
        }
        return this.cacheManager.getWarArmor();
    }

    public boolean canUseMilitaryWeapons() {
        if (this.cacheManager.getMilitaryWeapons() == null) {
          this.cacheManager.setMilitaryWeapons(this.hasCapability(Capability.MILITARY_WEAPONS_CAPABILITY, (String) null));
        }
        return this.cacheManager.getMilitaryWeapons();
    }

    public CacheManager getCacheManager() {
        return this.cacheManager;
    }


    @Override
    public String toString() {
        final String name = this.getCompleteNameRepresentation();
        return "CharacterPlayer{"
                + (name != null && !name.isBlank() ? "name=" + name + ", " : "")
                + "specie=" + this.specie
                + ", upbringing=" + this.upbringing
                + ", faction=" + this.faction
                + ", calling=" + this.calling
                + ", level=" + this.getLevel()
                + '}';
    }

    public void setRaisedInSpace(boolean raisedInSpace) {
        if (this.getUpbringing() != null) {
          this.getUpbringing().setRaisedInSpace(raisedInSpace);
          this.getCacheManager().reset();
        }
    }

    public boolean isRaisedInSpace() {
        if (this.getUpbringing() != null) {
            return this.getUpbringing().isRaisedInSpace();
        }
        return false;
    }
}
