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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Settings;
import com.softwaremagico.tm.exceptions.InvalidCyberdeviceException;

import java.util.ArrayList;
import java.util.List;

public class Cybernetics {

    private List<Cyberdevice> elements = new ArrayList<>();

    public List<Cyberdevice> getElements() {
        return elements;
    }

    public void setElements(List<Cyberdevice> elements) {
        this.elements = elements;
    }

    public void canAddDevice(CharacterPlayer characterPlayer, Cyberdevice cyberdevice, Settings settings)
            throws InvalidCyberdeviceException {
        if (cyberdevice == null) {
            throw new InvalidCyberdeviceException("Cyberdevice cannot be null.");
        }
        //Enough perks points.
        if (characterPlayer.getCyberneticsPointsAvailable() <= characterPlayer.getCyberneticsPointsSpent()) {
            throw new InvalidCyberdeviceException("Invalid perk numbers for acquiring a new cyberdevice. Allowed points are '"
                    + characterPlayer.getCyberneticsPointsAvailable() + "' and current number of powers is '"
                    + characterPlayer.getCyberneticsPointsSpent() + "'");
        }

        if (cyberdevice.getRestrictions().isRestricted(characterPlayer)) {
            throw new InvalidCyberdeviceException("Cyberdevice '" + cyberdevice + "' is restricted to  '" + cyberdevice.getRestrictions() + "'.");
        }
    }

    public boolean hasDevice(Cyberdevice cyberdevice) {
        return getElements().contains(cyberdevice);
    }
}
