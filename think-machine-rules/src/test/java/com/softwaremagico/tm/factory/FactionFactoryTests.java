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


import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.factions.random.RandomFactionFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"factionsFactory"})
public class FactionFactoryTests {
    private static final int DEFINED_FACTIONS = 1;
    private static final int DEFINED_MALE_NAMES = 103;
    private static final int DEFINED_FEMALE_NAMES = 100;
    private static final int DEFINED_SURNAMES = 125;


    @Test
    public void checkTotalElements() throws InvalidXmlElementException {
        Assert.assertEquals(FactionFactory.getInstance().getElements().size(),
                DEFINED_FACTIONS);
    }


    @Test
    public void readNames() throws InvalidXmlElementException {
        final Faction hazat = FactionFactory.getInstance().getElements("hazat");
        Assert.assertNotNull(hazat);
        Assert.assertTrue(RandomFactionFactory.getInstance().getElements("hazat").getNames(Gender.MALE).size() >= DEFINED_MALE_NAMES);
        Assert.assertTrue(
                RandomFactionFactory.getInstance().getElements("hazat").getNames(Gender.FEMALE).size() >= DEFINED_FEMALE_NAMES);
        Assert.assertTrue(RandomFactionFactory.getInstance().getElements("hazat").getData().getSurnames().size() >= DEFINED_SURNAMES);
    }

    @Test
    public void checkSuggestedBenefices() throws InvalidXmlElementException {
        final Faction obun = FactionFactory.getInstance().getElements("obun");
        //TODO(softwaremagico): complete this.
    }

//    @Test
//    public void checkRestrictedBenefices() throws InvalidXmlElementException {
//        final Faction amaltheans = FactionFactory.getInstance().getElement("amaltheans");
//        Assert.assertEquals(amaltheans.getRestrictedBenefices().size(), 1);
//        Assert.assertEquals(amaltheans.getRestrictedBenefices().iterator().next().getId(), "cash");
//        Assert.assertEquals((int) amaltheans.getRestrictedBenefices().iterator().next().getMaxValue(), 8);
//    }
}
