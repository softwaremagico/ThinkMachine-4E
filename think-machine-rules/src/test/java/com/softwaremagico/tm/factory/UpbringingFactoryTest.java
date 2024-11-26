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

import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"upbringingFactory"})
public class UpbringingFactoryTest {
    private static final int DEFINED_UPBRINGINGS= 5;


    @Test
    public void checkTotalElements() throws InvalidXmlElementException {
        Assert.assertEquals(UpbringingFactory.getInstance().getElements().size(),
                DEFINED_UPBRINGINGS);
    }

    @Test
    public void getUpbringingOption() throws InvalidXmlElementException {
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getCapabilityOptions().size(), 6);
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getCapabilityOptions().get(0).getOptions().size(), 7);
    }

    @Test
    public void getCharacteristicOption() throws InvalidXmlElementException {
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getCharacteristicOptions().size(), 4);
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getCharacteristicOptions().get(2).getCharacteristicBonus("presence").getBonus(), 2);
    }

    @Test
    public void getSkillOption() throws InvalidXmlElementException {
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getSkillOptions().size(), 4);
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getSkillOptions().get(0).getSkillBonus("academia").getBonus(), 1);
    }

    @Test
    public void getPerksOption() throws InvalidXmlElementException {
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getPerksOptions().size(), 2);
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getPerksOptions().get(1).getOptions().size(), 27);
    }

    @Test
    public void getAnyOption() throws InvalidXmlElementException {
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("yeoman").getSkillOptions().size(), 5);
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("yeoman").getCharacteristicOptions().size(), 5);
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("yeoman").getCapabilityOptions().size(), 6);
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("yeoman").getCapabilityOptions().get(5).getOptions().size(), CapabilityFactoryTest.TOTAL_ELEMENTS);
    }
}
