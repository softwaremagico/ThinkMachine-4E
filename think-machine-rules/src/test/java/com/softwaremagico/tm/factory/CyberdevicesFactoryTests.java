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


import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.cybernetics.CyberdeviceFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"cyberdevicesFactory"})
public class CyberdevicesFactoryTests {
    private static final int DEFINED_CYBERDEVICES = 5;


    @Test
    public void readCyberdevicesStyles() throws InvalidXmlElementException {
        Assert.assertEquals(CyberdeviceFactory.getInstance().getElements().size(), DEFINED_CYBERDEVICES);
    }
}
