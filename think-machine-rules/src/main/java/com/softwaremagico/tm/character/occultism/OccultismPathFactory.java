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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class OccultismPathFactory extends XmlFactory<OccultismPath> {
    private static final String XML_FILE = "occultism_paths.xml";

    private final List<OccultismPath> psiPaths = new ArrayList<>();
    private final List<OccultismPath> theurgyPaths = new ArrayList<>();

    private static final class OccultismPathFactoryInit {
        public static final OccultismPathFactory INSTANCE = new OccultismPathFactory();
    }

    public static OccultismPathFactory getInstance() {
        return OccultismPathFactoryInit.INSTANCE;
    }


    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<OccultismPath> getElements() throws InvalidXmlElementException {
        return readXml(OccultismPath.class);
    }

    @Override
    public List<OccultismPath> readXml(Class<OccultismPath> entityClass) throws InvalidXmlElementException {
        final List<OccultismPath> occultismPaths = super.readXml(entityClass);
        //Update power values related to rites.
        occultismPaths.forEach(occultismPath -> {
            occultismPath.getOccultismPowersElements().forEach(occultismPower -> {
                occultismPower.getRestrictions().setRestrictedToSpecies(occultismPath.getRestrictions().getRestrictedToSpecies());
                occultismPower.getRestrictions().setRestrictedToFactions(occultismPath.getRestrictions().getRestrictedToFactions());
                occultismPower.getRestrictions().setRestrictedToUpbringing(occultismPath.getRestrictions().getRestrictedToUpbringing());
                occultismPower.setOfficial(occultismPath.isOfficial());
            });
        });
        return occultismPaths;
    }

    public OccultismPath getOccultismPath(OccultismPower power) {
        try {
            for (final OccultismPath occultismPath : getElements()) {
                if (occultismPath.getOccultismPowers().containsKey(power.getId())) {
                    return occultismPath;
                }
            }
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
        }
        return null;
    }

    public List<OccultismPath> getPsiPaths() {
        if (psiPaths.isEmpty()) {
            try {
                for (final OccultismPath path : getElements()) {
                    if (OccultismTypeFactory.getPsi() != null) {
                        if (Objects.equals(path.getOccultismType(), OccultismTypeFactory.getPsi().getId())) {
                            psiPaths.add(path);
                        }
                    }
                }
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return Collections.unmodifiableList(psiPaths);
    }

    public List<OccultismPath> getTheurgyPaths() {
        if (theurgyPaths.isEmpty()) {
            try {
                for (final OccultismPath path : getElements()) {
                    if (OccultismTypeFactory.getTheurgy() != null) {
                        if (Objects.equals(path.getOccultismType(), OccultismTypeFactory.getTheurgy().getId())) {
                            theurgyPaths.add(path);
                        }
                    }
                }
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return Collections.unmodifiableList(theurgyPaths);
    }
}
