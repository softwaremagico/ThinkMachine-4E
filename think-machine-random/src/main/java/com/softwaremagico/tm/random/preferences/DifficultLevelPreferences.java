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

import java.util.Set;

/**
 * Defines difficulty levels for character generation, with corresponding threat levels
 * and bonuses to characteristics, skills, and experience.
 *
 * Each difficulty level adjusts the generated character's power to match a target
 * threat level for party balancing.
 */
public enum DifficultLevelPreferences implements IRandomPreference {

	/**
	 * Very Easy - Target threat level ~50
	 * Characters are significantly below normal power level.
	 */
	VERY_EASY(-5, -15, 0),

	/**
	 * Easy - Target threat level ~58
	 * Characters are below normal power level.
	 */
	EASY(-3, -10, 0),

	/**
	 * Medium - Target threat level ~65
	 * Standard character generation with default bonuses.
	 */
	MEDIUM(0, 0, 0),

	/**
	 * Hard - Target threat level ~85
	 * Characters are above normal power level.
	 */
	HARD(0, 5, 50),

	/**
	 * Very Hard - Target threat level ~100
	 * Characters are significantly above normal power level.
	 */
	VERY_HARD(5, 10, 100);

	private final int characteristicsBonus;
	private final int skillsBonus;
	private final int experienceBonus;

	/**
	 * Creates a difficulty level preference.
	 *
	 * @param characteristicsBonus
	 *            Bonus to characteristic generation (-5 to +5).
	 * @param skillsBonus
	 *            Bonus to skill points (-15 to +10).
	 * @param experienceBonus
	 *            Bonus to starting experience points (0 to 100).
	 */
	DifficultLevelPreferences(int characteristicsBonus, int skillsBonus, int experienceBonus) {
		this.characteristicsBonus = characteristicsBonus;
		this.skillsBonus = skillsBonus;
		this.experienceBonus = experienceBonus;
	}

	/**
	 * Gets the characteristics bonus for this difficulty level.
	 * Positive values increase raw characteristic values during generation.
	 *
	 * @return Characteristics bonus (-5 to +5).
	 */
	public int getCharacteristicsBonus() {
		return characteristicsBonus;
	}

	/**
	 * Gets the skills bonus for this difficulty level.
	 * Affects the total skill points available during generation.
	 *
	 * @return Skills bonus (-15 to +10).
	 */
	public int getSkillsBonus() {
		return skillsBonus;
	}

	/**
	 * Gets the experience bonus for this difficulty level.
	 * Increases starting experience pool for advanced characters.
	 *
	 * @return Experience bonus (0 to 100).
	 */
	public int getExperienceBonus() {
		return experienceBonus;
	}

	/**
	 * Gets the estimated threat level for this difficulty level.
	 *
	 * @return Approximate threat level (50-100).
	 */
	public int getEstimatedThreatLevel() {
		return switch (this) {
			case VERY_EASY -> 50;
			case EASY -> 58;
			case MEDIUM -> 65;
			case HARD -> 85;
			case VERY_HARD -> 100;
		};
	}

	/**
	 * Selects a difficulty preference from a set of preferences.
	 * Returns MEDIUM if no difficulty preference is found.
	 *
	 * @param preferences
	 *            Set of preferences to search.
	 * @return Selected DifficultLevelPreferences or MEDIUM as default.
	 */
	public static DifficultLevelPreferences getSelected(Set<IRandomPreference> preferences) {
		if (preferences == null) {
			return MEDIUM;
		}
		for (final IRandomPreference preference : preferences) {
			if (preference instanceof DifficultLevelPreferences) {
				return (DifficultLevelPreferences) preference;
			}
		}
		return MEDIUM;
	}

}
