package com.softwaremagico.tm.character.occultism;


import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.Settings;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class Occultism {
    private final Map<String, Integer> psiqueValue;
    private final Map<String, Integer> darkSideValue;
    private Wyrd extraWyrd;

    // Path --> Set<Power>
    private final Map<String, List<OccultismPower>> selectedPowers;

    public Occultism() {
        selectedPowers = new HashMap<>();
        psiqueValue = new HashMap<>();
        darkSideValue = new HashMap<>();
    }

    public Wyrd getExtraWyrd() {
        return extraWyrd;
    }

    public void setExtraWyrd(int extraWyrd, String language, String moduleName) {
        if (extraWyrd > 0) {
            this.extraWyrd = new Wyrd(language, moduleName, extraWyrd);
        } else {
            this.extraWyrd = null;
        }
    }

    public int getPsiqueLevel(OccultismType occultismType) {
        if (psiqueValue.get(occultismType.getId()) != null) {
            return psiqueValue.get(occultismType.getId());
        }
        return 0;
    }

    public void setPsiqueLevel(OccultismType occultismType, int psyValue,
                               Race race) throws InvalidPsiqueLevelException {
        if (psyValue < 0) {
            throw new InvalidPsiqueLevelException("Psique level cannot be less than zero.");
        }
        AvailableBenefice noOccult = null;
        AvailableBenefice noPsi = null;
        try {
            noOccult = AvailableBeneficeFactory.getInstance().getElement("noOccult");
        } catch (InvalidXmlElementException e) {
            // Module without noOccult benefice.
        }
        try {
            noPsi = AvailableBeneficeFactory.getInstance().getElement("noPsi");
        } catch (InvalidXmlElementException e) {
            // Module without noOccult benefice.
        }

        if (psyValue != 0 && (race == null || race.getBenefices().contains(noOccult))) {
            throw new InvalidPsiqueLevelException("Race '" + race + "' cannot have psique levels.");
        }
        if (Objects.equals(occultismType, OccultismTypeFactory.getPsi()) && psyValue != 0
                && race.getBenefices().contains(noPsi)) {
            throw new InvalidPsiqueLevelException("Race '" + race + "' cannot have psi levels.");
        }
        psiqueValue.put(occultismType.getId(), psyValue);
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

    public void canAddPower(OccultismPath path, OccultismPower power, String language, Faction faction, Race race, Settings settings)
            throws InvalidOccultismPowerException {
        if (power == null) {
            throw new InvalidOccultismPowerException("Power cannot be null.");
        }
        if (faction == null && settings.isRestrictionsChecked()) {
            throw new InvalidOccultismPowerException("Faction cannot be null.");
        }
        // Correct level of psi or theurgy
        try {
            if (power.getLevel() > getPsiqueLevel(OccultismTypeFactory.getInstance().getElement(path.getOccultismType()))) {
                throw new InvalidPsiqueLevelException("Insufficient psi/theurgy level to acquire '" + power + "'.");
            }
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(this.getClass(), e);
        }
        // Limited to some factions
        if (!path.getFactionsAllowed().isEmpty() && settings.isRestrictionsChecked() && !path.getFactionsAllowed().contains(faction)) {
            throw new InvalidFactionOfPowerException("Power '" + power + "' can only be acquired by  '"
                    + path.getFactionsAllowed() + "' character faction is '" + faction + "'.");
        }
        // Limited to some races
        if (!path.getRestrictedToRaces().isEmpty() && settings.isRestrictionsChecked() && !path.getRestrictedToRaces().contains(race)) {
            throw new InvalidFactionOfPowerException("Power '" + power + "' can only be acquired by  '"
                    + path.getFactionsAllowed() + "' character faction is '" + faction + "'.");
        }

        // Psi must have previous level.
        if (Objects.equals(path.getOccultismType(), OccultismTypeFactory.getPsi())) {
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

    public boolean addPower(OccultismPath path, OccultismPower power, String language, Faction faction, Race race, Settings settings)
            throws InvalidOccultismPowerException {
        canAddPower(path, power, language, faction, race, settings);
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
}
