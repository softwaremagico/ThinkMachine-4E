package com.softwaremagico.tm.random.profile;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
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
import com.softwaremagico.tm.random.character.RandomizeCharacter;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.restrictions.RestrictionMode;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Set;

public class RandomProfileTest {

    @Test
    public void tankDriverMandatoryRestrictionsMatchWarcraftRequirements() throws Exception {
        final RandomProfile profile = RandomProfileFactory.getInstance().getElements().stream()
                .filter(candidate -> "tankDriver".equals(candidate.getId())).findFirst().orElseThrow();

        Assert.assertEquals(profile.getMandatoryCapabilities(), Set.of("warcraft"));
        Assert.assertEquals(profile.getMandatoryRestrictions().getMode(), RestrictionMode.ANY);
        Assert.assertEquals(profile.getMandatoryRestrictions().getRestrictedToFactions(),
                Set.of("brotherBattle", "theDispossessed"));
        Assert.assertEquals(profile.getMandatoryRestrictions().getRestrictedToUpbringing(), Set.of("noble", "merchant"));
        Assert.assertEquals(profile.getMandatoryRestrictions().getRestrictedToCallings(), Set.of("mercenary", "scout"));
    }

    @Test
    public void tankDriverGeneratesWarcraftOrFailsExplicitly() throws Exception {
        final RandomProfile profile = RandomProfileFactory.getInstance().getElements().stream()
                .filter(candidate -> "tankDriver".equals(candidate.getId())).findFirst().orElseThrow();
        final CharacterPlayer characterPlayer = new CharacterPlayer();

        try {
            new RandomizeCharacter(characterPlayer, profile).createCharacter();
            Assert.assertTrue(characterPlayer.hasCapability("warcraft", (String) null));
        } catch (InvalidRandomElementSelectedException e) {
            Assert.assertTrue(e.getMessage().contains("Mandatory capability 'warcraft'"));
        }
    }

    @DataProvider(name = "profiles")
    public Object[][] profiles() throws Exception {
        return RandomProfileFactory.getInstance().getElements().stream()
                .map(profile -> new Object[]{profile}).toArray(Object[][]::new);
    }

    @Test(dataProvider = "profiles")
    public void generatedCharacterMeetsProfileRequirements(RandomProfile profile)
            throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        try {
            new RandomizeCharacter(characterPlayer, profile).createCharacter();
        } catch (InvalidRandomElementSelectedException e) {
            Assert.assertTrue(profile.getMandatoryCapabilities().stream().anyMatch(e.getMessage()::contains), profile.getId());
            return;
        }

        for (final String skill : profile.getMandatorySkills()) {
            Assert.assertTrue(characterPlayer.getSkillValue(skill) > 0, profile.getId() + ": " + skill);
        }
        for (final String capability : profile.getMandatoryCapabilities()) {
            Assert.assertTrue(characterPlayer.hasCapability(capability, (String) null), profile.getId() + ": " + capability);
        }
        for (final String perk : profile.getMandatoryPerks()) {
            Assert.assertTrue(profile.getMandatoryPerks().stream().anyMatch(characterPlayer::hasPerk), profile.getId());
            break;
        }
        if (!profile.getMandatorySpecies().isEmpty()) {
            Assert.assertTrue(profile.getMandatorySpecies().contains(characterPlayer.getSpecie().getId()), profile.getId());
        }
        if (profile.getMandatoryRestrictions() != null) {
            Assert.assertFalse(profile.getMandatoryRestrictions().isRestricted(characterPlayer), profile.getId());
        }
    }
}
