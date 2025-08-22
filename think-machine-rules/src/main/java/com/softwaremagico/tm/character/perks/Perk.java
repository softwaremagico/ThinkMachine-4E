package com.softwaremagico.tm.character.perks;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.TranslatedText;

public class Perk extends Element {
    @JsonProperty("repeatable")
    private boolean repeatable = false;
    @JsonProperty("benefice")
    private TranslatedText benefice;

    @JsonProperty
    private PerkSource source;
    @JsonProperty
    private PerkType type;

    public TranslatedText getBenefice() {
        return benefice;
    }

    public void setBenefice(TranslatedText benefice) {
        this.benefice = benefice;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public PerkSource getSource() {
        return source;
    }

    public void setSource(PerkSource source) {
        this.source = source;
    }

    public PerkType getType() {
        return type;
    }

    public void setType(PerkType type) {
        this.type = type;
    }
}
