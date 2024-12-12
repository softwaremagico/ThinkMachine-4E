package com.softwaremagico.tm.character.cybernetics;

import com.softwaremagico.tm.ElementList;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Settings;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.exceptions.InvalidCyberdeviceException;

public class Cybernetics extends ElementList<Cyberdevice> {

    public static int getMaxCyberneticIncompatibility(CharacterPlayer characterPlayer) {
        return (characterPlayer.getCharacteristicValue(CharacteristicName.WILL) * 3) + 2;
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
}
