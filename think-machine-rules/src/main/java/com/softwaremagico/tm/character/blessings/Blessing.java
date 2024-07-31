package com.softwaremagico.tm.character.blessings;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.character.values.IElementWithBonification;
import com.softwaremagico.tm.character.values.IValue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@JacksonXmlRootElement(localName = "blessing")
public class Blessing extends Element<Blessing> implements IElementWithBonification {
    private final Integer cost;
    private final Set<Bonification> bonifications;
    private final BlessingClassification blessingClassification;
    @JsonProperty("group")
    private final BlessingGroup blessingGroup;

    /**
     * For creating empty elements.
     */
    public Blessing() {
        super();
        cost = 0;
        bonifications = new HashSet<>();
        blessingClassification = null;
        blessingGroup = null;
    }

    public Blessing(String id, TranslatedText name, TranslatedText description, String language, String moduleName, Integer cost,
                    Set<Bonification> bonifications, BlessingClassification blessingClassification, BlessingGroup blessingGroup) {
        super(id, name, description, language, moduleName);
        this.cost = cost;
        this.bonifications = bonifications;
        this.blessingClassification = blessingClassification;
        this.blessingGroup = blessingGroup;
    }

    public Integer getCost() {
        if (getBlessingClassification() != null && getBlessingClassification().equals(BlessingClassification.CURSE)) {
            return -Math.abs(cost);
        }
        return cost;
    }

    public BlessingClassification getBlessingClassification() {
        return blessingClassification;
    }

    public BlessingGroup getBlessingGroup() {
        return blessingGroup;
    }

    @Override
    public Set<Bonification> getBonifications() {
        return bonifications;
    }

    public String getTrait() {
        if (bonifications != null && !bonifications.isEmpty()) {
            final Iterator<Bonification> iterator = bonifications.iterator();
            final StringBuilder text = new StringBuilder();
            while (iterator.hasNext()) {
                final IValue affects = iterator.next().getAffects();
                if (affects != null && affects.getName() != null) {
                    if (!text.isEmpty()) {
                        text.append(", ");
                    }
                    text.append(affects.getName());
                }
            }
            return text.toString();
        }
        return "";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
