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

package com.sayem.pages;

import com.google.common.base.Strings;
import com.sayem.configuration.Configuration;
import com.sayem.logger.LogLevels;
import com.sayem.logger.Logger;
import org.openqa.selenium.By;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;


/**
 * Class represents page definition which is stored in separate properties file.
 * Each page should have PageName.properties file in the same package as page class but in resources location.
 * Page definition file should contain properties like expected page title, page URL and definitions of page elements location.
 * All that information is stored in separate file to reduce brittleness of the tests. When page layout changes only page definition file should be updated and no need to change any code.
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 28/08/2011
 */
public class PageDefinition {
    private Properties pageDefinition;


    public PageDefinition(Class pageClass) {
        String pageName = pageClass.getSimpleName();
        pageDefinition = new Properties();
        try {
            InputStream pageDefinitionStream = pageClass.getResourceAsStream(pageName + ".properties");
            pageDefinition.load(pageDefinitionStream);
            checkMandatoryPropertiesPresence(pageDefinition);
        } catch (IOException e) {
            String message = "Can't load page definition for " + pageName + ".\n" +
                    "Error details: " + e.toString();
            Logger.error(LogLevels.LEVEL_8, pageClass.getSimpleName(), message);
            throw new InstantiationError(message);
        } catch (NullPointerException e) {
            String message = "Can't load page definition for " + pageName + ".";
            Logger.error(LogLevels.LEVEL_8, pageClass.getSimpleName(), message);
            throw new InstantiationError(message);
        }
    }

    /**
     * Used only for unit testing, don't use in production as API may change.
     *
     * @param pageDefinition Properties object representing pageModuleDefinition
     */
    public PageDefinition(Properties pageDefinition) {
        this.pageDefinition = pageDefinition;
    }

    /**
     * Checks that mandatory page definition properties are present in page definition file.
     * Usually those properties are expected page title and page URL.
     *
     * @param pageDefinition Properties object which contains loaded page definition properties
     * @throws InstantiationError if mandatory properties are missing
     */
    private void checkMandatoryPropertiesPresence(Properties pageDefinition) throws InstantiationError {
        try {
            assertThat(pageDefinition.stringPropertyNames(), hasItems("URL", "Title"));
        } catch (AssertionError e) {
            throw new InstantiationError("Mandatory properties missing in page definition: " + e.getMessage());
        }
    }

    public String getTitle() {
        return pageDefinition.getProperty("Title");
    }

    /**
     * Retrieves page url based on page definition file and environment settings
     *
     * @return String page URL
     */
    public String getUrl() {
        String environmentUrl = Configuration.getEnvironmentProperty("url");
        if (Strings.isNullOrEmpty(environmentUrl)) {
            return pageDefinition.getProperty("URL");
        } else {
            return environmentUrl + pageDefinition.getProperty("URL");
        }
    }

    /**
     * Retrieves element locator for given element name from page definition.
     *
     * @param elementName name of the element in page definition file
     * @return By object which represents element locator in one of following formats: id, name, xpath, css, tagName, linkText, partialLinkText.
     * @throws IllegalArgumentException if element locator can not be found or parsed.
     */
    public By getElementLocator(String elementName) throws IllegalArgumentException {
        String elementLocator = pageDefinition.getProperty(elementName);
        if (Strings.isNullOrEmpty(elementLocator)) {
            String message = "Can't find element locator for element " + elementName + " in page definition. \n" +
                    "Element definition should be in format elementName=locatorType~locatorValue.";
            Logger.error(LogLevels.LEVEL_8, getClass().getSimpleName(), message);
            throw new IllegalArgumentException(message);
        }

        String[] parsedElementLocator = parseElementLocator(elementLocator);

        String locatorType = parsedElementLocator[0];
        String locatorValue = parsedElementLocator[1];

        if (locatorType.equalsIgnoreCase("id")) {
            return By.id(locatorValue);

        } else if (locatorType.equalsIgnoreCase("name")) {
            return By.name(locatorValue);

        } else if (locatorType.equalsIgnoreCase("xpath")) {
            return By.xpath(locatorValue);
        } else if (locatorType.equalsIgnoreCase("css")) {
            return By.cssSelector(locatorValue);

        } else if (locatorType.equalsIgnoreCase("tagName")) {
            return By.tagName(locatorValue);

        } else if (locatorType.equalsIgnoreCase("linkText")) {
            return By.linkText(locatorValue);

        } else if (locatorType.equalsIgnoreCase("partialLinkText")) {
            return By.partialLinkText(locatorValue);

        } else {
            String message = "Can't parse element locator [" + elementLocator + "] for element " + elementName + ".\n" +
                    "Locator type " + locatorType + " is not recognised. Allowed values are id, name, xpath, css, tagName, linkText, partialLinkText.";
            Logger.error(LogLevels.LEVEL_8, getClass().getSimpleName(), message);
            throw new IllegalArgumentException(message);
        }

    }

    /**
     * Parse element locator string in format locatorType~locatorValue and return it as array of two strings.
     *
     * @param elementLocator String element locator to parse.
     * @return String array  where first element is locator type and second is locator value
     * @throws IllegalArgumentException if element locator can not be parsed
     */
    private String[] parseElementLocator(String elementLocator) throws IllegalArgumentException {
        String[] parsedElementLocator = new String[2];
        int firstSeparatorIndex = elementLocator.indexOf("~");
        if (firstSeparatorIndex > 0) {
            parsedElementLocator[0] = elementLocator.substring(0, firstSeparatorIndex);
            parsedElementLocator[1] = elementLocator.substring(firstSeparatorIndex + 1, elementLocator.length());

        } else {
            throw new IllegalArgumentException("Can't parse element locator [" + elementLocator + "].\n" +
                    "Element locator should have format [locatorType~locatorValue]");
        }

        if (Strings.isNullOrEmpty(parsedElementLocator[0])) {
            throw new IllegalArgumentException("Error parsing element locator [" + elementLocator + "].\n" +
                    "Locator Type can't be empty");
        }
        if (Strings.isNullOrEmpty(parsedElementLocator[1])) {
            throw new IllegalArgumentException("Error parsing element locator [" + elementLocator + "].\n" +
                    "Locator value can't be empty");
        }

        return parsedElementLocator;
    }
}
