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

import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.occultism.TheurgyComponentFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.definition.ElementClassification;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"occultismFactory"})
public class OccultismFactoryTests {
    private static final int DEFINED_PSI_PATHS = 6;
    private static final int DEFINED_THEURGY_PATHS = 6;
    private static final int DEFINED_THEURGY_COMPONENTS = 3;
    private static final int OCCULTISM_TYPES = 2;

    @Test
    public void readPaths() throws InvalidXmlElementException {
        Assert.assertEquals(OccultismPathFactory.getInstance().getElements().size(),
                DEFINED_PSI_PATHS + DEFINED_THEURGY_PATHS);
    }

    @Test
    public void readPsiPaths() {
        Assert.assertEquals(OccultismPathFactory.getInstance().getPsiPaths().size(), DEFINED_PSI_PATHS);
    }

    @Test
    public void readPsiPower() {
        final OccultismPower occultismPower = OccultismPathFactory.getInstance().getPsiPaths().get(0).getOccultismPowersElements().get(0);
        Assert.assertEquals(occultismPower.getSkills().get(0), "focus");
        Assert.assertEquals(occultismPower.getCharacteristic(), "wits");
        Assert.assertNotNull(occultismPower.getImpact());
        Assert.assertNotNull(occultismPower.getTime());
        Assert.assertNotNull(occultismPower.getCost());
        Assert.assertNotNull(occultismPower.getResistance());
        Assert.assertEquals(occultismPower.getOccultismLevel(), 1);
    }

    @Test
    public void readTheurgyPaths() {
        Assert.assertEquals(OccultismPathFactory.getInstance().getTheurgyPaths()
                .size(), DEFINED_THEURGY_PATHS);
    }

    @Test
    public void readTheurgyComponents() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_THEURGY_COMPONENTS,
                TheurgyComponentFactory.getInstance().getElements().size());
    }

    @Test
    public void getOccultismTypes() throws InvalidXmlElementException {
        Assert.assertEquals(OCCULTISM_TYPES, OccultismTypeFactory.getInstance().getElements().size());
    }

    @Test
    public void checkRestrictions() throws InvalidXmlElementException {
        final OccultismPower occultismPower = OccultismPathFactory.getInstance().getPsiPaths().get(0).getOccultismPowersElements().get(0);
        Assert.assertEquals(occultismPower.getRestrictions().getRestrictedToSpecies().size(), 2);
        Assert.assertEquals(occultismPower.getRestrictions().getRestrictedToCallings().size(), 2);
    }

    @Test(enabled = false)
    public void getClassifications() throws InvalidXmlElementException {
        Assert.assertEquals(ElementClassification.ENHANCEMENT,
                OccultismPathFactory.getInstance().getElement("sixthSense").getRandomDefinition().getClassification());
        Assert.assertEquals(ElementClassification.COMBAT,
                OccultismPathFactory.getInstance().getElement("soma").getRandomDefinition().getClassification());
        Assert.assertEquals(ElementClassification.OTHERS,
                OccultismPathFactory.getInstance().getElement("sympathy").getRandomDefinition().getClassification());
        Assert.assertEquals(ElementClassification.ALTERATION,
                OccultismPathFactory.getInstance().getElement("templeAvestiRituals").getRandomDefinition()
                        .getClassification());
    }


    @Test(enabled = false)
    public void checkNonOfficialPaths() throws InvalidXmlElementException {
        Assert.assertFalse(OccultismPathFactory.getInstance()
                .getElement("bedlam").getOccultismPowers()
                .get("prana").isOfficial());
    }
}
