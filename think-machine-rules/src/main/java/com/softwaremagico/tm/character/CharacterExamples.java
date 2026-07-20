package com.softwaremagico.tm.character;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicReassign;
import com.softwaremagico.tm.character.equipment.EquipmentOption;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.skills.SkillBonusOption;
import com.softwaremagico.tm.character.specie.ElementValues;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.exceptions.MaxValueExceededException;

import java.util.ArrayList;
import java.util.List;

public final class CharacterExamples {
    public static final int AGE = 31;

    // String constants to avoid duplication (S1192)
    private static final String OLIVER = "Oliver";
    private static final String QUEEN = "Queen";
    private static final String PLAYER_1 = "Player 1";
    private static final String HUMAN = "human";
    private static final String NOBLE = "noble";
    private static final String SUTEK = "sutek";

    private CharacterExamples() {

    }

    public static CharacterPlayer generateHumanNobleDecadosCommander() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();

        characterPlayer.getInfo().addName(new Name(OLIVER, Gender.MALE, null, null));
        characterPlayer.getInfo().setSurname(new Surname(QUEEN, null, null));
        characterPlayer.getInfo().setPlayer(PLAYER_1);
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.getInfo().setAge(AGE);
        characterPlayer.getInfo().setPlanet(PlanetFactory.getInstance().getElement(SUTEK));

        characterPlayer.setSpecie(HUMAN);
        characterPlayer.setUpbringing(NOBLE);
        characterPlayer.setFaction("decados");
        characterPlayer.setCalling("commander");

        populateCharacter(characterPlayer);

        // Remove a material Award. Dress Sword
        characterPlayer.getCalling().getSelectedMaterialAwards().get(1)
                .addRemoved(new Selection(ItemFactory.getInstance().getElement("dressSword")));

        // Add a weapon
        characterPlayer.addEquipmentPurchased(WeaponFactory.getInstance().getElement("soeAlembic"));
        characterPlayer.addEquipmentPurchased(ArmorFactory.getInstance().getElement("synthsilk"));
        characterPlayer.addEquipmentPurchased(ShieldFactory.getInstance().getElement("duelingShield"));

