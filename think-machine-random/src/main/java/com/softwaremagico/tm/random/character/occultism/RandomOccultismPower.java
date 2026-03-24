package com.softwaremagico.tm.random.character.occultism;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.exceptions.InvalidSpecieException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.random.character.selectors.AssignableRandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.preferences.IRandomPreference;
import com.softwaremagico.tm.random.preferences.RandomSelector;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RandomOccultismPower extends RandomSelector<OccultismPower> implements AssignableRandomSelector {

    public RandomOccultismPower(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidSpecieException, InvalidRandomElementSelectedException, UnofficialElementNotAllowedException {
        while (!getAllElements().isEmpty()
                && getCharacterPlayer().getOccultismPointsAvailable() > getCharacterPlayer().getOccultismPointsSpent()) {
            try {
                getCharacterPlayer().addOccultismPower(selectElementByWeight());
            } catch (InvalidXmlElementException | InvalidRandomElementSelectedException e) {
                System.out.println(getWeightedElements());
                break;
            }
        }
    }

    @Override
    protected Collection<OccultismPower> getAllElements() throws InvalidXmlElementException {
        if (getCharacterPlayer().getOccultismType() != null
                && Objects.equals(getCharacterPlayer().getOccultismType().getId(), OccultismTypeFactory.PSI_TAG)) {
            return OccultismPathFactory.getInstance().getPsiPowers();
        }
        if (getCharacterPlayer().getOccultismType() != null
                && Objects.equals(getCharacterPlayer().getOccultismType().getId(), OccultismTypeFactory.THEURGY_TAG)) {
            return OccultismPathFactory.getInstance().getTheurgyPowers();
        }
        return List.of();
    }

    @Override
    protected int getWeight(OccultismPower occultismPower) throws InvalidRandomElementSelectedException {
        if (occultismPower.getOccultismLevel() > getCharacterPlayer().getOccultismLevel()) {
            throw new InvalidRandomElementSelectedException("OccultismPower '" + occultismPower + "' restricted to level '"
                    + occultismPower.getOccultismLevel()
                    + "'. Character max allowed level is '" + getCharacterPlayer().getOccultismLevel() + "'.");
        }
        if (!getCharacterPlayer().canAddOccultismPower(occultismPower)) {
            throw new InvalidRandomElementSelectedException("OccultismPower '" + occultismPower + "' is invalid.'");
        }
        return super.getWeight(occultismPower);
    }
}
