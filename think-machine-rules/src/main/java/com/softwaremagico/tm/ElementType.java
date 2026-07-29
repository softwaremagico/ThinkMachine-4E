package com.softwaremagico.tm;

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

/**
 * Classifies any Element's thematic role for use in threat level calculation
 * and other mechanical purposes. Applicable to perks, occultism paths,
 * cybernetics, and any other Element subclass.
 */
public enum ElementType {
    /** Elements related to combat, attacks, damage, or defense in battle. */
    COMBAT,
    /** Elements related to social interactions, reputation, influence, or diplomacy. */
    SOCIAL,
    /** Elements related to commanding others, military hierarchy, or organizing groups. */
    LEADERSHIP,
    /** Elements related to technology, engineering, medicine, or knowledge. */
    TECHNICAL,
    /** Elements related to religion, faith, church, occultism, or mysticism. */
    SPIRITUAL,
    /** Elements related to trade, money, business, or legal matters. */
    COMMERCE,
    /** Elements related to hiding, deception, infiltration, or covert operations. */
    STEALTH;
}
