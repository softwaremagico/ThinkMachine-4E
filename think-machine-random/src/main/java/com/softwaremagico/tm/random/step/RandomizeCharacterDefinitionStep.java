package com.softwaremagico.tm.random.step;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.exceptions.InvalidSelectionException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Set;

public class RandomizeCharacterDefinitionStep<T extends Element> {

    private final CharacterPlayer characterPlayer;
    private final CharacterDefinitionStepSelection characterDefinitionStepSelection;
    private final Set<RandomPreference> preferences;


    public RandomizeCharacterDefinitionStep(CharacterPlayer characterPlayer,
                                            CharacterDefinitionStepSelection characterDefinitionStepSelection,
                                            Set<RandomPreference> preferences) {
        this.characterPlayer = characterPlayer;
        this.characterDefinitionStepSelection = characterDefinitionStepSelection;
        this.preferences = preferences;
    }

    private CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
    }

    private Set<RandomPreference> getPreferences() {
        return preferences;
    }


    public void assign() throws InvalidSelectionException, InvalidRandomElementSelectedException {
        assignCharacteristics();
    }


    private void assignCharacteristics() throws InvalidRandomElementSelectedException {
        if (!characterDefinitionStepSelection.getCharacteristicOptions().isEmpty()) {
            for (int i = 0; i < characterDefinitionStepSelection.getCharacteristicOptions().size(); i++) {
                final RandomCharacteristicBonusOption randomCharacteristicBonusOption =
                        new RandomCharacteristicBonusOption(getCharacterPlayer(), getPreferences(),
                                characterDefinitionStepSelection.getCharacteristicOptions().get(i));

                for (int j = 0; j < characterDefinitionStepSelection.getCharacteristicOptions().get(i).getTotalOptions(); j++) {
                    characterDefinitionStepSelection.getSelectedCharacteristicOptions().get(i).getSelections()
                            .add(new Selection(randomCharacteristicBonusOption.selectElementByWeight().getId()));
                }
            }
        }
    }

}
