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

package com.sayem.configuration;

import com.google.common.base.Strings;
import com.sayem.enums.BrowserType;
import com.sayem.helpers.FileHelper;
import com.sayem.logger.LogLevels;
import com.sayem.logger.Logger;

import java.io.FileInputStream;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Configuration class implements test automation framework configuration functionality
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 03/09/2011
 */
public abstract class Configuration {

    private static Properties frameworkConfiguration = new Properties();
    private static Properties environmentsDefinition = new Properties();
    private static BrowserType browserType;
    private static String environment;
    private static String testDataSetName;

    public static final String FRAMEWORK_CONFIG_FILE_NAME = "configuration.properties";
    public static final String ENVIRONMENTS_DEFINITION_FILE_NAME = "environments.properties";

    public static final String TEST_RESULTS_DIRECTORY_PROPERTY_NAME = "testResultsDirectory";
    public static final String TEST_RESULTS_DIRECTORY_SYS_PROPERTY_NAME = "concordion.output.dir";
    public static final String CONCORDION_EXTENSIONS_PROPERTY_NAME = "concordionExtensions";
    public static final String CONCORDION_EXTENSIONS_SYS_PROPERTY_NAME = "concordion.extensions";
    public static final String DEFAULT_BROWSER_TYPE_PROPERTY_NAME = "defaultBrowserType";
    public static final String CHROME_DRIVER_PATH_PROPERTY_NAME = "pathToChromeDriver";
    public static final String DEFAULT_ELEMENT_TIMEOUT_PROPERTY_NAME = "defaultElementTimeout";
    public static final String DEFAULT_ENVIRONMENT_PROPERTY_NAME = "defaultEnvironment";
    public static final String DEFAULT_TEST_DATA_SET_PROPERTY_NAME = "defaultTestDataSet";
    public static final String ENVIRONMENT_SYSTEM_PROPERTY_NAME = "test.runconfig.env";
    public static final String TEST_DATA_SET_SYSTEM_PROPERTY_NAME = "test.runconfig.dataset";
    public static final String BROWSER_TYPE_SYSTEM_PROPERTY_NAME = "test.runconfig.browser";


    /**
     * Configure test automation framework
     *
     * @throws InstantiationError if configuration failed
     */
    public static void configure() throws InstantiationError {
        try {
            configureTestResultsDirectory();
            configureConcordionExtensions();
            configureBrowserType();
            configureEnvironment();
            configureTestDataSetName();
        } catch (InstantiationError e) {
            Logger.error(LogLevels.LEVEL_1, "FrameworkConfiguration", e.getMessage());
            throw new InstantiationError(e.toString());
        }
    }


    /**
     * Configure which Concordion extensions should be used
     *
     * @throws InstantiationError if configuration failed
     */
    private static void configureConcordionExtensions() throws InstantiationError {
        String concordionExtensions = getFrameworkConfiguration().getProperty(CONCORDION_EXTENSIONS_PROPERTY_NAME);
        if (!Strings.isNullOrEmpty(concordionExtensions)) {
            System.setProperty(CONCORDION_EXTENSIONS_SYS_PROPERTY_NAME, concordionExtensions);
        }
    }


    /**
     * Configures where test results should be saved. Path can be relative to target folder or absolute
     *
     * @throws InstantiationError if configuration failed
     */
    private static void configureTestResultsDirectory() throws InstantiationError {
        String testResultsDirectory = getFrameworkConfiguration().getProperty(TEST_RESULTS_DIRECTORY_PROPERTY_NAME);
        if (!Strings.isNullOrEmpty(testResultsDirectory)) {
            System.setProperty(TEST_RESULTS_DIRECTORY_SYS_PROPERTY_NAME, testResultsDirectory);
        }
    }


    /**
     * Configures browser type depending on values in configuration file and active run configuration
     *
     * @throws InstantiationError if browser type can not be configured
     */
    private static void configureBrowserType() throws InstantiationError {
        String runConfigurationBrowserType = System.getProperty(BROWSER_TYPE_SYSTEM_PROPERTY_NAME);
        String browserTypeString;
        if (Strings.isNullOrEmpty(runConfigurationBrowserType)) {
            browserTypeString = getFrameworkConfiguration().getProperty(DEFAULT_BROWSER_TYPE_PROPERTY_NAME);
        } else {
            browserTypeString = runConfigurationBrowserType;
        }

        if (Strings.isNullOrEmpty(browserTypeString)) {
            browserType = BrowserType.UNKNOWN;
        } else {
            try {
                browserType = BrowserType.valueOf(browserTypeString);
            } catch (IllegalArgumentException e) {
                throw new InstantiationError("Default Browser Type value is incorrect. Allowed values: HTML_UNIT, IE, FIREFOX, CHROME");
            }
        }
    }

    /**
     * Configures environment depending on values in configuration file and on active run configuration
     */
    private static void configureEnvironment() {
        String runConfigurationEnvironment = System.getProperty(ENVIRONMENT_SYSTEM_PROPERTY_NAME);
        if (Strings.isNullOrEmpty(runConfigurationEnvironment)) {
            environment = getFrameworkConfiguration().getProperty(DEFAULT_ENVIRONMENT_PROPERTY_NAME);
        } else {
            environment = System.getProperty(ENVIRONMENT_SYSTEM_PROPERTY_NAME);
        }
    }

    /**
     * Configures default test data set name depending on values in configuration file and active run configuration
     */
    private static void configureTestDataSetName() {
        String runConfigurationTestDataSetName = System.getProperty(TEST_DATA_SET_SYSTEM_PROPERTY_NAME);
        if (Strings.isNullOrEmpty(runConfigurationTestDataSetName)) {
            testDataSetName = getFrameworkConfiguration().getProperty(DEFAULT_TEST_DATA_SET_PROPERTY_NAME);
        } else {
            testDataSetName = runConfigurationTestDataSetName;
        }
    }

