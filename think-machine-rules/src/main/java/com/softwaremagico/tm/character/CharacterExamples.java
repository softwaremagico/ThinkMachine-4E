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

import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;

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
        characterPlayer.getCalling().getSelectedMaterialAwards().get(1).addRemoved(new Selection("dressSword"));


        //Add a weapon
        characterPlayer.addEquipmentPurchased(WeaponFactory.getInstance().getElement("soeAlembic"));
        characterPlayer.addEquipmentPurchased(ArmorFactory.getInstance().getElement("synthsilk"));
        characterPlayer.addEquipmentPurchased(ShieldFactory.getInstance().getElement("duelingShield"));

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
        for (int i = 0; i < characterPlayer.getUpbringing().getCharacteristicOptions().size(); i++) {
            for (int j = characterPlayer.getUpbringing().getSelectedCharacteristicOptions().get(i).getSelections().size();
                 j < characterPlayer.getUpbringing().getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getUpbringing().getCharacteristicOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < characterPlayer.getUpbringing().getNotRepeatedCapabilityOptions().size(); i++) {
            for (int j = characterPlayer.getUpbringing().getSelectedCapabilityOptions().get(i).getSelections().size();
                 j < characterPlayer.getUpbringing().getNotRepeatedCapabilityOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getSelectedCapabilityOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getUpbringing().getNotRepeatedCapabilityOptions().get(i).getOptions().get(j).getId(),
                                characterPlayer.getUpbringing().getNotRepeatedCapabilityOptions().get(i).getOptions().get(j).getSelectedSpecialization()));
            }
        }
        for (int i = 0; i < characterPlayer.getUpbringing().getSkillOptions().size(); i++) {
            for (int j = characterPlayer.getUpbringing().getSelectedSkillOptions().get(i).getSelections().size();
                 j < characterPlayer.getUpbringing().getSkillOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getSelectedSkillOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getUpbringing().getSkillOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < characterPlayer.getUpbringing().getNotRepeatedPerksOptions().size(); i++) {
            for (int j = 0; j < characterPlayer.getUpbringing().getNotRepeatedPerksOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getSelectedPerksOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getUpbringing().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getId(),
                                characterPlayer.getUpbringing().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getSpecializations() != null
                                        && !characterPlayer.getUpbringing().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getSpecializations()
                                        .isEmpty()
                                        ? characterPlayer.getUpbringing().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getSpecializations().get(0)
                                        : null));
            }
        }
        for (int i = 0; i < characterPlayer.getUpbringing().getMaterialAwardsOptions().size(); i++) {
            for (int j = characterPlayer.getUpbringing().getSelectedMaterialAwards().get(i).getSelections().size();
                 j < characterPlayer.getUpbringing().getMaterialAwardsOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getSelectedMaterialAwards().get(i).getSelections()
                        .add(new Selection(characterPlayer.getUpbringing().getMaterialAwardsOptions().get(i).getOptions().get(j).getId()));
            }
        }
    }

    public static void populateFaction(CharacterPlayer characterPlayer) {
        for (int i = 0; i < characterPlayer.getFaction().getCharacteristicOptions().size(); i++) {
            for (int j = characterPlayer.getFaction().getSelectedCharacteristicOptions().get(i).getSelections().size();
                 j < characterPlayer.getFaction().getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getFaction().getCharacteristicOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < characterPlayer.getFaction().getNotRepeatedCapabilityOptions().size(); i++) {
            for (int j = characterPlayer.getFaction().getSelectedCapabilityOptions().get(i).getSelections().size();
                 j < characterPlayer.getFaction().getNotRepeatedCapabilityOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getSelectedCapabilityOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getFaction().getNotRepeatedCapabilityOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < characterPlayer.getFaction().getSkillOptions().size(); i++) {
            for (int j = characterPlayer.getFaction().getSelectedSkillOptions().get(i).getSelections().size();
                 j < characterPlayer.getFaction().getSkillOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getSelectedSkillOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getFaction().getSkillOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < characterPlayer.getFaction().getMaterialAwardsOptions().size(); i++) {
            for (int j = characterPlayer.getFaction().getSelectedMaterialAwards().get(i).getSelections().size();
                 j < characterPlayer.getFaction().getMaterialAwardsOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getSelectedMaterialAwards().get(i).getSelections()
                        .add(new Selection(characterPlayer.getFaction().getMaterialAwardsOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < characterPlayer.getFaction().getNotRepeatedPerksOptions().size(); i++) {
            for (int j = characterPlayer.getFaction().getSelectedPerksOptions().get(i).getSelections().size();
                 j < characterPlayer.getFaction().getNotRepeatedPerksOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getSelectedPerksOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getFaction().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getId(),
                                characterPlayer.getFaction().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getSpecializations() != null
                                        && !characterPlayer.getFaction().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getSpecializations()
                                        .isEmpty()
                                        ? characterPlayer.getFaction().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getSpecializations().get(0)
                                        : null));
            }
        }
    }

    public static void populateCalling(CharacterPlayer characterPlayer) {
        for (int i = 0; i < characterPlayer.getCalling().getCharacteristicOptions().size(); i++) {
            for (int j = characterPlayer.getCalling().getSelectedCharacteristicOptions().get(i).getSelections().size();
                 j < characterPlayer.getCalling().getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getCalling().getCharacteristicOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < characterPlayer.getCalling().getNotRepeatedCapabilityOptions().size(); i++) {
            for (int j = characterPlayer.getCalling().getSelectedCapabilityOptions().get(i).getSelections().size();
                 j < characterPlayer.getCalling().getNotRepeatedCapabilityOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getSelectedCapabilityOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getCalling().getNotRepeatedCapabilityOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < characterPlayer.getCalling().getSkillOptions().size(); i++) {
            for (int j = characterPlayer.getCalling().getSelectedSkillOptions().get(i).getSelections().size();
                 j < characterPlayer.getCalling().getSkillOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getSelectedSkillOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getCalling().getSkillOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < characterPlayer.getCalling().getNotRepeatedPerksOptions().size(); i++) {
            for (int j = characterPlayer.getCalling().getSelectedPerksOptions().get(i).getSelections().size();
                 j < characterPlayer.getCalling().getNotRepeatedPerksOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getSelectedPerksOptions().get(i).getSelections()
                        .add(new Selection(characterPlayer.getCalling().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getId(),
                                characterPlayer.getCalling().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getSpecializations() != null
                                && !characterPlayer.getCalling().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getSpecializations().isEmpty()
                                        ? characterPlayer.getCalling().getNotRepeatedPerksOptions().get(i).getOptions().get(j).getSpecializations().get(0)
                                        : null));
            }
        }
        for (int i = 0; i < characterPlayer.getCalling().getMaterialAwardsOptions().size(); i++) {
            for (int j = characterPlayer.getCalling().getSelectedMaterialAwards().get(i).getSelections().size();
                 j < characterPlayer.getCalling().getMaterialAwardsOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getSelectedMaterialAwards().get(i).getSelections()
                        .add(new Selection(characterPlayer.getCalling().getMaterialAwardsOptions().get(i).getOptions().get(j).getId()));
            }
        }
    }
}
