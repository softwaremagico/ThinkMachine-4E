package com.softwaremagico.tm.character;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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

import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.log.MachineLog;

import java.util.List;

/**
 * Calculates the threat level of a character based on combat capabilities,
 * equipment, and abilities. Adapted from Fading Suns 3E to work with 4E
 * character model.
 */
public final class ThreatLevel {
	private static final int DAMAGE_THREAT_MULTIPLICATOR = 2;
	private static final int WEAPON_THREAT_MULTIPLICATOR = 3;
	private static final int MELEE_THREAT_MULTIPLICATOR = 3;
	private static final int CYBERNETICSCOMBAT_THREAT_MULTIPLICATOR = 2;
	private static final int CYBERNETICS_ENHANCEMENT_THREAT_MULTIPLICATOR = 1;
	private static final int RESISTANCE_THREAT_MULTIPLICATOR = 2;

	private static int total = 0;
	private static int combatThreatLevel = 0;
	private static int weaponThreatLevel = 0;
	private static int cyberneticsThreatLevel = 0;
	private static int resistanceThreatLevel = 0;
	private static float totalThreatLevel = 0;

	private ThreatLevel() {
	}

	/**
	 * Calculates the overall threat level of a character.
	 *
	 * @param characterPlayer
	 *            the character to evaluate
	 * @return the threat level value
	 */
	public static int getThreatLevel(CharacterPlayer characterPlayer) {
		try {
			int threatLevel = 0;
			threatLevel += getCombatThreatLevel(characterPlayer);
			threatLevel += getWeaponThreatLevel(characterPlayer);
			threatLevel += getCyberneticsThreatLevel(characterPlayer);
			threatLevel += getVitalityThreatLevel(characterPlayer);
			totalThreatLevel += threatLevel;
			total++;
			return threatLevel;
		} catch (final Exception e) {
			MachineLog.errorMessage(ThreatLevel.class.getName(), e);
			return 0;
		}
	}

	/**
	 * Calculates threat from combat-related skills (fight, melee, shoot).
	 *
	 * @param characterPlayer
	 *            the character to evaluate
	 * @return combat skill threat level
	 */
	private static int getCombatThreatLevel(CharacterPlayer characterPlayer) {
		try {
			int threatLevel = 0;

			// Fight skill (unarmed combat)
			try {
				final int fightRanks = characterPlayer.getSkillValue("fight");
				threatLevel += fightRanks * 2;
				threatLevel += characterPlayer.getCharacteristicValue(CharacteristicName.STRENGTH);
			} catch (final Exception e) {
				// Skill or characteristic not found
			}

			// Melee skill (armed combat)
			try {
				final int meleeRanks = characterPlayer.getSkillValue("melee");
				threatLevel += meleeRanks * MELEE_THREAT_MULTIPLICATOR;
				threatLevel += characterPlayer.getCharacteristicValue(CharacteristicName.STRENGTH) / 2;
			} catch (final Exception e) {
				// Skill or characteristic not found
			}

			// Shoot skill (ranged combat)
			try {
				final int shootRanks = characterPlayer.getSkillValue("shoot");
				threatLevel += shootRanks * 2;
				threatLevel += characterPlayer.getCharacteristicValue(CharacteristicName.WITS);
			} catch (final Exception e) {
				// Skill or characteristic not found
			}

			combatThreatLevel += threatLevel;
			return threatLevel;
		} catch (final Exception e) {
			MachineLog.errorMessage(ThreatLevel.class.getName(), e);
			return 0;
		}
	}

	/**
	 * Calculates threat from weapons carried by the character.
	 *
	 * @param characterPlayer
	 *            the character to evaluate
	 * @return weapon threat level
	 */
	private static int getWeaponThreatLevel(CharacterPlayer characterPlayer) {
		try {
			int threatLevel = 0;
			final List<Weapon> weapons = characterPlayer.getWeapons();

			// Threat for each weapon
			for (final Weapon weapon : weapons) {
				threatLevel += getThreatLevel(weapon);
			}

			// Bonus for having multiple weapons
			if (weapons.size() > 1) {
				threatLevel += (weapons.size() - 1) * 2;
			}

			weaponThreatLevel += threatLevel;
			return threatLevel;
		} catch (final Exception e) {
			MachineLog.errorMessage(ThreatLevel.class.getName(), e);
			return 0;
		}
	}

