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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.exceptions.MaxValueExceededException;
import com.softwaremagico.tm.file.modules.ModuleManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "zgo")
public class ZgoUnskilledTests extends com.softwaremagico.tm.factory.FactoryTest {

    @Override
    @BeforeClass
    public void enableBasicModule() {
        ModuleManager.enableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.enableModule(ModuleManager.LOST_WORLDS_BOOK_MODULE);
        ModuleManager.resetModules();
    }

    private int skill(CharacterPlayer cp, String skillId) {
        try {
            return cp.getSkillValue(skillId);
        } catch (MaxValueExceededException e) {
            throw new AssertionError("Unexpected MaxValueExceededException for skill '" + skillId + "'", e);
        }
    }

    private CharacterPlayer buildCharacterWithLwZgoUnskilledProfile() {
        final CharacterPlayer cp = new CharacterPlayer();
        cp.setSpecie("zgo");
        return cp;
    }

    @Test
    public void lwZgoUnskilledProfileHasCraftsAtZero() {
        final CharacterPlayer cp = buildCharacterWithLwZgoUnskilledProfile();
        Assert.assertEquals(skill(cp, "crafts"), 0);
    }

    @Test
    public void lwZgoUnskilledProfileHasMeleeAtZero() {
        final CharacterPlayer cp = buildCharacterWithLwZgoUnskilledProfile();
        Assert.assertEquals(skill(cp, "melee"), 0);
    }

    @Test
    public void lwZgoUnskilledProfileHasShootAtZero() {
        final CharacterPlayer cp = buildCharacterWithLwZgoUnskilledProfile();
        Assert.assertEquals(skill(cp, "shoot"), 0);
    }

    @Test
    public void lwZgoUnskilledProfileKeepsOtherNaturalSkillsAtNaturalValue() {
        final CharacterPlayer cp = buildCharacterWithLwZgoUnskilledProfile();
        Assert.assertEquals(skill(cp, "observe"), Skill.NATURAL_SKILL_INITIAL_VALUE);
    }
}
