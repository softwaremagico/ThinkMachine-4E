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


import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CyberneticDeviceFactory extends XmlFactory<CyberneticDevice> {
    private static final String XML_FILE = "cybernetics.xml";
    //Id --> devices.
    private Map<String, Set<CyberneticDevice>> requiredBy;

    private static final class CyberneticDeviceFactoryInit {
        public static final CyberneticDeviceFactory INSTANCE = new CyberneticDeviceFactory();
    }

    public static CyberneticDeviceFactory getInstance() {
        return CyberneticDeviceFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<CyberneticDevice> getElements() throws InvalidXmlElementException {
        return readXml(CyberneticDevice.class);
    }


    public Set<CyberneticDevice> getDevicesThatRequires(CyberneticDevice device) {
        if (requiredBy == null) {
            initializeRequirements();
        }

        final Set<CyberneticDevice> requiredDevice = new HashSet<>();
        if (requiredBy.get(device.getId()) != null) {
            requiredDevice.addAll(requiredBy.get(device.getId()));
        }
        return requiredDevice;
    }


    private void initializeRequirements() {
        requiredBy = new HashMap<>();
        try {
            for (final CyberneticDevice device : getElements()) {
                addRequirement(device);
            }
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
        }
    }


    private void addRequirement(CyberneticDevice device) {
        if (device == null) {
            return;
        }
        if (device.getRequirement() != null) {
            requiredBy.computeIfAbsent(device.getRequirement(), k -> new HashSet<>());
            requiredBy.get(device.getRequirement()).add(device);
        }
    }
}
