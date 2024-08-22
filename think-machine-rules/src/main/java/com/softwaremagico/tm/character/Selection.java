package com.softwaremagico.tm.character;

public class Selection {
    private String id;
    private String specialization;

    public Selection(String id) {
        super();
        this.id = id;
    }

    public Selection(String id, String specialization) {
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
}
