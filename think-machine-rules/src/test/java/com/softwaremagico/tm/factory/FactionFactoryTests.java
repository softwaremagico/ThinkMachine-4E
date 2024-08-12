package com.softwaremagico.tm.factory;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2024 Softwaremagico
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
import com.softwaremagico.tm.character.equipment.item.handheldshield.CustomizedHandheldShield;
import com.softwaremagico.tm.character.equipment.weapons.CustomizedWeapon;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.random.RandomFactionFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"factionsFactory"})
public class FactionFactoryTests {
    private static final int DEFINED_FACTIONS = 20;
    private static final int DEFINED_MALE_NAMES = 103;
    private static final int DEFINED_FEMALE_NAMES = 100;
    private static final int DEFINED_SURNAMES = 125;


    @Test
    public void checkTotalElements() throws InvalidXmlElementException {
        Assert.assertEquals(FactionFactory.getInstance().getElements().size(),
                DEFINED_FACTIONS);
    }

    @Test
    public void checkBlessing() throws InvalidXmlElementException {
        final Faction alMalik = FactionFactory.getInstance().getElement("alMalik");
        Assert.assertNotNull(alMalik.getBlessing());
        Assert.assertEquals(alMalik.getBlessing().getId(), "extrovert");
    }

    @Test
    public void checkCurse() throws InvalidXmlElementException {
        final Faction alMalik = FactionFactory.getInstance().getElement("alMalik");
        Assert.assertNotNull(alMalik.getCurse());
        Assert.assertEquals(alMalik.getCurse().getId(), "impetuous");
    }

    @Test
    public void checkFavoredCallings() throws InvalidXmlElementException {
        final Faction alMalik = FactionFactory.getInstance().getElement("alMalik");
        Assert.assertTrue(alMalik.getFavoredCallings().contains("enthusiast"));
    }


    @Test
    public void readNames() throws InvalidXmlElementException {
        final Faction hazat = FactionFactory.getInstance().getElement("hazat");
        Assert.assertNotNull(hazat);
        Assert.assertTrue(RandomFactionFactory.getInstance().getElement("hazat").getNames(Gender.MALE).size() >= DEFINED_MALE_NAMES);
        Assert.assertTrue(
                RandomFactionFactory.getInstance().getElement("hazat").getNames(Gender.FEMALE).size() >= DEFINED_FEMALE_NAMES);
        Assert.assertTrue(RandomFactionFactory.getInstance().getElement("hazat").getData().getSurnames().size() >= DEFINED_SURNAMES);
    }


    @Test
    public void readCustomizedWeapon() throws InvalidXmlElementException {
        final Faction hawkwood = FactionFactory.getInstance().getElement("hawkwood");
        Assert.assertNotNull(hawkwood);
        Assert.assertEquals(hawkwood.getMaterialAwards().size(), 1);
        Assert.assertEquals(hawkwood.getMaterialAwards().get(0).getItems().size(), 9);
        Assert.assertTrue(hawkwood.getMaterialAwards().get(0).getItems().get(0) instanceof CustomizedWeapon);
        Assert.assertEquals(((CustomizedWeapon) hawkwood.getMaterialAwards().get(0).getItems().get(0)).getQuality(), Quality.PREMIUM);
    }

    @Test
    public void readBlessedWeapon() throws InvalidXmlElementException {
        final Faction brotherBattle = FactionFactory.getInstance().getElement("brotherBattle");
        Assert.assertNotNull(brotherBattle);
        Assert.assertEquals(brotherBattle.getMaterialAwards().size(), 1);
        Assert.assertEquals(brotherBattle.getMaterialAwards().get(0).getItems().size(), 82);
        Assert.assertTrue(brotherBattle.getMaterialAwards().get(0).getItems().get(0) instanceof CustomizedWeapon);
        Assert.assertEquals(((CustomizedWeapon) brotherBattle.getMaterialAwards().get(0).getItems().get(0)).getStatus(), Status.BLESSED);
    }

    @Test
    public void anySpecialization() throws InvalidXmlElementException {
        final Faction charioteers = FactionFactory.getInstance().getElement("charioteers");
        Assert.assertNotNull(charioteers);
        Assert.assertEquals(charioteers.getCapabilityOptions().size(), 2);
        //Known Worlds or Barbarian Worlds
        Assert.assertEquals(charioteers.getCapabilityOptions().get(1).getCapabilities().size(), 7);
    }

    @Test
    public void itemTechConvulsion() throws InvalidXmlElementException {
        final Faction engineers = FactionFactory.getInstance().getElement("engineers");
        Assert.assertNotNull(engineers);
        Assert.assertEquals(engineers.getMaterialAwards().get(0).getItems().get(0).getTechCompulsion(), "industrious");
        Assert.assertNotNull(TechCompulsionFactory.getInstance().getElement("industrious"));
    }

    @Test
    public void getFenixQuantity() throws InvalidXmlElementException {
        final Faction vagabonds = FactionFactory.getInstance().getElement("vagabonds");
        Assert.assertNotNull(vagabonds);
        Assert.assertEquals(vagabonds.getMaterialAwards().get(0).getItems().get(0).getQuantity(), 100);
    }

    @Test
    public void getWeaponQuality() throws InvalidXmlElementException {
        final Faction vuldrok = FactionFactory.getInstance().getElement("vuldrok");
        Assert.assertNotNull(vuldrok);
        Assert.assertTrue(vuldrok.getMaterialAwards().get(0).getItems()
                .get(vuldrok.getMaterialAwards().get(0).getItems().size() - 1) instanceof CustomizedHandheldShield);
    }
}
