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

import com.softwaremagico.tm.character.capabilities.CapabilitySelection;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.combat.CombatActionRequirement;
import com.softwaremagico.tm.character.skills.Skill;

import java.util.ArrayList;
import java.util.List;

public class CharacterPlayer {

    private String specie;

    private CharacterDefinitionStepSelection faction;

    private CharacterDefinitionStepSelection uprising;

    private List<CapabilitySelection> capabilities;

    private final Settings settings;

    public CharacterPlayer() {
        settings = new Settings();
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public CharacterDefinitionStepSelection getFaction() {
        return faction;
    }

    public void setFaction(CharacterDefinitionStepSelection faction) {
        this.faction = faction;
    }

    public Settings getSettings() {
        return settings;
    }

    public int getSkillTotalRanks(Skill restriction) {
        return 0;
    }

    public CombatActionRequirement getCharacteristic(String id) {
        return null;
    }

    public int getCharacteristicValue(CharacteristicName characteristicName) {
        return 0;
    }

    public CharacterDefinitionStepSelection getUprising() {
        return uprising;
    }

    public void setUprising(CharacterDefinitionStepSelection uprising) {
        this.uprising = uprising;
    }

    public List<CapabilitySelection> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<CapabilitySelection> capabilities) {
        this.capabilities = capabilities;
    }

    public List<String> getCallings() {
        return new ArrayList<>();
    }

    public List<String> getPerks() {
        return new ArrayList<>();
    }
}
