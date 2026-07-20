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

import com.softwaremagico.tm.ElementType;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.Cyberdevice;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.perks.SpecializedPerk;
import com.softwaremagico.tm.character.resistances.ResistanceType;
import com.softwaremagico.tm.log.MachineLog;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Calculates the threat level of a character based on combat capabilities,
 * equipment, and abilities. Comprehensive threat assessment including weapons,
 * armor, shields, cybernetics, and special damage types. Adapted from Fading
 * Suns 3E to work with 4E character model.
 */
public final class ThreatLevel {
    private static final int DAMAGE_THREAT_MULTIPLICATOR = 2;
    private static final int WEAPON_THREAT_MULTIPLICATOR = 3;
    private static final int MELEE_THREAT_MULTIPLICATOR = 3;
    private static final int DAMAGE_AREA_THREAT_MULTIPLICATOR = 3;
    private static final int DAMAGE_TYPES_THREAT_MULTIPLICATOR = 3;
    private static final int RANGE_THREAT_DIVISOR = WEAPON_THREAT_MULTIPLICATOR * DAMAGE_THREAT_MULTIPLICATOR
            + RESISTANCE_THREAT_MULTIPLICATOR + RESISTANCE_THREAT_MULTIPLICATOR;
    private static final int CYBERNETICSCOMBAT_THREAT_MULTIPLICATOR = 2;
    private static final int CYBERNETICS_ENHANCEMENT_THREAT_MULTIPLICATOR = 1;
    private static final int RESISTANCE_THREAT_MULTIPLICATOR = 2;
    private static final int OCCULTISM_COMBAT_THREAT_MULTIPLICATOR = 1;
    private static final int OCCULTISM_OTHER_THREAT_MULTIPLICATOR = 1;
    private static final int OCCULTISM_TYPE_THREAT_MULTIPLICATOR = 2;
    private static final int PERKS_COMBAT_THREAT_MULTIPLICATOR = 10;

    // Specific damage type multiplicators (from 3E)
    private static final int FIRE_DAMAGE_THREAT = 3;
    private static final int LASER_DAMAGE_THREAT = 1;
    private static final int XASER_DAMAGE_THREAT = 3;
    private static final int GRASER_DAMAGE_THREAT = 6;
    private static final int SHOCK_DAMAGE_THREAT = 5;
    private static final int PLASMA_DAMAGE_THREAT = 5;
    private static final int HALVE_ARMOUR_THREAT = 3;
    private static final int IGNORE_ARMOUR_THREAT = 6;

    private static int total = 0;
    private static int combatThreatLevel = 0;
    private static int weaponThreatLevel = 0;
    private static int armourThreatLevel = 0;
    private static float shieldThreatLevel = 0;
    private static int cyberneticsThreatLevel = 0;
    private static int occultismThreatLevel = 0;
    private static int resistanceThreatLevel = 0;
    private static int perksThreatLevel = 0;
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
            threatLevel += getArmourThreatLevel(characterPlayer);
            threatLevel += getCyberneticsThreatLevel(characterPlayer);
            threatLevel += getVitalityThreatLevel(characterPlayer);
            threatLevel += getOccultismThreatLevel(characterPlayer);
            threatLevel += getPerksThreatLevel(characterPlayer);
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
     * Calculates threat from a single weapon, including damage, damage types, area
     * effect, range, and rate of fire.
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
            final WeaponDamage weaponDamage = weapon.getWeaponDamages().get(0);

            // Threat from main damage
            try {
                final int mainDamage = weaponDamage.getMainDamage();
                threatLevel += mainDamage * DAMAGE_THREAT_MULTIPLICATOR;
            } catch (final Exception e) {
                // Weapon damage not available
            }

            // Threat from area damage
            try {
                final int areaDamage = weaponDamage.getAreaMeters();
                threatLevel += areaDamage * DAMAGE_AREA_THREAT_MULTIPLICATOR;
            } catch (final Exception e) {
                // Area damage not available
            }

            // Threat from damage type count
            try {
                threatLevel += weapon.getDamageTypes().size() * DAMAGE_TYPES_THREAT_MULTIPLICATOR;
            } catch (final Exception e) {
                // Damage types not available
            }

