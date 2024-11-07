package com.softwaremagico.tm.character.occultism;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.TimeFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OccultismPower extends Element {
    private String characteristic;
    @JsonProperty("skills")
    private List<String> skills;
    private List<IValue> values;
    @JsonProperty("psiLevel")
    private int level;
    private String time;
    private Set<String> components;
    private TranslatedText cost;
    private TranslatedText resistance;
    private TranslatedText impact;

    public OccultismPower() {
        super();
    }

    public int getLevel() {
        return level;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public List<IValue> getValues() {
        return values;
    }

    public String getRoll() {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(CharacteristicsDefinitionFactory.getInstance().getElement(getCharacteristic()).getAbbreviation());
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(this.getClass(), e);
        }
        stringBuilder.append("+");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                stringBuilder.append("/");
            }
            stringBuilder.append(values.get(i).getName());
        }
        return stringBuilder.toString();
    }

    public Set<String> getComponents() {
        return components;
    }

    public String getComponentsRepresentation() {
        final List<String> sortedComponents = new ArrayList<>(getComponents());
        Collections.sort(sortedComponents);
        final StringBuilder representation = new StringBuilder();
        for (final String theurgyComponent : sortedComponents) {
            try {
                representation.append(TheurgyComponentFactory.getInstance().getElement(theurgyComponent).getAbbreviation());
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass(), e);
            }
        }
        return representation.toString();
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public void setValues(List<IValue> values) {
        this.values = values;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setComponents(String componentsContent) {
        this.components = new HashSet<>();
        readCommaSeparatedTokens(components, componentsContent);
    }

    public void setComponents(Set<String> components) {
        this.components = components;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skill) {
        this.skills = skill;
    }

    public TranslatedText getResistance() {
        return resistance;
    }

    public void setResistance(TranslatedText resistance) {
        this.resistance = resistance;
    }

    public TranslatedText getImpact() {
        return impact;
    }

    public void setImpact(TranslatedText impact) {
        this.impact = impact;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TranslatedText getCost() {
        return cost;
    }

    public void setCost(TranslatedText cost) {
        this.cost = cost;
    }

    @Override
    public int compareTo(Element element) {
        if (element instanceof OccultismPower) {
            if (getLevel() != ((OccultismPower) element).getLevel()) {
                return Integer.compare(getLevel(), ((OccultismPower) element).getLevel());
            }
        }
        return super.compareTo(element);
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        skills.forEach(skill -> SkillFactory.getInstance().getElement(skill));
        CharacteristicsDefinitionFactory.getInstance().getElement(getCharacteristic());
        TimeFactory.getInstance().getElement(getTime());
    }

}
