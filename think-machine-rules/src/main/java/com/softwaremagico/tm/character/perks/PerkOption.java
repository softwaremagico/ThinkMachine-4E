package com.softwaremagico.tm.character.perks;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.capabilities.Capability;

public class PerkOption {
    @JsonProperty("id")
    private String id;

    @JsonProperty("group")
    private String group;

    public PerkOption() {
        super();
    }

    public PerkOption(Perk perk) {
        this();
        setId(perk.getId());
    }

    public PerkOption(Capability capability) {
        this();
        setId(capability.getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return id;
    }
}
