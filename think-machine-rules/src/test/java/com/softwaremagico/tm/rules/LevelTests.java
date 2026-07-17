package com.softwaremagico.tm.rules;

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

import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.characteristics.CharacteristicBonusOption;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.perks.PerkOption;
import com.softwaremagico.tm.exceptions.InvalidCallingException;
import com.softwaremagico.tm.exceptions.InvalidFactionException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Test(groups = "level")
public class LevelTests extends RulesTest {

    @Test
    public void assignOneLevel() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleDecadosCommander();

        final LevelSelector level = characterPlayer.addLevel();

        Assert.assertEquals(level.getLevelDefinition().getCharacteristicsTotalPoints(), 2);
        Assert.assertEquals(level.getLevelDefinition().getSkillsTotalPoints(), 3);
        Assert.assertEquals(level.getLevelDefinition().getTotalCapabilitiesOptions(), 1);
        Assert.assertEquals(level.getLevelDefinition().getTotalClassPerksOptions(), 0);
        Assert.assertEquals(level.getLevelDefinition().getTotalCallingPerksOptions(), 1);

        for (int i = 0; i < level.getCharacteristicOptions().size(); i++) {
            final List<CharacteristicBonusOption> options = new ArrayList<>(level.getCharacteristicOptions().get(i).getOptions());
            for (int j = level.getSelectedCharacteristicOptions().get(i).getSelections().size();
                 j < level.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(options.get(j)));
            }
        }
    }

    @Test
    public void assignTwoLevels() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleDecadosCommander();

        characterPlayer.addLevel();
        CharacterExamples.populateLevel(characterPlayer);

        final LevelSelector level = characterPlayer.addLevel();

        Assert.assertEquals(level.getLevelDefinition().getCharacteristicsTotalPoints(), 1);
        Assert.assertEquals(level.getLevelDefinition().getSkillsTotalPoints(), 2);
        Assert.assertEquals(level.getLevelDefinition().getTotalCapabilitiesOptions(), 1);
        Assert.assertEquals(level.getLevelDefinition().getTotalClassPerksOptions(), 1);
        Assert.assertEquals(level.getLevelDefinition().getTotalCallingPerksOptions(), 1);

        for (int i = 0; i < level.getCharacteristicOptions().size(); i++) {
            final List<CharacteristicBonusOption> options = new ArrayList<>(level.getCharacteristicOptions().get(i).getOptions());
            for (int j = level.getSelectedCharacteristicOptions().get(i).getSelections().size();
                 j < level.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(options.get(j)));
            }
        }
    }

    @Test
    public void favoredCallingsInLevel_hasRiches() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleDecadosSybarite();

        characterPlayer.addLevel();
        CharacterExamples.populateLevel(characterPlayer);

        final LevelSelector level = characterPlayer.addLevel();

        final PerkOption royalties = new PerkOption(PerkFactory.getInstance().getElement("cash1000"));
        Assert.assertTrue(level.getClassPerksOptions().get(0).getAvailableSelections()
                .contains(new Selection(royalties)));

    }


    @Test
    public void notFavoredCallingsInLevel_hasNotRiches() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleDecadosCommander();

        characterPlayer.addLevel();
        CharacterExamples.populateLevel(characterPlayer);

        final LevelSelector level = characterPlayer.addLevel();

        final PerkOption royalties = new PerkOption(PerkFactory.getInstance().getElement("cash1000"));
        Assert.assertFalse(level.getClassPerksOptions().get(0).getAvailableSelections()
                .contains(new Selection(royalties)));
    }

    @Test
    public void changingCallingAtLevelShouldBeAllowedAndPersistByDefault() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleDecadosCommander();

        final LevelSelector level2 = characterPlayer.addLevel();
        level2.setCalling("conspiracist");
        CharacterExamples.populateLevel(characterPlayer);

        Assert.assertEquals(characterPlayer.getCallingAtLevel(1).getId(), "commander");
        Assert.assertEquals(characterPlayer.getCallingAtLevel(2).getId(), "conspiracist");

        characterPlayer.addLevel();
        Assert.assertEquals(characterPlayer.getCallingAtLevel(3).getId(), "conspiracist");
        Assert.assertEquals(characterPlayer.getCallingCombinationIds(), List.of("commander", "conspiracist"));
    }

    @Test
    public void brotherBattleCallingShouldReturnOnOddLevels() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("brotherBattle");
        characterPlayer.setFaction("brotherBattle");
        characterPlayer.setCalling("brotherBattle");
        CharacterExamples.populateCharacter(characterPlayer);

        final LevelSelector level2 = characterPlayer.addLevel();
        level2.setCalling("theurgist");
        CharacterExamples.populateLevel(characterPlayer);

        characterPlayer.addLevel();
        Assert.expectThrows(InvalidFactionException.class, characterPlayer::validate);
    }

    @Test
    public void brotherBattleCallingCanAlternateWhenReturningCorrectly() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("brotherBattle");
        characterPlayer.setFaction("brotherBattle");
        characterPlayer.setCalling("brotherBattle");
        CharacterExamples.populateCharacter(characterPlayer);

        final LevelSelector level2 = characterPlayer.addLevel();
        level2.setCalling("theurgist");
        CharacterExamples.populateLevel(characterPlayer);

        final LevelSelector level3 = characterPlayer.addLevel();
        level3.setCalling("brotherBattle");
        CharacterExamples.populateLevel(characterPlayer);

        characterPlayer.validate();
        Assert.assertEquals(characterPlayer.getCallingAtLevel(1).getId(), "brotherBattle");
        Assert.assertEquals(characterPlayer.getCallingAtLevel(2).getId(), "theurgist");
        Assert.assertEquals(characterPlayer.getCallingAtLevel(3).getId(), "brotherBattle");
    }

    @Test
    public void levelCallingShouldRejectRestrictedCalling() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleDecadosCommander();

        final LevelSelector level2 = characterPlayer.addLevel();
        Assert.expectThrows(InvalidCallingException.class, () -> level2.setCalling("brotherBattle"));
    }

    @Test
    public void changingCallingShouldUpdateCombinationRepresentation() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleDecadosCommander();

        final LevelSelector level2 = characterPlayer.addLevel();
        level2.setCalling("conspiracist");
        CharacterExamples.populateLevel(characterPlayer);

        final LevelSelector level3 = characterPlayer.addLevel();
        level3.setCalling("commander");
        CharacterExamples.populateLevel(characterPlayer);

        Assert.assertEquals(characterPlayer.getCallingCombinationIds(), List.of("commander", "conspiracist", "commander"));
        Assert.assertEquals(characterPlayer.getCallingCombinationRepresentation(" / "), "Commander / Conspiracist / Commander");
    }

    @Test
    public void brotherBattleCallingShouldRejectNotAllowedAlternativeOnEvenLevel() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("brotherBattle");
        characterPlayer.setFaction("brotherBattle");
        characterPlayer.setCalling("brotherBattle");
        CharacterExamples.populateCharacter(characterPlayer);

        final LevelSelector level2 = characterPlayer.addLevel();
        level2.setCalling("dervish");
        CharacterExamples.populateLevel(characterPlayer);

        Assert.expectThrows(InvalidFactionException.class, characterPlayer::validate);
    }
}
