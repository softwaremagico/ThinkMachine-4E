package com.softwaremagico.tm.exceptions;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2024 Softwaremagico
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

public class MaxValueExceededException extends RuntimeException {
    private static final long serialVersionUID = -6952414025114964424L;
    private final String element;
    private final int bonus;
    private final int maxValue;

    public MaxValueExceededException(String message, String element, int bonus, int maxValue) {
        super(message);
        this.element = element;
        this.bonus = bonus;
        this.maxValue = maxValue;
    }

    public MaxValueExceededException(String message, String element, Throwable e) {
        super(message, e);
        this.element = element;
        this.bonus = 0;
        this.maxValue = 0;
    }

    public int getBonus() {
        return bonus;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public String getElement() {
        return element;
    }
}
