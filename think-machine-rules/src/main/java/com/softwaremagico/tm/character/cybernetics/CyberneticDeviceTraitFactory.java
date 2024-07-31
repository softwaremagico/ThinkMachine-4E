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

import com.softwaremagico.tm.xml.XmlFactory;

import java.io.IOException;
import java.util.List;

public final class CyberneticDeviceTraitFactory extends XmlFactory<CyberneticDeviceTrait> {

    private static final class CyberneticDeviceTraitFactoryInit {
        public static final CyberneticDeviceTraitFactory INSTANCE = new CyberneticDeviceTraitFactory();
    }

    public static CyberneticDeviceTraitFactory getInstance() {
        return CyberneticDeviceTraitFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return "cybernetics_traits.xml";
    }

    @Override
    public List<CyberneticDeviceTrait> getElements() throws IOException {
        return readXml(CyberneticDeviceTrait.class);
    }
}
