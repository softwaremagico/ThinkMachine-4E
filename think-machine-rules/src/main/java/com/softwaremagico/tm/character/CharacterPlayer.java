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
import com.softwaremagico.tm.character.factions.FactionCharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.upbringing.UpbringingCharacterDefinitionStepSelection;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;

import java.util.ArrayList;
import java.util.List;

public class CharacterPlayer {

    private String specie;

    private UpbringingCharacterDefinitionStepSelection upbringing;

    private FactionCharacterDefinitionStepSelection faction;

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
