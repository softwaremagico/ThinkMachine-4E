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
 * Defines a party template for random generation with character profiles,
 * minimum/maximum members per profile, and threat level balance.
 */
public class RandomParty {
	private final String language;
	private final String moduleName;

	/**
	 * Creates a random party definition.
	 *
	 * @param language
	 *            Language for text generation (ES/EN).
	 * @param moduleName
	 *            Module name (e.g., "Fading Suns 4E").
	 */
	public RandomParty(String language, String moduleName) {
		this.language = language;
		this.moduleName = moduleName;
	}

	/**
	 * Gets the language for this random party.
	 *
	 * @return Language code (ES/EN).
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Gets the module name for this random party.
	 *
	 * @return Module name.
	 */
	public String getModuleName() {
		return moduleName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((moduleName == null) ? 0 : moduleName.hashCode());
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
		final RandomParty other = (RandomParty) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (moduleName == null) {
			if (other.moduleName != null)
				return false;
		} else if (!moduleName.equals(other.moduleName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RandomParty [language=" + language + ", moduleName=" + moduleName + "]";
	}

}