    /**
     * Retrieves configured default browser type
     *
     * @return BrowserType enum value representing which type of browser should be used as default.
     *         UNKNOWN is returned if that property is not configured
     */

    public static BrowserType getBrowserType() {
        return browserType;
    }

    /**
     * Retrieves framework configuration from configuration file. It will read configuration file only once,
     * for all subsequent calls information stored in memory will be returned.
     * This method should not be accessed directly from outside of Configuration class and it's automated tests.
     *
     * @return Properties object which represents framework configuration
     * @throws InstantiationError if framework configuration can not be retrieved or incorrect
     */
    public static Properties getFrameworkConfiguration() throws InstantiationError {
        if (frameworkConfiguration.isEmpty()) {
            try {
                frameworkConfiguration.load(new FileInputStream(FileHelper.getAbsoluteResourcePath(FRAMEWORK_CONFIG_FILE_NAME)));
            } catch (Exception e) {
                throw new InstantiationError("Can't read test automation framework configuration: " + e.toString());
            }
        }

        checkMandatoryPropertiesPresent(frameworkConfiguration);
        return frameworkConfiguration;
    }

    /**
     * Checks that mandatory framework configuration properties present
     *
     * @param frameworkConfiguration Properties object which contains loaded framework configuration
     * @throws InstantiationError if mandatory properties missing
     */
    private static void checkMandatoryPropertiesPresent(Properties frameworkConfiguration) throws InstantiationError {
        checkArgument(!frameworkConfiguration.isEmpty(), "Framework Configuration is empty");
        try {
            checkPropertyPresent(TEST_RESULTS_DIRECTORY_PROPERTY_NAME, frameworkConfiguration);
            checkPropertyPresent(CONCORDION_EXTENSIONS_PROPERTY_NAME, frameworkConfiguration);
        } catch (IllegalArgumentException e) {
            throw new InstantiationError("Mandatory framework configuration properties missing.\n" +
                    e.toString());
        }
    }

    /**
     * Checks that property with given name is present in given properties object
     *
     * @param propertyName String name of a property to check
     * @param properties   Properties object where to check property presence
     * @throws IllegalArgumentException if property is not present
     */
    private static void checkPropertyPresent(String propertyName, Properties properties) throws IllegalArgumentException {
        checkArgument(!frameworkConfiguration.isEmpty(), "Framework Configuration is empty");
        if (!properties.containsKey(propertyName)) {
            throw new IllegalArgumentException("Property " + propertyName + " was not found");
        }
    }

    /**
     * Sets framework configuration from properties file.
     * Should not be used directly.
     * Used for unit testing of framework configuration
     *
     * @param frameworkConfiguration Properties object representing loaded framework configuration
     */
    public static void setFrameworkConfiguration(Properties frameworkConfiguration) {
        Configuration.frameworkConfiguration = frameworkConfiguration;
    }

    /**
     * Forces reinitialisation of framework configuration. It will load it from config file and from run configuration.
     *
     * @throws InstantiationError if configuration failed
     */
    public static void reload() throws InstantiationError {
        frameworkConfiguration = new Properties();
        configure();
    }

    /**
     * Retrieves path to Chrome driver executable from configuration file
     *
     * @return String path to Chrome driver executable
     */
    public static String getPathToChromeDriver() {
        String path = getFrameworkConfiguration().getProperty(CHROME_DRIVER_PATH_PROPERTY_NAME);
        checkArgument(!Strings.isNullOrEmpty(path), "Path to Google Chrome Driver is null or empty");
        return path;
    }

    /**
     * Retrieves default timeout for how long WebDriver should wait for element to appear on page in seconds.
     *
     * @return int timeout in seconds
     * @throws IllegalArgumentException if timeout can not be retrieved correctly
     */
    public static int getDefaultElementTimeout() throws IllegalArgumentException {
        String timeout = getFrameworkConfiguration().getProperty(DEFAULT_ELEMENT_TIMEOUT_PROPERTY_NAME);
        if (Strings.isNullOrEmpty(timeout)) {
            return 0;
        }
        try {
            return Integer.parseInt(timeout);
        } catch (Exception e) {
            throw new IllegalArgumentException("Default element timeout is incorrect, can't convert " + timeout + " to number.");
        }
    }

    /**
     * Retrieves default environment that should be used for test runs
     *
     * @return String environment name
     */
    public static String getEnvironmentName() {
        return environment;
    }

    /**
     * Retrieves test data set name that should be used for test runs.
     *
     * @return String test data set name
     */
    public static String getTestDataSetName() {
        return testDataSetName;
    }


    /**
     * Retrieves configuration-specific property for environment that was specified to be used
     *
     * @param propertyName name of environment property
     * @return property value
     */
    public static String getEnvironmentProperty(String propertyName) {
        propertyName = getEnvironmentName() + "." + propertyName;
        return getEnvironmentsDefinition().getProperty(propertyName);
    }


    /**
     * Retrieves environments definition from definition file. It will read definition file only once,
     * for all subsequent calls information stored in memory will be returned.
     *
     * @return Properties object which represents environments definition
     * @throws InstantiationError if environments definition can not be read
     */
    private static Properties getEnvironmentsDefinition() {
        if (environmentsDefinition.isEmpty()) {
            try {
                environmentsDefinition.load(new FileInputStream(FileHelper.getAbsoluteResourcePath(ENVIRONMENTS_DEFINITION_FILE_NAME)));
            } catch (Exception e) {
                throw new InstantiationError("Can't read environments definition: " + e.toString());
            }
        }

        return environmentsDefinition;
    }
}
