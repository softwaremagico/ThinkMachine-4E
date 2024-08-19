package com.softwaremagico.tm.txt;

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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.capabilities.Capability;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.TechCompulsionFactory;
import com.softwaremagico.tm.character.equipment.armors.Armor;
import com.softwaremagico.tm.character.equipment.item.Item;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.Blessing;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.specie.SpecieFactory;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CharacterSheet {
    private static final String ELEMENT_SEPARATOR = ", ";

    private final CharacterPlayer characterPlayer;

    public CharacterSheet(CharacterPlayer characterPlayer) {
        this.characterPlayer = characterPlayer;
    }

    private CharacterPlayer getCharacterPlayer() {
        return characterPlayer;
    }


    private void setCharacterInfoText(StringBuilder stringBuilder) throws InvalidXmlElementException {
        stringBuilder.append(getCharacterPlayer().getCompleteNameRepresentation());
        stringBuilder.append("\n");
        if (getCharacterPlayer().getSpecie() != null) {
            stringBuilder.append(SpecieFactory.getInstance().getElement(getCharacterPlayer().getSpecie()).getName());
        }
        if (getCharacterPlayer().getInfo() != null) {
            if (getCharacterPlayer().getInfo().getGender() != null) {
                stringBuilder.append(" ").append(TextFactory.getInstance().getElement(getCharacterPlayer().getInfo().getGender().toString()).getName());
            }
            if (getCharacterPlayer().getInfo().getAge() != null) {
                stringBuilder.append(" ").append(getCharacterPlayer().getInfo().getAge()).append(" ")
                        .append(TextFactory.getInstance().getElement("years").getName().getTranslatedText().toLowerCase());
            }
            if (getCharacterPlayer().getInfo().getPlanet() != null) {
                stringBuilder.append(" (").append(getCharacterPlayer().getInfo().getPlanet().getName()).append(")");
            }
            stringBuilder.append("\n");
        }
        final StringBuilder profession = new StringBuilder();
        if (getCharacterPlayer().getUpbringing() != null) {
            if (profession.length() > 0) {
                profession.append(ELEMENT_SEPARATOR);
            }
            profession.append(getCharacterPlayer().getUpbringing().getName());
        }
        if (getCharacterPlayer().getFaction() != null) {
            if (profession.length() > 0) {
                profession.append(ELEMENT_SEPARATOR);
            }
            profession.append(getCharacterPlayer().getFaction().getName());
        }
        if (getCharacterPlayer().getCalling() != null) {
            if (profession.length() > 0) {
                profession.append(ELEMENT_SEPARATOR);
            }
            profession.append(getCharacterPlayer().getCalling().getName());
        }
        stringBuilder.append(profession);
        stringBuilder.append("\n");
    }


    private void setCharacteristicsText(StringBuilder stringBuilder) {
        stringBuilder.append(TextFactory.getInstance().getElement("characteristics").getName()).append(": ");
        String separator = "";
        for (final CharacteristicName characteristicName : CharacteristicName.getBasicCharacteristics()) {
            stringBuilder.append(separator);
            stringBuilder.append(TextFactory.getInstance().getElement(characteristicName.getId()).getName());
            stringBuilder.append(" ");
            stringBuilder.append(getCharacterPlayer().getCharacteristicValue(characteristicName));
            separator = ELEMENT_SEPARATOR;
        }
        stringBuilder.append(".\n");
    }

    private void representSkill(StringBuilder stringBuilder, Skill skill) {
        stringBuilder.append(skill.getName()).append(" (");
        stringBuilder.append(characterPlayer.getSkillValue(skill));
        stringBuilder.append(")");
    }

    private void setSkillsText(StringBuilder stringBuilder) throws InvalidXmlElementException {
        stringBuilder.append(TextFactory.getInstance().getElement("skills").getName()).append(": ");
        String separator = "";
        for (final Skill skill : SkillFactory.getInstance().getElements()) {
            final int skillValue = characterPlayer.getSkillValue(skill);
            if ((skill.isNatural() && skillValue > Skill.NATURAL_SKILL_INITIAL_VALUE)
                    || (!skill.isNatural() && skillValue > 0)) {
                stringBuilder.append(separator);
                representSkill(stringBuilder, skill);
                separator = ELEMENT_SEPARATOR;
            }
        }
    }

    private void setCapabilitiesText(StringBuilder stringBuilder) throws InvalidXmlElementException {
        final Set<Capability> characterCapabilities = characterPlayer.getCapabilities();
        if (!characterCapabilities.isEmpty()) {
            stringBuilder.append(TextFactory.getInstance().getElement("capabilities").getName()).append(": ");
            String separator = "";
            for (final Capability capability : characterCapabilities) {
                stringBuilder.append(separator);
                stringBuilder.append(capability.getNameRepresentation());
                separator = ELEMENT_SEPARATOR;
            }
        }
    }

    private String getBlessingRepresentation(Blessing blessing) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(blessing.getNameRepresentation());
        if (!blessing.getDescriptionRepresentation().isEmpty()) {
            stringBuilder.append(" (");
            stringBuilder.append(blessing.getDescriptionRepresentation());
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }

    private void setBeneficesText(StringBuilder stringBuilder) throws InvalidXmlElementException {
        if (characterPlayer.getFaction() != null) {
            stringBuilder.append(TextFactory.getInstance().getElement("blessingTable").getName()).append(":\n");
            if (characterPlayer.getFaction().get().getBlessing() != null) {
                stringBuilder.append("\t- ").append(getBlessingRepresentation(characterPlayer.getFaction().get().getBlessing())).append("\n");
            }
            if (characterPlayer.getFaction().get().getCurse() != null) {
                stringBuilder.append("\t- ").append(getBlessingRepresentation(characterPlayer.getFaction().get().getCurse())).append("\n");
            }
            stringBuilder.append("\n");
        }
    }

    private void setResistancesRepresentation(StringBuilder stringBuilder) throws InvalidXmlElementException {
        stringBuilder.append(TextFactory.getInstance().getElement("resistance").getName());
        stringBuilder.append(": ");
        stringBuilder.append("\n");
        stringBuilder.append(TextFactory.getInstance().getElement("bodyResistance").getName()).append(": ")
                .append(characterPlayer.getBodyResistance()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(TextFactory.getInstance().getElement("mindResistance").getName()).append(": ")
                .append(characterPlayer.getMindResistance()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(TextFactory.getInstance().getElement("spiritResistance").getName()).append(": ")
                .append(characterPlayer.getSpiritResistance()).append(".\n");
    }

    private void setBankRepresentation(StringBuilder stringBuilder) throws InvalidXmlElementException {
        stringBuilder.append(TextFactory.getInstance().getElement("bank").getName());
        stringBuilder.append(": ");
        stringBuilder.append(characterPlayer.getBank());
        stringBuilder.append("\n");
    }

    private void setSurgesRepresentation(StringBuilder stringBuilder) throws InvalidXmlElementException {
        stringBuilder.append(TextFactory.getInstance().getElement("surges").getName());
        stringBuilder.append(": ");
        stringBuilder.append(characterPlayer.getSurgesRating());
        stringBuilder.append("/");
        stringBuilder.append(characterPlayer.getSurgesNumber());
        stringBuilder.append("\n");
    }


    private void setVitalityRepresentation(StringBuilder stringBuilder) throws InvalidXmlElementException {
        stringBuilder.append(TextFactory.getInstance().getElement("vitality").getName());
        stringBuilder.append(": ").append(characterPlayer.getVitalityValue());
        stringBuilder.append("\n");
    }

    private void setRevivalsRepresentation(StringBuilder stringBuilder) throws InvalidXmlElementException {
        stringBuilder.append(TextFactory.getInstance().getElement("revivals").getName());
        stringBuilder.append(": ").append(characterPlayer.getRevivalsRating());
        stringBuilder.append("/");
        stringBuilder.append(characterPlayer.getRevivalsNumber());
        stringBuilder.append("\n");
    }


    private void setWeapons(StringBuilder stringBuilder) {
        for (final Weapon weapon : getCharacterPlayer().getWeapons()) {
            stringBuilder.append("\t- ").append(weapon.getName());
            stringBuilder.append(" (");
            if (weapon.getWeaponDamages().get(0).getGoal() != null && !weapon.getWeaponDamages().get(0).getGoal().isEmpty()
                    && !weapon.getWeaponDamages().get(0).getGoal().equals("0")) {
                stringBuilder.append(weapon.getWeaponDamages().get(0).getGoal());
                stringBuilder.append(TextFactory.getInstance().getElement("weaponGoal").getName());
                stringBuilder.append(ELEMENT_SEPARATOR);
            }
            stringBuilder.append(weapon.getWeaponDamages().get(0).getDamageWithoutArea());
            if (!weapon.getWeaponDamages().get(0).getDamageWithoutArea().endsWith("d")) {
                stringBuilder.append("d");
            }
            if (weapon.getWeaponDamages().get(0).getAreaMeters() > 0) {
                stringBuilder.append(" ");
                stringBuilder.append(weapon.getWeaponDamages().get(0).getAreaMeters());
            }
            stringBuilder.append(ELEMENT_SEPARATOR);
            if (weapon.getWeaponDamages().get(0).getRange() != null && !weapon.getWeaponDamages().get(0).getRange().isEmpty()) {
                stringBuilder.append(weapon.getWeaponDamages().get(0).getRange());
                stringBuilder.append(ELEMENT_SEPARATOR);
            }
            if (weapon.getWeaponDamages().get(0).getRate() != null && !weapon.getWeaponDamages().get(0).getRate().isEmpty()) {
                stringBuilder.append(TextFactory.getInstance().getElement("weaponRate").getName());
                stringBuilder.append(" ");
                stringBuilder.append(weapon.getWeaponDamages().get(0).getRate());
                stringBuilder.append(ELEMENT_SEPARATOR);
            }
            for (final String damageTypeName : weapon.getDamageTypes()) {
                final DamageType damageType = DamageTypeFactory.getInstance().getElement(damageTypeName);
                stringBuilder.append(damageType.getName());
                stringBuilder.append(ELEMENT_SEPARATOR);
            }
            // Remove last separator
            stringBuilder.setLength(stringBuilder.length() - ELEMENT_SEPARATOR.length());
            stringBuilder.append(")");
            stringBuilder.append("\n");
        }
    }

    private void setArmor(StringBuilder stringBuilder) {
        final Armor armor = getCharacterPlayer().getArmor();
        if (armor != null) {
            stringBuilder.append("\t- ").append(armor.getName());
            stringBuilder.append(" (");
            stringBuilder.append(armor.getProtection()).append("d");
            stringBuilder.append(ELEMENT_SEPARATOR);
            if (armor.getStandardPenalization().getDexterityModification() != 0) {
                stringBuilder.append(TextFactory.getInstance().getElement(CharacteristicName.DEXTERITY.getId()).getName()).append(" ");
                stringBuilder.append(armor.getStandardPenalization().getDexterityModification());
                stringBuilder.append(ELEMENT_SEPARATOR);
            }
            if (armor.getStandardPenalization().getStrengthModification() != 0) {
                stringBuilder.append(TextFactory.getInstance().getElement(CharacteristicName.STRENGTH.getId()).getName()).append(" ");
                stringBuilder.append(armor.getStandardPenalization().getStrengthModification());
                stringBuilder.append(ELEMENT_SEPARATOR);
            }
            if (armor.getStandardPenalization().getEnduranceModification() != 0) {
                stringBuilder.append(TextFactory.getInstance().getElement(CharacteristicName.ENDURANCE.getId()).getName()).append(" ");
                stringBuilder.append(armor.getStandardPenalization().getEnduranceModification());
                stringBuilder.append(ELEMENT_SEPARATOR);
            }
            if (armor.getStandardPenalization().getInitiativeModification() != 0) {
                stringBuilder.append(TextFactory.getInstance().getElement(CharacteristicName.INITIATIVE.getId()).getName()).append(" ");
                stringBuilder.append(armor.getStandardPenalization().getInitiativeModification());
                stringBuilder.append(ELEMENT_SEPARATOR);
            }
            final List<DamageType> damages = DamageTypeFactory.getInstance().getElements(armor.getDamageTypes());
            Collections.sort(damages);
            for (final DamageType damageType : damages) {
                stringBuilder.append(damageType.getName());
                stringBuilder.append(ELEMENT_SEPARATOR);
            }
            stringBuilder.setLength(stringBuilder.length() - ELEMENT_SEPARATOR.length());
            stringBuilder.append(")");
            stringBuilder.append("\n");
        }
    }

    private void setShield(StringBuilder stringBuilder) {
        final Shield shield = getCharacterPlayer().getShield();
        if (shield != null) {
            stringBuilder.append("\t- ").append(shield.getName());
            stringBuilder.append(" (");
            stringBuilder.append(shield.getImpact());
            stringBuilder.append("/");
            stringBuilder.append(shield.getForce());
            stringBuilder.append(" ");
            stringBuilder.append(shield.getHits());
            stringBuilder.append(" ");
            stringBuilder.append(TextFactory.getInstance().getElement("shieldHits").getName());
            stringBuilder.append(")");
            stringBuilder.append("\n");
        }
    }

    private void setItems(StringBuilder stringBuilder) {
        for (final Item item : getCharacterPlayer().getItems()) {
            stringBuilder.append("\t- ").append(item.getName());
            final StringBuilder data = new StringBuilder();
            String separator = "";
            if (item.getTechLevel() > 0) {
                data.append(separator).append(TextFactory.getInstance().getElement("techLevel").getName()).append(" ")
                        .append(item.getTechLevel());
                separator = ELEMENT_SEPARATOR;
            }
            if (item.getQuantity() > 1) {
                data.append(separator).append(" (").append(item.getQuantity()).append(")");
                separator = ELEMENT_SEPARATOR;
            }
            if (item.getSize() != null) {
                data.append(separator).append(TextFactory.getInstance().getElement("size").getName()).append(" ")
                        .append(item.getSize());
                separator = ELEMENT_SEPARATOR;
            }
            if (item.getTechCompulsion() != null) {
                data.append(separator).append(TextFactory.getInstance().getElement("techCompulsion").getName()).append(" ")
                        .append(TechCompulsionFactory.getInstance().getElement(item.getTechCompulsion()).getName());
            }
            if (data.length() > 0) {
                stringBuilder.append(" (");
                stringBuilder.append(data);
                stringBuilder.append(")");
            }
            stringBuilder.append("\n");
        }
    }

    private void setEquipment(StringBuilder stringBuilder) {
        if (!getCharacterPlayer().getWeapons().isEmpty() || getCharacterPlayer().getShield() != null
                || getCharacterPlayer().getArmor() != null || getCharacterPlayer().getItems() != null) {
            stringBuilder.append(TextFactory.getInstance().getElement("equipment").getName()).append(":\n");
            setWeapons(stringBuilder);
            setArmor(stringBuilder);
            setShield(stringBuilder);
            setItems(stringBuilder);

            stringBuilder.append("\n");
        }
    }

    private String createContent() {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            setCharacterInfoText(stringBuilder);
            stringBuilder.append("\n");
            setCharacteristicsText(stringBuilder);
            stringBuilder.append("\n");
            setSkillsText(stringBuilder);
            stringBuilder.append("\n");
            setCapabilitiesText(stringBuilder);
            stringBuilder.append("\n");
            setBeneficesText(stringBuilder);
            stringBuilder.append("\n");
            setResistancesRepresentation(stringBuilder);
            stringBuilder.append("\n");
            setBankRepresentation(stringBuilder);
            setSurgesRepresentation(stringBuilder);
            stringBuilder.append("\n");
            setVitalityRepresentation(stringBuilder);
            setRevivalsRepresentation(stringBuilder);
            stringBuilder.append("\n");
            setEquipment(stringBuilder);
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return createContent();
    }
}
