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

import com.sayem.configuration.Configuration;
import com.sayem.helpers.WindowHelper;
import com.sayem.logger.LogLevels;
import com.sayem.logger.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Page Factory is responsible for creation of page objects.
 * Read more about page factory here – https://bitbucket.org/zyxit/test-automation-framework/wiki/Page%20Factory
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 27/08/2011
 */
public abstract class PageFactory {

    //TODO: Implement Page Factory using dynamic proxy in similar way as Selenium guys did but made it to use properties file
    //TODO: Implement methods to detect and return page which is currently open

    private static WebDriver driver;


    /**
     * Instantiates page object. Page object should implement {@link Page} interface and should have constructor which accepts WebDriver instance.
     * Instantiated page will not open browser during or after instantiation.
     *
     * @param pageClass class of a page which should be instantiated
     * @return object of given page class
     * @throws InstantiationError if page object can not be instantiated
     */
    public static <T extends Page> T getPage(Class<T> pageClass) throws InstantiationError {
        try {
            return pageClass.getConstructor(WebDriver.class).newInstance(getDriver());

        } catch (InstantiationException e) {
            String message = "Can't instantiate page " + pageClass.getName() + ".\n" +
                    "Error details: " + e;
            Logger.error(LogLevels.LEVEL_1, "PageFactory", message);
            throw new InstantiationError(message);

        } catch (IllegalAccessException e) {
            String message = "Can't instantiate page " + pageClass.getName() + ".\n" +
                    "Error details: " + e;
            Logger.error(LogLevels.LEVEL_1, "PageFactory", message);
            throw new InstantiationError(message);

        } catch (InvocationTargetException e) {
            String message = "Can't instantiate page " + pageClass.getName() + ".\n" +
                    "Error details: " + e.getTargetException();
            Logger.error(LogLevels.LEVEL_1, "PageFactory", message);
            throw new InstantiationError(message);

        } catch (NoSuchMethodException e) {
            String message = "Can't instantiate page " + pageClass.getName() + ".\n" +
                    "Constructor which accepts WebDriver object is not defined, use BasePage as example.\n" +
                    "Error details: " + e;
            Logger.error(LogLevels.LEVEL_1, "PageFactory", message);
            throw new InstantiationError(message);
        }
    }

    /**
     * Checks that WebDriver instance is ready for use
     *
     * @param driver WebDriver object
     * @throws IllegalArgumentException if driver object is not ready for use
     */
    public static void checkDriver(WebDriver driver) {
        try {
            checkNotNull(driver);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("WebDriver object is null and not ready for use.");
        }
        try {
            driver.getWindowHandle();   //To check that driver is connected to the browser
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("WebDriver object is not connected to the browser and not ready for use.");
        }
    }

    /**
     * Implements singleton-like logic for WebDriver. If no active WebDriver object exists this method will create new one.
     * If active WebDriver object exists method will return it. Type of driver depends on framework configuration. If configuration is not set then HtmlUnit will be used.
     *
     * @return active WebDriver object which is ready for use
     */
    public static WebDriver getDriver() {
        try {
            checkDriver(driver);
        } catch (Exception e) {
            switch (Configuration.getBrowserType()) {
                case UNKNOWN:
                    driver = new HtmlUnitDriver();
                    break;
                case IE:
                    driver = new InternetExplorerDriver();
                    break;
                case FIREFOX:
                    driver = new FirefoxDriver();
                    break;
                case HTML_UNIT:
                    driver = new HtmlUnitDriver();
                    ((HtmlUnitDriver) driver).setJavascriptEnabled(true);
                    break;
                case CHROME:
                    System.setProperty("webdriver.chrome.driver", Configuration.getPathToChromeDriver());
                    driver = new ChromeDriver();
                    break;
                default:
                    driver = new HtmlUnitDriver();
                    break;
            }

            //Set how long webDriver should wait for elements to be found
            driver.manage().timeouts().implicitlyWait(Configuration.getDefaultElementTimeout(), TimeUnit.SECONDS);

            WindowHelper.focus(driver);
            WindowHelper.maximize(driver);
        }


        return driver;
    }


    /**
     * Tear down active WebDriver object
     */
    public static void tearDown() {
        try {
            checkDriver(driver);
            driver.quit();
            driver = null;

        } catch (Exception e) {
            //If driver is already killed don't do anything
        }
    }
}
