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
import java.util.Objects;

public class OptionsList<E extends CharacterSelectedElement> extends SelectionList<E> {

    private static final long serialVersionUID = 2154478596675658061L;

    public OptionsList(Collection<E> elements) {
        super(elements);
        if (elements != null && !elements.isEmpty()) {
            elements.stream().filter(Objects::nonNull).forEach(element -> {
                element.addSelectionUpdatedListeners(this::notifySelectionAddedListener);
                notifySelectionAddedListener();
            });
        }
    }


    @Override
    public boolean add(E element) {
        try {
            return super.add(element);
        } finally {
            element.addSelectionUpdatedListeners(this::notifySelectionAddedListener);
            notifySelectionAddedListener();
        }
    }

    @Override
    public void add(int index, E element) {
        try {
            super.add(index, element);
        } finally {
            element.addSelectionUpdatedListeners(this::notifySelectionAddedListener);
            notifySelectionAddedListener();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        try {
            return super.addAll(c);
        } finally {
            c.stream().filter(Objects::nonNull).forEach(element ->
                    element.addSelectionUpdatedListeners(this::notifySelectionAddedListener));
            notifySelectionAddedListener();
        }
    }

    @Override
    public boolean remove(Object o) {
        try {
            return super.remove(o);
        } finally {
            notifySelectionAddedListener();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        try {
            return super.removeAll(c);
        } finally {
            notifySelectionAddedListener();
        }
    }

    @Override
    public E set(int index, E element) {
        try {
            return super.set(index, element);
        } finally {
            element.addSelectionUpdatedListeners(this::notifySelectionAddedListener);
            notifySelectionAddedListener();
        }
    }
}
