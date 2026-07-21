package com.softwaremagico.tm.random.party;

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

/**
 * Represents an adjective descriptor for random party generation.
 * Combines with PartyName to create full party names.
 */
public class PartyAdjective {
    private final String adjective;
    private final RandomParty randomParty;
    private final String language;
    private final String moduleName;

    /**
     * Creates a party adjective element.
     *
     * @param adjective
     *            Descriptive adjective (e.g., "de las Sombras", "of Shadows").
     * @param randomParty
     *            Parent RandomParty definition.
     * @param language
     *            Language code (ES/EN).
     * @param moduleName
     *            Module name (e.g., "Fading Suns 4E").
     */
    public PartyAdjective(String adjective, RandomParty randomParty, String language, String moduleName) {
        this.adjective = adjective;
        this.randomParty = randomParty;
        this.language = language;
        this.moduleName = moduleName;
    }

    /**
     * Gets the adjective text.
     *
     * @return Adjective descriptor.
     */
    public String getName() {
        return adjective;
    }

    /**
     * Gets the parent random party definition.
     *
     * @return RandomParty definition.
     */
    public RandomParty getRandomParty() {
        return randomParty;
    }

    /**
     * Gets the language for this adjective.
     *
     * @return Language code (ES/EN).
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Gets the module name for this adjective.
     *
     * @return Module name.
     */
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public int hashCode() {
        return adjective.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PartyAdjective other = (PartyAdjective) obj;
        return adjective.equals(other.adjective);
    }

}
