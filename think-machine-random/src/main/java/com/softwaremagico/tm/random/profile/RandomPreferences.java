package com.softwaremagico.tm.random.profile;

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

import com.softwaremagico.tm.random.preferences.IRandomPreference;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps random preferences and profiles as distinct inputs while allowing both
 * to travel through the existing selector hierarchy.
 */
public class RandomPreferences extends HashSet<IRandomPreference> {
    private final Set<RandomProfile> profiles;

    public RandomPreferences(Collection<IRandomPreference> preferences, Collection<RandomProfile> profiles) {
        super(preferences);
        this.profiles = new HashSet<>(profiles);
    }

    public Set<RandomProfile> getProfiles() {
        return this.profiles;
    }
}
