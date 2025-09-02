package com.softwaremagico.tm.random.step;

import com.softwaremagico.tm.character.CharacterDefinitionStepSelection;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.level.LevelSelector;
import com.softwaremagico.tm.character.perks.CharacterPerkOptions;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.List;
import java.util.Set;

public class RandomizeCharacterLevelStep extends RandomizeCharacterDefinitionStep {
    private final LevelSelector levelSelector;

    public RandomizeCharacterLevelStep(CharacterPlayer characterPlayer, CharacterDefinitionStepSelection characterDefinitionStepSelection,
                                       Set<RandomPreference> preferences) {
        super(characterPlayer, characterDefinitionStepSelection, preferences);
        this.levelSelector = (LevelSelector) characterDefinitionStepSelection;
    }

    @Override
    protected void assignPerks() throws InvalidRandomElementSelectedException {
        assignClassPerks();
        assignCallingPerks();
    }

    private void assignClassPerks() throws InvalidRandomElementSelectedException {
        final List<CharacterPerkOptions> perkOptions = levelSelector.getNotRepeatedClassPerksOptions();

        if (perkOptions != null && !perkOptions.isEmpty()) {
            for (int i = 0; i < perkOptions.size(); i++) {
                try {
                    for (int j = levelSelector.getSelectedClassPerksOptions().get(i).getSelections().size();
                         j < perkOptions.get(i).getTotalOptions(); j++) {
                        final RandomPerk randomPerk =
                                new RandomPerk(getCharacterPlayer(), getPreferences(),
                                        perkOptions.get(i),
                                        levelSelector.getPhase(), levelSelector.getLevel());
                        levelSelector.getSelectedClassPerksOptions().get(i).getSelections()
                                .add(randomPerk.selectElementByWeight());
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidXmlElementException("Error on perks options '"
                            + perkOptions.get(i) + "' from level.", e);
                }
            }
        }
    }

    private void assignCallingPerks() throws InvalidRandomElementSelectedException {
        final List<CharacterPerkOptions> perkOptions = levelSelector.getNotRepeatedCallingPerksOptions();

        if (perkOptions != null && !perkOptions.isEmpty()) {
            for (int i = 0; i < perkOptions.size(); i++) {
                try {
                    for (int j = levelSelector.getSelectedCallingPerksOptions().get(i).getSelections().size();
                         j < perkOptions.get(i).getTotalOptions(); j++) {
                        final RandomPerk randomPerk =
                                new RandomPerk(getCharacterPlayer(), getPreferences(),
                                        perkOptions.get(i),
                                        levelSelector.getPhase(), levelSelector.getLevel());
                        levelSelector.getSelectedCallingPerksOptions().get(i).getSelections()
                                .add(randomPerk.selectElementByWeight());
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidXmlElementException("Error on perks options '"
                            + perkOptions.get(i) + "' from level.", e);
                }
            }
        }
    }

}
