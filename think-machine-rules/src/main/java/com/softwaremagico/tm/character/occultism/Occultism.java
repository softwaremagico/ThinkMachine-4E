package com.softwaremagico.tm.character.occultism;

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


import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Settings;
import com.softwaremagico.tm.exceptions.InvalidFactionOfPowerException;
import com.softwaremagico.tm.exceptions.InvalidNumberOfPowersException;
import com.softwaremagico.tm.exceptions.InvalidOccultismPowerException;
import com.softwaremagico.tm.exceptions.InvalidPowerLevelException;
import com.softwaremagico.tm.exceptions.InvalidPsiqueLevelException;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;


public class Occultism {
    private final Map<String, Integer> darkSideValue;

    // Path --> Set<Power>
    private final Map<String, List<OccultismPower>> selectedPowers;

    public Occultism() {
        selectedPowers = new HashMap<>();
        darkSideValue = new HashMap<>();
    }

    public int getDarkSideLevel(OccultismType occultismType) {
        if (darkSideValue.get(occultismType.getId()) != null) {
            return darkSideValue.get(occultismType.getId());
        }
        return 0;
    }

    public void setDarkSideLevel(OccultismType occultismType, int darkSideValue) {
        this.darkSideValue.put(occultismType.getId(), darkSideValue);
    }

    public Map<String, List<OccultismPower>> getSelectedPowers() {
        return selectedPowers;
    }

    public int getTotalSelectedPowers() {
        int total = 0;
        for (final Entry<String, List<OccultismPower>> entry : getSelectedPowers().entrySet()) {
            if (entry.getValue() != null) {
                total += entry.getValue().size();
            }
        }
        return total;
    }

    public int getTotalSelectedPaths() {
        return getSelectedPowers().keySet().size();
    }

    public void canAddPower(CharacterPlayer characterPlayer, OccultismPath path, OccultismPower power, String faction, String specie, Settings settings)
            throws InvalidOccultismPowerException {
        if (power == null) {
            throw new InvalidOccultismPowerException("Power cannot be null.");
        }
        if (faction == null && settings.isRestrictionsChecked()) {
            throw new InvalidOccultismPowerException("Faction cannot be null.");
        }
        // Correct level of psi or theurgy
        try {
            if (power.getOccultismLevel() > characterPlayer.getCharacteristicValue(path.getOccultismType())) {
                throw new InvalidPsiqueLevelException("Insufficient psi/theurgy level to acquire '" + power + "'.");
            }
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(this.getClass(), e);
        }
        //Enough capability points.
        if (characterPlayer.getOccultismPoints() <= countPowers()) {
            throw new InvalidNumberOfPowersException("Invalid perk numbers for acquiring a new occultism power. Allowed points are '" + characterPlayer.getOccultismPoints()
                    + "' and current number of powers is '" + countPowers() + "'");
        }
        // Limited to some factions
        if (path.getFactionsAllowed() != null && !path.getFactionsAllowed().isEmpty() && settings.isRestrictionsChecked() && faction != null
                && !path.getFactionsAllowed().contains(faction)) {
            throw new InvalidFactionOfPowerException("Power '" + power + "' can only be acquired by  '"
                    + path.getFactionsAllowed() + "' character faction is '" + faction + "'.");
        }
        // Limited to some species
        if (!path.getRestrictions().getRestrictedToSpecies().isEmpty() && settings.isRestrictionsChecked() && specie != null
                && !path.getRestrictions().getRestrictedToSpecies().contains(specie)) {
            throw new InvalidFactionOfPowerException("Power '" + power + "' can only be acquired by  '"
                    + path.getFactionsAllowed() + "' character faction is '" + faction + "'.");
        }

        // Psi must have previous level.
        if (OccultismTypeFactory.getPsi() != null && Objects.equals(path.getOccultismType(), OccultismTypeFactory.getPsi().getId())) {
            boolean acquiredLevel = false;
            for (final OccultismPower previousLevelPower : path.getPreviousLevelPowers(power)) {
                if (selectedPowers.get(path.getId()) != null
                        && selectedPowers.get(path.getId()).contains(previousLevelPower)) {
                    acquiredLevel = true;
                    break;
                }
            }
            if (!acquiredLevel && !path.getPreviousLevelPowers(power).isEmpty()) {
                throw new InvalidPowerLevelException(
                        "At least one power from '" + path.getPreviousLevelPowers(power) + "' must be selected.");
            }
        }
    }

    public boolean addPower(CharacterPlayer characterPlayer, OccultismPath path, OccultismPower power, String faction, String specie, Settings settings)
            throws InvalidOccultismPowerException {
        canAddPower(characterPlayer, path, power, faction, specie, settings);
        selectedPowers.computeIfAbsent(path.getId(), k -> new ArrayList<>());
        return selectedPowers.get(path.getId()).add(power);
    }

    public boolean removePower(OccultismPath path, OccultismPower power) {
        selectedPowers.computeIfAbsent(path.getId(), k -> new ArrayList<>());
        return selectedPowers.get(path.getId()).remove(power);

    }

    public boolean hasPower(OccultismPath path, OccultismPower power) {
        if (selectedPowers.get(path.getId()) == null) {
            return false;
        }
        return selectedPowers.get(path.getId()).contains(power);
    }

    public boolean hasPath(OccultismPath path) {
        return selectedPowers.containsKey(path.getId());
    }

    public int countPowers() {
        return (int) selectedPowers.values().stream().mapToLong(Collection::size).sum();
    }
}
