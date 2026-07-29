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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Settings;
import com.softwaremagico.tm.character.ThreatLevel;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.random.character.RandomizeCharacter;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Generates a balanced party of random characters up to a target threat level.
 * Respects minimum/maximum constraints per member profile and balances threat
 * using ThreatLevel calculations.
 */
public class RandomPartyDefinition {
    private static final int THREAT_MARGIN = 10;
    private static final int MAX_ITERATIONS = 10000;
    private static final Random RANDOM = new Random();

    private final Party party;
    private final int targetThreatLevel;
    private final Collection<RandomPartyMember> memberTemplates;
    private final Settings settings;

    // Tracks how many characters of each profile have been added
    private final Map<RandomPartyMember, Integer> profilesAssigned = new HashMap<>();
    // Tracks cumulative threat per profile for weight calculations
    private final Map<RandomPartyMember, Integer> threatByProfile = new HashMap<>();

    /**
     * Creates a party generator.
     *
     * @param language
     *            Language for character generation (ES/EN).
     * @param moduleName
     *            Module name (e.g., "Fading Suns 4E").
     * @param targetThreatLevel
     *            Target total party threat level.
     * @param memberTemplates
     *            Collection of party member profiles to generate from.
     */
    public RandomPartyDefinition(String language, String moduleName, int targetThreatLevel,
            Collection<RandomPartyMember> memberTemplates) {
        this.party = new Party(language, moduleName);
        this.targetThreatLevel = targetThreatLevel;
        this.memberTemplates = memberTemplates;
        this.settings = new Settings();
    }

    /**
     * Gets the generated party.
     *
     * @return Party containing all generated characters.
     */
    public Party getParty() {
        return party;
    }

    /**
     * Gets the number of characters assigned with a specific profile.
     *
     * @param member
     *            Party member profile.
     * @return Count of characters with this profile.
     */
    private Integer getProfileAssigned(RandomPartyMember member) {
        return profilesAssigned.getOrDefault(member, 0);
    }

    /**
     * Gets the cumulative threat of all characters with a specific profile.
     *
     * @param member
     *            Party member profile.
     * @return Total threat for this profile.
     */
    private Integer getThreatByProfile(RandomPartyMember member) {
        return threatByProfile.getOrDefault(member, 0);
    }

    /**
     * Generates a random character for a specific member profile and adds to party.
     *
     * @param member
     *            Party member template.
     * @throws InvalidXmlElementException
     *             if character generation fails.
     */
    private void assignProfile(RandomPartyMember member) throws InvalidXmlElementException {
        // Check if we've already reached max members for this profile
        if (member.getMaxNumber() != null && getProfileAssigned(member) >= member.getMaxNumber()) {
            return;
        }

        try {
            profilesAssigned.put(member, getProfileAssigned(member) + 1);
            final CharacterPlayer characterPlayer = createCharacter(member);
            party.addMember(characterPlayer);

            // Track threat by profile for future weight calculations
            final int characterThreat = ThreatLevel.getThreatLevel(characterPlayer);
            threatByProfile.put(member, getThreatByProfile(member) + characterThreat);

            // Remove profile from selection if max is reached
            if (member.getMaxNumber() != null && getProfileAssigned(member) >= member.getMaxNumber()) {
                MachineLog.debug(this.getClass().getName(), "Profile '{}' reached max assignments.", member.getName());
            }
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(this.getClass().getName(), "Failed to assign profile: " + member, e);
            throw e;
        }
    }

    /**
     * Creates a random character using the specified profile.
     *
     * @param member
     *            Party member template.
     * @return Generated character.
     * @throws InvalidXmlElementException
     *             if character generation fails.
     */
    private CharacterPlayer createCharacter(RandomPartyMember member)
            throws InvalidXmlElementException {
        final CharacterPlayer characterPlayer = new CharacterPlayer();
        characterPlayer.getSettings().copy(settings);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer);
        try {
            randomizeCharacter.createCharacter();
        } catch (InvalidRandomElementSelectedException e) {
            throw new InvalidXmlElementException("Failed to randomize character", e);
        }
        return characterPlayer;
    }

    /**
     * Calculates weight for selecting a profile (0 = cannot select).
     *
     * @param member
     *            Party member profile.
     * @return Weight (0 = excluded).
     */
    private int getWeight(RandomPartyMember member) {
        // If max is already reached, cannot select this profile
        if (member.getMaxNumber() != null && getProfileAssigned(member) >= member.getMaxNumber()) {
            return 0;
        }

        // Threat-based weight: reduce weight if adding more would exceed target
        if (getProfileAssigned(member) > 0) {
            final int estimatedThreat = getThreatByProfile(member) / getProfileAssigned(member);
            if (party.getThreatLevel() + estimatedThreat > targetThreatLevel + THREAT_MARGIN) {
                return 0; // Too much threat, cannot add more
            }
        }

        // Use specified weight or default to 1
        return member.getWeight() != null ? member.getWeight() : 1;
    }

    /**
     * Selects a random profile by weight.
     *
     * @return Selected profile, or null if none available.
     */
    private RandomPartyMember selectMemberByWeight() {
        // Calculate total weight of available members
        int totalWeight = 0;
        for (final RandomPartyMember member : memberTemplates) {
            totalWeight += getWeight(member);
        }

        if (totalWeight <= 0) {
            return null; // No members available
        }

        // Select by weight
        int roll = RANDOM.nextInt(totalWeight);
        for (final RandomPartyMember member : memberTemplates) {
            final int weight = getWeight(member);
            if (roll < weight) {
                return member;
            }
            roll -= weight;
        }

        return null;
    }

    /**
     * Generates the party by randomly adding members until mandatory minimums are
     * met and threat target is reached.
     *
     * @throws InvalidXmlElementException
     *             if character generation fails.
     */
    public void generate() throws InvalidXmlElementException {
        // First, add mandatory members (minNumber > 0)
        for (final RandomPartyMember member : memberTemplates) {
            while (member.getMinNumber() != null && getProfileAssigned(member) < member.getMinNumber()) {
                try {
                    assignProfile(member);
                } catch (InvalidXmlElementException e) {
                    MachineLog.errorMessage(this.getClass().getName(),
                            "Cannot generate mandatory member: " + member.getName(), e);
                    break;
                }
            }
        }

        // Then add random members up to threat target
        int iterations = 0;
        while (iterations < MAX_ITERATIONS) {
            final RandomPartyMember selectedMember = selectMemberByWeight();
            if (selectedMember == null) {
                break; // No more members can be added
            }

            try {
                assignProfile(selectedMember);
                iterations++;
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass().getName(), "Failed to add member during generation", e);
                iterations++;
            }
        }

        MachineLog.info(this.getClass().getName(),
                "Party generation complete. Members: " + party.getMemberCount() + ", Threat: " + party.getThreatLevel());
    }

}
