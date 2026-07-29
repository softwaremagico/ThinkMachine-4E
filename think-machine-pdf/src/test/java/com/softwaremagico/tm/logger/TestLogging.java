package com.softwaremagico.tm.logger;

/*-
 * #%L
 * Think Machine 4E (PDF Sheets)
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

import com.softwaremagico.tm.log.BasicLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines basic log behavior for tests.
 */
public class TestLogging extends BasicLogger {

    private static final Logger logger = LoggerFactory.getLogger(TestLogging.class);

    public static void info(String className, String message) {
        info(logger, className, message);
    }

    public static void info(String message) {
        info(logger, message);
    }

    public static void warning(String className, String message) {
        warning(logger, className, message);
    }

    public static void warning(String message) {
        warning(logger, message);
    }

    public static void debug(String className, String message) {
        debug(logger, className, message);
    }

    public static void debug(String message) {
        debug(logger, message);
    }

    public static void severe(String className, String message) {
        severe(logger, className, message);
    }

    public static void errorMessage(String className, Throwable throwable) {
        errorMessageNotification(logger, className, throwable);
    }

    public static void errorMessage(Class<?> clazz, Throwable throwable) {
        errorMessageNotification(logger, clazz.getName(), throwable);
    }

    public static void errorMessage(String className, String error) {
        errorMessageNotification(logger, className, error);
    }

    public static void errorMessage(String error) {
        severe(logger, error);
    }

    public static void errorMessage(Object object, Throwable throwable) {
        errorMessageNotification(logger, object.getClass().getName(), throwable);
    }
}

