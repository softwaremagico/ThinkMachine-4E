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

import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOption;
import com.softwaremagico.tm.character.characteristics.CharacteristicReassign;
import com.softwaremagico.tm.character.equipment.EquipmentOption;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.skills.SkillBonusOption;

import java.util.ArrayList;
import java.util.List;

public final class CharacterExamples {
    public static final int AGE = 31;

    private CharacterExamples() {

    }

    public static CharacterPlayer generateHumanNobleDecadosCommander() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();

        characterPlayer.getInfo().addName(new Name("Oliver", Gender.MALE, null, null));
        characterPlayer.getInfo().setSurname(new Surname("Queen", null, null));
        characterPlayer.getInfo().setPlayer("Player 1");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.getInfo().setAge(AGE);
        characterPlayer.getInfo().setPlanet(PlanetFactory.getInstance().getElement("sutek"));

        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("decados");
        characterPlayer.setCalling("commander");

        populateCharacter(characterPlayer);


        //Remove a material Award. Dress Sword
        characterPlayer.getCalling().getSelectedMaterialAwards().get(1).addRemoved(new Selection(ItemFactory.getInstance().getElement("dressSword")));


        //Add a weapon
        characterPlayer.addEquipmentPurchased(WeaponFactory.getInstance().getElement("soeAlembic"));
        characterPlayer.addEquipmentPurchased(ArmorFactory.getInstance().getElement("synthsilk"));
        characterPlayer.addEquipmentPurchased(ShieldFactory.getInstance().getElement("duelingShield"));

