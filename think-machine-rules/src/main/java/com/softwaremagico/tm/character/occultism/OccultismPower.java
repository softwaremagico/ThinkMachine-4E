package com.softwaremagico.tm.character.occultism;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OccultismPower extends Element<OccultismPower> {
    private String characteristic;
    private List<IValue> values;
    private int level;
    private String range;
    private String duration;
    @JsonProperty("wyrd")
    private String cost;
    private Set<String> components;

    public OccultismPower() {
        super();
    }

    public OccultismPower(String id, TranslatedText name, TranslatedText description, String language, String moduleName,
                          String characteristic, List<IValue> values, int level, String range,
                          String duration, String cost, Set<String> components) {
        super(id, name, description, language, moduleName);
        this.characteristic = characteristic;
        this.values = values;
        this.level = level;
        this.range = range;
        this.duration = duration;
        this.cost = cost;
        this.components = components;
    }

    public int getLevel() {
        return level;
    }

    public String getRange() {
        return range;
    }

    public String getDuration() {
        return duration;
    }

    public String getCost() {
        return cost;
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

    public void setRange(String range) {
        this.range = range;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setComponents(String componentsContent) {
        this.components = new HashSet<>();
        readCommaSeparatedTokens(components, componentsContent);
    }

    public void setComponents(Set<String> components) {
        this.components = components;
    }

    @Override
    public int compareTo(OccultismPower element) {
        if (getLevel() != element.getLevel()) {
            return Integer.compare(getLevel(), element.getLevel());
        }
        return super.compareTo(element);
    }

}
