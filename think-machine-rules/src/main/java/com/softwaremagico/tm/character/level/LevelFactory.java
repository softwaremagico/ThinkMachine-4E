package com.softwaremagico.tm.character.level;

import com.softwaremagico.tm.character.CharacterPlayer;

public class LevelFactory {

    private static final class LevelFactoryInit {
        public static final LevelFactory INSTANCE = new LevelFactory();
    }

    public static LevelFactory getInstance() {
        return LevelFactory.LevelFactoryInit.INSTANCE;
    }

    public Level getElement(CharacterPlayer characterPlayer, int index) {
        if (index <= 1) {
            return null;
        }
        return new Level(characterPlayer, index);
    }
}
