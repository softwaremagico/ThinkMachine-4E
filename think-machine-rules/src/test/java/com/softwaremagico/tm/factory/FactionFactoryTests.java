package com.softwaremagico.tm.factory;

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


import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.equipment.TechCompulsionFactory;
import com.softwaremagico.tm.character.equipment.item.Quality;
import com.softwaremagico.tm.character.equipment.item.Status;
import com.softwaremagico.tm.character.equipment.weapons.CustomizedWeapon;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.file.modules.ModuleManager;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"factionsFactory"})
public class FactionFactoryTests {
    private static final int DEFINED_FACTIONS_FS_4E = 20;
    private static final int DEFINED_FACTIONS_FACTION_BOOK = 37;
    private static final int DEFINED_MALE_NAMES = 103;
    private static final int DEFINED_FEMALE_NAMES = 100;
    private static final int DEFINED_SURNAMES = 125;


    @Test
    public void checkTotalElements() throws InvalidXmlElementException {
        Assert.assertEquals(FactionFactory.getInstance().getElements().size(),
                DEFINED_FACTIONS_FS_4E + DEFINED_FACTIONS_FACTION_BOOK);
    }

    @Test(dependsOnMethods = "checkTotalElements")
    public void checkTotalElementsOnlyOnFactionBook() throws InvalidXmlElementException {
        ModuleManager.enableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.disableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        FactionFactory.getInstance().reset();
        Assert.assertEquals(FactionFactory.getInstance().getElements().size(),
                DEFINED_FACTIONS_FACTION_BOOK);
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFactionBook")
    public void checkTotalElementsOnlyOnFS4E() throws InvalidXmlElementException {
        ModuleManager.disableModule(ModuleManager.FACTION_BOOK_MODULE);
        ModuleManager.enableModule(ModuleManager.FADING_SUNS_PLAYER_GUIDE_MODULE);
        FactionFactory.getInstance().reset();
        Assert.assertEquals(FactionFactory.getInstance().getElements().size(),
                DEFINED_FACTIONS_FS_4E);
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void checkBlessing() throws InvalidXmlElementException {
        final Faction alMalik = FactionFactory.getInstance().getElement("alMalik");
        Assert.assertNotNull(alMalik.getBlessing());
        Assert.assertEquals(alMalik.getBlessing().getId(), "extrovert");
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void checkCurse() throws InvalidXmlElementException {
        final Faction alMalik = FactionFactory.getInstance().getElement("alMalik");
        Assert.assertNotNull(alMalik.getCurse());
        Assert.assertEquals(alMalik.getCurse().getId(), "impetuous");
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void checkFavoredCallings() throws InvalidXmlElementException {
        final Faction alMalik = FactionFactory.getInstance().getElement("alMalik");
        Assert.assertTrue(alMalik.getFavoredCallings().contains("enthusiast"));
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void checkRestriction() throws InvalidXmlElementException {
        final Faction brotherBattle = FactionFactory.getInstance().getElement("brotherBattle");
        Assert.assertNotNull(brotherBattle);
        Assert.assertFalse(brotherBattle.getRestrictions().getRestrictedToUpbringing().isEmpty());
    }


    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void readNames() throws InvalidXmlElementException {
        final Faction hazat = FactionFactory.getInstance().getElement("hazat");
        Assert.assertNotNull(hazat);
        Assert.assertTrue(FactionFactory.getInstance().getAllNames("hazat", Gender.MALE).size() >= DEFINED_MALE_NAMES);
        Assert.assertTrue(
                FactionFactory.getInstance().getAllNames("hazat", Gender.FEMALE).size() >= DEFINED_FEMALE_NAMES);
        Assert.assertTrue(FactionFactory.getInstance().getAllSurnames("hazat").size() >= DEFINED_SURNAMES);
    }


    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void readCustomizedWeapon() throws InvalidXmlElementException {
        final Faction hawkwood = FactionFactory.getInstance().getElement("hawkwood");
        Assert.assertNotNull(hawkwood);
        Assert.assertEquals(hawkwood.getMaterialAwards().size(), 1);
        Assert.assertEquals(hawkwood.getMaterialAwards().get(0).getOptions().size(), 6);
        Assert.assertTrue(hawkwood.getMaterialAwards().get(0).getOptions().iterator().next().getElement() instanceof CustomizedWeapon);
        Assert.assertEquals(hawkwood.getMaterialAwards().get(0).getOptions().iterator().next().getElement().getQuality(), Quality.PREMIUM);
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void readBlessedWeapon() throws InvalidXmlElementException {
        final Faction brotherBattle = FactionFactory.getInstance().getElement("brotherBattle");
        Assert.assertNotNull(brotherBattle);
        Assert.assertEquals(brotherBattle.getMaterialAwards().size(), 1);
        Assert.assertEquals(brotherBattle.getMaterialAwards().get(0).getOptions().size(), 79);
        Assert.assertTrue(brotherBattle.getMaterialAwards().get(0).getOptions().iterator().next().getElement() instanceof CustomizedWeapon);
        Assert.assertEquals(brotherBattle.getMaterialAwards().get(0).getOptions().iterator().next().getElement().getStatus(), Status.BLESSED);
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void anySpecialization() throws InvalidXmlElementException {
        final Faction charioteers = FactionFactory.getInstance().getElement("charioteers");
        Assert.assertNotNull(charioteers);
        Assert.assertEquals(charioteers.getCapabilityOptions().size(), 2);
        //Known Worlds or Barbarian Worlds
        Assert.assertEquals(charioteers.getCapabilityOptions().get(1).getOptions().size(), 7);
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void itemTechConvulsion() throws InvalidXmlElementException {
        final Faction engineers = FactionFactory.getInstance().getElement("engineers");
        Assert.assertNotNull(engineers);
        Assert.assertEquals(engineers.getMaterialAwards().get(0).getOptions().iterator().next().getElement().getTechCompulsion(), "industrious");
        Assert.assertNotNull(TechCompulsionFactory.getInstance().getElement("industrious"));
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void getFenixQuantity() throws InvalidXmlElementException {
        final Faction vagabonds = FactionFactory.getInstance().getElement("vagabonds");
        Assert.assertNotNull(vagabonds);
        Assert.assertEquals(vagabonds.getMaterialAwards().get(0).getOptions().iterator().next().getQuantity(), 100);
    }

    @Test(dependsOnMethods = "checkTotalElementsOnlyOnFS4E")
    public void getWeaponQuality() throws InvalidXmlElementException {
        final Faction vuldrok = FactionFactory.getInstance().getElement("vuldrok");
        Assert.assertNotNull(vuldrok);
        Assert.assertTrue(vuldrok.getMaterialAwards().get(0).getOptions().stream()
                .anyMatch(equipmentOption -> equipmentOption.getElement() instanceof CustomizedWeapon));
    }
}
