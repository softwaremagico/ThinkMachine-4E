package com.softwaremagico.tm.rules;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
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
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.character.perks.PerkOption;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Test(groups = "level")
public class LevelTests {

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
            for (int j = 0; j < level.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(level.getCharacteristicOptions().get(i).getOptions().get(j).getId()));
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
            for (int j = 0; j < level.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                level.getSelectedCharacteristicOptions().get(i).getSelections()
                        .add(new Selection(level.getCharacteristicOptions().get(i).getOptions().get(j).getId()));
            }
        }
    }

    @Test
    public void favoredCallingsInLevel() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleHawkwoodCommander();

        characterPlayer.addLevel();
        CharacterExamples.populateLevel(characterPlayer);

        final LevelSelector level = characterPlayer.addLevel();

        final PerkOption militaryRank = new PerkOption(PerkFactory.getInstance().getElement("militaryRank1"));
        Assert.assertTrue(level.getNotRepeatedClassPerksOptions().get(0).getOptions().contains(militaryRank));

    }


    @Test
    public void notFavoredCallingsInLevel() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleDecadosCommander();

        characterPlayer.addLevel();
        CharacterExamples.populateLevel(characterPlayer);

        final LevelSelector level = characterPlayer.addLevel();

        final PerkOption militaryRank = new PerkOption(PerkFactory.getInstance().getElement("militaryRank1"));
        Assert.assertFalse(level.getNotRepeatedClassPerksOptions().get(0).getOptions().contains(militaryRank));
    }
}
