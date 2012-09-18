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

import com.sayem.logger.LogLevels;
import com.sayem.logger.Logger;

/**
 * StopTestException should be thrown when test can not continue it's execution and should be stopped.
 * For example if test was not able to open page it doesn't make much sense to continue test so it should be stopped
 * to fail quickly and not waste time and resources
 * <p/>
 * StopTestException ideally should be thrown only from test fixtures
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 31/08/2011
 */
public class StopTestException extends Exception {

    public StopTestException(String message) {
        super(message);
        Logger.error(LogLevels.LEVEL_3, this.getClass().getSimpleName(), message);
    }
}
