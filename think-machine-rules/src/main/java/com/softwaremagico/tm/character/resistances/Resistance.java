package com.softwaremagico.tm.character.resistances;

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
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.equipment.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.thinkmachines.ThinkMachineFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Resistance {
    @JsonProperty("type")
    private ResistanceType type;
    @JsonProperty("category")
    private ResistanceCategory category;
    @JsonProperty("bonus")
    private int bonus;

    public Resistance() {
        super();
        this.bonus = 0;
    }

    public Resistance(ResistanceType type, ResistanceCategory category, int bonus) {
        this();
        this.type = type;
        this.category = category;
        this.bonus = bonus;
    }

    public ResistanceType getType() {
        return type;
    }

    public void setType(ResistanceType type) {
        this.type = type;
    }

    public ResistanceCategory getCategory() {
        return category;
    }

    public void setCategory(ResistanceCategory category) {
        this.category = category;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public static int getBonus(ResistanceType type, Element element) {
        return element.getResistances().stream().filter(r -> Objects.equals(r.getType(), type)).mapToInt(Resistance::getBonus).sum();
    }

    public static int getBonus(ResistanceType type, CharacterPlayer player) {
        int bonus = 0;
        final Map<ResistanceCategory, List<Resistance>> resistancesByCategory = groupResistances(player).get(type);
        if (resistancesByCategory == null) {
            return bonus;
        }
        //Abilities do not combine.
        if (resistancesByCategory.get(ResistanceCategory.ABILITY) != null) {
            bonus += resistancesByCategory.get(ResistanceCategory.ABILITY).stream().max(Comparator.comparing(Resistance::getBonus))
                    .orElse(new Resistance()).getBonus();
        }
        //Austerities do not combine.
        if (resistancesByCategory.get(ResistanceCategory.AUSTERITY) != null) {
            bonus += resistancesByCategory.get(ResistanceCategory.AUSTERITY).stream().max(Comparator.comparing(Resistance::getBonus))
                    .orElse(new Resistance()).getBonus();
        }
        //Cyberdevices do not combine.
        if (resistancesByCategory.get(ResistanceCategory.CYBER_DEVICE) != null) {
            bonus += resistancesByCategory.get(ResistanceCategory.CYBER_DEVICE).stream().max(Comparator.comparing(Resistance::getBonus))
                    .orElse(new Resistance()).getBonus();
        }
        //Privileges combine.
        if (resistancesByCategory.get(ResistanceCategory.PRIVILEGE) != null) {
            bonus += resistancesByCategory.get(ResistanceCategory.PRIVILEGE).stream().mapToInt(Resistance::getBonus).sum();
        }
        //Species combine.
        if (resistancesByCategory.get(ResistanceCategory.SPECIE) != null) {
            bonus += resistancesByCategory.get(ResistanceCategory.SPECIE).stream().mapToInt(Resistance::getBonus).sum();
        }
        //Items not combine.
        if (resistancesByCategory.get(ResistanceCategory.ITEM) != null) {
            bonus += resistancesByCategory.get(ResistanceCategory.ITEM).stream().max(Comparator.comparing(Resistance::getBonus))
                    .orElse(new Resistance()).getBonus();
        }
        //Handhandled Shields are extras.
        if (resistancesByCategory.get(ResistanceCategory.HANDHELD_SHIELD) != null) {
            bonus += resistancesByCategory.get(ResistanceCategory.HANDHELD_SHIELD).stream().max(Comparator.comparing(Resistance::getBonus))
                    .orElse(new Resistance()).getBonus();
        }
        return bonus;
    }

    public static Map<ResistanceType, Map<ResistanceCategory, List<Resistance>>> groupResistances(CharacterPlayer characterPlayer) {
        final Map<ResistanceType, Map<ResistanceCategory, List<Resistance>>> resistancesByCategory = new HashMap<>();
        if (characterPlayer.getSpecie() != null) {
            populateResistanceFromOptions(characterPlayer.getSpecie(), SpecieFactory.getInstance(), resistancesByCategory);
        }
        if (characterPlayer.getUpbringing() != null) {
            populateResistanceFromOptions(characterPlayer.getUpbringing(), UpbringingFactory.getInstance(), resistancesByCategory);
        }
        if (characterPlayer.getFaction() != null) {
            populateResistanceFromOptions(characterPlayer.getFaction(), FactionFactory.getInstance(), resistancesByCategory);
        }
        if (characterPlayer.getCalling() != null) {
            populateResistanceFromOptions(characterPlayer.getCalling(), CallingFactory.getInstance(), resistancesByCategory);
        }
        if (characterPlayer.getBestArmor() != null) {
            characterPlayer.getBestArmor().getResistances().forEach(resistance -> {
                resistancesByCategory.computeIfAbsent(resistance.getType(), k -> new HashMap<>());
                resistancesByCategory.get(resistance.getType()).computeIfAbsent(resistance.getCategory(), k -> new ArrayList<>()).add(resistance);
            });
        }
        if (characterPlayer.getBestHandHandledShield() != null) {
            characterPlayer.getBestHandHandledShield().getResistances().forEach(resistance -> {
                resistancesByCategory.computeIfAbsent(resistance.getType(), k -> new HashMap<>());
                resistancesByCategory.get(resistance.getType()).computeIfAbsent(resistance.getCategory(), k -> new ArrayList<>()).add(resistance);
            });
        }

        //TODO(softwaremagico): include level improvements here.

        return resistancesByCategory;
    }

    private static void populateResistanceFromOptions(CharacterDefinitionStepSelection characterDefinitionStepSelection,
                                                      XmlFactory<?> xmlFactory,
                                                      Map<ResistanceType, Map<ResistanceCategory, List<Resistance>>> resistancesByCategory) {
        //Basic resistances
        xmlFactory.getElement(characterDefinitionStepSelection.getId()).getResistances().forEach(resistance -> {
            resistancesByCategory.computeIfAbsent(resistance.getType(), k -> new HashMap<>());
            resistancesByCategory.get(resistance.getType()).computeIfAbsent(resistance.getCategory(), k -> new ArrayList<>()).add(resistance);
        });
        //Resistances based on selections.
        getResistanceFromOptions(characterDefinitionStepSelection).forEach(resistance -> {
            resistancesByCategory.computeIfAbsent(resistance.getType(), k -> new HashMap<>());
            resistancesByCategory.get(resistance.getType()).computeIfAbsent(resistance.getCategory(), k -> new ArrayList<>()).add(resistance);
        });
    }

    private static List<Resistance> getResistanceFromOptions(CharacterDefinitionStepSelection characterDefinitionStepSelection) {
        if (characterDefinitionStepSelection == null) {
            return new ArrayList<>();
        }
        final List<Resistance> resistances = new ArrayList<>();

        characterDefinitionStepSelection.getSelectedCapabilityOptions().forEach(element ->
                element.getSelections().forEach(selection ->
                        resistances.addAll(CapabilityFactory.getInstance().getElement(selection.getId()).getResistances())));

        characterDefinitionStepSelection.getSelectedCharacteristicOptions().forEach(element ->
                element.getSelections().forEach(selection ->
                        resistances.addAll(CharacteristicsDefinitionFactory.getInstance().getElement(selection.getId()).getResistances())));

        characterDefinitionStepSelection.getSelectedSkillOptions().forEach(element ->
                element.getSelections().forEach(selection ->
                        resistances.addAll(SkillFactory.getInstance().getElement(selection.getId()).getResistances())));

        characterDefinitionStepSelection.getSelectedPerksOptions().forEach(element ->
                element.getSelections().forEach(selection ->
                        resistances.addAll(PerkFactory.getInstance().getElement(selection.getId()).getResistances())));

        characterDefinitionStepSelection.getSelectedMaterialAwards().forEach(element ->
                element.getSelections().forEach(selection -> {
                    if (!element.getRemoved().contains(selection)) {
                        //Armors are ignored, as selecting best later.
                        try {
                            resistances.addAll(WeaponFactory.getInstance().getElement(selection.getId()).getResistances());
                            return;
                        } catch (InvalidXmlElementException ignored) {

                        }
                        try {
                            resistances.addAll(ItemFactory.getInstance().getElement(selection.getId()).getResistances());
                            return;
                        } catch (InvalidXmlElementException ignored) {

                        }
                        try {
                            resistances.addAll(ShieldFactory.getInstance().getElement(selection.getId()).getResistances());
                            return;
                        } catch (InvalidXmlElementException ignored) {

                        }
                        try {
                            resistances.addAll(HandheldShieldFactory.getInstance().getElement(selection.getId()).getResistances());
                            return;
                        } catch (InvalidXmlElementException ignored) {

                        }
                        try {
                            resistances.addAll(ThinkMachineFactory.getInstance().getElement(selection.getId()).getResistances());
                            return;
                        } catch (InvalidXmlElementException ignored) {

                        }
                    }
                }));

        return resistances;
    }
}
