package com.softwaremagico.tm.random.preferences;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
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

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;

public class PowerLevelPreferenceTest {

    @DataProvider
    public Object[][] powerLevels() {
        return new Object[][]{
                {PowerLevelPreference.LOW, 1, 3},
                {PowerLevelPreference.STANDARD, 2, 5},
                {PowerLevelPreference.VETERAN, 5, 8},
                {PowerLevelPreference.ELITE, 9, 15}
        };
    }

    @Test(dataProvider = "powerLevels")
    public void randomGaussianStaysWithinConfiguredRange(PowerLevelPreference preference, int minimum, int maximum) {
        for (int i = 0; i < 100; i++) {
            final int level = preference.randomGaussian();

            Assert.assertTrue(level >= minimum && level <= maximum);
        }
    }

    @Test
    public void selectsConfiguredPowerLevelPreference() {
        Assert.assertEquals(PowerLevelPreference.getSelected(Collections.singleton(PowerLevelPreference.ELITE)),
                PowerLevelPreference.ELITE);
    }

    @Test
    public void returnsNullWhenNoPowerLevelPreferenceIsConfigured() {
        Assert.assertNull(PowerLevelPreference.getSelected(null));
    }

    @Test
    public void parsesPowerLevelPreference() {
        Assert.assertEquals(IRandomPreference.valueOf("VETERAN"), PowerLevelPreference.VETERAN);
    }
}
