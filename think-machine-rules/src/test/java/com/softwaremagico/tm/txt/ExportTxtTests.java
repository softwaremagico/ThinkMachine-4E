package com.softwaremagico.tm.txt;

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


import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.CharacterExamples;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.character.cybernetics.CyberdeviceFactory;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.perks.PerkFactory;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.language.Translator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Test(groups = {"exportTxt"})
public class ExportTxtTests {
    private static final String LANGUAGE = "es";

    /**
     * Expected texts were generated with this specific set of modules, so it must be forced before the tests
     * are executed. Otherwise the result depends on the execution order of the suite.
     */
    @BeforeClass(alwaysRun = true)
    public void enableBasicModule() {
        ModuleManager.disableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.LOST_WORLDS_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        ModuleManager.resetModules();
    }

    @Test
    public void checkDecadosCommanderCharacter() throws URISyntaxException, IOException {
        Translator.setLanguage(LANGUAGE);
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final CharacterSheet characterSheet = new CharacterSheet(player);

        final String text = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                .getResource("DecadosCommander.txt").toURI())));
        Assert.assertEquals(characterSheet.toString().trim(), text.trim());
    }

    @Test
    public void checkHumanHawkwoodCommanderCharacter() throws URISyntaxException, IOException {
        Translator.setLanguage(LANGUAGE);
        final CharacterPlayer player = CharacterExamples.generateHumanNobleHawkwoodCommander();
        final CharacterSheet characterSheet = new CharacterSheet(player);

        final String text = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                .getResource("HawkwoodCommander.txt").toURI())));
        Assert.assertEquals(characterSheet.toString().trim(), text.trim());
    }

    @Test
    public void checkCallingCombinationIsShownOnTxtSheet() {
        Translator.setLanguage("EN");
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        player.addLevel().setCalling("conspiracist");
        CharacterExamples.populateLevel(player);

        final CharacterSheet characterSheet = new CharacterSheet(player);
        Assert.assertTrue(characterSheet.toString().contains("Commander / Conspiracist"));
    }

    @Test
    public void checkLevel2CallingChangeIsShownOnTxtSheet() {
        Translator.setLanguage("EN");
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        player.addLevel().setCalling("conspiracist");
        CharacterExamples.populateLevel(player);

        Assert.assertEquals(player.getLevel(), 2);
        Assert.assertEquals(player.getCallingCombinationRepresentation(" / "), "Commander / Conspiracist");

        final CharacterSheet characterSheet = new CharacterSheet(player);
        Assert.assertTrue(characterSheet.toString().contains("Commander / Conspiracist"));
    }

    @Test
    public void ignoresWeaponWithoutDamageDefinitionOnTxtSheet() {
        Translator.setLanguage("EN");
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final Weapon invalidWeapon = new Weapon();
        invalidWeapon.setId("invalidWeapon");
        invalidWeapon.setName(new TranslatedText("Invalid Weapon"));
        player.addEquipmentPurchased(invalidWeapon);

        final CharacterSheet characterSheet = new CharacterSheet(player);
        Assert.assertNotNull(characterSheet.toString());
        Assert.assertTrue(characterSheet.toString().contains(player.getCompleteNameRepresentation()));
    }

    @Test
    public void exportsCharacterDetailsCapabilitiesPerksAndBlessings() {
        Translator.setLanguage("EN");
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        final String text = new CharacterSheet(player).toString();

        Assert.assertTrue(text.contains(player.getCompleteNameRepresentation()));
        Assert.assertTrue(text.contains(player.getSpecie().getNameRepresentation()));
        Assert.assertTrue(text.contains(player.getUpbringing().getNameRepresentation()));
        Assert.assertTrue(text.contains(player.getFaction().getNameRepresentation()));
        Assert.assertTrue(text.contains(player.getCalling().getNameRepresentation()));
        Assert.assertFalse(player.getCapabilitiesWithSpecialization().isEmpty());
        Assert.assertFalse(player.getPerks().isEmpty());
        Assert.assertTrue(text.contains("Capabilities:"));
        Assert.assertTrue(text.contains("Perks:"));
        Assert.assertTrue(text.contains("Blessings:"));
    }

    @Test
    public void exportsOccultismEquipmentAndCyberdevices() throws Exception {
        Translator.setLanguage("EN");
        final CharacterPlayer player = CharacterExamples.generateHumanNobleDecadosCommander();
        player.addEquipmentPurchased(ItemFactory.getInstance().getElement("multitool"));
        player.setCalling("psychic");
        player.getCalling().getSelectedPerksOptions().get(0).getSelections()
                .add(new Selection(CyberdeviceFactory.getInstance().getElement("advisor")));
        player.getCalling().getSelectedPerksOptions().get(0).getSelections()
                .add(new Selection(PerkFactory.getInstance().getElement(PerkFactory.PSYCHIC_POWERS_PERK)));
        player.addOccultismPower(OccultismPathFactory.getInstance().getElement("farHand")
                .getOccultismPowers().get("liftingHand"));

        final String text = new CharacterSheet(player).toString();

        Assert.assertTrue(text.contains("Occultism:"));
        Assert.assertTrue(text.contains("Occultism Powers:"));
        Assert.assertTrue(text.contains(WeaponFactory.getInstance().getElement("soeAlembic").getName().getTranslatedText()));
        Assert.assertTrue(text.contains(ArmorFactory.getInstance().getElement("synthsilk").getName().getTranslatedText()));
        Assert.assertTrue(text.contains(ShieldFactory.getInstance().getElement("duelingShield").getName().getTranslatedText()));
        Assert.assertTrue(text.contains(ItemFactory.getInstance().getElement("multitool").getName().getTranslatedText()));
        Assert.assertTrue(text.contains(CyberdeviceFactory.getInstance().getElement("advisor").getName().getTranslatedText()));
    }
}
