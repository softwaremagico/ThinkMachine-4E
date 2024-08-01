package com.softwaremagico.tm.character.values;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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
import com.softwaremagico.tm.TranslatedText;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillDefinition;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = Characteristic.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AvailableSkill.class, name = "skill"),
        @JsonSubTypes.Type(value = Characteristic.class, name = "characteristic"),
        @JsonSubTypes.Type(value = CharacteristicDefinition.class, name = "characteristicDefinition"),
        @JsonSubTypes.Type(value = OccultismType.class, name = "occultismType"),
        @JsonSubTypes.Type(value = SkillDefinition.class, name = "skillDefinition"),
        @JsonSubTypes.Type(value = SpecialValue.class, name = "specialValue"),
})
public interface IValue {

    String getId();

    TranslatedText getName();

    void setId(String id);

    void setName(TranslatedText name);
}
