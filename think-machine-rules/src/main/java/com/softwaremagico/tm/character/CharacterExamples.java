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

import com.softwaremagico.tm.character.callings.Calling;
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;

public final class CharacterExamples {
    public static final int AGE = 31;

    private CharacterExamples() {

    }

    public static CharacterPlayer generateHumanNobleDecadosCommander() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();

        characterPlayer.getInfo().addName(new Name("Oliver", Gender.MALE, null));
        characterPlayer.getInfo().setSurname(new Surname("Queen", null));
        characterPlayer.getInfo().setPlayer("Player 1");
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.getInfo().setAge(AGE);
        characterPlayer.getInfo().setPlanet(PlanetFactory.getInstance().getElement("sutek"));

        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");
        final Upbringing upbringing = UpbringingFactory.getInstance().getElement("noble");
        for (int i = 0; i < upbringing.getCharacteristicOptions().size(); i++) {
            for (int j = 0; j < upbringing.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(upbringing.getCharacteristicOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < upbringing.getCapabilityOptions().size(); i++) {
            for (int j = 0; j < upbringing.getCapabilityOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getCapabilityOptions().get(i).getSelections()
                        .add(new Selection(upbringing.getCapabilityOptions().get(i).getOptions().get(j).getId(),
                                upbringing.getCapabilityOptions().get(i).getOptions().get(j).getSelectedSpecialization()));
            }
        }
        for (int i = 0; i < upbringing.getSkillOptions().size(); i++) {
            for (int j = 0; j < upbringing.getSkillOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getSkillOptions().get(i).getSelections()
                        .add(new Selection(upbringing.getSkillOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < upbringing.getPerksOptions().size(); i++) {
            for (int j = 0; j < upbringing.getPerksOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getPerksOptions().get(i).getSelections()
                        .add(new Selection(upbringing.getPerksOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < upbringing.getMaterialAwards().size(); i++) {
            for (int j = 0; j < upbringing.getMaterialAwards().get(i).getTotalOptions(); j++) {
                characterPlayer.getUpbringing().getMaterialAwards().get(i).getSelections()
                        .add(new Selection(upbringing.getMaterialAwards().get(i).getOptions().get(j).getId()));
            }
        }


        characterPlayer.setFaction("decados");
        final Faction faction = FactionFactory.getInstance().getElement("decados");
        for (int i = 0; i < faction.getCharacteristicOptions().size(); i++) {
            for (int j = 0; j < faction.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(faction.getCharacteristicOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < faction.getCapabilityOptions().size(); i++) {
            for (int j = 0; j < faction.getCapabilityOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getCapabilityOptions().get(i).getSelections()
                        .add(new Selection(faction.getCapabilityOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < faction.getSkillOptions().size(); i++) {
            for (int j = 0; j < faction.getSkillOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getSkillOptions().get(i).getSelections()
                        .add(new Selection(faction.getSkillOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < faction.getMaterialAwards().size(); i++) {
            for (int j = 0; j < faction.getMaterialAwards().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getMaterialAwards().get(i).getSelections()
                        .add(new Selection(faction.getMaterialAwards().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < faction.getPerksOptions().size(); i++) {
            for (int j = 0; j < faction.getPerksOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getFaction().getPerksOptions().get(i).getSelections()
                        .add(new Selection(faction.getPerksOptions().get(i).getOptions().get(j).getId()));
            }
        }


        characterPlayer.setCalling("commander");
        final Calling calling = CallingFactory.getInstance().getElement("commander");
        for (int i = 0; i < calling.getCharacteristicOptions().size(); i++) {
            for (int j = 0; j < calling.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(calling.getCharacteristicOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < calling.getCapabilityOptions().size(); i++) {
            for (int j = 0; j < calling.getCapabilityOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getCapabilityOptions().get(i).getSelections()
                        .add(new Selection(calling.getCapabilityOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < calling.getSkillOptions().size(); i++) {
            for (int j = 0; j < calling.getSkillOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getSkillOptions().get(i).getSelections()
                        .add(new Selection(calling.getSkillOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < calling.getPerksOptions().size(); i++) {
            for (int j = 0; j < calling.getPerksOptions().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getPerksOptions().get(i).getSelections()
                        .add(new Selection(calling.getPerksOptions().get(i).getOptions().get(j).getId()));
            }
        }
        for (int i = 0; i < calling.getMaterialAwards().size(); i++) {
            for (int j = 0; j < calling.getMaterialAwards().get(i).getTotalOptions(); j++) {
                characterPlayer.getCalling().getMaterialAwards().get(i).getSelections()
                        .add(new Selection(calling.getMaterialAwards().get(i).getOptions().get(j).getId()));
            }
        }

        //Remove a material Award. Dress Sword
        characterPlayer.getCalling().getMaterialAwards().get(1).addRemoved(characterPlayer.getCalling().getMaterialAwards().get(1).getSelections().get(0));


        //Add a weapon
        characterPlayer.addEquipmentPurchased(WeaponFactory.getInstance().getElement("soeAlembic"));
        characterPlayer.addEquipmentPurchased(ArmorFactory.getInstance().getElement("synthsilk"));
        characterPlayer.addEquipmentPurchased(ShieldFactory.getInstance().getElement("duelingShield"));

        return characterPlayer;
    }
}
