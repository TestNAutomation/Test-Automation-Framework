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

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

/**
 * File helpers
 *
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 02/09/2011
 */
public abstract class FileHelper {

    private static final String BACKUP_FILE_EXTENSION = ".bak";

    /**
     * Restore file from backup and delete backup afterwards.
     * Backup file should be located in the same folder and have .bak extension
     *
     * @param fileName name of a file to be restored without .bak extension
     * @throws java.io.IOException if backup can not be restored
     */
    public static void restoreFile(String fileName) throws IOException {
        try {
            File originalFile = new File(fileName);
            File backUpFile = new File(fileName + BACKUP_FILE_EXTENSION);
            Files.copy(backUpFile, originalFile);
            backUpFile.delete();
        } catch (Exception e) {
            throw new IOException("Can't restore " + fileName + "file from backup: " + e.toString());
        }
    }

    /**
     * Backup file to new file with .bak extension
     *
     * @param fileName name of a file to back up
     * @throws java.io.IOException if backup can not be created
     */
    public static void backUpFile(String fileName) throws IOException {
        try {
            File originalFile = new File(fileName);
            File backUpFile = new File(fileName + BACKUP_FILE_EXTENSION);
            Files.copy(originalFile, backUpFile);
        } catch (Exception e) {
            throw new IOException("Can't back up " + fileName + ": " + e.toString());
        }
    }

    /**
     * Retrieves absolute path to specified resource
     *
     * @param resourceName name of a resource, resource should be present in class path
     * @return String absolute path to resource
     * @throws java.io.IOException if absolute path can not be determined
     */
    public static String getAbsoluteResourcePath(String resourceName) throws IOException {
        try {
            return FileHelper.class.getClassLoader().getResource(resourceName).toURI().getPath();
        } catch (Exception e) {
            throw new IOException("Can't get absolute path for resource " + resourceName + ": " + e.toString());
        }
    }
}
