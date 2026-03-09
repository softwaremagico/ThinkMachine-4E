package com.softwaremagico.tm.character;

/*-
 * #%L
 * Think Machine 4E (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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

import com.softwaremagico.tm.structures.SelectionList;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CharacterSelectedElement {
    private SelectionList<Selection> selections = new SelectionList<>();

    private final Set<ISelectionUpdatedListener> selectionUpdatedListeners;

    public interface ISelectionUpdatedListener {
        void updated();
    }

    public CharacterSelectedElement() {
        this.selectionUpdatedListeners = new HashSet<>();
        selections.addSelectionAddedListeners(this::selectionAdded);
    }

    private void selectionAdded() {
        notifyCapabilityUpdatedListenerListeners();
    }

    public void addSelectionUpdatedListeners(ISelectionUpdatedListener listener) {
        selectionUpdatedListeners.add(listener);
    }

    public void notifyCapabilityUpdatedListenerListeners() {
        for (ISelectionUpdatedListener listener : selectionUpdatedListeners) {
            listener.updated();
        }
    }

    public List<Selection> getSelections() {
        return selections;
    }

    public void setSelections(Collection<Selection> selections) {
        this.selections = new SelectionList<>(selections);
        this.selections.addSelectionAddedListeners(this::selectionAdded);
    }

    @Override
    public String toString() {
        return "CharacterSelectedElement{"
                + "selections=" + selections
                + '}';
    }
}
