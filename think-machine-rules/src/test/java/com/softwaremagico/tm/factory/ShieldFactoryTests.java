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

import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.definition.ProbabilityMultiplier;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"shieldFactory"})
public class ShieldFactoryTests {
    private static final int DEFINED_SHIELDS = 5;

    @Test
    public void readShields() throws InvalidXmlElementException {
        Assert.assertEquals(ShieldFactory.getInstance().getElements().size(), DEFINED_SHIELDS);
    }

    @Test
    public void getShieldValues() throws InvalidXmlElementException {
        Assert.assertEquals(ShieldFactory.getInstance().getElement("duelingShield").getImpact(), 5);
        Assert.assertEquals(ShieldFactory.getInstance().getElement("duelingShield").getForce(), 10);
        Assert.assertEquals(ShieldFactory.getInstance().getElement("duelingShield").getHits(), 15);
    }

    @Test
    public void getShieldSize() throws InvalidXmlElementException {
        Assert.assertEquals(ShieldFactory.getInstance().getElement("antiqueShield").getSize(), Size.S);
    }

    @Test
    public void getShieldProbability() throws InvalidXmlElementException {
        Assert.assertEquals(ShieldFactory.getInstance().getElement("antiqueShield").getRandomDefinition().getProbabilityMultiplier(), ProbabilityMultiplier.RARE);
    }
}
