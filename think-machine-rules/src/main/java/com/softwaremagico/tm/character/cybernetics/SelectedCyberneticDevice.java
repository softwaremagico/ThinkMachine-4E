package com.softwaremagico.tm.character.cybernetics;

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


import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.character.values.IElementWithBonification;
import com.softwaremagico.tm.character.values.StaticValue;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SelectedCyberneticDevice extends Element<SelectedCyberneticDevice> implements ICyberneticDevice,
        IElementWithBonification {

    private List<String> customizations;
    private final CyberneticDevice cyberneticDevice;

    public SelectedCyberneticDevice(CyberneticDevice cyberneticDevice) {
        super(cyberneticDevice.getId(), cyberneticDevice.getName(), cyberneticDevice.getDescription(), cyberneticDevice.getLanguage(),
                cyberneticDevice.getModuleName());
        this.cyberneticDevice = cyberneticDevice;
        customizations = new ArrayList<>();
    }

    @Override
    public int getPoints() {
        int basicPoints = getCyberneticDevice().getPoints();
        for (final String customization : customizations) {
            try {
                final CyberneticDeviceTrait cyberneticDeviceTrait = CyberneticDeviceTraitFactory.getInstance().getElement(customization);
                basicPoints += cyberneticDeviceTrait.getExtraPoints();
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        }
        return basicPoints;
    }

    @Override
    public int getIncompatibility() {
        int basicIncompatibility = getCyberneticDevice().getIncompatibility();
        for (final String customization : customizations) {
            try {
                final CyberneticDeviceTrait cyberneticDeviceTrait = CyberneticDeviceTraitFactory.getInstance().getElement(customization);
                basicIncompatibility += cyberneticDeviceTrait.getExtraIncompatibility();
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        }
        return basicIncompatibility;
    }

    @Override
    public String getTrait(CyberneticDeviceTraitCategory category) {
        for (final String customization : customizations) {
            try {
                final CyberneticDeviceTrait cyberneticDeviceTrait = CyberneticDeviceTraitFactory.getInstance().getElement(customization);
                if (cyberneticDeviceTrait.getCategory().equals(category)) {
                    return customization;
                }
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        }
        return getCyberneticDevice().getTrait(category);
    }

    public List<String> getCustomizations() {
        return customizations;
    }

    @Override
    public int getCost() {
        int basicCost = getCyberneticDevice().getCost();
        for (final String customization : customizations) {
            try {
                final CyberneticDeviceTrait cyberneticDeviceTrait = CyberneticDeviceTraitFactory.getInstance().getElement(customization);
                basicCost *= (int) cyberneticDeviceTrait.getExtraCostMultiplier();
                basicCost += cyberneticDeviceTrait.getExtraCost();
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        }
        return basicCost;
    }

    @Override
    public int getTechLevel() {
        int techLevel = getCyberneticDevice().getTechLevel();
        for (final String customization : customizations) {
            try {
                techLevel = Math.max(techLevel, CyberneticDeviceTraitFactory.getInstance().getElement(customization).getMinimumTechLevel());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        }
        return techLevel;
    }

    public CyberneticDevice getCyberneticDevice() {
        return cyberneticDevice;
    }

    @Override
    public String getWeapon() {
        return getCyberneticDevice().getWeapon();
    }

    @Override
    public Set<StaticValue> getStaticValues() {
        return getCyberneticDevice().getStaticValues();
    }

    @Override
    public List<String> getTraits() {
        final List<String> traits = new ArrayList<>(getCyberneticDevice().getTraits());
        for (final String trait : getCyberneticDevice().getTraits()) {
            for (final String customization : getCustomizations()) {
                try {
                    if (CyberneticDeviceTraitFactory.getInstance().getElement(customization).getCategory()
                            == CyberneticDeviceTraitFactory.getInstance().getElement(trait).getCategory()) {
                        traits.remove(trait);
                    }
                } catch (InvalidXmlElementException e) {
                    MachineLog.errorMessage(this.getClass(), e);
                }
            }
        }
        traits.addAll(customizations);
        Collections.sort(traits);
        return traits;
    }

    @Override
    public String getRequirement() {
        return getCyberneticDevice().getRequirement();
    }

    @Override
    public Set<Bonification> getBonifications() {
        return getCyberneticDevice().getBonifications();
    }

    public void addCustomization(CyberneticDeviceTrait trait) {
        if (trait != null) {
            customizations.add(trait.getId());
        }
    }

    @Override
    public String toString() {
        return getCyberneticDevice().getId();
    }

    public void setCustomizations(List<String> customizations) {
        this.customizations = customizations;
    }


    @Override
    public boolean isOfficial() {
        return cyberneticDevice.isOfficial() && customizations.stream().allMatch(customization -> {
            try {
                return CyberneticDeviceTraitFactory.getInstance().getElement(customization).isOfficial();
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
            return false;
        });
    }

}
