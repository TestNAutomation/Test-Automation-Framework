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
import com.sayem.exceptions.NavigationException;
import com.sayem.logger.LogLevels;
import com.sayem.logger.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


/**
 * Pages Base class, implements some common methods used by all page objects.
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 27/08/2011
 */
public abstract class BasePage implements Page {

    protected WebDriver driver;
    protected PageDefinition pageDefinition;

    public BasePage(WebDriver driver) {
        setDriver(driver);
        pageDefinition = new PageDefinition(this.getClass());
    }


    private void setDriver(WebDriver driver) {
        PageFactory.checkDriver(driver);
        this.driver = driver;
    }

    /**
     * Open page in the browser. Uses URL property from page definition file to determine how to navigate
     *
     * @throws NavigationException if page can not be opened
     */
    public void navigate() throws NavigationException {
        navigate(pageDefinition.getUrl());
        verify();
    }

    /**
     * Open given URL in active browser. Doesn't verify that correct page is opened.
     *
     * @param url String URL to open
     * @throws NavigationException if URL can not be opened
     */
    public void navigate(String url) throws NavigationException {
        //TODO: implement support for environments

        if (Strings.isNullOrEmpty(url)) {
            String message = "Can't open page, URL is null or empty";
            Logger.error(LogLevels.LEVEL_8, this.getClass().getSimpleName(), message);
            throw new NavigationException(message);
        }

        try {
            Logger.info(LogLevels.LEVEL_8, this.getClass().getSimpleName(), "Opening " + url);
            driver.get(url);
        } catch (Exception e) {
            String message = "Can't open " + url + ". " + e.toString();
            Logger.error(LogLevels.LEVEL_8, this.getClass().getSimpleName(), message);
            throw new NavigationException(message);
        }
    }


    /**
     * Verifies that correct page is opened. By default it verifies that opened page Title and URL match with expected.
     * This verification mechanism is not universal and wouldn't be applicable in all situations.
     * So this method is good candidate to be overwritten in child pages to implement page-specific verification.
     *
     * @throws NavigationException if wrong page is opened
     */
    public void verify() throws NavigationException {
        verifyByTitle();
        verifyByUrl();
    }

    /**
     * Verify that correct page is loaded by comparing actual title of the page loaded in browser and expected page title
     *
     * @throws NavigationException if verification failed
     */
    public void verifyByTitle() throws NavigationException {
        String actualPageTitle = getCurrentTitle();
        String expectedPageTitle = getTitle();

        if (!actualPageTitle.equalsIgnoreCase(expectedPageTitle)) {
            String message = "Wrong page is opened. Expected page with title [" + expectedPageTitle + "], \n" +
                    "got [" + actualPageTitle + "]";
            Logger.error(LogLevels.LEVEL_8, this.getClass().getSimpleName(), message);
            throw new NavigationException(message);
        }
    }

    /**
     * Verify that correct page is loaded by comparing actual URL of the page loaded in browser and expected page URL
     *
     * @throws NavigationException if verification failed
     */
    public void verifyByUrl() throws NavigationException {
        String actualUrl = getCurrentUrl();
        String expectedUrl = getUrl();

        if (!actualUrl.equalsIgnoreCase(expectedUrl)) {
            String message = "Wrong page is opened. Expected page with URL [" + expectedUrl + "], \n" +
                    "got [" + actualUrl + "]";
            Logger.error(LogLevels.LEVEL_8, this.getClass().getSimpleName(), message);
            throw new NavigationException(message);
        }

    }

    /**
     * Retrieve expected page title from page definition file
     *
     * @return String expected page title
     */
    public String getTitle() {
        return pageDefinition.getTitle();
    }

    /**
     * Retrieve title of the page currently loaded in the browser
     *
     * @return String current page title
     */
    public String getCurrentTitle() {
        return driver.getTitle();
    }

    /**
     * Retrieve expected page URL from page definition file
     *
     * @return String expected page URL
     */
    public String getUrl() {
        return pageDefinition.getUrl();
    }

    /**
     * Retrieve URL of page currently loaded in the browser
     *
     * @return String current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Retrieves element locator for given element name from page definition file
     *
     * @param elementName String element name
     * @return By object which represents element locator
     */
    protected By getElementLocator(String elementName) {
        return this.pageDefinition.getElementLocator(elementName);
    }
}
