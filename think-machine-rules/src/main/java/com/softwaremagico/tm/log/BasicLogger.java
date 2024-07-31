package com.softwaremagico.tm.log;

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

import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public abstract class BasicLogger {

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not
     * allowed for this user, ...
     *
     * @param message
     */
    public static void warning(Logger logger, String message) {
        logger.warn(message);
    }

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not
     * allowed for this user, ...
     *
     * @param message
     */
    public static void warning(Logger logger, String className, String message) {
        logger.warn(className + ": " + message);
    }

    /**
     * Events that have business meaning (i.e. creating category, deleting form,
     * ...). To follow user actions.
     *
     * @param message
     */
    public static void info(Logger logger, String message) {
        logger.info(message);
    }

    /**
     * Events that have business meaning (i.e. creating category, deleting form,
     * ...). To follow user actions.
     *
     * @param message
     */
    public static void info(Logger logger, String className, String message) {
        info(logger, className + ": " + message);
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application
     * access to a method, opening database connection, etc.
     *
     * @param message
     */
    public static void debug(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application
     * access to a method, opening database connection, etc.
     *
     * @param message
     */
    public static void debug(Logger logger, String className, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(className + ": " + message);
        }
    }

    /**
     * To log any not expected error that can cause application malfunctions.
     * I.e. couldn't open database connection, etc..
     *
     * @param message
     */
    protected static void severe(Logger logger, String message) {
        logger.error(message);
    }

    /**
     * To log any not expected error that can cause application malfunctions.
     *
     * @param message
     */
    public static void severe(Logger logger, String className, String message) {
        severe(logger, className + ": " + message);
    }

    /**
     * Logs an error and send an email to the email configured in settings.conf
     * file.
     *
     * @param className
     * @param error
     */
    public static void errorMessageNotification(Logger logger, String className, String error) {
        severe(logger, className, error);
    }

    public static String getStackTrace(Throwable throwable) {
        final Writer writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        return writer.toString();
    }

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not
     * allowed for this user, ...
     *
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void warning(Logger logger, String messageTemplate, Object... arguments) {
        logger.warn(messageTemplate, arguments);
    }

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not
     * allowed for this user, ...
     *
     * @param logger          the Logger.
     * @param className       the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void warning(Logger logger, String className, String messageTemplate, Object... arguments) {
        logger.warn(className + ": " + messageTemplate, arguments);
    }

    /**
     * Events that have business meaning (i.e. creating category, deleting form,
     * ...). To follow user actions.
     *
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void info(Logger logger, String messageTemplate, Object... arguments) {
        logger.info(messageTemplate, arguments);
    }

    /**
     * Events that have business meaning (i.e. creating category, deleting form,
     * ...). To follow user actions.
     * <p>
     *
     * @param logger          the Logger.
     * @param className       the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void info(Logger logger, String className, String messageTemplate, Object... arguments) {
        info(logger, className + ": " + messageTemplate, arguments);
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application
     * access to a method, opening database connection, etc.
     *
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void debug(Logger logger, String messageTemplate, Object... arguments) {
        if (logger.isDebugEnabled()) {
            logger.debug(messageTemplate, arguments);
        }
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application
     * access to a method, opening database connection, etc.
     *
     * @param logger          the Logger.
     * @param className       the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void debug(Logger logger, String className, String messageTemplate, Object... arguments) {
        if (logger.isDebugEnabled()) {
            logger.debug(className + ": " + messageTemplate, arguments);
        }
    }

    /**
     * To log any not expected error that can cause application malfunctions.
     * I.e. couldn't open database connection, etc..
     *
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    protected static void severe(Logger logger, String messageTemplate, Object... arguments) {
        logger.error(messageTemplate, arguments);
    }

    /**
     * To log any not expected error that can cause application malfunctions.
     *
     * @param logger          the Logger.
     * @param className       the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void severe(Logger logger, String className, String messageTemplate, Object... arguments) {
        severe(logger, className + ": " + messageTemplate, arguments);
    }

    /**
     * Logs an error and send an email to the email configured in settings.conf
     * file.
     *
     * @param logger          the Logger.
     * @param className       the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void errorMessageNotification(Logger logger, String className, String messageTemplate, Object... arguments) {
        severe(logger, className, messageTemplate, arguments);
    }

    public static void errorMessageNotification(Logger logger, String className, Throwable throwable) {
        logger.error("Exception on class {}:\n", className, throwable);
    }
}
