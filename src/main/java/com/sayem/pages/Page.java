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

import com.sayem.exceptions.NavigationException;

/**
 * Minimal interface which every page should implement
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 27/08/2011
 */
public interface Page {

    public void navigate() throws NavigationException, NavigationException;

    public void verify() throws NavigationException;

    public String getTitle();

    public String getCurrentTitle();

    public String getUrl();

    public String getCurrentUrl();


}
