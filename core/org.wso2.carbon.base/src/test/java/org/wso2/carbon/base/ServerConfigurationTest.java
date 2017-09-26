/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.base;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by kasun on 9/26/17.
 */
public class ServerConfigurationTest {

    @Test
    public void testInit() throws ServerConfigurationException, NoSuchFieldException, IllegalAccessException {
        ServerConfiguration carbonServerConfiguration = ServerConfiguration.getInstance();

        assertFalse(getIsInitialized(carbonServerConfiguration,"isInitialized"));
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = readFile("carbon.xml");
        carbonServerConfiguration.forceInit(inputStream);
        assertTrue(getIsInitialized(carbonServerConfiguration,"isInitialized"));
    }

    public boolean getIsInitialized(ServerConfiguration carbonServerConfiguration,String name) throws
            NoSuchFieldException, IllegalAccessException {
        boolean isInitialized;
        Field isInitializedField = ServerConfiguration.class.getDeclaredField(name);
        isInitializedField.setAccessible(true);
        isInitialized = (boolean) isInitializedField.get(carbonServerConfiguration);
        return isInitialized;
    }


    /**
     * This is used to get the contents of the txt file as an input stream
     *
     * @param path location path to the relevant txt file
     * @return an inputstream containing the contents of the given file
     */
    private InputStream readFile(String path) {
        InputStream inputStream;
        ClassLoader classLoader = getClass().getClassLoader();
        inputStream = classLoader.getResourceAsStream(path);

        return inputStream;
    }
}