        return characterPlayer;
    }

    public static CharacterPlayer generateHumanNobleHawkwoodCommander() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();

        characterPlayer.getInfo().addName(new Name(OLIVER, Gender.MALE, null, null));
        characterPlayer.getInfo().setSurname(new Surname(QUEEN, null, null));
        characterPlayer.getInfo().setPlayer(PLAYER_1);
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.getInfo().setAge(AGE);
        characterPlayer.getInfo().setPlanet(PlanetFactory.getInstance().getElement(SUTEK));

        characterPlayer.setSpecie(HUMAN);
        characterPlayer.setUpbringing(NOBLE);
        characterPlayer.setFaction("hawkwood");
        characterPlayer.setCalling("commander");

        populateCharacter(characterPlayer);

        characterPlayer.getCharacteristicReassigns().add(new CharacteristicReassign("presence", "strength"));

        return characterPlayer;
    }

    public static CharacterPlayer generateHumanNobleDecadosSybarite() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();

        characterPlayer.getInfo().addName(new Name(OLIVER, Gender.MALE, null, null));
        characterPlayer.getInfo().setSurname(new Surname(QUEEN, null, null));
        characterPlayer.getInfo().setPlayer(PLAYER_1);
        characterPlayer.getInfo().setGender(Gender.MALE);
        characterPlayer.getInfo().setAge(AGE);
        characterPlayer.getInfo().setPlanet(PlanetFactory.getInstance().getElement(SUTEK));

        characterPlayer.setSpecie(HUMAN);
        characterPlayer.setUpbringing(NOBLE);
        characterPlayer.setFaction("decados");
        characterPlayer.setCalling("sybarite");

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
        populateCharacterStep(characterPlayer, characterPlayer.getUpbringing());
    }

    public static void populateFaction(CharacterPlayer characterPlayer) {
        populateCharacterStep(characterPlayer, characterPlayer.getFaction());
    }

    public static void populateCalling(CharacterPlayer characterPlayer) {
        populateCharacterStep(characterPlayer, characterPlayer.getCalling());
    }

    public static void populateLevel(CharacterPlayer characterPlayer) {
        final LevelSelector level = characterPlayer.getLevels().peek();
        populateLevelCharacteristics(characterPlayer, level);
        populateLevelCapabilities(level);
        populateLevelSkills(characterPlayer, level);
        populateLevelClassPerks(level);
        populateLevelCallingPerks(level);
    }

    private static void populateLevelCharacteristics(CharacterPlayer characterPlayer, LevelSelector level) {
        for (int i = 0; i < level.getCharacteristicOptions().size(); i++) {
            final List<CharacteristicBonusOption> options = new ArrayList<>(
                    level.getCharacteristicOptions().get(i).getOptions());
            for (int j = level.getSelectedCharacteristicOptions().get(i).getSelections().size(); j < level
                    .getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                addLevelCharacteristicSelection(characterPlayer, level, options, i, j);
            }
        }
    }

    private static void addLevelCharacteristicSelection(CharacterPlayer characterPlayer, LevelSelector level,
            List<CharacteristicBonusOption> options, int i, int startIndex) {
        int index = startIndex;
        while (true) {
            try {
                if (isLevelCharacteristicAddable(characterPlayer, options, index)) {
                    level.getSelectedCharacteristicOptions().get(i).getSelections()
                            .add(new Selection(options.get(index)));
                    return;
                }
            } catch (final MaxValueExceededException ignored) {
                // try next option
            }
            index++;
        }
    }

    private static boolean isLevelCharacteristicAddable(CharacterPlayer characterPlayer,
            List<CharacteristicBonusOption> options, int index) throws MaxValueExceededException {
        if (index >= options.size() - 1) {
            return true;
        }
        final int level = characterPlayer.getLevel();
        final CharacteristicName charName = options.get(index).getElement().getCharacteristicName();
        final int currentValue = characterPlayer.getCharacteristicValue(charName) + options.get(index).getBonus();
        if (level == ElementValues.INTERMEDIATE_LEVEL) {
            return currentValue <= ElementValues.MAX_INTERMEDIATE_VALUE;
        }
        final int specieMax = SpecieFactory.getInstance().getElement(characterPlayer.getSpecie())
                .getSpecieCharacteristic(options.get(index).getId()).getMaximumValue();
        if (level > ElementValues.INTERMEDIATE_LEVEL && level < ElementValues.ADVANCED_LEVEL) {
            return currentValue <= Math.min(ElementValues.LEVEL_MAX_VALUE, specieMax);
        }
        if (level > ElementValues.ADVANCED_LEVEL) {
            return currentValue <= specieMax;
        }
        return true;
    }

    private static void populateLevelCapabilities(LevelSelector level) {
        for (int i = 0; i < level.getNotRepeatedCapabilityOptions().size(); i++) {
            final List<CapabilityOption> options = new ArrayList<>(
                    level.getNotRepeatedCapabilityOptions().get(i).getOptions());
            for (int j = level.getSelectedCapabilityOptions().get(i).getSelections().size(); j < level
                    .getNotRepeatedCapabilityOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedCapabilityOptions().get(i).getSelections()
                        .add(new Selection(options.get(j), options.get(j).getSelectedSpecialization()));
            }
        }
    }

    private static void populateLevelSkills(CharacterPlayer characterPlayer, LevelSelector level) {
        for (int i = 0; i < level.getSkillOptions().size(); i++) {
            final List<SkillBonusOption> options = new ArrayList<>(level.getSkillOptions().get(i).getOptions());
            for (int j = level.getSelectedSkillOptions().get(i).getSelections().size(); j < level.getSkillOptions()
                    .get(i).getTotalOptions(); j++) {
                addLevelSkillSelection(characterPlayer, level, options, i, j);
            }
        }
    }

    private static void addLevelSkillSelection(CharacterPlayer characterPlayer, LevelSelector level,
            List<SkillBonusOption> options, int i, int startIndex) {
        int index = startIndex;
        while (true) {
            try {
                if (isLevelSkillAddable(characterPlayer, options, index)) {
                    level.getSelectedSkillOptions().get(i).getSelections().add(new Selection(options.get(index)));
                    return;
                }
            } catch (final MaxValueExceededException ignored) {
                // try next option
            }
            index++;
        }
    }

    private static boolean isLevelSkillAddable(CharacterPlayer characterPlayer, List<SkillBonusOption> options,
            int index) throws MaxValueExceededException {
        if (index >= options.size() - 1) {
            return true;
        }
        final int currentValue = characterPlayer.getSkillValue(options.get(index).getElement())
                + options.get(index).getBonus();
        if (characterPlayer.getLevel() == ElementValues.INTERMEDIATE_LEVEL) {
            return currentValue <= ElementValues.MAX_INTERMEDIATE_VALUE;
        }
        if (characterPlayer.getLevel() > ElementValues.INTERMEDIATE_LEVEL) {
            return currentValue <= ElementValues.LEVEL_MAX_VALUE;
        }
        return true;
    }

    private static void populateLevelClassPerks(LevelSelector level) {
        for (int i = 0; i < level.getNotSelectedPerksOptions(true).size(); i++) {
            final List<Selection> options = new ArrayList<>(
                    level.getNotSelectedPerksOptions(true).get(i).getAvailableSelections());
            for (int j = level.getSelectedClassPerksOptions().get(i).getSelections().size(); j < level
                    .getNotSelectedPerksOptions(true).get(i).getTotalOptions(); j++) {
                level.getSelectedClassPerksOptions().get(i).getSelections().add(options.get(j));
            }
        }
    }

    private static void populateLevelCallingPerks(LevelSelector level) {
        for (int i = 0; i < level.getNotRepeatedCallingPerksOptions().size(); i++) {
            final List<Selection> options = new ArrayList<>(
                    level.getNotRepeatedCallingPerksOptions().get(i).getAvailableSelections());
            for (int j = level.getSelectedCallingPerksOptions().get(i).getSelections().size(); j < level
                    .getNotRepeatedCallingPerksOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedCallingPerksOptions().get(i).getSelections().add(options.get(j));
            }
        }
    }

    public static void populateCharacterStep(CharacterPlayer characterPlayer, CharacterDefinitionStepSelection step) {
        populateStepCharacteristics(characterPlayer, step);
        populateStepCapabilities(step);
        populateStepSkills(characterPlayer, step);
        populateStepPerks(step);
        populateStepMaterialAwards(step);
    }

    private static void populateStepCharacteristics(CharacterPlayer characterPlayer,
            CharacterDefinitionStepSelection step) {
        for (int i = 0; i < step.getCharacteristicOptions().size(); i++) {
            final List<CharacteristicBonusOption> options = new ArrayList<>(
                    step.getCharacteristicOptions().get(i).getOptions());
            for (int j = step.getSelectedCharacteristicOptions().get(i).getSelections().size(); j < step
                    .getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                addStepCharacteristicSelection(characterPlayer, step, options, i, j);
            }
        }
    }

    private static void addStepCharacteristicSelection(CharacterPlayer characterPlayer,
            CharacterDefinitionStepSelection step, List<CharacteristicBonusOption> options, int i, int startIndex) {
        int index = startIndex;
        while (true) {
            final int currentValue = characterPlayer.getCharacteristicValue(
                    options.get(index).getElement().getCharacteristicName()) + options.get(index).getBonus();
            if (index >= options.size() - 1 || characterPlayer.getLevel() > ElementValues.INTERMEDIATE_LEVEL
                    || currentValue <= ElementValues.MAX_INITIAL_VALUE) {
                step.getSelectedCharacteristicOptions().get(i).getSelections().add(new Selection(options.get(index)));
                return;
            }
            index++;
        }
    }

    private static void populateStepCapabilities(CharacterDefinitionStepSelection step) {
        for (int i = 0; i < step.getNotRepeatedCapabilityOptions().size(); i++) {
            final List<CapabilityOption> options = new ArrayList<>(
                    step.getNotRepeatedCapabilityOptions().get(i).getOptions());
            for (int j = step.getSelectedCapabilityOptions().get(i).getSelections().size(); j < step
                    .getNotRepeatedCapabilityOptions().get(i).getTotalOptions(); j++) {
                step.getSelectedCapabilityOptions().get(i).getSelections()
                        .add(new Selection(options.get(j), options.get(j).getSelectedSpecialization()));
            }
        }
    }

    private static void populateStepSkills(CharacterPlayer characterPlayer, CharacterDefinitionStepSelection step) {
        for (int i = 0; i < step.getSkillOptions().size(); i++) {
            final List<SkillBonusOption> options = new ArrayList<>(step.getSkillOptions().get(i).getOptions());
            for (int j = step.getSelectedSkillOptions().get(i).getSelections().size(); j < step.getSkillOptions().get(i)
                    .getTotalOptions(); j++) {
                addStepSkillSelection(characterPlayer, step, options, i, j);
            }
        }
    }

    private static void addStepSkillSelection(CharacterPlayer characterPlayer, CharacterDefinitionStepSelection step,
            List<SkillBonusOption> options, int i, int startIndex) {
        int index = startIndex;
        while (true) {
            if (index >= options.size() - 1 || characterPlayer.getLevel() > ElementValues.INTERMEDIATE_LEVEL
                    || characterPlayer.getSkillValue(options.get(index).getElement())
                            + options.get(index).getBonus() <= ElementValues.MAX_INITIAL_VALUE) {
                step.getSelectedSkillOptions().get(i).getSelections().add(new Selection(options.get(index)));
                return;
            }
            index++;
        }
    }

    private static void populateStepPerks(CharacterDefinitionStepSelection step) {
        for (int i = 0; i < step.getNotSelectedPerksOptions(true).size(); i++) {
            final List<Selection> options = new ArrayList<>(
                    step.getNotSelectedPerksOptions(true).get(i).getAvailableSelections());
            for (int j = step.getSelectedPerksOptions().get(i).getSelections().size(); j < step
                    .getNotSelectedPerksOptions(true).get(i).getTotalOptions(); j++) {
                step.getSelectedPerksOptions().get(i).getSelections().add(options.get(j));
            }
        }
    }

    private static void populateStepMaterialAwards(CharacterDefinitionStepSelection step) {
        if (step.getMaterialAwardsOptions() == null) {
            return;
        }
        for (int i = 0; i < step.getMaterialAwardsOptions().size(); i++) {
            final List<EquipmentOption> options = new ArrayList<>(step.getMaterialAwardsOptions().get(i).getOptions());
            for (int j = step.getSelectedMaterialAwards().get(i).getSelections().size(); j < step
                    .getMaterialAwardsOptions().get(i).getTotalOptions(); j++) {
                step.getSelectedMaterialAwards().get(i).getSelections().add(new Selection(options.get(j)));
            }
        }
    }
}
