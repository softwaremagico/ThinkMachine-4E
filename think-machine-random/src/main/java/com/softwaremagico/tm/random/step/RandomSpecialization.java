package com.softwaremagico.tm.random.step;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RandomSpecialization extends RandomSelector<Specialization> {
    private final List<Specialization> specializations;
    private final String capability;

    public RandomSpecialization(CharacterPlayer characterPlayer, Set<RandomPreference> preferences,
                                List<Specialization> specializations, String capability) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.specializations = specializations;
        this.capability = capability;
    }


    @Override
    protected Collection<Specialization> getAllElements() throws InvalidXmlElementException {
        return specializations;
    }


    @Override
    protected int getWeight(Specialization element) throws InvalidRandomElementSelectedException {
        //Selected specializations cannot be reselected.
        if (getCharacterPlayer().hasCapability(capability, element.getId())) {
            return 0;
        }
        return super.getWeight(element);
    }
}
