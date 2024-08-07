package com.softwaremagico.tm.character.capabilities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CapabilityOption {
    @JsonProperty("id")
    private String id;
    @JsonProperty("group")
    private String group;
    @JsonProperty("specialization")
    private String specialization;

    public CapabilityOption() {
        super();
    }

    public CapabilityOption(Capability capability) {
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return id != null ? id : (group != null ? group : null);
    }
}
