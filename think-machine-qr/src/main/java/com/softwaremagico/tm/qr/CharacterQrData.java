package com.softwaremagico.tm.qr;

/*-
 * #%L
 * Think Machine 4E (QR)
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Compact representation of all character data for QR-code encoding.
 *
 * <p>Short field names are used in JSON to minimise payload size once compressed.
 * All IDs are stable string identifiers (never positional indices), ensuring
 * backward compatibility when new elements are added to factions, callings, etc.
 *
 * <p>Long free-text descriptions ({@code characterDescription} and
 * {@code backgroundDescription}) are deliberately excluded because they would
 * make the payload too large for a QR code.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CharacterQrData {

    /** Format version for future migration. */
    @JsonProperty("v")
    private int version = 1;

    // ── Character info ──────────────────────────────────────────────────────

    @JsonProperty("nm")
    private String name;

    @JsonProperty("sur")
    private String surname;

    @JsonProperty("pl")
    private String player;

    @JsonProperty("gen")
    private String gender;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("pla")
    private String planet;

    @JsonProperty("hr")
    private String hair;

    @JsonProperty("ey")
    private String eyes;

    @JsonProperty("cx")
    private String complexion;

    @JsonProperty("ht")
    private String height;

    @JsonProperty("wt")
    private String weight;

    // ── Core selections ─────────────────────────────────────────────────────

    @JsonProperty("pc")
    private String primaryCharacteristic;

    @JsonProperty("sc")
    private String secondaryCharacteristic;

    @JsonProperty("sp")
    private String specie;

    @JsonProperty("up")
    private String upbringing;

    @JsonProperty("fa")
    private String faction;

    @JsonProperty("ca")
    private String calling;

    // ── Step selections ──────────────────────────────────────────────────────

    @JsonProperty("spSel")
    private StepSelectionData specieSelections;

    @JsonProperty("upSel")
    private StepSelectionData upbringingSelections;

    @JsonProperty("faSel")
    private StepSelectionData factionSelections;

    @JsonProperty("caSel")
    private StepSelectionData callingSelections;

    // ── Levels ───────────────────────────────────────────────────────────────

    @JsonProperty("lvls")
    private List<LevelSelectionData> levels;

    // ── Occultism ────────────────────────────────────────────────────────────

    /** occultismTypeId → darkSide value. */
    @JsonProperty("ds")
    private Map<String, Integer> darkSide;

    /** pathId → list of power IDs selected in that path. */
    @JsonProperty("pw")
    private Map<String, List<String>> powers;

    // ── Equipment purchased ───────────────────────────────────────────────────

    @JsonProperty("eq")
    private List<String> equipmentPurchased;

    // ── Reassigns ────────────────────────────────────────────────────────────

    /** Each entry is a two-element array [from, to]. */
    @JsonProperty("cr")
    private List<String[]> characteristicReassigns;

    /** Each entry is a two-element array [from, to]. */
    @JsonProperty("sr")
    private List<String[]> skillsReassigns;

    // ── Affliction ───────────────────────────────────────────────────────────

    @JsonProperty("aff")
    private String affliction;

    // ── Getters / setters ────────────────────────────────────────────────────

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public String getUpbringing() {
        return upbringing;
    }

    public void setUpbringing(String upbringing) {
        this.upbringing = upbringing;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public String getCalling() {
        return calling;
    }

    public void setCalling(String calling) {
        this.calling = calling;
    }

    public StepSelectionData getSpecieSelections() {
        return specieSelections;
    }

    public void setSpecieSelections(StepSelectionData specieSelections) {
        this.specieSelections = specieSelections;
    }

    public StepSelectionData getUpbringingSelections() {
        return upbringingSelections;
    }

    public void setUpbringingSelections(StepSelectionData upbringingSelections) {
        this.upbringingSelections = upbringingSelections;
    }

    public StepSelectionData getFactionSelections() {
        return factionSelections;
    }

    public void setFactionSelections(StepSelectionData factionSelections) {
        this.factionSelections = factionSelections;
    }

    public StepSelectionData getCallingSelections() {
        return callingSelections;
    }

    public void setCallingSelections(StepSelectionData callingSelections) {
        this.callingSelections = callingSelections;
    }

    public List<LevelSelectionData> getLevels() {
        return levels;
    }

    public void setLevels(List<LevelSelectionData> levels) {
        this.levels = levels;
    }

    public Map<String, Integer> getDarkSide() {
        return darkSide;
    }

    public void setDarkSide(Map<String, Integer> darkSide) {
        this.darkSide = darkSide;
    }

    public Map<String, List<String>> getPowers() {
        return powers;
    }

    public void setPowers(Map<String, List<String>> powers) {
        this.powers = powers;
    }

    public List<String> getEquipmentPurchased() {
        return equipmentPurchased;
    }

    public void setEquipmentPurchased(List<String> equipmentPurchased) {
        this.equipmentPurchased = equipmentPurchased;
    }

    public List<String[]> getCharacteristicReassigns() {
        return characteristicReassigns;
    }

    public void setCharacteristicReassigns(List<String[]> characteristicReassigns) {
        this.characteristicReassigns = characteristicReassigns;
    }

    public List<String[]> getSkillsReassigns() {
        return skillsReassigns;
    }

    public void setSkillsReassigns(List<String[]> skillsReassigns) {
        this.skillsReassigns = skillsReassigns;
    }

    public String getAffliction() {
        return affliction;
    }

    public void setAffliction(String affliction) {
        this.affliction = affliction;
    }
}
