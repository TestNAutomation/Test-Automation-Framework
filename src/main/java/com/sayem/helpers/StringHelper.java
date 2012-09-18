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

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Collection of static methods to help with String manipulations
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 01/09/2011
 */
public abstract class StringHelper {

    /**
     * Convert camelCase to human-friendly text
     *
     * @param camelCase string
     * @return human-friendly text
     */
    public static String humanize(String camelCase) {
        return camelCase.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    /**
     * Convert space separated string into camelCase string
     *
     * @param text to convert where words separated with spaces
     * @return camelCase string
     */
    public static String toCamelCase(String text) {
        Iterable<String> words = Splitter.on(' ').omitEmptyStrings().trimResults().split(text);
        List<String> convertedWords = newArrayList();
        for (String word : words) {
            String firstSymbol = String.valueOf(word.charAt(0)).toUpperCase();
            String restOfString = word.substring(1, word.length()).toLowerCase();
            String convertedWord = Joiner.on("").join(firstSymbol, restOfString);
            convertedWords.add(convertedWord);
        }
        return Joiner.on("").join(convertedWords);
    }
}
