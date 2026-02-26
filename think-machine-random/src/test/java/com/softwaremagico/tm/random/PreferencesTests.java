package com.softwaremagico.tm.random;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.random.character.factions.RandomFaction;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.AlignmentPreference;
import com.softwaremagico.tm.random.preferences.RandomSelector;
import com.softwaremagico.tm.random.preferences.TechPreference;
import com.softwaremagico.tm.random.definition.ProbabilityMultiplier;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomCharacteristicBonusOption;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Test(groups = {"preferences"})
public class PreferencesTests {

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
    public void checkObunPreferences() {
        Assert.assertEquals(SpecieFactory.getInstance().getElement("obun").getRandomDefinition().getRecommendedPreferences().size(), 2);
    }

    @Test
    public void checkCharacteristicsPreferencesWeightsForPrimitive() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("noble");

        final RandomCharacteristicBonusOption randomCharacteristicBonusOption =
                new RandomCharacteristicBonusOption(characterPlayer, convert(TechPreference.PRIMITIVE),
                        characterPlayer.getUpbringing().getCharacteristicOptions().get(0));

        randomCharacteristicBonusOption.updateWeights();
        //3 characteristics plus the latest null value for selection.
        Assert.assertEquals(randomCharacteristicBonusOption.getWeightedElements().size(), 4);
        //Dexterity is recommended to faction.
        Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(0).intValue(), (int) ProbabilityMultiplier.NORMAL.getValue() * RandomSelector.BASIC_PROBABILITY);
        //Endurance is a preferred characteristic for primitive.
        Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(1).intValue(), (int) (ProbabilityMultiplier.NORMAL.getValue() + RandomSelector.USER_SELECTION_MULTIPLIER) * RandomSelector.BASIC_PROBABILITY);
        //Strength is a preferred characteristic for primitive.
        Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(2).intValue(), (int) (ProbabilityMultiplier.NORMAL.getValue() + RandomSelector.USER_SELECTION_MULTIPLIER) * RandomSelector.BASIC_PROBABILITY);
    }

    @Test
    public void checkCharacteristicsPreferencesWeightsForVorox() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("vorox");
        characterPlayer.setUpbringing("noble");

        final RandomCharacteristicBonusOption randomCharacteristicBonusOption =
                new RandomCharacteristicBonusOption(characterPlayer, convert(TechPreference.PRIMITIVE),
                        characterPlayer.getUpbringing().getCharacteristicOptions().get(0));

        randomCharacteristicBonusOption.updateWeights();
        //3 characteristics plus the latest null value for selection.
        Assert.assertEquals(randomCharacteristicBonusOption.getWeightedElements().size(), 4);
        //Dexterity is recommended to faction.
        Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(0).intValue(), (int) ProbabilityMultiplier.NORMAL.getValue() * RandomSelector.BASIC_PROBABILITY);
        //Endurance is a preferred characteristic for primitive.
        Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(1).intValue(), (int) (ProbabilityMultiplier.NORMAL.getValue() + RandomSelector.USER_SELECTION_MULTIPLIER * 2) * RandomSelector.BASIC_PROBABILITY);
        //Strength is a preferred characteristic for primitive.
        Assert.assertEquals(randomCharacteristicBonusOption.getAssignedWeight(2).intValue(), (int) (ProbabilityMultiplier.NORMAL.getValue() + RandomSelector.USER_SELECTION_MULTIPLIER * 2) * RandomSelector.BASIC_PROBABILITY);
    }

    @Test
    public void checkFactionPreferencesWeightsForEvil() throws InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.setUpbringing("merchant");
        characterPlayer.setFaction("musters");
        final RandomFaction randomFaction = new RandomFaction(characterPlayer, convert(AlignmentPreference.EVIL));
        randomFaction.updateWeights();
        //Musters has a plus for evil preference.
        final Faction musters = FactionFactory.getInstance().getElement("musters");
        Assert.assertEquals(randomFaction.getElementWeight(musters),
                (int) (musters.getRandomDefinition().getProbabilityMultiplier().getValue() + RandomSelector.USER_SELECTION_MULTIPLIER) * RandomSelector.BASIC_PROBABILITY);

        final Faction reeves = FactionFactory.getInstance().getElement("reeves");
        Assert.assertEquals(randomFaction.getElementWeight(reeves),
                (int) (musters.getRandomDefinition().getProbabilityMultiplier().getValue()) * RandomSelector.BASIC_PROBABILITY);
    }
}
