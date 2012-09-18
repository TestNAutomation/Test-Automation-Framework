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

package com.sayem.exceptions;

/**
 * Exception for failed page navigation. Should be thrown when page which was not expected is open.
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 29/08/2011
 */
public class NavigationException extends Exception {
    public NavigationException(String message) {
        super(message);
    }
}
