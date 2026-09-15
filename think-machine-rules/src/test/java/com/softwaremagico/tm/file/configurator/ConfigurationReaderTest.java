package com.softwaremagico.tm.file.configurator;

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

import com.softwaremagico.tm.exceptions.PropertyNotFoundException;
import com.softwaremagico.tm.exceptions.PropertyNotStoredException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Properties;

@Test(groups = "configuration")
public class ConfigurationReaderTest {

    private static class TestConfigurationReader extends ConfigurationReader {
        void addProperty(String name, Object value) {
            setProperty(name, value);
        }

        String[] values(String name) throws PropertyNotFoundException {
            return getCommaSeparatedValues(name);
        }

        @Override
        public void storeProperties() throws PropertyNotStoredException {
        }

        @Override
        public File getUserProperties() {
            return null;
        }

        @Override
        public String getUserPropertiesPath() {
            return "";
        }
    }

    private static class PropertiesSource implements IPropertiesSource {
        private final Properties properties;

        PropertiesSource(Properties properties) {
            this.properties = properties;
        }

        @Override
        public Properties loadFile() {
            return properties;
        }

        @Override
        public String getFilePath() {
            return "";
        }

        @Override
        public String getFileName() {
            return "";
        }
    }

    @Test
    public void readsDefaultsOverridesAndConversions() throws PropertyNotFoundException {
        final TestConfigurationReader reader = new TestConfigurationReader();
        reader.addProperty("name", " default ");
        reader.addProperty("enabled", true);
        reader.addProperty("limit", 2);
        reader.addProperty("ratio", 1.5d);
        reader.addProperty("tags", " one, , two ");

        final Properties first = new Properties();
        first.setProperty("name", "first");
        first.setProperty("enabled", "false");
        final Properties second = new Properties();
        second.setProperty("name", "second");
        second.setProperty("limit", "3");
        second.setProperty("ratio", "2.5");
        reader.addPropertiesSource(new PropertiesSource(first));
        reader.addPropertiesSource(new PropertiesSource(second));
        reader.readConfigurations();

        Assert.assertEquals(reader.getProperty("name"), "second");
        Assert.assertFalse(reader.getProperty("enabled", Boolean.class));
        Assert.assertEquals(reader.getProperty("limit", Integer.class).intValue(), 3);
        Assert.assertEquals(reader.getProperty("ratio", Double.class).doubleValue(), 2.5d);
        Assert.assertEquals(reader.values("tags"), new String[]{"one", "two"});
    }

    @Test
    public void initializesPropertiesAndFindsPrefixes() throws PropertyNotFoundException {
        final TestConfigurationReader reader = new TestConfigurationReader();
        final Properties properties = new Properties();
        properties.setProperty("ui.theme", "dark");
        properties.setProperty("plain", "value");
        reader.addPropertiesSource(new PropertiesSource(properties));

        reader.initializeAllProperties();

        Assert.assertEquals(reader.getProperty("ui.theme"), "");
        Assert.assertTrue(reader.getAllPropertiesPrefixes().contains("ui"));
        Assert.assertFalse(reader.getAllPropertiesPrefixes().contains("plain"));
    }
}
