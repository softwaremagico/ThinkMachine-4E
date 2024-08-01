package com.softwaremagico.tm.character.skills;

import com.softwaremagico.tm.character.characteristics.CharacteristicType;

public enum SkillGroup {

    ANALYTICAL(CharacteristicType.MIND),

    COMBAT(CharacteristicType.BODY),

    CONTROL(null),

    CREATIVE(null),

    LORE(CharacteristicType.MIND),

    MALEFACTION(null),

    PHYSICAL(CharacteristicType.BODY),

    SCIENCE(CharacteristicType.MIND),

    SOCIAL(CharacteristicType.SPIRIT),

    TECHNICAL(CharacteristicType.MIND);

    private final CharacteristicType preferredCharacteristicsGroups;

    SkillGroup(CharacteristicType preferredCharacteristicsGroups) {
        this.preferredCharacteristicsGroups = preferredCharacteristicsGroups;
    }

    public static SkillGroup getSkillGroup(String tag) {
        if (tag != null) {
            for (final SkillGroup skillGroup : SkillGroup.values()) {
                if (skillGroup.name().toLowerCase().equals(tag.toLowerCase())) {
                    return skillGroup;
                }
            }
        }
        return null;
    }

    public CharacteristicType getPreferredCharacteristicsGroups() {
        return preferredCharacteristicsGroups;
    }
}
