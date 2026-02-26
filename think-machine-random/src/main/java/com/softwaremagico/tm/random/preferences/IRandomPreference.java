package com.softwaremagico.tm.random.preferences;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
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

public interface IRandomPreference {

    String name();

    static IRandomPreference valueOf(String name) {
        try {
            return AffiliationPreference.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return AlignmentPreference.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return LegalStatusPreference.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return OccultismPreference.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return OperationalRolePreference.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return OriginPreference.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return TechPreference.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return RankValueAssignationPreference.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return WealthPreference.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        throw new IllegalArgumentException("Unknown enum value: " + name);
    }
}
