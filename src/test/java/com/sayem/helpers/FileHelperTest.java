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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.sayem.matchers.FileMatchers.exists;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Kostya Marchenko, kostya@sqanta.com
 *         Date 02/09/2011
 */
public class FileHelperTest {
    private static final String ORIGINAL_FILE_NAME = "SampleFile.txt";
    private static final String BACKUP_FILE_NAME = "SampleFile.txt.bak";

    @Before
    public void setUp() {
        deleteFiles();
    }

    @Test
    public void testFileBackup() throws Exception {
        new File(ORIGINAL_FILE_NAME).createNewFile();

        FileHelper.backUpFile(ORIGINAL_FILE_NAME);

        assertThat("File backup doesn't work", new File(BACKUP_FILE_NAME), exists());
        assertThat("Original file disappeared after backup", new File(ORIGINAL_FILE_NAME), exists());
    }

    @Test
    public void testFileBackupWhenBackupFileAlreadyExists() throws Exception {
        new File(ORIGINAL_FILE_NAME).createNewFile();
        new File(BACKUP_FILE_NAME).createNewFile();

        FileHelper.backUpFile(ORIGINAL_FILE_NAME);

        assertThat("File backup doesn't work", new File(BACKUP_FILE_NAME), exists());
        assertThat("Original file disappeared after backup", new File(ORIGINAL_FILE_NAME), exists());

    }

    @Test
    public void testBackupOfMissingFile() throws Exception {
        try {
            FileHelper.backUpFile(ORIGINAL_FILE_NAME);
            fail("Exception not thrown when trying to backup non existent file");
        } catch (IOException e) {
            assertThat("File which was missing before backup now exist", new File(ORIGINAL_FILE_NAME), not(exists()));
            assertThat("Backup file was created for missing original file", new File(BACKUP_FILE_NAME), not(exists()));
        }
    }

    @Test
    public void testFileRestore() throws Exception {
        new File(BACKUP_FILE_NAME).createNewFile();

        FileHelper.restoreFile(ORIGINAL_FILE_NAME);

        assertThat("File restore doesn't work", new File(ORIGINAL_FILE_NAME), exists());
        assertThat("Backup file was not deleted after restore", new File(BACKUP_FILE_NAME), not(exists()));
    }

    @Test
    public void testFileRestoreWhenOriginalFileAlreadyExists() throws Exception {
        new File(ORIGINAL_FILE_NAME).createNewFile();
        new File(BACKUP_FILE_NAME).createNewFile();

        FileHelper.restoreFile(ORIGINAL_FILE_NAME);

        assertThat("File restore doesn't work", new File(ORIGINAL_FILE_NAME), exists());
        assertThat("Backup file was not deleted after restore", new File(BACKUP_FILE_NAME), not(exists()));
    }

    @Test
    public void testFileRestoreOfMissingFile() throws Exception {
        try {
            FileHelper.restoreFile(ORIGINAL_FILE_NAME);
            fail("Exception not thrown when trying to restore non existent file");
        } catch (IOException e) {
            assertThat("File which was missing before restore now exist", new File(ORIGINAL_FILE_NAME), not(exists()));
            assertThat("Backup file was missing before restore now exists", new File(BACKUP_FILE_NAME), not(exists()));
        }
    }


    @After
    public void tearDown() {
        deleteFiles();
    }

    private void deleteFiles() {
        try {
            new File(ORIGINAL_FILE_NAME).delete();
            new File(BACKUP_FILE_NAME).delete();
        } catch (Exception e) {
            //Ignore
        }
    }
}
