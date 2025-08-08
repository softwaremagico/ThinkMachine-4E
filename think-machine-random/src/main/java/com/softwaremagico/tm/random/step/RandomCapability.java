package com.softwaremagico.tm.random.step;

import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.Collection;
import java.util.Set;

public class RandomCapability extends RandomSelector<Capability> {

    private final CapabilityOptions capabilityOptions;

    public RandomCapability(CharacterPlayer characterPlayer, Set<RandomPreference> preferences,
                            CapabilityOptions capabilityOptions) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.capabilityOptions = capabilityOptions;
    }

    @Override
    protected Collection<Capability> getAllElements() throws InvalidXmlElementException {
        return capabilityOptions.getOptions().stream().map(Option::getElement).toList();
    }

    @Override
    protected int getWeight(Capability element) throws InvalidRandomElementSelectedException {
        return super.getWeight(element);
    }
}
