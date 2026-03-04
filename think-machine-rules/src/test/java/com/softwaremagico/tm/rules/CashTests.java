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
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

@Test(groups = "cash")
public class CashTests {

    @Test
    public void testCash1000OnPerk() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleHawkwoodCommander();
        final CharacterPerkOptions selectedElement = characterPlayer.getUpbringing().getNotSelectedPerksOptions(true).get(1);
        characterPlayer.getUpbringing().getSelectedPerksOptions().get(1).getSelections().clear();
        characterPlayer.getUpbringing().getSelectedPerksOptions().get(1).getSelections().add(
                selectedElement.getAvailableSelections().stream().filter(s -> Objects.equals(s.getId(), "cash1000")).findFirst().orElse(null));
        Assert.assertEquals(characterPlayer.getCashMoney(), 1000d);
    }


    @Test
    public void testCash2000OnPerkWithLevel2() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleHawkwoodCommander();
        CharacterPerkOptions selectedElement = characterPlayer.getUpbringing().getNotSelectedPerksOptions(true).get(1);
        characterPlayer.getUpbringing().getSelectedPerksOptions().get(1).getSelections().clear();
        characterPlayer.getUpbringing().getSelectedPerksOptions().get(1).getSelections().add(
                selectedElement.getAvailableSelections().stream().filter(s -> Objects.equals(s.getId(), "cash1000")).findFirst().orElse(null));

        characterPlayer.addLevel();
        selectedElement = characterPlayer.getCalling().getNotSelectedPerksOptions(true).get(0);
        characterPlayer.getLatestLevel().getSelectedCallingPerksOptions().get(0).getSelections().add(
                selectedElement.getAvailableSelections().stream().filter(s -> Objects.equals(s.getId(), "cash2000")).findFirst().orElse(null));

        Assert.assertEquals(characterPlayer.getCashMoney(), 2000d);
    }

    @Test
    public void testCash300AfterPurchasingItem() {
        final CharacterPlayer characterPlayer = CharacterExamples.generateHumanNobleHawkwoodCommander();
        final CharacterPerkOptions selectedElement = characterPlayer.getUpbringing().getNotSelectedPerksOptions(true).get(1);
        characterPlayer.getUpbringing().getSelectedPerksOptions().get(1).getSelections().clear();
        characterPlayer.getUpbringing().getSelectedPerksOptions().get(1).getSelections().add(
                selectedElement.getAvailableSelections().stream().filter(s -> Objects.equals(s.getId(), "cash1000")).findFirst().orElse(null));

        //SOE Alembic costs 700 firebirds.
        characterPlayer.addEquipmentPurchased(WeaponFactory.getInstance().getElement("soeAlembic"));

        Assert.assertEquals(characterPlayer.getCashMoney(), 1000d);
        Assert.assertEquals(characterPlayer.getRemainingCash(), 300d);
    }
}