            // Threat from specific damage types (based on weapon damage types)
            try {
                for (final String damageType : weapon.getDamageTypes()) {
                    final String typeKey = damageType.toLowerCase();

                    if (typeKey.contains("fire")) {
                        threatLevel += FIRE_DAMAGE_THREAT * weaponDamage.getMainDamage();
                    }
                    if (typeKey.contains("laser")) {
                        threatLevel += LASER_DAMAGE_THREAT * weaponDamage.getMainDamage();
                    }
                    if (typeKey.contains("xaser")) {
                        threatLevel += XASER_DAMAGE_THREAT * weaponDamage.getMainDamage();
                    }
                    if (typeKey.contains("graser")) {
                        threatLevel += GRASER_DAMAGE_THREAT * weaponDamage.getMainDamage();
                    }
                    if (typeKey.contains("shock")) {
                        threatLevel += SHOCK_DAMAGE_THREAT * weaponDamage.getMainDamage();
                    }
                    if (typeKey.contains("plasma")) {
                        threatLevel += PLASMA_DAMAGE_THREAT * weaponDamage.getMainDamage();
                    }
                    if (typeKey.contains("halvearmour") || typeKey.contains("halve armour")) {
                        threatLevel += HALVE_ARMOUR_THREAT * weaponDamage.getMainDamage();
                    }
                    if (typeKey.contains("ignorearmour") || typeKey.contains("ignore armour")) {
                        threatLevel += IGNORE_ARMOUR_THREAT * weaponDamage.getMainDamage();
                    }
                }
            } catch (final Exception e) {
                // Specific damage types not available
            }

            // Threat from range
            try {
                final int mainRange = weaponDamage.getMainRange();
                threatLevel += mainRange / RANGE_THREAT_DIVISOR;
            } catch (final Exception e) {
                // Range not available
            }

            // Threat from rate of fire
            try {
                final int mainRate = weaponDamage.getMainRate();
                threatLevel += mainRate;
            } catch (final Exception e) {
                // Rate not available
            }

            // Threat from weapon being automatic
            try {
                if (weapon.isAutomaticWeapon()) {
                    threatLevel *= 2;
                }
            } catch (final Exception e) {
                // Automatic property not available
            }

            weaponThreatLevel += threatLevel;
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
     * Calculates threat from armor worn by the character.
     *
     * @param characterPlayer
     *            the character to evaluate
     * @return armor threat level
     */
    private static int getArmourThreatLevel(CharacterPlayer characterPlayer) {
        try {
            int threatLevel = 0;

            try {
                final Armor armor = characterPlayer.getPurchasedArmor();
                if (armor != null) {
                    threatLevel += getThreatLevel(armor);
                }
            } catch (final Exception e) {
                // Armor not available
            }

            armourThreatLevel += threatLevel;
            return threatLevel;
        } catch (final Exception e) {
            MachineLog.errorMessage(ThreatLevel.class.getName(), e);
            return 0;
        }
    }

    /**
     * Calculates threat from a single armor piece.
     *
     * @param armor
     *            the armor to evaluate
     * @return armor threat level
     */
    private static int getThreatLevel(Armor armor) {
        try {
            if (armor == null) {
                return 0;
            }

            int threatLevel = 0;

            try {
                // Armor threat based on damage types it protects against
                final int damageTypeCount = armor.getDamageTypes().size();
                threatLevel += damageTypeCount * DAMAGE_TYPES_THREAT_MULTIPLICATOR;
                threatLevel += armor.getResistanceValue(ResistanceType.BODY) * DAMAGE_THREAT_MULTIPLICATOR;
            } catch (final Exception e) {
                // Armor properties not available
            }

            return threatLevel;
        } catch (final Exception e) {
            MachineLog.errorMessage(ThreatLevel.class.getName(), e);
            return 0;
        }
    }