        return characterPlayer;
    }

    public static CharacterPlayer generateHumanNobleHawkwoodCommander() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();

        characterPlayer.getInfo().addName(new Name("Oliver", Gender.MALE, null, null));
        characterPlayer.getInfo().setSurname(new Surname("Queen", null, null));
        characterPlayer.getInfo().setPlayer("Player 1");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.getInfo().setAge(AGE);
        characterPlayer.getInfo().setPlanet(PlanetFactory.getInstance().getElement("sutek"));

        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("hawkwood");
        characterPlayer.setCalling("commander");

        populateCharacter(characterPlayer);

        characterPlayer.getCharacteristicReassigns().add(new CharacteristicReassign("presence", "strength"));

        return characterPlayer;
    }

    public static void populateCharacter(CharacterPlayer characterPlayer) {
        characterPlayer.setPrimaryCharacteristic("dexterity");
        characterPlayer.setSecondaryCharacteristic("wits");
        populateUpbringing(characterPlayer);
        populateFaction(characterPlayer);
        populateCalling(characterPlayer);
    }

    public static void populateUpbringing(CharacterPlayer characterPlayer) {
        populateCharacterStep(characterPlayer.getUpbringing());
    }

    public static void populateFaction(CharacterPlayer characterPlayer) {
        populateCharacterStep(characterPlayer.getFaction());
    }

    public static void populateCalling(CharacterPlayer characterPlayer) {
        populateCharacterStep(characterPlayer.getCalling());
    }


    public static void populateLevel(CharacterPlayer characterPlayer) {
        final LevelSelector level = characterPlayer.getLevels().peek();

        for (int i = 0; i < level.getCharacteristicOptions().size(); i++) {
            final List<CharacteristicBonusOption> options = new ArrayList<>(level.getCharacteristicOptions().get(i).getOptions());
            for (int j = level.getSelectedCharacteristicOptions().get(i).getSelections().size();
                 j < level.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(options.get(j)));
            }
        }
        for (int i = 0; i < level.getNotRepeatedCapabilityOptions().size(); i++) {
            final List<CapabilityOption> options = new ArrayList<>(level.getNotRepeatedCapabilityOptions().get(i).getOptions());
            for (int j = level.getSelectedCapabilityOptions().get(i).getSelections().size();
                 j < level.getNotRepeatedCapabilityOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedCapabilityOptions().get(i).getSelections()
                        .add(new Selection(options.get(j), options.get(j).getSelectedSpecialization()));
            }
        }
        for (int i = 0; i < level.getSkillOptions().size(); i++) {
            final List<SkillBonusOption> options = new ArrayList<>(level.getSkillOptions().get(i).getOptions());
            for (int j = level.getSelectedSkillOptions().get(i).getSelections().size();
                 j < level.getSkillOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedSkillOptions().get(i).getSelections()
                        .add(new Selection(options.get(j)));
            }
        }
        for (int i = 0; i < level.getNotSelectedPerksOptions().size(); i++) {
            final List<Selection> options = new ArrayList<>(level.getNotSelectedPerksOptions().get(i).getAvailableSelections());
            for (int j = level.getSelectedClassPerksOptions().get(i).getSelections().size();
                 j < level.getNotSelectedPerksOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedClassPerksOptions().get(i).getSelections()
                        .add(options.get(j));
            }
        }

        for (int i = 0; i < level.getNotRepeatedCallingPerksOptions().size(); i++) {
            final List<Selection> options = new ArrayList<>(level.getNotRepeatedCallingPerksOptions().get(i).getAvailableSelections());
            for (int j = level.getSelectedCallingPerksOptions().get(i).getSelections().size();
                 j < level.getNotRepeatedCallingPerksOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedCallingPerksOptions().get(i).getSelections()
                        .add(options.get(j));
            }
        }
    }

    public static void populateCharacterStep(CharacterDefinitionStepSelection step) {
        for (int i = 0; i < step.getCharacteristicOptions().size(); i++) {
            final List<CharacteristicBonusOption> options = new ArrayList<>(step.getCharacteristicOptions().get(i).getOptions());
            for (int j = step.getSelectedCharacteristicOptions().get(i).getSelections().size();
                 j < step.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                step.getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(options.get(j)));
            }
        }
        for (int i = 0; i < step.getNotRepeatedCapabilityOptions().size(); i++) {
            final List<CapabilityOption> options = new ArrayList<>(step.getNotRepeatedCapabilityOptions().get(i).getOptions());
            for (int j = step.getSelectedCapabilityOptions().get(i).getSelections().size();
                 j < step.getNotRepeatedCapabilityOptions().get(i).getTotalOptions(); j++) {
                step.getSelectedCapabilityOptions().get(i).getSelections()
                        .add(new Selection(options.get(j),
                                options.get(j).getSelectedSpecialization()));
            }
        }
        for (int i = 0; i < step.getSkillOptions().size(); i++) {
            final List<SkillBonusOption> options = new ArrayList<>(step.getSkillOptions().get(i).getOptions());
            for (int j = step.getSelectedSkillOptions().get(i).getSelections().size();
                 j < step.getSkillOptions().get(i).getTotalOptions(); j++) {
                step.getSelectedSkillOptions().get(i).getSelections()
                        .add(new Selection(options.get(j)));
            }
        }
        for (int i = 0; i < step.getNotSelectedPerksOptions().size(); i++) {
            final List<Selection> options = new ArrayList<>(step.getNotSelectedPerksOptions().get(i).getAvailableSelections());
            for (int j = step.getSelectedPerksOptions().get(i).getSelections().size();
                 j < step.getNotSelectedPerksOptions().get(i).getTotalOptions(); j++) {
                step.getSelectedPerksOptions().get(i).getSelections()
                        .add(options.get(j));
            }
        }
        if (step.getMaterialAwardsOptions() != null) {
            for (int i = 0; i < step.getMaterialAwardsOptions().size(); i++) {
                final List<EquipmentOption> options = new ArrayList<>(step.getMaterialAwardsOptions().get(i).getOptions());
                for (int j = step.getSelectedMaterialAwards().get(i).getSelections().size();
                     j < step.getMaterialAwardsOptions().get(i).getTotalOptions(); j++) {
                    step.getSelectedMaterialAwards().get(i).getSelections()
                            .add(new Selection(options.get(j)));
                }
            }
        }
    }
}
