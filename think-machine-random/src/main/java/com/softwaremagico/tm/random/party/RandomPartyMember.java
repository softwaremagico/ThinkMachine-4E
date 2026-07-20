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
 * Defines a party member template with constraints on how many characters
 * with a specific profile can be generated.
 */
public class RandomPartyMember {
	private final String name;
	private final String profileId;
	private final Integer minNumber;
	private final Integer maxNumber;
	private final Integer weight;

	/**
	 * Creates a party member template.
	 *
	 * @param name
	 *            Display name of this party member template (e.g., "Commander").
	 * @param profileId
	 *            Character profile ID to randomize from (e.g., "decadosCommander").
	 * @param minNumber
	 *            Minimum number of this profile in the party (mandatory).
	 * @param maxNumber
	 *            Maximum number of this profile in the party.
	 * @param weight
	 *            Weight for random selection (higher = more likely to appear).
	 */
	public RandomPartyMember(String name, String profileId, Integer minNumber, Integer maxNumber, Integer weight) {
		this.name = name;
		this.profileId = profileId;
		this.minNumber = minNumber;
		this.maxNumber = maxNumber;
		this.weight = weight;
	}

	/**
	 * Gets the display name of this party member template.
	 *
	 * @return Name (e.g., "Commander").
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the character profile ID.
	 *
	 * @return Profile ID.
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * Gets the minimum number of members with this profile (mandatory).
	 *
	 * @return Minimum count or null if not mandatory.
	 */
	public Integer getMinNumber() {
		return minNumber;
	}

	/**
	 * Gets the maximum number of members with this profile.
	 *
	 * @return Maximum count or null for unlimited.
	 */
	public Integer getMaxNumber() {
		return maxNumber;
	}

	/**
	 * Gets the weight for random selection.
	 *
	 * @return Weight (higher = more likely).
	 */
	public Integer getWeight() {
		return weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((profileId == null) ? 0 : profileId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RandomPartyMember other = (RandomPartyMember) obj;
		if (profileId == null) {
			if (other.profileId != null)
				return false;
		} else if (!profileId.equals(other.profileId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RandomPartyMember [name=" + name + ", profileId=" + profileId + ", minNumber=" + minNumber
				+ ", maxNumber=" + maxNumber + ", weight=" + weight + "]";
	}

}
