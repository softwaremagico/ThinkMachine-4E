package com.softwaremagico.tm.random.step;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.character.values.Phase;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RandomCapabilityOption extends RandomSelector<CapabilityOption> {

    private final CapabilityOptions capabilityOptions;
    private final Phase phase;

    public RandomCapabilityOption(CharacterPlayer characterPlayer, Set<RandomPreference> preferences,
                                  CapabilityOptions capabilityOptions, Phase phase) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
        this.capabilityOptions = capabilityOptions;
        this.phase = phase;
    }

    @Override
    protected int getWeight(CapabilityOption element) throws InvalidRandomElementSelectedException {
        if (getCharacterPlayer().hasCapability(element.getId(), element.getSelectedSpecialization() != null
                ? element.getSelectedSpecialization().getId() : null, phase)) {
            return 0;
        }
        //Better to know your own planet!
        if (Objects.equals(element.getId(), PLANET_LORE)) {
            if (element.getSelectedSpecialization() != null
                    && Objects.equals(element.getSelectedSpecialization().getId(), getCharacterPlayer().getInfo().getPlanet())) {
                return GOOD_PROBABILITY;
            } else {
                return RARE_PROBABILITY;
            }
        }
        //Better to know your own faction!
        if (Objects.equals(element.getId(), FACTION_LORE)) {
            if (element.getSelectedSpecialization() != null && getCharacterPlayer().getFaction() != null
                    && Objects.equals(element.getSelectedSpecialization().getId(), getCharacterPlayer().getFaction().getId())) {
                return GOOD_PROBABILITY;
            } else {
                return RARE_PROBABILITY;
            }
        }
        return super.getWeight(element);
    }

    @Override
    protected Collection<CapabilityOption> getAllElements() throws InvalidXmlElementException {
        final List<CapabilityOption> capabilities = new ArrayList<>();
        //Separate each capability with specialization in multiples capabilities.
        for (CapabilityOption capabilityOption : capabilityOptions.getOptions()) {
            if (capabilityOption.getId() != null) {
                capabilities.addAll(extendCapabilitiesBySpecialization(capabilityOption));
            } else if (capabilityOption.getGroup() != null) {
                for (Capability capability : CapabilityFactory.getInstance().getElementsByGroup(capabilityOption.getGroup())) {
                    capabilities.addAll(extendCapabilitiesBySpecialization(capability));
                }
            }
        }
        return capabilities;
    }

    private List<CapabilityOption> extendCapabilitiesBySpecialization(CapabilityOption capability) {
        final List<CapabilityOption> capabilities = new ArrayList<>();
        if (capability.getSelectedSpecialization() == null) {
            final Capability sourceCapability = CapabilityFactory.getInstance().getElement(capability);
            if (sourceCapability.getSpecializations() != null && !sourceCapability.getSpecializations().isEmpty()) {
                return extendCapabilitiesBySpecialization(sourceCapability);
            }
        }
        capabilities.add(capability);
        return capabilities;
    }

    private List<CapabilityOption> extendCapabilitiesBySpecialization(Capability capability) {
        final List<CapabilityOption> capabilities = new ArrayList<>();
        if (capability.getSpecializations() != null && !capability.getSpecializations().isEmpty()) {
            for (Specialization specialization : capability.getSpecializations()) {
                capabilities.add(new CapabilityOption(capability, specialization));
            }
        } else {
            capabilities.add(new CapabilityOption(capability));
        }
        return capabilities;
    }
}
