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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OptionSelector<E extends Element, T extends Option<E>> {
    @JsonProperty("total")
    private int totalOptions = 1;
    @JsonProperty("options")
    private List<T> options = new ArrayList<>();

    public OptionSelector() {
        super();
    }

    public OptionSelector(OptionSelector<E, T> optionSelector) {
        this();
        totalOptions = optionSelector.totalOptions;
        options.addAll(optionSelector.options);
    }

    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
    }

    public List<T> getOptions() {
        return getSourceOptions();
    }

    @JsonIgnore
    public List<T> getSourceOptions() {
        return options;
    }

    public void setOptions(List<T> options) {
        this.options = options;
    }

    public void addOptions(Collection<T> options) {
        if (getOptions() == null) {
            setOptions(new ArrayList<>());
        }
        getOptions().addAll(options);
    }

    public void validate() throws InvalidXmlElementException {
        if (options != null) {
            options.forEach(Element::validate);
        }
    }
}