	/**
	 * Calculates threat from a single weapon.
	 *
	 * @param weapon
	 *            the weapon to evaluate
	 * @return weapon threat level
	 */
	private static int getThreatLevel(Weapon weapon) {
		try {
			if (weapon == null || weapon.getWeaponDamages().isEmpty()) {
				return 1;
			}

			int threatLevel = WEAPON_THREAT_MULTIPLICATOR;

			// Threat from damage
			try {
				final int mainDamage = weapon.getWeaponDamages().getFirst().getMainDamage();
				threatLevel += mainDamage * DAMAGE_THREAT_MULTIPLICATOR;
			} catch (final Exception e) {
				// Weapon damage not available
			}

			// Threat from weapon being automatic
			try {
				if (weapon.isAutomaticWeapon()) {
					threatLevel *= 2;
				}
			} catch (final Exception e) {
				// Automatic property not available
			}

			return threatLevel;
		} catch (final Exception e) {
			MachineLog.errorMessage(ThreatLevel.class.getName(), e);
			return WEAPON_THREAT_MULTIPLICATOR;
		}
	}

	/**
	 * Calculates threat from cybernetic devices.
	 *
	 * @param characterPlayer
	 *            the character to evaluate
	 * @return cybernetics threat level
	 */
	private static int getCyberneticsThreatLevel(CharacterPlayer characterPlayer) {
		try {
			int threatLevel = 0;
			final List<Cyberdevice> cyberdevices = characterPlayer.getCyberdevices();

			for (final Cyberdevice cyberdevice : cyberdevices) {
				// Deduce if combat-related from name or description
				final String id = cyberdevice.getId() != null ? cyberdevice.getId().toLowerCase() : "";
				final String name = cyberdevice.getName() != null
						? cyberdevice.getName().getTranslatedText().toLowerCase()
						: "";
				final String description = cyberdevice.getDescription() != null
						? cyberdevice.getDescription().getTranslatedText().toLowerCase()
						: "";

				final boolean isCombat = isCombatRelated(id, name, description);

				if (isCombat) {
					threatLevel += CYBERNETICSCOMBAT_THREAT_MULTIPLICATOR;
				} else {
					threatLevel += CYBERNETICS_ENHANCEMENT_THREAT_MULTIPLICATOR;
				}
			}

			cyberneticsThreatLevel += threatLevel;
			return threatLevel;
		} catch (final Exception e) {
			MachineLog.errorMessage(ThreatLevel.class.getName(), e);
			return 0;
		}
	}

	/**
	 * Detects if a cyberdevice is combat-related by examining its id, name, and
	 * description.
	 *
	 * @param id
	 *            the device ID
	 * @param name
	 *            the device name
	 * @param description
	 *            the device description
	 * @return true if combat-related
	 */
	private static boolean isCombatRelated(String id, String name, String description) {
		final String fullText = id + " " + name + " " + description;

		final String[] combatKeywords = {"combat", "fight", "attack", "weapon", "armor", "shield", "weapon system",
				"targeting", "sniper", "cannon", "gun", "blade", "sword", "enhanced strength", "enhanced durability",
				"reflex", "dodge", "defensive", "plasma", "laser", "explosive"};

		for (final String keyword : combatKeywords) {
			if (fullText.contains(keyword)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Calculates threat from character vitality (resistances and health).
	 *
	 * @param characterPlayer
	 *            the character to evaluate
	 * @return vitality threat level
	 */
	private static int getVitalityThreatLevel(CharacterPlayer characterPlayer) {
		try {
			int threatLevel = 0;

			try {
				threatLevel += characterPlayer.getBodyResistance() * RESISTANCE_THREAT_MULTIPLICATOR;
				threatLevel += characterPlayer.getMindResistance() * RESISTANCE_THREAT_MULTIPLICATOR;
				threatLevel += characterPlayer.getSpiritResistance() * RESISTANCE_THREAT_MULTIPLICATOR;
			} catch (final Exception e) {
				// Resistances not available
			}

			resistanceThreatLevel += threatLevel;
			return threatLevel;
		} catch (final Exception e) {
			MachineLog.errorMessage(ThreatLevel.class.getName(), e);
			return 0;
		}
	}

	/**
	 * Prints threat level statistics to console.
	 */
	public static void showStatistics() {
		if (total > 0) {
			MachineLog.info(ThreatLevel.class, "######################################");
			MachineLog.info(ThreatLevel.class, "Total threat: {} ({})", totalThreatLevel / total, total);
			MachineLog.info(ThreatLevel.class, "Combat threat: {}", combatThreatLevel / total);
			MachineLog.info(ThreatLevel.class, "Weapon threat: {}", weaponThreatLevel / total);
			MachineLog.info(ThreatLevel.class, "Cybernetics threat: {}", cyberneticsThreatLevel / total);
			MachineLog.info(ThreatLevel.class, "Resistance threat: {}", resistanceThreatLevel / total);
		}
	}

	/**
	 * Resets static threat level statistics.
	 */
	public static void resetStatistics() {
		total = 0;
		combatThreatLevel = 0;
		weaponThreatLevel = 0;
		cyberneticsThreatLevel = 0;
		resistanceThreatLevel = 0;
		totalThreatLevel = 0;
	}
}
