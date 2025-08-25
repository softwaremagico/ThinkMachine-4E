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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ObjectMapperFactory {
    private static JsonMapper jsonObjectMapper;
    private static XmlMapper xmlObjectMapper;

    private ObjectMapperFactory() {
    }

    public static ObjectMapper getJsonObjectMapper() {
        if (jsonObjectMapper == null) {
            final JavaTimeModule module = new JavaTimeModule();
            final LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
            jsonObjectMapper = JsonMapper.builder()
                    .addModule(module)
                    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .build();
            jsonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            jsonObjectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonObjectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }
        return jsonObjectMapper;
    }

    public static ObjectMapper getXmlObjectMapper() {
        if (xmlObjectMapper == null) {
            xmlObjectMapper = XmlMapper.builder()
                    .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
                    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).serializationInclusion(JsonInclude.Include.NON_EMPTY)
//                    .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
                    .addModule(new JavaTimeModule())
                    .build();
        }
        return xmlObjectMapper;
    }

}
