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

package com.sayem.helpers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Collection of useful helpers to manipulate browser window
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         08/09/2011
 */
public class WindowHelper {


    /**
     * Sets active browser window in focus
     *
     * @param driver WebDriver object linked to browser window
     */
    public static void focus(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.focus();");
        } catch (Exception e) {
            //Ignore
        }
    }

    /**
     * Maximizes active browser window
     *
     * @param driver WebDriver object linked to browser window
     */
    public static void maximize(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.moveTo(0,0);" +
                    "window.resizeTo(screen.width,screen.height);");
        } catch (Exception e) {
            //Ignore exception
        }
    }
}
