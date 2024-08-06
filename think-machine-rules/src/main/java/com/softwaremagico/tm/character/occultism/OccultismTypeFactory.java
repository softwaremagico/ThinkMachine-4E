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

import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import com.softwaremagico.tm.xml.XmlFactory;

import java.util.List;

public final class OccultismTypeFactory extends XmlFactory<OccultismType> {
    private static final String XML_FILE = "occultism_types.xml";

    private static final String DARK_SIDE = "darkSide";
    public static final String PSI_TAG = "psi";
    public static final String THEURGY_TAG = "theurgy";

    private static final class OccultismTypeFactoryInit {
        public static final OccultismTypeFactory INSTANCE = new OccultismTypeFactory();
    }

    public static OccultismTypeFactory getInstance() {
        return OccultismTypeFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<OccultismType> getElements() throws InvalidXmlElementException {
        return readXml(OccultismType.class);
    }

    public static OccultismType getPsi() {
        try {
            return OccultismTypeFactory.getInstance().getElements(PSI_TAG);
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(OccultismTypeFactory.class.getName(), e);
            return null;
        }
    }

    public static OccultismType getTheurgy() {
        try {
            return OccultismTypeFactory.getInstance().getElements(THEURGY_TAG);
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(OccultismTypeFactory.class.getName(), e);
            return null;
        }
    }
}
