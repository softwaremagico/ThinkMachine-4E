package com.softwaremagico.tm.random.step;

/*-
 * #%L
 * Think Machine 4E (Random Generator)
 * %%
 * Copyright (C) 2017 - 2025 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.character.capabilities.CapabilityFactory;
import com.softwaremagico.tm.character.capabilities.CapabilityOption;
import com.softwaremagico.tm.character.capabilities.CapabilityOptions;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.random.character.selectors.RandomPreference;
import com.softwaremagico.tm.random.character.selectors.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        final List<Capability> capabilities = new ArrayList<Capability>();
        for (CapabilityOption capabilityOption : capabilityOptions.getOptions()) {
            if (capabilityOption.getId() != null) {
                capabilities.add(capabilityOption.getElement());
            } else if (capabilityOption.getGroup() != null) {
                capabilities.addAll(CapabilityFactory.getInstance().getElementsByGroup(capabilityOption.getGroup()));
            }
        }
        return capabilities;
    }
}
