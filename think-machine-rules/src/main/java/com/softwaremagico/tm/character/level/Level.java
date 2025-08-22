package com.softwaremagico.tm.character.level;

import com.softwaremagico.tm.character.CharacterDefinitionStep;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOptions;
import com.softwaremagico.tm.character.equipment.EquipmentOptions;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.character.perks.PerkOptions;
import com.softwaremagico.tm.character.perks.PerkType;
import com.softwaremagico.tm.character.skills.SkillBonusOptions;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Level extends CharacterDefinitionStep<Level> {
    private static final int THREE = 3;
    private static final int FIVE = 5;

    private final CharacterPlayer characterPlayer;

    private int index;

    public Level(CharacterPlayer characterPlayer, int index) {
        this.characterPlayer = characterPlayer;
        this.index = index;
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
    public List<PerkOptions> getPerksOptions() {
        return null;
    }

    public List<PerkOptions> getClassPerksOptions() {
        final List<PerkOptions> perks = characterPlayer.getFaction().getPerksOptions();
        if (characterPlayer.isFavoredCalling()) {
            for (PerkOptions perkOptions : perks) {
                perkOptions.getOptions().addAll(getCallingPerksOptions().get(0).getOptions().stream().filter(p ->
                        p.getElement().getType() == PerkType.PRIVILEGE
                ).collect(Collectors.toList()));
            }
        }
        return perks;
    }

    public List<PerkOptions> getCallingPerksOptions() {
        return characterPlayer.getCalling().getPerksOptions();
    }

    @Override
    public List<EquipmentOptions> getMaterialAwards() {
        return new ArrayList<>();
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

    @Override
    public int getTotalCapabilitiesOptions() {
        return getExtraCapabilities();
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
    }
}
