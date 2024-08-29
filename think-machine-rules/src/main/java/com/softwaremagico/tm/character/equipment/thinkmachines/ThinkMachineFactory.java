package com.softwaremagico.tm.character.equipment.thinkmachines;

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

import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.List;

public class ThinkMachineFactory extends XmlFactory<ThinkMachine> {
    private static final String XML_FILE = "think_machines.xml";

    private static final class ThinkMachineFactoryInit {
        public static final ThinkMachineFactory INSTANCE = new ThinkMachineFactory();
    }

    public static ThinkMachineFactory getInstance() {
        return ThinkMachineFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<ThinkMachine> getElements() throws InvalidXmlElementException {
        return readXml(ThinkMachine.class);
    }
}