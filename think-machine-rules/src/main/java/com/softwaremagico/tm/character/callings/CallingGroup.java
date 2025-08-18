package com.softwaremagico.tm.character.callings;

public enum CallingGroup {
    PSI,
    THEURGY,
    NONE;

    public static CallingGroup get(String groupName) {
        for (final CallingGroup callingGroup : CallingGroup.values()) {
            if (callingGroup.name().equalsIgnoreCase(groupName)) {
                return callingGroup;
            }
        }
        return CallingGroup.NONE;
    }
}
