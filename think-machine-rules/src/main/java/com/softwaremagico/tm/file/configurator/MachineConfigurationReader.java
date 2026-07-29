package com.softwaremagico.tm.file.configurator;

/*-
 * #%L
 * Think Machine (Rules)
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
import com.softwaremagico.tm.file.FileManager;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.file.watcher.FileWatcher;
import com.softwaremagico.tm.file.watcher.FileWatcher.FileModifiedListener;
import com.softwaremagico.tm.log.ConfigurationLog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MachineConfigurationReader extends ConfigurationReader {
    private static final String DEFAULT_CONFIG_FILE = "settings.conf";
    private static final String USER_CONFIG_FILE = "settings.conf";
    private static final String FOLDER_STORE_USER_DATA = "ThinkMachine";
    private FileWatcher modulesFileWatcher;

    // Tags
    private static final String MODULES_PATH = "modulesPath";

    // Default
    private PropertiesSourceFile userSourceFile;

    private static final class InstanceHolder {
        private static final MachineConfigurationReader INSTANCE = new MachineConfigurationReader();

        private InstanceHolder() {
        }
    }

    public static MachineConfigurationReader getInstance() {
        return InstanceHolder.INSTANCE;
    }

    protected MachineConfigurationReader() {
        super();

        this.setProperty(MODULES_PATH, System.getProperty("java.io.tmpdir"));

        final PropertiesSourceFile sourceFile = new PropertiesSourceFile(DEFAULT_CONFIG_FILE);
        sourceFile.addFileModifiedListeners(new FileModifiedListener() {

            @Override
            public void changeDetected(Path pathToFile) {
                ConfigurationLog.info(this.getClass().getName(),
                        "Application's settings file '" + pathToFile + "' change detected.");
                MachineConfigurationReader.this.readConfigurations();
            }
        });
        this.addPropertiesSource(sourceFile);

        this.setUserSourceFile(getSettingsFolderAtHome(), USER_CONFIG_FILE);

        // Log if any property has changed the value.
        this.addPropertyChangedListener(new PropertyChangedListener() {

            @Override
            public void propertyChanged(String propertyId, String oldValue, String newValue) {
                ConfigurationLog.info(this.getClass().getName(), "Property '" + propertyId
                        + "' has changed value from '" + oldValue + "' to '" + newValue + "'.");
            }
        });

        this.readConfigurations();
    }

    public void setUserSourceFile(String path, String file) {
        this.removePropertiesSource(this.userSourceFile);
        this.userSourceFile = new PropertiesSourceFile(file);
        this.userSourceFile.setFilePath(path);
        ConfigurationLog.info(this.getClass().getName(),
                "User config file set to '" + this.userSourceFile.toString() + "'.");
        this.userSourceFile.addFileModifiedListeners(new FileModifiedListener() {

            @Override
            public void changeDetected(Path pathToFile) {
                ConfigurationLog.info(this.getClass().getName(),
                        "Application's settings file '" + pathToFile + "' change detected.");
                MachineConfigurationReader.this.readConfigurations();
            }
        });
        this.addPropertiesSource(this.userSourceFile);
    }

    public static String getSettingsFolderAtHome() {
        final String folder = System.getProperty("user.home") + File.separator + "." + FOLDER_STORE_USER_DATA;
        FileManager.makeFolderIfNotExist(folder);
        return folder;
    }

    @Override
    public void storeProperties() throws PropertyNotStoredException {
        try {
            this.userSourceFile.storeInFile(this.getAllProperties());
        } catch (final IOException e) {
            throw new PropertyNotStoredException(MachineConfigurationReader.class.getName(), e);
        }
    }

    @Override
    public File getUserProperties() {
        return new File(this.userSourceFile.getFilePath() + File.separator + this.userSourceFile.getFileName());
    }

    @Override
    public String getUserPropertiesPath() {
        return this.userSourceFile.getFilePath() + File.separator + this.userSourceFile.getFileName();
    }

    protected String getPropertyLogException(String propertyId) {
        try {
            return this.getProperty(propertyId);
        } catch (final PropertyNotFoundException e) {
            ConfigurationLog.errorMessage(this.getClass().getName(), e);
            return null;
        }
    }

    public String getModulesPath() {
        return this.getPropertyLogException(MODULES_PATH);
    }

    /**
     * This method is intended to be only used by the ModuleManager.
     *
     * @param modulesFolder
     *            path to the new modules folder.
     * @param fileModifiedListener
     *            Listen if a new module is added.
     */
    public void setModulesPath(String modulesFolder, FileModifiedListener fileModifiedListener) {
        this.setProperty(MODULES_PATH, modulesFolder);
        this.setModulesWatcher(modulesFolder);
        if (fileModifiedListener != null) {
            this.addModulesFileModifiedListener(fileModifiedListener);
        }
    }

    private void setModulesWatcher(final String modulesFolderpath) {
        this.stopModulesFileWatcher();
        try {
            this.modulesFileWatcher = new FileWatcher(modulesFolderpath);
        } catch (final IOException e) {
            ConfigurationLog.errorMessage(ModuleManager.class.getName(), e);
        } catch (final NullPointerException npe) {
            ConfigurationLog.warning(ModuleManager.class.getName(), "Modules directory to watch not found!");
        }
    }

    public void stopModulesFileWatcher() {
        if (this.modulesFileWatcher != null) {
            this.modulesFileWatcher.closeFileWatcher();
        }
    }

    public void addModulesFileModifiedListener(FileModifiedListener fileModifiedListener) {
        if (this.modulesFileWatcher != null) {
            this.modulesFileWatcher.addFileModifiedListener(fileModifiedListener);
        }
    }
}
