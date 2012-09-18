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

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.fail;


/**
 * Unit tests for PageFactory. For acceptance tests see /src/spec folder.
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 27/08/2011
 */
public class PageFactoryUnitTest {
    //TODO: Implement this test with WebDriver mock

    @Test
    public void testCheckDriver() throws Exception {
        WebDriver driver = new HtmlUnitDriver();

        try {
            PageFactory.checkDriver(driver);
        } catch (Exception e) {
            driver.quit();
            fail("CheckDriver fails for valid WebDriver object");
        }

        driver.quit();
        try {
            PageFactory.checkDriver(driver);
            fail("CheckDriver didn't fail for empty driver object");
        } catch (IllegalArgumentException e) {
            //Don't do anything, it's expected to throw exception
        }
    }


    @Test
    public void testTearDown() throws Exception {

        try {
            PageFactory.tearDown();
        } catch (Exception e) {
            fail("PageFactory throws " + e.toString() + " when tearDown is called for empty driver");
        }

        WebDriver driver = PageFactory.getDriver();
        PageFactory.tearDown();
        try {
            driver.getWindowHandle();   //Should throw NullPointerException
            fail("PageFactory didn't tear-down WebDriver object properly");
        } catch (Exception e) {
            //Don't do anything
        }

    }


    @After
    public void tearDown() {
        PageFactory.tearDown();
    }


}
