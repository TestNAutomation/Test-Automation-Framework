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

/**
 * Log levels used by TestLogger.
 * Levels from 1 to 3 are allocated for Test Suites, Test Fixtures and critical framework errors
 * Levels from 4 to 6 are allocated for underlying test libraries and glue code between test fixture and low-level page objects
 * Levels from 7 to 9 are allocated for low-level code which actually performs operations in system under tests such as page objects
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 31/08/2011
 */
public enum LogLevels {
    LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5, LEVEL_6, LEVEL_7, LEVEL_8, LEVEL_9
}
