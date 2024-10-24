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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.armors.ArmorFactory;
import com.softwaremagico.tm.character.equipment.armors.CustomizedArmor;
import com.softwaremagico.tm.character.equipment.item.CustomizedItem;
import com.softwaremagico.tm.character.equipment.item.Item;
import com.softwaremagico.tm.character.equipment.item.ItemFactory;
import com.softwaremagico.tm.character.equipment.item.handheldshield.CustomizedHandheldShield;
import com.softwaremagico.tm.character.equipment.item.handheldshield.HandheldShield;
import com.softwaremagico.tm.character.equipment.item.handheldshield.HandheldShieldFactory;
import com.softwaremagico.tm.character.equipment.shields.CustomizedShield;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.thinkmachines.CustomizedThinkMachine;
import com.softwaremagico.tm.character.equipment.thinkmachines.ThinkMachine;
import com.softwaremagico.tm.character.equipment.thinkmachines.ThinkMachineFactory;
import com.softwaremagico.tm.character.equipment.weapons.CustomizedWeapon;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionFactory;
import com.softwaremagico.tm.character.upbringing.Upbringing;
import com.softwaremagico.tm.character.upbringing.UpbringingFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = Item.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Item.class, name = "item"),
        @JsonSubTypes.Type(value = CustomizedItem.class, name = "customizedItem"),
        @JsonSubTypes.Type(value = Shield.class, name = "e-shield"),
        @JsonSubTypes.Type(value = CustomizedShield.class, name = "customizedShield"),
        @JsonSubTypes.Type(value = Armor.class, name = "armor"),
        @JsonSubTypes.Type(value = CustomizedArmor.class, name = "customizedArmor"),
        @JsonSubTypes.Type(value = HandheldShield.class, name = "handheldShield"),
        @JsonSubTypes.Type(value = CustomizedHandheldShield.class, name = "customizedHandheldShield"),
        @JsonSubTypes.Type(value = CustomizedArmor.class, name = "customizedArmor"),
        @JsonSubTypes.Type(value = Weapon.class, name = "weapon"),
        @JsonSubTypes.Type(value = CustomizedWeapon.class, name = "customizedWeapon"),
        @JsonSubTypes.Type(value = ThinkMachine.class, name = "thinkMachine"),
        @JsonSubTypes.Type(value = CustomizedThinkMachine.class, name = "customizedThinkMachine")
})
public abstract class Equipment extends Element implements IElementWithTechnologyLevel {
    @JsonProperty("cost")
    private float cost;
    @JsonProperty("techLevel")
    private Integer techLevel;
    @JsonProperty("size")
    private Size size;
    @JsonProperty("traits")
    private List<String> traits;
    @JsonProperty("techCompulsion")
    private String techCompulsion;
    @JsonProperty("quantity")
    private int quantity = 1;
    @JsonProperty("agora")
    private Agora agora;
    @JsonProperty("features")
    private List<EquipmentFeature> features;

    public Equipment() {
        super();
        this.cost = 0;
        this.techLevel = 0;
    }

    public float getCost() {
        return cost;
    }

    @Override
    public Integer getTechLevel() {
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

    public List<String> getTraits() {
        return traits;
    }

    public void setTraits(List<String> traits) {
        this.traits = traits;
    }

    public String getTechCompulsion() {
        return techCompulsion;
    }

    public void setTechCompulsion(String techCompulsion) {
        this.techCompulsion = techCompulsion;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = Objects.requireNonNullElse(quantity, 1);
    }

    public static Equipment completeItem(Equipment equipment) {
        Equipment finalItem = null;
        if (equipment instanceof Item) {
            finalItem = ItemFactory.getInstance().getElement(equipment.getId());
        } else if (equipment instanceof Weapon) {
            finalItem = WeaponFactory.getInstance().getElement(equipment.getId());
        } else if (equipment instanceof Armor) {
            finalItem = ArmorFactory.getInstance().getElement(equipment.getId());
        } else if (equipment instanceof Shield) {
            finalItem = ShieldFactory.getInstance().getElement(equipment.getId());
        } else if (equipment instanceof ThinkMachine) {
            finalItem = ThinkMachineFactory.getInstance().getElement(equipment.getId());
        } else if (equipment instanceof HandheldShield) {
            finalItem = HandheldShieldFactory.getInstance().getElement(equipment.getId());
        }
        if (finalItem == null) {
            throw new InvalidXmlElementException("Equipment '" + equipment + "' is not copied correctly.");
        }
        equipment.copy(finalItem);
        return equipment;
    }

    public void copy(Equipment equipment) {
        super.copy(equipment);
        setCost(equipment.getCost());
        setTechLevel(equipment.getTechLevel());
        setSize(equipment.getSize());
        setTechCompulsion(equipment.getTechCompulsion());
        setAgora(equipment.getAgora());
        if (equipment.getFeatures() != null) {
            setFeatures(new ArrayList<>(equipment.getFeatures()));
        }
        //setQuantity(equipment.getQuantity());
        if (equipment.getTraits() != null) {
            setTraits(new ArrayList<>(equipment.getTraits()));
        }
    }


    public Agora getAgora() {
        return agora;
    }

    public void setAgora(Agora agora) {
        this.agora = agora;
        try {
            final Faction faction = FactionFactory.getInstance().getElement(agora.name().toLowerCase());
            getRestrictions().setRestrictedToFactions(Collections.singleton(faction.getId()));
        } catch (Exception ignored) {
            //Not a faction.
        }
        try {
            final Upbringing upbringing = UpbringingFactory.getInstance().getElement(agora.name().toLowerCase());
            getRestrictions().setRestrictedToUpbringing(Collections.singleton(upbringing.getId()));
        } catch (Exception ignored) {
            //Not a faction.
        }
        getRandomDefinition().setAgoraProbabilityMultiplier(agora);
    }


    public List<EquipmentFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<EquipmentFeature> features) {
        this.features = features;
    }

    @Override
    public void validate() throws InvalidXmlElementException {
        super.validate();
        if (techCompulsion != null) {
            TechCompulsionFactory.getInstance().getElement(techCompulsion);
        }
    }

    @Override
    public String toString() {
        return getId() + (getQuantity() > 1 ? " (" + getQuantity() + ")" : "");
    }
}
