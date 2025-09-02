package com.softwaremagico.tm.character.level;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
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

import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.CharacterDefinitionStep;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOptions;
import com.softwaremagico.tm.character.equipment.EquipmentOptions;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.character.perks.PerkType;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Level extends CharacterDefinitionStep {
    private static final int THREE = 3;
    private static final int FIVE = 5;

    private final CharacterPlayer characterPlayer;

    private int index;

    public Level(CharacterPlayer characterPlayer, int index) {
        this.characterPlayer = characterPlayer;
        this.index = index;
        setName(new TranslatedText("Level" + index));
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int getExtraCapabilities() {
        return 1;
    }

    private int getExtraCharacteristics() {
        if (index % 2 == 0) {
            return 2;
        }
        return 1;
    }

    private int getExtraClassPerks() {
        if (index % 2 == 0) {
            return 0;
        }
        return 1;
    }

    private int getExtraCallingPerks() {
        return 1;
    }

    public int getExtraRevivalRating() {
        return 1;
    }

    public int getExtraRevivalNumber() {
        if ((index - 1) % THREE == 0) {
            return 0;
        }
        return 1;
    }

    public int getExtraSkillsPoints() {
        if (index % 2 == 0) {
            return THREE;
        }
        return 2;
    }

    public int getExtraSurgeRating() {
        return 1;
    }

    public int getExtraSurgeNumber() {
        if ((index - 1) % THREE == 0) {
            return 0;
        }
        return 1;
    }

    public int getExtraVitality() {
        return 1;
    }

    public int getExtraVPBank() {
        if (index % 2 == 0) {
            return FIVE;
        }
        return 0;
    }

    @Override
    public List<CharacteristicBonusOptions> getCharacteristicOptions() {
        final List<CharacteristicBonusOptions> characteristics = new ArrayList<>();
        for (int i = 0; i < getExtraCharacteristics(); i++) {
            final CharacteristicBonusOptions characteristicBonusOptions = new CharacteristicBonusOptions();
            characteristicBonusOptions.setTotalOptions(1);
            characteristics.add(characteristicBonusOptions);
        }
        return characteristics;
    }

    @Override
    public List<SkillBonusOptions> getSkillOptions() {
        final List<SkillBonusOptions> skills = new ArrayList<>();
        for (int i = 0; i < getExtraSkillsPoints(); i++) {
            final SkillBonusOptions skillBonusOptions = new SkillBonusOptions();
            skillBonusOptions.setTotalOptions(1);
            skills.add(skillBonusOptions);
        }
        return skills;
    }

    @Override
    public List<CharacterPerkOptions> getCharacterAvailablePerksOptions() {
        return new ArrayList<>();
    }

    public List<CharacterPerkOptions> getFactionPerksOptions() {
        if (characterPlayer.getFaction() == null || characterPlayer.getCalling() == null) {
            return new ArrayList<>();
        }
        final List<CharacterPerkOptions> perks = characterPlayer.getFaction().getPerksOptions();
        if (characterPlayer.isFavoredCalling()) {
            for (PerkOptions perkOptions : perks) {
                perkOptions.getOptions().addAll(characterPlayer.getCalling().getPerksOptions().get(0).getOptions().stream()
                        .filter(p -> p.getElement() != null && p.getElement().getType() == PerkType.PRIVILEGE
                        ).collect(Collectors.toList()));
            }
        }
        return perks;
    }

    public List<CharacterPerkOptions> getNotRepeatedFactionPerksOptions() {
        if (characterPlayer.getCalling() == null) {
            return new ArrayList<>();
        }
        final List<CharacterPerkOptions> perks = characterPlayer.getFaction().getNotSelectedPerksOptions();
        if (characterPlayer.isFavoredCalling()) {
            for (PerkOptions perkOptions : perks) {
                perkOptions.getOptions().addAll(characterPlayer.getCalling().getNotSelectedPerksOptions().get(0).getOptions().stream()
                        .filter(p -> p.getElement() != null && p.getElement().getType() == PerkType.PRIVILEGE
                        ).collect(Collectors.toList()));
            }
        }
        return perks;
    }

    public List<CharacterPerkOptions> getUpbringingPerksOptions() {
        if (characterPlayer.getUpbringing() == null) {
            return new ArrayList<>();
        }
        return characterPlayer.getUpbringing().getPerksOptions();
    }

    public List<CharacterPerkOptions> getNotRepeatedUpbringingPerksOptions() {
        if (characterPlayer.getUpbringing() == null) {
            return new ArrayList<>();
        }
        return characterPlayer.getUpbringing().getNotSelectedPerksOptions(Phase.LEVEL);
    }

    public List<CharacterPerkOptions> getCallingPerksOptions() {
        if (characterPlayer.getCalling() == null) {
            return new ArrayList<>();
        }
        return characterPlayer.getCalling().getPerksOptions();
    }

    public List<CharacterPerkOptions> getNotRepeatedCallingPerksOptions() {
        if (characterPlayer.getCalling() == null) {
            return new ArrayList<>();
        }
        return characterPlayer.getCalling().getNotSelectedPerksOptions(Phase.LEVEL);
    }

    @Override
    public List<CapabilityOptions> getCapabilityOptions() {
        final List<CapabilityOptions> capabilities = new ArrayList<>();
        for (int i = 0; i < getExtraCapabilities(); i++) {
            final CapabilityOptions capabilityOptions = new CapabilityOptions();
            capabilityOptions.setTotalOptions(1);
            capabilities.add(capabilityOptions);
        }
        return capabilities;
    }

    @Override
    public List<EquipmentOptions> getMaterialAwards() {
        return null;
    }

    @Override
    public int getCharacteristicsTotalPoints() {
        return getExtraCharacteristics();
    }

    @Override
    public int getSkillsTotalPoints() {
        return getExtraSkillsPoints();
    }

    @Override
    public int getTotalPerksOptions() {
        return 0;
    }

    public int getTotalClassPerksOptions() {
        return getExtraClassPerks();
    }

    public int getTotalCallingPerksOptions() {
        return getExtraCallingPerks();
    }

    @Override
    public int getTotalCapabilitiesOptions() {
        return getExtraCapabilities();
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
    }
}
