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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Test(groups = "configuration")
public class MachineConfigurationReaderTest {
    private Path temporaryHome;
    private String originalUserHome;

    @BeforeMethod
    public void useTemporaryHome() throws IOException {
        originalUserHome = System.getProperty("user.home");
        temporaryHome = Files.createTempDirectory("think-machine-home");
        System.setProperty("user.home", temporaryHome.toString());
    }

    @AfterMethod
    public void restoreUserHome() throws IOException {
        System.setProperty("user.home", originalUserHome);
        if (temporaryHome != null) {
            Files.walk(temporaryHome)
                    .sorted(java.util.Comparator.reverseOrder())
                    .forEach(path -> path.toFile().delete());
        }
    }

    @Test
    public void readsAndStoresUserConfigurationInTemporaryHome()
            throws IOException, PropertyNotFoundException, PropertyNotStoredException {
        final TestMachineConfigurationReader reader = new TestMachineConfigurationReader();
        final Path configurationFile = temporaryHome.resolve("configuration/settings.conf");
        Files.createDirectories(configurationFile.getParent());
        Files.writeString(configurationFile, "modulesPath=from-file\n", StandardCharsets.UTF_8);
        reader.setUserSourceFile(configurationFile.getParent().toString(), configurationFile.getFileName().toString());

        reader.readConfigurations();

        Assert.assertEquals(reader.getModulesPath(), "from-file");
        Assert.assertEquals(reader.getUserProperties().toPath(), configurationFile);
        Assert.assertEquals(reader.getUserPropertiesPath(), configurationFile.toString());

        reader.setValue("modulesPath", "stored-value");
        reader.storeProperties();

        Assert.assertEquals(PropertiesFile.load(configurationFile.toFile()).getProperty("modulesPath"), "stored-value");
    }

    @Test
    public void returnsNullForUnknownPropertyLogException() {
        final TestMachineConfigurationReader reader = new TestMachineConfigurationReader();

        Assert.assertNull(reader.findValue("unknown"));
    }

    private static final class TestMachineConfigurationReader extends MachineConfigurationReader {
        void setValue(String property, String value) {
            setProperty(property, value);
        }

        String findValue(String property) {
            return getPropertyLogException(property);
        }
    }
}
