package com.softwaremagico.tm.restrictions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestrictedCapability implements IComparableRestriction {
    @JsonProperty("id")
    private String id;
    @JsonProperty("selectedSpecialization")
    private String specialization;

    public RestrictedCapability() {
        super();
    }

    public RestrictedCapability(String id) {
        super();
        setId(id);
    }

    public RestrictedCapability(String id, String specialization) {
        this(id);
        this.specialization = specialization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String getComparisonId() {
        return getId() + (getSpecialization() != null ? "_" + getSpecialization() : "");
    }

    @Override
    public String toString() {
        return "RestrictedCapability{"
                + "id='" + id + '\''
                + (specialization != null ? ", specialization=" + specialization : "")
                + '}';
    }
}
