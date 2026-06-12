package com.softwaremagico.tm.character.occultism;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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
    private final List<OccultismPower> psiPowers = new ArrayList<>();
    private final List<OccultismPath> theurgyPaths = new ArrayList<>();
    private final List<OccultismPower> theurgyPowers = new ArrayList<>();
    private List<OccultismPath> occultismPaths = null;

    private static final class OccultismPathFactoryInit {
        public static final OccultismPathFactory INSTANCE = new OccultismPathFactory();
    }

    public static OccultismPathFactory getInstance() {
        return OccultismPathFactoryInit.INSTANCE;
    }

    @Override
    public void reset() {
        super.reset();
        this.occultismPaths = null;
        this.psiPaths.clear();
        this.psiPowers.clear();
        this.theurgyPaths.clear();
        this.theurgyPowers.clear();
    }

    @Override
    public String getXmlFile() {
        return XML_FILE;
    }

    @Override
    public List<OccultismPath> getElements() throws InvalidXmlElementException {
        return this.readXml(OccultismPath.class);
    }

    @Override
    public List<OccultismPath> readXml(Class<OccultismPath> entityClass) throws InvalidXmlElementException {
        if (this.occultismPaths == null) {
            this.occultismPaths = super.readXml(entityClass);
            // Update power values related to some rites.
            this.occultismPaths
                    .forEach(occultismPath -> occultismPath.getOccultismPowersElements().forEach(occultismPower -> {
                        occultismPower.copyRestrictions(occultismPath);
                        occultismPower.setOfficial(occultismPath.isOfficial());
                    }));
        }
        return this.occultismPaths;
    }

    public OccultismPath getOccultismPath(OccultismPower power) {
        try {
            for (final OccultismPath occultismPath : this.getElements()) {
                if (occultismPath.getOccultismPowers().containsKey(power.getId())) {
                    return occultismPath;
                }
            }
        } catch (final InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
        }
        return null;
    }

    public List<OccultismPath> getPsiPaths() {
        if (this.psiPaths.isEmpty()) {
            try {
                for (final OccultismPath path : this.getElements()) {
                    if (OccultismTypeFactory.getPsi() != null
                            && Objects.equals(path.getOccultismType(), OccultismTypeFactory.getPsi().getId())) {
                        this.psiPaths.add(path);
                    }

                }
            } catch (final InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return Collections.unmodifiableList(this.psiPaths);
    }

    public List<OccultismPower> getPsiPowers() {
        if (this.psiPowers.isEmpty()) {
            try {
                for (final OccultismPath path : this.getPsiPaths()) {
                    this.psiPowers.addAll(path.getOccultismPowersElements());
                }
            } catch (final InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return Collections.unmodifiableList(this.psiPowers);
    }

    public List<OccultismPath> getTheurgyPaths() {
        if (this.theurgyPaths.isEmpty()) {
            try {
                for (final OccultismPath path : this.getElements()) {
                    if (OccultismTypeFactory.getTheurgy() != null
                            && Objects.equals(path.getOccultismType(), OccultismTypeFactory.getTheurgy().getId())) {
                        this.theurgyPaths.add(path);
                    }

                }
            } catch (final InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return Collections.unmodifiableList(this.theurgyPaths);
    }

    public List<OccultismPower> getTheurgyPowers() {
        if (this.theurgyPowers.isEmpty()) {
            try {
                for (final OccultismPath path : this.getTheurgyPaths()) {
                    this.theurgyPowers.addAll(path.getOccultismPowersElements());
                }
            } catch (final InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return Collections.unmodifiableList(this.theurgyPowers);
    }
}
