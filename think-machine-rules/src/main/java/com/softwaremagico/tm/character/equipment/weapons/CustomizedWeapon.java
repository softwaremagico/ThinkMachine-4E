package com.softwaremagico.tm.character.equipment.weapons;

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
import com.softwaremagico.tm.character.equipment.item.Quality;
import com.softwaremagico.tm.character.equipment.item.Status;
import com.softwaremagico.tm.txt.TextFactory;

public class CustomizedWeapon extends Weapon {

    @JsonProperty("quality")
    private Quality quality;

    @JsonProperty("status")
    private Status status;

    public CustomizedWeapon() {
        super();
    }

    public CustomizedWeapon(Weapon weapon) {
        super();
        setAccessories(weapon.getAccessories());
        setWeaponClass(weapon.getWeaponClass());

    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getWeaponOthersText() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (quality != null && quality != Quality.STANDARD) {
            stringBuilder.append(TextFactory.getInstance().getElement(quality.name().toLowerCase()));
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.append(", ");
        }
        if (status != null) {
            stringBuilder.append(TextFactory.getInstance().getElement(status.name().toLowerCase()));
        }

        final String standardOthers = super.getWeaponOthersText();
        if (stringBuilder.length() > 0 && !standardOthers.isEmpty()) {
            stringBuilder.append(", ");
            stringBuilder.append(standardOthers);
        }
        return stringBuilder.toString();
    }
}
