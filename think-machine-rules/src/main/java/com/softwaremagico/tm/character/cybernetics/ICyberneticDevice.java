package com.softwaremagico.tm.character.cybernetics;

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

import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.values.IElementWithBonification;
import com.softwaremagico.tm.character.values.StaticValue;

import java.util.List;
import java.util.Set;

public interface ICyberneticDevice extends IElementWithBonification {

    TranslatedText getName();

    String getWeapon();

    int getCost();

    Set<StaticValue> getStaticValues();

    List<String> getTraits();

    int getTechLevel();

    String getRequirement();

    String getTrait(CyberneticDeviceTraitCategory category);

    int getIncompatibility();

    int getPoints();

}
