package com.softwaremagico.tm.file;

/*
 * #%L
 * KendoTournamentGenerator
 * %%
 * Copyright (C) 2008 - 2012 Softwaremagico
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

import java.io.File;

public final class PathManager {
    private static final String APPLICATION_FOLDER = "think-machine";
    public static final String MODULES_FOLDER = "modules";
    public static final String DEFAULT_MODULE_FOLDER = "Fading Suns Revised Edition";
    public static final String SKILLS_FOLDER = "skills";

    private PathManager() {
    }

    public static String getRootPath() {
        final String soName = System.getProperty("os.name");
        if (soName.contains("Linux") || soName.contains("linux")) {
            final File file = new File(CustomPaths.APPLICATION_LOCATION.getValue() + APPLICATION_FOLDER);
            if (file.exists()) {
                return file.getPath() + File.separator;
            } else {
                return "";
            }
        } else if (soName.contains("Windows") || soName.contains("windows") || soName.contains("vista")
                || soName.contains("Vista")) {
            return "";
        }
        return "";
    }

    public static String getImagePath() {
        return getRootPath() + "images" + File.separator;
    }

    public static String getDefaultModulesFolder() {
        return getModulePath(null);
    }

    public static String getModulePath(String moduleName) {
        if (moduleName == null) {
            return MODULES_FOLDER + File.separator;
        }
        return MODULES_FOLDER + File.separator + moduleName + File.separator;
    }

    public static String getBackgroundPath() {
        return getImagePath() + "background" + File.separator + "background.png";
    }

    public static String getBannerPath() {
        return getImagePath() + "banner" + File.separator + "banner.png";
    }

    public static String getLogoPath() {
        return getImagePath() + "fading-suns.png";
    }

    public static String getDefault() {
        return getImagePath() + "defaults" + File.separator;
    }

    public static String getLogFile() {
        return APPLICATION_FOLDER + ".log";
    }

    public static String getIconFolder() {
        return getImagePath() + "icons" + File.separator;
    }

    public static String getDefaultPdfPath() {
        return System.getProperty("user.home");
    }
}
