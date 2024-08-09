package com.softwaremagico.tm.character.equipment;

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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.item.Item;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = Characteristic.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Item.class, name = "item"),
        @JsonSubTypes.Type(value = Shield.class, name = "shield"),
        @JsonSubTypes.Type(value = Armor.class, name = "armor"),
        @JsonSubTypes.Type(value = Weapon.class, name = "weapon")
})
public abstract class Equipment<E extends Element<?>> extends Element<E> implements IElementWithTechnologyLevel {
    private float cost;
    private int techLevel;
    private Size size;

    public Equipment() {
        super();
        this.cost = 0;
        this.techLevel = 0;
    }

    public float getCost() {
        return cost;
    }

    @Override
    public int getTechLevel() {
        return techLevel;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setTechLevel(int techLevel) {
        this.techLevel = techLevel;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
