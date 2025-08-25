package com.softwaremagico.tm;

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
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;

public abstract class Option<T extends Element> extends Element {

    @JsonProperty("quantity")
    private int quantity = 1;

    public T getElement() {
        return getElement(getId());
    }

    public abstract T getElement(String id);

    @JsonIgnore
    @Override
    public TranslatedText getName() {
        if (getId() != null && !getId().isEmpty()) {
            try {
                return getElement(getId()).getName();
            } catch (InvalidXmlElementException e) {
                return new TranslatedText("{" + getId() + "}");
            } catch (NullPointerException e) {
                throw new InvalidXmlElementException("Option " + getId() + " has no name!");
            }
        }
        return null;
    }

    @JsonIgnore
    @Override
    public void setName(TranslatedText name) {
        throw new UnsupportedOperationException();
    }

    @JsonIgnore
    @Override
    public TranslatedText getDescription() {
        if (getId() != null) {
            try {
                return getElement(getId()).getDescription();
            } catch (InvalidXmlElementException e) {
                return null;
            }
        }
        return null;
    }

    @JsonIgnore
    @Override
    public void setDescription(TranslatedText description) {
        throw new UnsupportedOperationException();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
