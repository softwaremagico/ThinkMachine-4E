package com.softwaremagico.tm.file;

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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@Test(groups = "file")
public class FileManagerTest {

    @Test
    public void readsAndDeletesTemporaryFiles() throws Exception {
        final File file = Files.createTempFile("think-machine", ".txt").toFile();
        Files.writeString(file.toPath(), "first\nsecond\n", StandardCharsets.UTF_8);

        Assert.assertTrue(FileManager.fileExist(file.getPath()));
        Assert.assertEquals(FileManager.inLines(file.getPath()), List.of("first", "second"));
        Assert.assertEquals(FileManager.readTextFile(file), "first\nsecond\n");
        Assert.assertEquals(FileManager.readTextFile(file.getPath(), StandardCharsets.UTF_8), "first\nsecond\n");
        Assert.assertTrue(FileManager.deleteFile(file.getPath()));
        Assert.assertFalse(FileManager.deleteFile(file.getPath()));
    }

    @Test
    public void readsResourcesAndStreams() throws Exception {
        Assert.assertTrue(FileManager.getFileFromResources("DecadosCommander.txt").size() > 1);
        Assert.assertTrue(FileManager.readTextFromJarInLines("/DecadosCommander.txt").size() > 1);
        Assert.assertFalse(FileManager.readTextFromJar("/DecadosCommander.txt").isEmpty());
        Assert.assertEquals(FileManager.convertStreamToString(new ByteArrayInputStream("text".getBytes(StandardCharsets.UTF_8))), "text");
        Assert.assertEquals(FileManager.convertStreamToString(null), "");
    }

    @Test
    public void findsResourcesAndJarFiles() throws Exception {
        final File resource = FileManager.getResource("DecadosCommander.txt");

        Assert.assertTrue(resource.exists());
        Assert.assertNotNull(FileManager.convert2OsPath(resource.toURI().toURL()));
        Assert.assertEquals(FileManager.findJarFiles(resource.getParent()).length, 1);
        Assert.assertTrue(FileManager.getFileFromResources("missing-resource").isEmpty());
        Assert.assertTrue(FileManager.readTextFromJar("/missing-resource").isEmpty());
    }
}
