package com.softwaremagico.tm.random.character.level;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.level.Level;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.AssignableRandomSelector;
import com.softwaremagico.tm.random.character.selectors.RandomInnerStepsSelector;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.step.RandomizeCharacterDefinitionStep;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RandomLevel extends RandomSelector<Level> implements AssignableRandomSelector, RandomInnerStepsSelector {
    private LevelSelector levelSelector;

    public RandomLevel(CharacterPlayer characterPlayer, Set<RandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }


    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        levelSelector = getCharacterPlayer().addLevel();
    }

    @Override
    public void complete() throws InvalidSpecieException, InvalidRandomElementSelectedException {
        final RandomizeCharacterDefinitionStep randomizeCharacterDefinitionStep = new RandomizeCharacterDefinitionStep(
                getCharacterPlayer(),
                levelSelector,
                getPreferences()
        );

        randomizeCharacterDefinitionStep.assign();
    }

    @Override
    protected Collection<Level> getAllElements() throws InvalidXmlElementException {
        //Levels are auto-generated.
        return List.of();
    }
}
