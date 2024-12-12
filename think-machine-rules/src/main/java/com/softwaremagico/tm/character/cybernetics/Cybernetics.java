package com.softwaremagico.tm.character.cybernetics;

import com.softwaremagico.tm.ElementList;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;

public class Cybernetics extends ElementList<Cyberdevice> {

    public static int getMaxCyberneticIncompatibility(CharacterPlayer characterPlayer) {
        return (characterPlayer.getCharacteristicValue(CharacteristicName.WILL) * 3) + 2;
    }
}
