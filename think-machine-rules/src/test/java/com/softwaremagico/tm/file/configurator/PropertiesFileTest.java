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

import com.softwaremagico.tm.exceptions.PropertyNotStoredException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

@Test(groups = "configuration")
public class PropertiesFileTest {
    private Path temporaryDirectory;

    @BeforeMethod
    public void createTemporaryDirectory() throws IOException {
        temporaryDirectory = Files.createTempDirectory("think-machine-properties");
    }

    @AfterMethod
    public void deleteTemporaryDirectory() throws IOException {
        if (temporaryDirectory != null) {
            Files.walk(temporaryDirectory)
                    .sorted(java.util.Comparator.reverseOrder())
                    .forEach(path -> path.toFile().delete());
        }
    }

    @Test
    public void storesAndLoadsPropertiesFromFilesAndUrls() throws IOException, PropertyNotStoredException {
        final Properties properties = new Properties();
        properties.setProperty("name", "think-machine");
        final File propertiesFile = temporaryDirectory.resolve("nested/settings.properties").toFile();

        Assert.assertFalse(PropertiesFile.store(properties, propertiesFile.getParent(), propertiesFile.getName()));
        Assert.assertEquals(PropertiesFile.load(propertiesFile).getProperty("name"), "think-machine");
        Assert.assertEquals(PropertiesFile.load(propertiesFile.toURI().toURL()).getProperty("name"), "think-machine");
    }

    @Test
    public void validatesStreamsAndLoadsStreamContents() throws IOException {
        final Properties properties = PropertiesFile.load(new ByteArrayInputStream("key=value".getBytes(StandardCharsets.UTF_8)));

        Assert.assertEquals(properties.getProperty("key"), "value");
        Assert.expectThrows(IllegalArgumentException.class, () -> PropertiesFile.load((File) null));
        Assert.expectThrows(IllegalArgumentException.class, () -> PropertiesFile.load((java.net.URL) null));
        Assert.expectThrows(IllegalArgumentException.class, () -> PropertiesFile.load((java.io.InputStream) null));
        Assert.expectThrows(IllegalArgumentException.class, () -> PropertiesFile.store(null, new java.io.ByteArrayOutputStream()));
        Assert.expectThrows(IllegalArgumentException.class,
                () -> PropertiesFile.store(new Properties(), (java.io.OutputStream) null));
    }

    @Test
    public void storesAndLoadsPropertiesSourceFileWithoutWatcher() throws IOException, PropertyNotStoredException {
        final PropertiesSourceFile sourceFile = new PropertiesSourceFile(temporaryDirectory.toString(), "settings.conf");

        sourceFile.storeInFile(Map.of("zeta", "last", "alpha", "first"));

        Assert.assertEquals(sourceFile.loadFile().getProperty("alpha"), "first");
        Assert.assertEquals(sourceFile.loadFile().getProperty("zeta"), "last");
        Assert.assertEquals(sourceFile.toString(), temporaryDirectory + File.separator + "settings.conf");
    }

    @Test
    public void returnsNullWhenPropertiesSourceFileDoesNotExist() {
        final PropertiesSourceFile sourceFile = new PropertiesSourceFile(temporaryDirectory.toString(), "missing.conf");

        Assert.assertNull(sourceFile.loadFile());
    }
}
