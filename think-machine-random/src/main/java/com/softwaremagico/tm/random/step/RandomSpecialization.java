package com.softwaremagico.tm.random.step;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RandomSpecialization extends RandomSelector<Specialization> {
    private final List<Specialization> specializations;

    public RandomSpecialization(CharacterPlayer characterPlayer, Set<RandomPreference> preferences,
                                List<Specialization> specializations) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.specializations = specializations;
    }


    @Override
    protected Collection<Specialization> getAllElements() throws InvalidXmlElementException {
        return specializations;
    }
}
