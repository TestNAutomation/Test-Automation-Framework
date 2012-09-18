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

package com.sayem.testdata;

import com.google.common.base.Strings;
import com.sayem.configuration.Configuration;
import com.sayem.logger.LogLevels;
import com.sayem.logger.Logger;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.InputStream;

public abstract class TestDataLoader {

    public static final String TEST_DATA_FOLDER_NAME = "testdata";
    public static final String DEFAULT_TEST_DATASET_NAME = "default";

    /**
     * Loads test data for given test class and given test dataset name.
     *
     * @param testClass   class of a test for which to load test data
     * @param dataSetName name of test dataset file without .xml extension
     * @return object that represents loaded test data
     * @throws InstantiationError if test data can not be loaded
     */
    public static Object loadTestData(Class testClass, String dataSetName) throws InstantiationError {
        try {
            InputStream inputStream = testClass.getResourceAsStream(TEST_DATA_FOLDER_NAME + File.separator + dataSetName + ".xml");
            XStream xStream = new XStream();
            return xStream.fromXML(inputStream);
        } catch (Exception e) {
            String message = "Can't load test data for \"" + testClass.getSimpleName() + "\" test from \"" + dataSetName + "\" test dataset.\n" +
                    "Error: " + e.toString();
            Logger.error(LogLevels.LEVEL_1, "TestDataLoader", message);
            throw new InstantiationError(message);
        }
    }

    /**
     * Loads default test data for given test class. Usually it's default.xml unless value was specified in Configuration or Run configuration
     *
     * @param testClass class of a test for which to load test data
     * @return object that represents loaded test data
     * @throws InstantiationError if test data can not be loaded
     */
    public static Object loadTestData(Class testClass) throws InstantiationError {
        String testDataSetName = Configuration.getTestDataSetName();
        if (Strings.isNullOrEmpty(testDataSetName)) {
            return loadTestData(testClass, DEFAULT_TEST_DATASET_NAME);
        } else {
            return loadTestData(testClass, testDataSetName);
        }
    }
}
