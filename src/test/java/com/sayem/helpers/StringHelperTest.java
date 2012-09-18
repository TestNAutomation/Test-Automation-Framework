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

import org.junit.Test;

import static com.sayem.helpers.StringHelper.humanize;
import static com.sayem.helpers.StringHelper.toCamelCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 01/09/2011
 */
public class StringHelperTest {
    @Test
    public void testHumanize() throws Exception {
        String errorMessage = "Can't convert camel case to human-friendly format";
        assertThat(errorMessage, humanize("CamelCase"), is(equalTo("Camel Case")));
        assertThat(errorMessage, humanize("ComplexCamelCase"), is(equalTo("Complex Camel Case")));
    }

    @Test
    public void testToCamelCase() throws Exception {
        String errorMessage = "Can't convert space separated string to camel case";
        assertThat(errorMessage, toCamelCase("Camel Case"), is(equalTo("CamelCase")));
        assertThat(errorMessage, toCamelCase("Complex Camel Case"), is(equalTo("ComplexCamelCase")));
        assertThat(errorMessage, toCamelCase("advanced Camel Case"), is(equalTo("AdvancedCamelCase")));


    }
}
