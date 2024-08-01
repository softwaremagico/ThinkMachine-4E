package com.softwaremagico.tm.character.benefices;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2024 Softwaremagico
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

import com.softwaremagico.tm.IElementRetriever;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.definition.RandomElementDefinition;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public final class AvailableBeneficeFactory implements IElementRetriever<AvailableBenefice> {
    private Map<String, AvailableBenefice> availableBenefices;
    private final Map<BeneficeDefinition, Set<AvailableBenefice>> availableBeneficesByDefinition;

    private AvailableBeneficeFactory() {
        availableBeneficesByDefinition = new HashMap<>();
    }

    private static final class AvailableBeneficeFactoryInit {
        public static final AvailableBeneficeFactory INSTANCE = new AvailableBeneficeFactory();
    }

    public static AvailableBeneficeFactory getInstance() {
        return AvailableBeneficeFactoryInit.INSTANCE;
    }

    public Map<String, AvailableBenefice> getAvailableBenefices() {
        if (availableBenefices == null) {
            try {
                getElements();
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        }
        return availableBenefices;
    }

    public Collection<AvailableBenefice> getElements() throws InvalidXmlElementException {
        if (availableBenefices == null) {
            availableBenefices = new HashMap<>();
            for (final BeneficeDefinition benefitDefinition : BeneficeDefinitionFactory.getInstance().getElements()) {
                if (benefitDefinition.getSpecializations().isEmpty()) {
                    for (final Integer cost : benefitDefinition.getCost()) {
                        final String id = benefitDefinition.getId() + (benefitDefinition.getCost().size() == 1 ? "" : "_" + cost);
                        final AvailableBenefice availableBenefice = new AvailableBenefice(id, benefitDefinition.getName(), benefitDefinition.getDescription(),
                                benefitDefinition, benefitDefinition.isAffliction(), cost, benefitDefinition.getRandomDefinition());
                        addAvailableBenefice(id, benefitDefinition, availableBenefice);
                    }
                } else {
                    for (final BeneficeSpecialization specialization : benefitDefinition.getSpecializations()) {
                        // Cost in specialization
                        if (specialization.getCost() != null) {
                            final String id = benefitDefinition.getId() + " [" + specialization.getId() + "]";
                            final AvailableBenefice availableBenefice = new AvailableBenefice(id, benefitDefinition.getName(),
                                    benefitDefinition.getDescription(), benefitDefinition, specialization.isAffliction(),
                                    specialization.getCost(),
                                    new RandomElementDefinition(benefitDefinition.getRandomDefinition(), specialization.getRandomDefinition()));
                            availableBenefice.setSpecialization(specialization);
                            addAvailableBenefice(id, benefitDefinition, availableBenefice);
                        } else {
                            for (final Integer cost : benefitDefinition.getCost()) {
                                final String id = benefitDefinition.getId() + (benefitDefinition.getCost().size() == 1 ? "" : "_" + cost) + " ["
                                        + specialization.getId() + "]";
                                final AvailableBenefice availableBenefice = new AvailableBenefice(id, benefitDefinition.getName(),
                                        benefitDefinition.getDescription(), benefitDefinition, specialization.isAffliction(), cost,
                                        new RandomElementDefinition(benefitDefinition.getRandomDefinition(), specialization.getRandomDefinition()));
                                availableBenefice.setSpecialization(specialization);
                                addAvailableBenefice(id, benefitDefinition, availableBenefice);
                            }
                        }
                    }
                }
            }
            availableBenefices = availableBenefices.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(new AvailableBeneficesComparator()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        }
        return availableBenefices.values();
    }

    private void addAvailableBenefice(String id, BeneficeDefinition beneficeDefinition,
                                      AvailableBenefice availableBenefice) {
        getAvailableBenefices().put(id, availableBenefice);
        availableBeneficesByDefinition.computeIfAbsent(beneficeDefinition, k -> new HashSet<>());
        availableBeneficesByDefinition.get(beneficeDefinition).add(availableBenefice);
    }


    public AvailableBenefice getElement(String beneficeId) throws InvalidXmlElementException {
        final AvailableBenefice availableBenefice = getAvailableBenefices().get(beneficeId);
        if (availableBenefice == null) {
            throw new InvalidBeneficeException("The benefice '" + beneficeId + "' does not exists.");
        }
        return availableBenefice;
    }

    public Set<AvailableBenefice> getAvailableBeneficesByDefinition(BeneficeDefinition beneficeDefinition)
            throws InvalidXmlElementException {
        // Force the load.
        if (availableBeneficesByDefinition == null) {
            getElements();
        }
        return availableBeneficesByDefinition.get(beneficeDefinition);
    }

    public Map<BeneficeDefinition, Set<AvailableBenefice>> getAvailableBeneficesByDefinition()
            throws InvalidXmlElementException {
        if (availableBeneficesByDefinition == null) {
            getElements();
        }
        return availableBeneficesByDefinition;
    }

    private static final class AvailableBeneficesComparator implements Comparator<AvailableBenefice>, Serializable {

        private static final long serialVersionUID = -6197075436977682790L;

        @Override
        public int compare(AvailableBenefice o1, AvailableBenefice o2) {
            if (!Objects.deepEquals(o1.getBeneficeDefinition().getName(), o2.getBeneficeDefinition().getName())) {
                return o1.getName().compareTo(o2.getName());
            }
            return o1.getCost() - o2.getCost();
        }

    }
}