    /**
     * Calculates shield threat multiplier effect on overall threat. Shields
     * increase threat due to defensive capability.
     *
     * @param shield
     *            the shield to evaluate
     * @return threat level multiplier (1.0 or higher)
     */
    private static float getThreatLevelMultiplier(Shield shield) {
        try {
            if (shield == null) {
                return 1.0f;
            }

            float threatMultiplier = 1.0f;

            try {
                // Shield's protective value based on force and impact
                final int force = shield.getForce();
                final int impact = shield.getImpact();
                final int hits = shield.getHits();
                final float protection = ((force - impact) + 5) / 10.0f + (hits / 10.0f);
                threatMultiplier = 1.0f + protection;
            } catch (final Exception e) {
                // Shield properties not available
            }

            shieldThreatLevel += threatMultiplier;
            return threatMultiplier;
        } catch (final Exception e) {
            MachineLog.errorMessage(ThreatLevel.class.getName(), e);
            return 1.0f;
        }
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
     * Calculates threat from occultism powers and occultism levels. Powers are
     * classified based on combat relevance: combat powers add their full level,
     * while other powers add half their level.
     *
     * @param characterPlayer
     *            the character to evaluate
     * @return occultism threat level
     */
    private static int getOccultismThreatLevel(CharacterPlayer characterPlayer) {
        try {
            int threatLevel = 0;

            // Threat from selected occultism powers, classified by path ElementType
            try {
                final Map<String, List<OccultismPower>> selectedPowers = characterPlayer.getSelectedPowers();
                if (selectedPowers != null) {
                    for (final Map.Entry<String, List<OccultismPower>> entry : selectedPowers.entrySet()) {
                        ElementType pathType = null;
                        try {
                            final OccultismPath path = OccultismPathFactory.getInstance().getElement(entry.getKey());
                            if (path != null) {
                                pathType = path.getElementType();
                            }
                        } catch (final Exception e) {
                            // Path not found, treat as non-combat
                        }
                        for (final OccultismPower power : entry.getValue()) {
                            if (pathType == ElementType.COMBAT) {
                                threatLevel += power.getOccultismLevel() * OCCULTISM_COMBAT_THREAT_MULTIPLICATOR;
                            } else {
                                threatLevel += power.getOccultismLevel() / 2;
                            }
                        }
                    }
                }
            } catch (final Exception e) {
                // Occultism powers not available
            }

            // Threat from total occultism levels (Psi, Theurgy, etc.)
            try {
                final int totalOccultismLevel = characterPlayer.getOccultismLevel();
                threatLevel += totalOccultismLevel * OCCULTISM_TYPE_THREAT_MULTIPLICATOR;
            } catch (final Exception e) {
                // Occultism level not available
            }

            occultismThreatLevel += threatLevel;
            return threatLevel;
        } catch (final Exception e) {
            MachineLog.errorMessage(ThreatLevel.class.getName(), e);
            return 0;
        }
    }

    /**
     * Determines if a perk is combat-related via its ElementType.
     *
     * @param perk
     *            the perk to evaluate
     * @return true if ElementType is COMBAT
     */
    private static boolean isCombatPerk(SpecializedPerk perk) {
        return perk != null && perk.getElementType() == ElementType.COMBAT;
    }

    /**
     * Calculates threat from combat perks.
     *
     * @param characterPlayer
     *            the character to evaluate
     * @return perks threat level
     */
    private static int getPerksThreatLevel(CharacterPlayer characterPlayer) {
        try {
            int threatLevel = 0;
            final Set<SpecializedPerk> perks = characterPlayer.getPerks();
            if (perks != null) {
                for (final SpecializedPerk perk : perks) {
                    if (isCombatPerk(perk)) {
                        threatLevel += PERKS_COMBAT_THREAT_MULTIPLICATOR;
                    }
                }
            }
            perksThreatLevel += threatLevel;
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
            MachineLog.info(ThreatLevel.class, "Armour threat: {}", armourThreatLevel / total);
            MachineLog.info(ThreatLevel.class, "Shield threat multiplier: {}", shieldThreatLevel / total);
            MachineLog.info(ThreatLevel.class, "Cybernetics threat: {}", cyberneticsThreatLevel / total);
            MachineLog.info(ThreatLevel.class, "Occultism threat: {}", occultismThreatLevel / total);
            MachineLog.info(ThreatLevel.class, "Perks threat: {}", perksThreatLevel / total);
            MachineLog.info(ThreatLevel.class, "Resistance threat: {}", resistanceThreatLevel / total);
        }
    }

    /**
     * Resets all static threat level statistics.
     */
    public static void resetStatistics() {
        total = 0;
        combatThreatLevel = 0;
        weaponThreatLevel = 0;
        armourThreatLevel = 0;
        shieldThreatLevel = 0;
        cyberneticsThreatLevel = 0;
        occultismThreatLevel = 0;
        perksThreatLevel = 0;
        resistanceThreatLevel = 0;
        totalThreatLevel = 0;
    }
}
