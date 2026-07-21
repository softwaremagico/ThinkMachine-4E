package com.softwaremagico.tm.party;

/*-
 * #%L
 * Think Machine 4E (Rules)
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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.ThreatLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a party of characters.
 */
public class Party {
    private final Set<CharacterPlayer> characterPlayers;
    private final transient Map<CharacterPlayer, Integer> threatByCharacter;
    private String partyName;
    private final String language;
    private final String moduleName;

    /**
     * Creates a party with language and module name.
     *
     * @param language
     *            the language code (e.g., "ES", "EN").
     * @param moduleName
     *            the module name.
     */
    public Party(String language, String moduleName) {
        this.language = language;
        this.moduleName = moduleName;
        this.characterPlayers = new HashSet<>();
        this.threatByCharacter = new HashMap<>();
    }

    /**
     * Adds a character member to the party.
     *
     * @param characterPlayer
     *            the character to add.
     */
    public void addMember(CharacterPlayer characterPlayer) {
        this.characterPlayers.add(characterPlayer);
        this.threatByCharacter.put(characterPlayer, ThreatLevel.getThreatLevel(characterPlayer));
    }

    /**
     * Removes a character from the party.
     *
     * @param characterPlayer
     *            the character to remove.
     */
    public void removeCharacter(CharacterPlayer characterPlayer) {
        this.characterPlayers.remove(characterPlayer);
        this.threatByCharacter.remove(characterPlayer);
    }

    /**
     * Gets an unmodifiable list of party members sorted by threat level (descending).
     *
     * @return list of character players sorted by threat level.
     */
    public List<CharacterPlayer> getMembers() {
        final List<CharacterPlayer> sortedCharacterPlayers = new ArrayList<>(this.characterPlayers);
        Collections.sort(sortedCharacterPlayers, new Comparator<CharacterPlayer>() {
            @Override
            public int compare(CharacterPlayer arg0, CharacterPlayer arg1) {
                final Integer threat0 = threatByCharacter.getOrDefault(arg0, 0);
                final Integer threat1 = threatByCharacter.getOrDefault(arg1, 0);
                return threat1 - threat0; // Descending: highest threat first
            }
        });
        return Collections.unmodifiableList(sortedCharacterPlayers);
    }

    /**
     * Gets the party name.
     *
     * @return the party name.
     */
    public String getPartyName() {
        return this.partyName;
    }

    /**
     * Sets the party name.
     *
     * @param partyName
     *            the party name to set.
     */
    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    /**
     * Gets the language.
     *
     * @return the language code.
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Gets the module name.
     *
     * @return the module name.
     */
    public String getModuleName() {
        return this.moduleName;
    }

    /**
     * Gets the number of members in the party.
     *
     * @return the member count.
     */
    public int getMemberCount() {
        return this.characterPlayers.size();
    }

    /**
     * Gets the total threat level of the party (sum of all members).
     *
     * @return the total threat level.
     */
    public int getThreatLevel() {
        int threatLevel = 0;
        for (final Integer threat : this.threatByCharacter.values()) {
            threatLevel += threat;
        }
        return threatLevel;
    }

    /**
     * Gets the threat level of a specific character.
     *
     * @param characterPlayer
     *            the character to query.
     * @return the threat level of the character, or 0 if not found.
     */
    public int getThreatLevel(CharacterPlayer characterPlayer) {
        if (this.threatByCharacter.get(characterPlayer) == null) {
            return 0;
        }
        return this.threatByCharacter.get(characterPlayer);
    }
}
