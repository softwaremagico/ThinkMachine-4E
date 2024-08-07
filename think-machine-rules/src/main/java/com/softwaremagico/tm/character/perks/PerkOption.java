package com.softwaremagico.tm.character.perks;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.character.capabilities.Capability;

public class PerkOption {
    @JsonProperty("id")
    private String id;

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

    @Override
    public String toString() {
        return id;
    }
}
