package com.softwaremagico.tm.xml;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.ObjectMapperFactory;
import com.softwaremagico.tm.character.Selection;
import com.softwaremagico.tm.exceptions.InvalidXmlElementException;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.log.MachineLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class XmlFactory<T extends Element> {
    //Id -> Element
    private Map<String, T> elements = null;
    private List<T> elementList = null;
    private Set<String> elementGroups;

    protected XmlFactory() {

    }

    public abstract String getXmlFile();

    public static ObjectMapper getObjectMapper() {
        return ObjectMapperFactory.getXmlObjectMapper();
    }

    public T getElement(Selection selection) throws InvalidXmlElementException {
        if (elements == null) {
            getElements();
        }
        final T element = elements.get(selection.getId());
        if (element == null) {
            throw new InvalidXmlElementException(this.getClass().getName() + " has no element with selection '" + selection + "'.");
        }
        return element;
    }


    public T getElement(Element e) throws InvalidXmlElementException {
        if (e == null) {
            return null;
        }
        return getElement(e.getId());
    }


    public T getElement(String id) throws InvalidXmlElementException {
        if (elements == null) {
            getElements();
        }
        if (id == null || id.isEmpty()) {
            return null;
        }
        final T element = elements.get(id);
        if (element == null) {
            throw new InvalidXmlElementException(this.getClass().getName() + " has no element with id '" + id + "'.");
        }
        return element;
    }

    public String getTranslatedText(String id) throws InvalidXmlElementException {
        return getElement(id).getName().getTranslatedText();
    }

    public List<T> getElements(Collection<String> ids) throws InvalidXmlElementException {
        if (ids == null) {
            return new ArrayList<>();
        }
        return getElements().stream().filter(t -> ids.contains(t.getId())).collect(Collectors.toList());
    }

    public Collection<T> getElements(Element e) throws InvalidXmlElementException {
        final Set<T> relatedElements = new HashSet<>();
        if (e == null) {
            return relatedElements;
        }
        if (e.getId() != null) {
            relatedElements.add(getElement(e));
        }
        if (e.getGroup() != null) {
            relatedElements.addAll(getElementsByGroup(e.getGroup()));
        }
        return relatedElements;
    }

    public List<T> getElementsByGroup(String group) throws InvalidXmlElementException {
        return getElements().stream().filter(t -> Objects.equals(group, t.getGroup())).collect(Collectors.toList());
    }

    public abstract List<T> getElements() throws InvalidXmlElementException;

    public List<T> getOpenElements() {
        return getElements().stream().filter(Element::isOpen).collect(Collectors.toList());
    }

    public List<T> readXml(Class<T> entityClass) throws InvalidXmlElementException {
        try {
            if (elementList == null) {
                elementList = readXml(entityClass, ModuleManager.DEFAULT_MODULE);
            }
            return new ArrayList<>(elementList);
        } catch (IOException e) {
            MachineLog.errorMessage(this.getClass(), e);
            throw new InvalidXmlElementException("Error reading xml for '" + entityClass + "'", e);
        }
    }

    public List<T> readXml(Class<T> entityClass, String moduleName) throws IOException {
        final Path filePath = Paths.get(PathManager.getModulePath(moduleName) + getXmlFile());
        return readXml(readFile(filePath.toString()), entityClass);
    }

    public List<T> readXml(String xmlContent, Class<T> entityClass) throws JsonProcessingException {
        final List<T> fileElements = getObjectMapper().readerForListOf(entityClass).readValue(xmlContent);
        final AtomicInteger order = new AtomicInteger();
        fileElements.forEach(element -> element.setOrder(order.getAndIncrement()));
        this.elements = new HashMap<>();
        fileElements.forEach(element -> this.elements.put(element.getId(), element));
        return fileElements;
    }

    public void validate() throws InvalidXmlElementException {
        for (T element : getElements()) {
            element.validate();
        }
    }

    public List<T> getRestrictedToUpbringing(String upbringing) throws InvalidXmlElementException {
        return getElements().stream().filter(t -> t.getRestrictions().getRestrictedToUpbringing().contains(upbringing)).collect(Collectors.toList());
    }

    private static String readFile(String filePath) {
        try {
            final URL resource;
            if (XmlFactory.class.getClassLoader().getResource(filePath) != null) {
                resource = XmlFactory.class.getClassLoader().getResource(filePath);
            } else if (new File(filePath).exists()) {
                //It is an external folder.
                resource = new File(filePath).toURI().toURL();
            } else {
                // Is inside a module.
                resource = ClassLoader.getSystemResource(filePath);
            }
            MachineLog.debug(XmlFactory.class.getName(), "Found xml factory '" + filePath + "' at '" + resource + "'.");
            final StringBuilder resultStringBuilder = new StringBuilder();
            if (resource == null) {
                throw new InvalidXmlElementException("Resource not found on '" + filePath + "'.");
            }
            try (BufferedReader read = new BufferedReader(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = read.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
            return resultStringBuilder.toString();
        } catch (NullPointerException | IOException e) {
            //Do nothing.
        }
        return null;
    }

    public Set<String> getElementGroups() {
        if (elementGroups == null) {
            elementGroups = new HashSet<>();
            getElements().forEach(elementsWithGroup -> elementGroups.add(elementsWithGroup.getGroup()));
        }
        return elementGroups;
    }
}
