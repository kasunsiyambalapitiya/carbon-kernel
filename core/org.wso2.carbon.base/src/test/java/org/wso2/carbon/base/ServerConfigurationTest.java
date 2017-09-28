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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by kasun on 9/26/17.
 */
public class ServerConfigurationTest {
    private static ServerConfiguration carbonServerConfiguration;

    @BeforeClass
    public static void createInstance() {
        carbonServerConfiguration = ServerConfiguration.getInstance();
    }

    @Before
    public void setIsInitializedToFalse() throws NoSuchFieldException, IllegalAccessException {
        Field isInitializedField = ServerConfiguration.class.getDeclaredField("isInitialized");
        isInitializedField.setAccessible(true);
        isInitializedField.set(carbonServerConfiguration, false);
    }

    @Test
    public void testInit() throws ServerConfigurationException, NoSuchFieldException, IllegalAccessException {
        assertFalse(getIsInitialized(carbonServerConfiguration, "isInitialized"));
        InputStream inputStream = readFile("carbon.xml");
        carbonServerConfiguration.forceInit(inputStream);
        assertTrue(getIsInitialized(carbonServerConfiguration, "isInitialized"));
    }

    public boolean getIsInitialized(ServerConfiguration carbonServerConfiguration, String name) throws
            NoSuchFieldException, IllegalAccessException {
        boolean isInitialized;
        Field isInitializedField = ServerConfiguration.class.getDeclaredField(name);
        isInitializedField.setAccessible(true);
        isInitialized = (boolean) isInitializedField.get(carbonServerConfiguration);
        return isInitialized;
    }

    @Test
    public void testInitWithLocationOfXMLConfig() throws NoSuchFieldException, IllegalAccessException, ServerConfigurationException {
        assertFalse(getIsInitialized(carbonServerConfiguration, "isInitialized"));
        URL resourceURL = CarbonBaseUtilsTest.class.getClassLoader().getResource("");
        String resourcePath = null;
        if (resourceURL != null) {
            resourcePath = resourceURL.getPath();
            resourcePath = resourcePath + "carbon.xml";
        }
        carbonServerConfiguration.init(resourcePath);
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

