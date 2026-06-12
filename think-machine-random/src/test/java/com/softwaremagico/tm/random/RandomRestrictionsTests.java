package com.softwaremagico.tm.random;

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
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.random.character.factions.RandomFaction;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.AlignmentPreference;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Test(groups = {"random"})
public class RandomRestrictionsTests {

    private Set<IRandomPreference> convert(IRandomPreference... preferences) {
        if (preferences != null) {
            final List<IRandomPreference> customPreferences = Arrays.asList(preferences);
            customPreferences.removeIf(Objects::isNull);
            return new HashSet<>(customPreferences);
        } else {
            return new HashSet<>();
        }
    }

    @Test
    public void upbringingLimitateFaction() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");

        final RandomFaction randomFaction = new RandomFaction(characterPlayer, convert(AlignmentPreference.EVIL));
        randomFaction.updateWeights();
        //Musters has a plus for evil preference.
        final Faction musters = FactionFactory.getInstance().getElement("musters");
        Assert.assertEquals(randomFaction.getElementWeight(musters), 0);
    }
}
