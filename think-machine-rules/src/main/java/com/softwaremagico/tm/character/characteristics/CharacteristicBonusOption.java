package com.softwaremagico.tm.character.characteristics;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softwaremagico.tm.Option;
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

public class CharacteristicBonusOption extends Option<CharacteristicDefinition> {
    @JsonProperty("bonus")
    private int bonus;
    @JsonProperty("extra")
    private boolean extra = false;

    public CharacteristicBonusOption() {
        super();
    }

    public CharacteristicBonusOption(CharacteristicDefinition characteristic) {
        this(characteristic.getId());
    }

    public CharacteristicBonusOption(String characteristic) {
        this();
        setId(characteristic);
    }

    public CharacteristicBonusOption(String characteristic, int bonus) {
        this(characteristic);
        setBonus(bonus);
    }

    public CharacteristicBonusOption(CharacteristicDefinition characteristic, int bonus) {
        this(characteristic.getId());
        setBonus(bonus);
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    @Override
    public CharacteristicDefinition getElement(String id) {
        return CharacteristicsDefinitionFactory.getInstance().getElement(id);
    }

    @Override
    public String toString() {
        return getId() + " (+" + bonus + ")";
    }

    @JsonIgnore
    @Override
    public TranslatedText getName() {
        if (getId() != null) {
            try {
                return new TranslatedText(CharacteristicsDefinitionFactory.getInstance().getElement(getId()).getName(), " (+" + bonus + ")");
            } catch (InvalidXmlElementException e) {
                return new TranslatedText("{" + getId() + "}");
            } catch (NullPointerException e) {
                throw new InvalidXmlElementException("CharacteristicBonusOption " + getId() + " has no name!");
            }
        }
        return null;
    }
}
