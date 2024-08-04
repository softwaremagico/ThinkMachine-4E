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
import com.softwaremagico.tm.character.callings.CallingFactory;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"upbringingFactory"})
public class UpbringingFactoryTest {
    private static final int DEFINED_UPRISINGS = 1;

    @Test
    public void checkTotalElements() throws InvalidXmlElementException {
        Assert.assertEquals(UpbringingFactory.getInstance().getElements().size(),
                DEFINED_UPRISINGS);
    }

    @Test
    public void getUpbringingOption() throws InvalidXmlElementException {
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getCapabilityOptions().size(), 6);
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getCapabilityOptions().get(0).getCapabilities().size(), 3);
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
        Assert.assertEquals(UpbringingFactory.getInstance().getElement("noble").getPerksOptions().get(1).getPerks().size(), 29);
    }
}
