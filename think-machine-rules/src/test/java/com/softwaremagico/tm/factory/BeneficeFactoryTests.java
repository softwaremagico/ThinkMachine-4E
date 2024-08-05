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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

@Test(groups = {"beneficeFactory"})
public class BeneficeFactoryTests {
    private static final int DEFINED_BENEFICES = 89;
    private static final int AVAILABLE_BENEFICES = 263;

    @Test
    public void readBenefices() throws InvalidXmlElementException {
        Assert.assertEquals(BeneficeDefinitionFactory.getInstance().getElements()
                .size(), DEFINED_BENEFICES);
    }

    @Test
    public void getCalculatedBenefices() throws InvalidXmlElementException {
        Assert.assertEquals(AvailableBeneficeFactory.getInstance().getElements().size(), AVAILABLE_BENEFICES);
    }

    @Test
    public void getBeneficesClassification() throws InvalidXmlElementException {
        Assert.assertEquals(AvailableBeneficeFactory.getInstance()
                .getAvailableBeneficesByDefinition().keySet().size(), DEFINED_BENEFICES);
        int count = 0;
        for (final Set<AvailableBenefice> benefices : AvailableBeneficeFactory.getInstance()
                .getAvailableBeneficesByDefinition().values()) {
            count += benefices.size();
        }
        Assert.assertEquals(count, AVAILABLE_BENEFICES);
    }

    @Test
    public void getBeneficeSpecialization() throws InvalidXmlElementException {
        Assert.assertNotNull(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds250]"));
    }

    @Test
    public void checkDescription() throws InvalidXmlElementException {
        Assert.assertEquals("The character is not able to perform fine manipulation actions.",
                AvailableBeneficeFactory.getInstance().getElement("noFineManipulation").getDescription().getTranslatedText());
    }

    @Test
    public void getRestrictedSpecies() throws InvalidXmlElementException {
        Assert.assertTrue(BeneficeDefinitionFactory.getInstance().getElement("prominentFamily")
                .getRestrictions().getRestrictedToSpecies().contains("gannok"));
    }

//    @Test
//    public void getSpecializationRestrictedFactions() throws InvalidXmlElementException {
//        Assert.assertTrue(AvailableBeneficeFactory.getInstance().getElement("language [vagabondPatois]")
//                .getRestrictions().getRestrictedToFactions().contains("vagabonds"));
//    }

}
