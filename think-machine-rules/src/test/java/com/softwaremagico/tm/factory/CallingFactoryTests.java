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
import com.softwaremagico.tm.character.callings.CallingFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"callingFactory"})
public class CallingFactoryTests {
    private static final int DEFINED_CALLINGS = 52;

    @Test
    public void checkTotalElements() throws InvalidXmlElementException {
        Assert.assertEquals(CallingFactory.getInstance().getElements().size(),
                DEFINED_CALLINGS);
    }

    @Test
    public void getCapabilityOption() throws InvalidXmlElementException {
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getCapabilityOptions().size(), 2);
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getCapabilityOptions().get(1).getOptions().size(), 3);
    }

    @Test
    public void getCharacteristicOption() throws InvalidXmlElementException {
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getCharacteristicOptions().size(), 4);
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getCharacteristicOptions().get(0).getCharacteristicBonus("endurance").getBonus(), 1);
    }

    @Test
    public void getSkillOption() throws InvalidXmlElementException {
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getSkillOptions().size(), 7);
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getSkillOptions().get(0).getSkillBonus("academia").getBonus(), 1);
    }

    @Test
    public void getPerksOption() throws InvalidXmlElementException {
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getPerksOptions().size(), 1);
        Assert.assertEquals(CallingFactory.getInstance().getElement("commander").getPerksOptions().get(0).getOptions().size(), 48);
    }
}
