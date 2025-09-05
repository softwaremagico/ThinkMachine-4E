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
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOptions;
import com.softwaremagico.tm.character.equipment.EquipmentOptions;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.perks.PerkSource;
import com.softwaremagico.tm.character.perks.PerkType;
import com.softwaremagico.tm.character.perks.SpecializedPerk;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;
import com.softwaremagico.tm.character.values.Phase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Level extends CharacterDefinitionStep {
    private static final int THREE = 3;
    private static final int FIVE = 5;

    private final CharacterPlayer characterPlayer;

    private int index;

    public Level(CharacterPlayer characterPlayer, int index) {
        this.characterPlayer = characterPlayer;
        this.index = index;
        setId("Level" + index);
        setName(new TranslatedText("Level" + index));
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
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

    public Set<PerkOption> getClassPerksOptions() {
        if (getTotalClassPerksOptions() == 0) {
            return new HashSet<>();
        }
        final Set<PerkOption> classPerks = PerkFactory.getInstance().getBySource(PerkSource.CLASS).stream().map(PerkOption::new).collect(Collectors.toSet());
        if (getCharacterPlayer().isFavoredCalling()) {
            //Set privilege calling perks. But already taken must be filtered.
            classPerks.addAll(getCallingPerksOptions().stream()
                    .filter(p -> p.getElement() != null && p.getElement().getType() == PerkType.PRIVILEGE
                    ).collect(Collectors.toList()));
        }
        return classPerks.stream().filter(perkOption -> !perkOption.getRestrictions().isRestricted(getCharacterPlayer())).collect(Collectors.toSet());
    }

    public List<CharacterPerkOptions> getAvailableSelections(boolean addPerksIfEmpty) {
        if (getCharacterPlayer().getUpbringing() == null) {
            return new ArrayList<>();
        }
        final List<CharacterPerkOptions> perks = getCharacterPlayer().getUpbringing().getNotSelectedPerksOptions(Phase.LEVEL, index, addPerksIfEmpty);
        if (getCharacterPlayer().isFavoredCalling()) {
            final Set<SpecializedPerk> favouredOptions = getFavouredPerksOptions();
            for (CharacterPerkOptions characterPerkOptions : perks) {
                //Set privilege calling perks. But already taken must be filtered.
                favouredOptions.forEach(p -> characterPerkOptions.getAvailableSelections().add(new Selection(p)));
            }
        }
        return perks;
    }

    public Set<SpecializedPerk> getFavouredPerksOptions() {
        if (getCharacterPlayer().isFavoredCalling()) {
            //Get privilege perks.
            final List<SpecializedPerk> selectedPerks = getCharacterPlayer().getPerks(getIndex() - 1);
            final Set<SpecializedPerk> favouredPerks = SpecializedPerk.create(PerkFactory.getInstance().getClassPrivilegePerks().stream().filter(perk ->
                    !perk.getRestrictions().isRestricted(getCharacterPlayer())
            ).collect(Collectors.toSet()));
            selectedPerks.forEach(favouredPerks::remove);
            favouredPerks.removeIf(s -> s.getRestrictions().isRestricted(getCharacterPlayer()));
            return favouredPerks;
        }
        return new HashSet<>();
    }

    public Set<Selection> getFavouredSelections() {
        if (getCharacterPlayer().isFavoredCalling()) {
            //Get privilege perks.
            final List<SpecializedPerk> selectedPerks = getCharacterPlayer().getPerks(getIndex() - 1);
            final Set<Selection> favouredPerks = PerkFactory.getInstance().getClassPrivilegeSelections().stream().filter(perk ->
                    !perk.getRestrictions().isRestricted(getCharacterPlayer())
            ).collect(Collectors.toSet());
            selectedPerks.forEach(s -> favouredPerks.remove(new Selection(s)));
            favouredPerks.removeIf(s -> s.getRestrictions().isRestricted(getCharacterPlayer()));
            return favouredPerks;
        }
        return new HashSet<>();
    }

    public Set<PerkOption> getCallingPerksOptions() {
        if (getTotalCallingPerksOptions() == 0) {
            return new HashSet<>();
        }
        return PerkFactory.getInstance().getBySource(PerkSource.CALLING).stream()
                .filter(perk -> !perk.getRestrictions().isRestricted(getCharacterPlayer()))
                .map(PerkOption::new).collect(Collectors.toSet());
    }

    public List<CharacterPerkOptions> getNotRepeatedCallingPerksOptions() {
        if (getCharacterPlayer().getCalling() == null || getTotalClassPerksOptions() == 0) {
            return new ArrayList<>();
        }
        return getCharacterPlayer().getCalling().getNotSelectedPerksOptions(Phase.LEVEL, index, true);
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
        return Collections.emptyList();
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
        return getTotalCallingPerksOptions() + getTotalClassPerksOptions();
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
}
