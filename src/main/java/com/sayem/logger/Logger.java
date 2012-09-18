/*
 * Copyright (c) 2011 SQANTA OÜ, info@sqanta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sayem.logger;

import org.slf4j.LoggerFactory;

import static com.sayem.helpers.StringHelper.humanize;

/**
 * Logger for tests. In facts it's a thin wrapper around SLF4J which makes it a bit easier to use different log levels
 * for log messages so they can be filtered depending on how detailed information user wants
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 31/08/2011
 */
public abstract class Logger {

    //SLF4J instance
    static final org.slf4j.Logger LOG = LoggerFactory.getLogger("");

    /**
     * Logs info message with specified log level and object name
     * Object name will be converted from camelCase to human-friendly format
     *
     * @param logLevel   - value from LEVEL_1 to LEVEL_9, see LogLevels enum for more details
     * @param objectName
     * @param message
     */
    public static void info(LogLevels logLevel, String objectName, String message) {
        objectName = humanize(objectName);
        LOG.info("[" + logLevel + "][" + objectName + "] " + message);
    }


    /**
     * Logs error messages
     *
     * @param message
     */
    public static void error(String message) {
        LOG.error(message);
    }

    /**
     * Log error message and specify object name where it's happened.
     * Object name will be converted from camelCase to human-friendly format
     *
     * @param objectName
     * @param message
     */
    public static void error(String objectName, String message) {
        objectName = humanize(objectName);
        LOG.error("[" + objectName + "] " + message);
    }


    /**
     * Logs error message with specified log level and object name
     * Object name will be converted from camelCase to human-friendly format
     *
     * @param logLevel   - value from LEVEL_1 to LEVEL_9, see LogLevels enum for more details
     * @param objectName
     * @param message
     */
    public static void error(LogLevels logLevel, String objectName, String message) {
        objectName = humanize(objectName);
        LOG.error("[" + logLevel + "][" + objectName + "] " + message);
    }


}
