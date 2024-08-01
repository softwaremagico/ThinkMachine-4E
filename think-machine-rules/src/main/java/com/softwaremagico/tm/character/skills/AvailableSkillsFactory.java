package com.softwaremagico.tm.character.skills;


import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AvailableSkillsFactory {
    private List<AvailableSkill> skills;
    private Map<String, Map<SkillGroup, Set<AvailableSkill>>> skillsByGroup;

    private int maximumNumberOfSpecializations = 0;

    private static final class AvailableSkillsFactoryInit {
        public static final AvailableSkillsFactory INSTANCE = new AvailableSkillsFactory();
    }

    public static AvailableSkillsFactory getInstance() {
        return AvailableSkillsFactoryInit.INSTANCE;
    }

    public Set<AvailableSkill> getSkillsByGroup(SkillGroup skillGroup, String language, String moduleName) {
        if (skillsByGroup == null || skillsByGroup.isEmpty() || skillsByGroup.get(language) == null
                || skillsByGroup.get(language).isEmpty()) {
            for (final AvailableSkill availableNaturalSkill : getElements()) {
                classifySkillByGroup(availableNaturalSkill, language);
            }
        }
        if (skillsByGroup == null || skillsByGroup.get(language) == null
                || skillsByGroup.get(language).get(skillGroup) == null) {
            return new HashSet<>();
        }
        return skillsByGroup.get(language).get(skillGroup);
    }

    public synchronized List<AvailableSkill> getSkills() throws InvalidXmlElementException {
        if (skills == null) {
            skills = new ArrayList<>();
            for (final SkillDefinition skillDefinition : SkillDefinitionFactory.getInstance().getElements()) {
                final Set<AvailableSkill> skills = createElement(skillDefinition);

                this.skills.addAll(skills);
            }
            Collections.sort(skills);
        }
        return skills;
    }

    private Set<AvailableSkill> createElement(SkillDefinition skillDefinition) {
        final Set<AvailableSkill> availableSkills = new HashSet<>();
        if (skillDefinition.isSpecializable()) {
            for (final Specialization specialization : skillDefinition.getSpecializations()) {
                availableSkills.add(new AvailableSkill(skillDefinition, specialization));
            }
            if (maximumNumberOfSpecializations < skillDefinition.getSpecializations().size()) {
                maximumNumberOfSpecializations = skillDefinition.getSpecializations().size();
            }
        } else {
            availableSkills.add(new AvailableSkill(skillDefinition));
        }
        return availableSkills;
    }

    public AvailableSkill getElement(String elementId)
            throws InvalidXmlElementException {
        return getElement(elementId, null);
    }

    public AvailableSkill getElement(String elementId, String specializationId)
            throws InvalidXmlElementException {
        for (final AvailableSkill element : getElements()) {
            if (element.getId() != null) {
                if (Objects.equals(element.getId().toLowerCase(), elementId.toLowerCase())) {
                    if (element.getSpecialization() == null) {
                        return element;
                    } else {
                        if (specializationId != null
                                && Objects.equals(element.getSpecialization().getId().toLowerCase(),
                                specializationId.toLowerCase())) {
                            return element;
                        }
                    }
                }
            }
        }
        if (specializationId == null) {
            throw new InvalidXmlElementException("Element '" + elementId + "' does not exists.");
        } else {
            throw new InvalidXmlElementException("Element '" + elementId + " [" + specializationId
                    + "]' does not exists.");
        }
    }

    private List<AvailableSkill> getElements() {
        try {
            return getSkills();
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(this.getClass(), e);
        }
        return new ArrayList<>();
    }

    public List<AvailableSkill> getAvailableSkills(SkillDefinition skillDefinition) {
        final List<AvailableSkill> availableSkills = new ArrayList<>();
        for (final AvailableSkill availableSkill : getElements()) {
            if (Objects.equals(availableSkill.getSkillDefinition(), skillDefinition)) {
                availableSkills.add(availableSkill);
            }
        }
        return availableSkills;
    }

    private void classifySkillByGroup(AvailableSkill availableSkill, String language) {
        skillsByGroup = new HashMap<>();
        skillsByGroup.computeIfAbsent(language, k -> new HashMap<>());
        skillsByGroup.get(language).computeIfAbsent(availableSkill.getSkillDefinition().getSkillGroup(),
                k -> new HashSet<>());
        skillsByGroup.get(language).get(availableSkill.getSkillDefinition().getSkillGroup()).add(availableSkill);
    }

    public int getMaximumNumberOfSpecializations() {
        return maximumNumberOfSpecializations;
    }

}
