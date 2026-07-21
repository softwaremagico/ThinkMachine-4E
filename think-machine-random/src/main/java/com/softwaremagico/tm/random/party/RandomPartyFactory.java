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
 * Factory for generating random party names combining PartyName and PartyAdjective
 * with proper gender agreement and language-specific rules.
 */
public final class RandomPartyFactory {

    private RandomPartyFactory() {
    }

    private static class RandomPartyFactoryInit {
        public static final RandomPartyFactory INSTANCE = new RandomPartyFactory();
    }

    public static RandomPartyFactory getInstance() {
        return RandomPartyFactoryInit.INSTANCE;
    }

    /**
     * Generates a full party name by combining a base name and adjective.
     * Applies language-specific rules (gender agreement in Spanish).
     *
     * @param partyName
     *            Base party name.
     * @param partyAdjective
     *            Adjective descriptor.
     * @param language
     *            Language code (es/en).
     * @return Combined party name with proper formatting, or null if parameters invalid.
     */
    public static String getPartyName(PartyName partyName, PartyAdjective partyAdjective, String language) {
        if (partyName == null || partyAdjective == null) {
            return null;
        }

        if ("es".equalsIgnoreCase(language)) {
            return getPartyNameSpanish(partyName, partyAdjective);
        } else {
            return getPartyNameEnglish(partyName, partyAdjective);
        }
    }

    /**
     * Generates a Spanish party name with gender agreement.
     * Adjusts adjective ending to match noun gender when possible.
     *
     * @param partyName
     *            Base party name.
     * @param partyAdjective
     *            Adjective descriptor.
     * @return Formatted Spanish party name.
     */
    private static String getPartyNameSpanish(PartyName partyName, PartyAdjective partyAdjective) {
        final StringBuilder name = new StringBuilder();
        name.append(partyName.getName());
        name.append(" ");

        // Gender modification: if adjective starts with "de ", keep as-is
        // Otherwise, adjust adjective ending if noun ends with "as"
        if (partyAdjective.getName().startsWith("de ")) {
            name.append(partyAdjective.getName());
        } else if (partyName.getName().endsWith("as")) {
            // Feminine noun: adjust adjective to feminine if possible
            final String adj = partyAdjective.getName();
            if (adj.endsWith("os")) {
                // Replace "os" with "as"
                name.append(adj, 0, adj.length() - 2);
                name.append("as");
            } else {
                name.append(adj);
            }
        } else {
            name.append(partyAdjective.getName());
        }

        return name.toString();
    }

    /**
     * Generates an English party name.
     *
     * @param partyName
     *            Base party name.
     * @param partyAdjective
     *            Adjective descriptor.
     * @return Formatted English party name.
     */
    private static String getPartyNameEnglish(PartyName partyName, PartyAdjective partyAdjective) {
        return partyName.getName() + " " + partyAdjective.getName();
    }

}
