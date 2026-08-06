package com.softwaremagico.tm.xml;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.ObjectMapperFactory;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.exceptions.ResourceNotFoundException;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.log.MachineLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class XmlFactory<T extends Element> {
    // Id -> Element
    private Map<String, T> elements = null;
    private List<T> elementList = null;
    private List<String> elementIdList = null;
    private List<T> selectableElementList = null;
    private Set<String> elementGroups;

    protected XmlFactory() {

    }

    public abstract String getXmlFile();

    public static ObjectMapper getObjectMapper() {
        return ObjectMapperFactory.getXmlObjectMapper();
    }

    public void reset() {
        this.elements = null;
        this.elementList = null;
        this.elementIdList = null;
        this.selectableElementList = null;
        this.elementGroups = null;
    }

    public T getElement(Selection selection) throws InvalidXmlElementException {
        if (this.elements == null) {
            this.getElements();
        }
        final T element = this.elements.get(selection.getId());
        if (element == null) {
            throw new InvalidXmlElementException(
                    this.getClass().getName() + " has no element with selection '" + selection + "'.");
        }
        return element;
    }

    public T getElement(Element e) throws InvalidXmlElementException {
        if (e == null) {
            return null;
        }
        return this.getElement(e.getId());
    }

    public T getElement(String id) throws InvalidXmlElementException {
        if (this.elements == null) {
            this.getElements();
        }
        if (id == null || id.isEmpty()) {
            return null;
        }
        final T element = this.elements.get(id);
        if (element == null) {
            throw new InvalidXmlElementException(this.getClass().getName() + " has no element with id '" + id + "'.");
        }
        return element;
    }

    public String getTranslatedText(String id) throws InvalidXmlElementException {
        return this.getElement(id).getName().getTranslatedText();
    }

    public List<T> getElements(Collection<String> ids) throws InvalidXmlElementException {
        if (ids == null) {
            return new ArrayList<>();
        }
        // Some sorts exists, that needs that this elements are in a mutable collection.
        return new ArrayList<>(this.getElements().stream().filter(t -> ids.contains(t.getId())).toList());
    }

    public Collection<T> getElements(Element e) throws InvalidXmlElementException {
        final Set<T> relatedElements = new HashSet<>();
        if (e == null) {
            return relatedElements;
        }
        if (e.getId() != null) {
            relatedElements.add(this.getElement(e));
        }
        if (e.getGroup() != null) {
            relatedElements.addAll(this.getElementsByGroup(e.getGroup()));
        }
        return relatedElements;
    }

    public List<T> getElementsByGroup(String group) throws InvalidXmlElementException {
        return this.getElements().stream().filter(t -> Objects.equals(group, t.getGroup())).toList();
    }

    public List<T> getElementsByGroup(Collection<String> groups) throws InvalidXmlElementException {
        if (groups == null || groups.isEmpty()) {
            return new ArrayList<>();
        }
        return this.getElements().stream().filter(t -> groups.contains(t.getGroup())).toList();
    }

    public abstract List<T> getElements() throws InvalidXmlElementException;

    public List<String> getElementsIds() {
        if (this.elementIdList == null) {
            this.elementIdList = this.getElements().stream().map(Element::getId).toList();
        }
        return this.elementIdList;
    }

    public List<T> getSelectableElements() {
        if (this.selectableElementList == null) {
            this.selectableElementList = this.getElements().stream().filter(Element::isSelectable).toList();
        }
        return this.selectableElementList;
    }

    public List<T> getOpenElements() {
        return this.getElements().stream().filter(Element::isOpen).toList();
    }

    public List<T> readXml(Class<T> entityClass) throws InvalidXmlElementException {
        try {
            if (this.elementList == null) {
                this.elementList = new ArrayList<>();
                for (final String module : ModuleManager.getEnabledModules()) {
                    try {
                        this.combineElements(this.elementList, this.readXml(entityClass, module));
                    } catch (final ResourceNotFoundException e) {
                        MachineLog.warning(XmlFactory.class, "Element '{}' not found on module '{}'.",
                                entityClass.getSimpleName(), module);
                    }
                }
            }
            this.elements = new HashMap<>();
            this.elementList.forEach(element -> this.elements.put(element.getId(), element));
            return new ArrayList<>(this.elementList);
        } catch (final IOException e) {
            MachineLog.errorMessage(this.getClass(), e);
            throw new InvalidXmlElementException("Error reading xml for '" + entityClass + "'", e);
        }
    }

    private void combineElements(List<T> currentElements, List<T> newElements) {
        final List<T> elementsToAdd = new ArrayList<>();
        for (final T newElement : newElements) {
            // Check if it is a new specialization of an existing element.
            boolean added = false;
            for (final T currentElement : currentElements) {
                if (currentElement.getId().equals(newElement.getId()) && newElement.getSpecializations() != null
                        && !newElement.getSpecializations().isEmpty()) {
                    if (currentElement.getSpecializations() == null) {
                        currentElement.setSpecializations(new ArrayList<>());
                    }
                    currentElement.getSpecializations().addAll(newElement.getSpecializations());
                    added = true;
                }
            }
            if (!added) {
                // Add it if is a new element.
                elementsToAdd.add(newElement);
            }
        }
        currentElements.addAll(elementsToAdd);
    }

    public List<T> readXml(Class<T> entityClass, String moduleName) throws IOException {
        final Path filePath = Paths.get(PathManager.getModulePath(moduleName) + this.getXmlFile());
        return this.readXml(readFile(filePath.toString()), entityClass, moduleName);
    }

    public List<T> readXml(String xmlContent, Class<T> entityClass) throws JsonProcessingException {
        return this.readXml(xmlContent, entityClass, null);
    }

    public List<T> readXml(String xmlContent, Class<T> entityClass, String moduleName) throws JsonProcessingException {
        final List<T> fileElements = getObjectMapper().readerForListOf(entityClass).readValue(xmlContent);
        final AtomicInteger order = new AtomicInteger();
        final String moduleId = ModuleManager.getModuleId(moduleName);
        final Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        fileElements.forEach(element -> {
            element.setOrder(order.getAndIncrement());
            this.assignModuleMetadata(element, moduleName, moduleId, visited);
        });
        return fileElements;
    }

    private void assignModuleMetadata(Object value, String moduleName, String moduleId, Set<Object> visited) {
        if (value == null || !visited.add(value)) {
            return;
        }

        if (value instanceof Element element) {
            element.setModuleName(moduleName);
            element.setModuleId(moduleId);
        }

        if (this.visitMap(value, moduleName, moduleId, visited)) {
            return;
        }

        if (this.visitCollection(value, moduleName, moduleId, visited)) {
            return;
        }

        final Class<?> valueClass = value.getClass();
        if (this.visitArray(value, moduleName, moduleId, visited, valueClass)) {
            return;
        }

        if (!valueClass.getPackageName().startsWith("com.softwaremagico.tm")) {
            return;
        }

        for (Class<?> currentClass = valueClass; currentClass != null && currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
            if (!currentClass.getPackageName().startsWith("com.softwaremagico.tm")) {
                break;
            }
            for (final Field field : currentClass.getDeclaredFields()) {
                this.visitField(value, moduleName, moduleId, visited, valueClass, field);
            }
        }
    }

    private boolean visitMap(Object value, String moduleName, String moduleId, Set<Object> visited) {
        if (!(value instanceof Map<?, ?> map)) {
            return false;
        }
        map.values().forEach(mapValue -> this.assignModuleMetadata(mapValue, moduleName, moduleId, visited));
        return true;
    }

    private boolean visitCollection(Object value, String moduleName, String moduleId, Set<Object> visited) {
        if (!(value instanceof Collection<?> collection)) {
            return false;
        }
        collection.forEach(item -> this.assignModuleMetadata(item, moduleName, moduleId, visited));
        return true;
    }

    private boolean visitArray(Object value, String moduleName, String moduleId, Set<Object> visited, Class<?> valueClass) {
        if (!valueClass.isArray()) {
            return false;
        }
        final int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            this.assignModuleMetadata(Array.get(value, i), moduleName, moduleId, visited);
        }
        return true;
    }

    private void visitField(Object value, String moduleName, String moduleId, Set<Object> visited, Class<?> valueClass, Field field) {
        if (Modifier.isStatic(field.getModifiers()) || field.getType().isPrimitive() || field.getType().isEnum()) {
            return;
        }
        try {
            field.setAccessible(true);
            this.assignModuleMetadata(field.get(value), moduleName, moduleId, visited);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Cannot assign module metadata to '" + valueClass.getName() + "'.", e);
        } catch (final RuntimeException e) {
            return;
        }
    }

    public void validate() throws InvalidXmlElementException {
        for (final T element : this.getElements()) {
            element.validate();
        }
    }

    public List<T> getRestrictedToUpbringing(String upbringing) throws InvalidXmlElementException {
        return this.getElements().stream()
                .filter(t -> t.getRestrictions().getRestrictedToUpbringing().contains(upbringing)).toList();
    }

    private static String readFile(String filePath) {
        try {
            final URL resource;
            if (XmlFactory.class.getClassLoader().getResource(filePath) != null) {
                resource = XmlFactory.class.getClassLoader().getResource(filePath);
            } else if (new File(filePath).exists()) {
                // It is an external folder.
                resource = new File(filePath).toURI().toURL();
            } else {
                // Is inside a module.
                resource = ClassLoader.getSystemResource(filePath);
            }
            MachineLog.debug(XmlFactory.class.getName(), "Found xml factory '" + filePath + "' at '" + resource + "'.");
            final StringBuilder resultStringBuilder = new StringBuilder();
            if (resource == null) {
                throw new ResourceNotFoundException("Resource not found on '" + filePath + "'.");
            }
            try (BufferedReader read = new BufferedReader(
                    new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = read.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
            return resultStringBuilder.toString();
        } catch (final NullPointerException | IOException e) {
            // Do nothing.
        }
        return null;
    }

    public Set<String> getElementGroups() {
        if (this.elementGroups == null) {
            this.elementGroups = new HashSet<>();
            this.getElements().forEach(elementsWithGroup -> this.elementGroups.add(elementsWithGroup.getGroup()));
        }
        return this.elementGroups;
    }
}
