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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.List;

public final class TheurgyComponentFactory extends XmlFactory<TheurgyComponent> {
    private static final String XML_FILE = "theurgy_components.xml";

    private static final class TheurgyComponentFactoryInit {
        public static final TheurgyComponentFactory INSTANCE = new TheurgyComponentFactory();
    }

    public static TheurgyComponentFactory getInstance() {
        return TheurgyComponentFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<TheurgyComponent> getElements() throws InvalidXmlElementException {
        return readXml(TheurgyComponent.class);
    }

    public TheurgyComponent getTheurgyComponent(char code)
            throws InvalidXmlElementException {
        for (final TheurgyComponent theurgyComponent : getElements()) {
            if (theurgyComponent.getCode() == code) {
                return theurgyComponent;
            }
        }
        return null;
    }
}
