package com.softwaremagico.tm.random.character.selectors;

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
            return RandomAffiliation.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return RandomAlignment.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return RandomLegalStatus.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return RandomOccultism.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return RandomOperationalRole.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return RandomOrigin.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return RandomTech.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return RandomValueAssignation.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        try {
            return RandomWealth.valueOf(name);
        } catch (IllegalArgumentException ignore) {
        }
        throw new IllegalArgumentException("Unknown enum value: " + name);
    }
}
