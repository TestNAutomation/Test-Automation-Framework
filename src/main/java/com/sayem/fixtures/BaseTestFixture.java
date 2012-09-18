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

package com.sayem.fixtures;

import com.sayem.configuration.Configuration;
import com.sayem.exceptions.StopTestException;
import com.sayem.pages.PageFactory;
import com.sayem.testdata.TestDataLoader;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * Base Test Fixture. All tests fixtures should extend this class.
 * In test fixture all exceptions should be handled and fixture should either recover from exception or throw StopTestException
 * or StopAllException depending on severity of underlying exception.
 * Errors can not be handled as they represent major problems with framework configuration and normally require abortion of test run
 * Base Test Fixture also takes care of framework configuration
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 27/08/2011
 */

// @RunWith(value = ConcordionRunner.class)

public abstract class BaseTestFixture {


    @BeforeClass
    public static void setUp() {
        //Configure test framework
        Configuration.configure();

    }

    @AfterClass
    public static void tearDown() {
        //That will kill WebDriver instance between tests
        PageFactory.tearDown();
    }

    /**
     * Retrieves test data for test.
     *
     * @param dataSetName name of test data set without .xml extension
     * @return Object which represents loaded test data
     * @throws InstantiationError if test data can not be loaded
     */
    protected Object getTestData(String dataSetName) throws InstantiationError {
        return TestDataLoader.loadTestData(getClass(), dataSetName);
    }

    /**
     * Retrieves default test data for test.
     *
     * @return Object which represents loaded test data
     * @throws InstantiationError if test data can not be loaded
     */
    protected Object getTestData() throws InstantiationError {
        return TestDataLoader.loadTestData(getClass());
    }

    /**
     * Stops current test execution
     *
     * @param message explanation why test should be stopped
     * @throws StopTestException
     */
    protected void stopTest(String message) throws StopTestException {
        throw new StopTestException(message);
    }
}
