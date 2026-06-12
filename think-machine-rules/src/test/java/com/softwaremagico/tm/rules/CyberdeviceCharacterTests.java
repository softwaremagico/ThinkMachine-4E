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
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.file.modules.ModuleManager;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Test(groups = "cyberdeviceRules")
public class CyberdeviceCharacterTests {

    @Test
    public void shelitCyborgShouldOfferFactionBookCyberdevices() {
        enableAllModules();
        final CharacterPlayer characterPlayer = createShelitCyborg();

        final Set<String> availableSelections = characterPlayer.getCalling().getNotSelectedPerksOptions(true).get(0)
                .getAvailableSelections().stream().map(Selection::getId).collect(Collectors.toSet());

        Assert.assertTrue(availableSelections.contains("pandoranDataInterface"));
        Assert.assertTrue(availableSelections.contains("smithShadowEye"));
        Assert.assertTrue(availableSelections.contains("ozymandiasDeepLung"));
    }

    @Test(dependsOnMethods = "shelitCyborgShouldOfferFactionBookCyberdevices")
    public void shelitCyborgCharacterCanInstallSeveralFactionBookCyberdevices() {
        enableAllModules();
        final CharacterPlayer characterPlayer = createShelitCyborg();

        replaceSelection(characterPlayer.getCalling().getSelectedPerksOptions().get(0),
                characterPlayer.getCalling().getNotSelectedPerksOptions(true).get(0), "pandoranDataInterface");

        characterPlayer.addLevel();
        CharacterExamples.populateLevel(characterPlayer);
        replaceSelection(characterPlayer.getLatestLevel().getSelectedCallingPerksOptions().get(0),
                characterPlayer.getLatestLevel().getNotRepeatedCallingPerksOptions().get(0), "smithShadowEye");

        characterPlayer.addLevel();
        CharacterExamples.populateLevel(characterPlayer);
        replaceSelection(characterPlayer.getLatestLevel().getSelectedCallingPerksOptions().get(0),
                characterPlayer.getLatestLevel().getNotRepeatedCallingPerksOptions().get(0), "ozymandiasDeepLung");

        characterPlayer.validate();

        final Set<String> cyberdeviceIds = characterPlayer.getCyberdevices().stream().map(Cyberdevice::getId)
                .collect(Collectors.toSet());

        Assert.assertEquals(cyberdeviceIds, Set.of("pandoranDataInterface", "smithShadowEye", "ozymandiasDeepLung"));
        Assert.assertEquals(characterPlayer.getCyberdevices().size(), 3);
        Assert.assertTrue(characterPlayer.getCyberdevices().stream()
                .anyMatch(cyberdevice -> Objects.equals(cyberdevice.getId(), "pandoranDataInterface")
                        && Objects.equals(cyberdevice.getTechCompulsion(), "inerrant")));
        Assert.assertTrue(characterPlayer.getCyberdevices().stream()
                .anyMatch(cyberdevice -> Objects.equals(cyberdevice.getId(), "smithShadowEye")
                        && Objects.equals(cyberdevice.getTechLevel(), 5)));
        Assert.assertTrue(characterPlayer.getCyberdevices().stream()
                .anyMatch(cyberdevice -> Objects.equals(cyberdevice.getId(), "ozymandiasDeepLung")
                        && Objects.equals(cyberdevice.getTechCompulsion(), "industrious")));
    }

    private CharacterPlayer createShelitCyborg() {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.setSpecie("human");
        characterPlayer.getInfo().setPlanet("nowhere");
        characterPlayer.getInfo().setGender(Gender.FEMALE);
        characterPlayer.setUpbringing("noble");
        characterPlayer.setFaction("shelit");
        characterPlayer.setCalling("cyborg");

        CharacterExamples.populateCharacter(characterPlayer);
        return characterPlayer;
    }

    private void replaceSelection(com.softwaremagico.tm.character.CharacterSelectedElement selectedPerks,
                                  CharacterPerkOptions availableOptions, String selectionId) {
        final Selection selection = availableOptions.getAvailableSelections().stream()
                .filter(availableSelection -> Objects.equals(availableSelection.getId(), selectionId))
                .findFirst().orElse(null);

        Assert.assertNotNull(selection, "Expected selection '" + selectionId + "' to be available.");
        selectedPerks.getSelections().clear();
        selectedPerks.getSelections().add(selection);
    }

    private void enableAllModules() {
        ModuleManager.enableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.resetModules();
    }

    @AfterClass(alwaysRun = true)
    public void restoreBasicModule() {
        ModuleManager.disableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.resetModules();
    }
}


