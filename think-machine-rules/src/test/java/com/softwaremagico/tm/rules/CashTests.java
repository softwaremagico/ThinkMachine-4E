package com.softwaremagico.tm.rules;

import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

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
