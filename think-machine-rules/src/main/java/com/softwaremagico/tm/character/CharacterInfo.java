package com.softwaremagico.tm.character;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2024 Softwaremagico
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


import com.softwaremagico.tm.character.planets.Planet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CharacterInfo {
    private static final int MAX_AGE = 110;

    private List<Name> names;

    private Surname surname;

    private String player;

    private Gender gender;

    private Integer age;

    private String planet;

    private String birthdate;

    private String hair;

    private String eyes;

    private String complexion;

    private String height;

    private String weight;

    private String characterDescription = "";

    private String backgroundDescription = "";

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public void setNames(String names) {
        if (names == null) {
            return;
        }
        this.names = new ArrayList<>();
        final String[] namesSeparated = names.split("(?=\\p{Lu})");
        for (final String name : namesSeparated) {
            addName(new Name(name, null, null, gender, null, null));
        }
    }

    public void addName(Name name) {
        if (names == null) {
            names = new ArrayList<>();
        }
        names.add(name);
        names = names.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        if (age == null) {
            this.age = null;
        } else if (age > MAX_AGE) {
            this.age = MAX_AGE;
        } else if (age < 1) {
            this.age = 1;
        } else {
            this.age = age;
        }
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        if (planet == null) {
            this.planet = null;
        } else {
            this.planet = planet.getId();
        }
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getNameRepresentation() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (getNames() != null && !getNames().isEmpty()) {
            for (final Name name : getNames()) {
                stringBuilder.append(name.getName());
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString().trim();
    }

    public Surname getSurname() {
        return surname;
    }

    public void setSurname(Surname surname) {
        this.surname = surname;
    }

    public void setSurname(String surname) {
        if (surname == null || surname.isEmpty()) {
            this.surname = null;
        } else {
            this.surname = new Surname(surname, null, null, null, null);
        }
    }

    public String getCharacterDescription() {
        return characterDescription;
    }

    public void setCharacterDescription(String characterDescription) {
        this.characterDescription = characterDescription;
    }

    public String getBackgroundDescription() {
        return backgroundDescription;
    }

    public void setBackgroundDescription(String backgroundDescription) {
        this.backgroundDescription = backgroundDescription;
    }

}
