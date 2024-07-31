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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.log.MachineLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class XmlFactory<T extends Element<T>> {

    private static XmlMapper objectMapper;

    //Id -> Element
    private Map<String, T> elements = null;

    public XmlFactory() {

    }

    public abstract String getXmlFile();

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = XmlMapper.builder()
                    .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).serializationInclusion(JsonInclude.Include.NON_EMPTY)
                    .addModule(new JavaTimeModule())
                    .build();
        }
        return objectMapper;
    }

    public T getElement(String id) {
        if (elements == null) {
            try {
                getElements();
            } catch (IOException e) {
                MachineLog.errorMessage(this.getClass(), e);
            }
        }
        return elements.get(id);
    }

    public abstract List<T> getElements() throws IOException;

    public List<T> readXml(Class<T> entityClass) throws IOException {
        return readXml(entityClass, ModuleManager.DEFAULT_MODULE);
    }

    public List<T> readXml(Class<T> entityClass, String moduleName) throws IOException {
        final Path filePath = Paths.get("../" + PathManager.getModulePath(moduleName) + getXmlFile());
        return readXml(Files.readString(filePath), entityClass);
    }

    public List<T> readXml(String xmlContent, Class<T> entityClass) throws JsonProcessingException {
        final List<T> elements = getObjectMapper().readerForListOf(entityClass).readValue(xmlContent);
        final AtomicInteger order = new AtomicInteger();
        elements.forEach(element -> element.setOrder(order.getAndIncrement()));
        this.elements = new HashMap<>();
        elements.forEach(element -> this.elements.put(element.getId(), element));
        return elements;
    }
}
