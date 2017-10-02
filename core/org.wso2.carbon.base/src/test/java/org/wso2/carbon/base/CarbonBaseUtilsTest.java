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

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * Test class for CarbonBaseUtil related methods with a correct sec policy.
 */
public class CarbonBaseUtilsTest {

    @BeforeClass
    public void setSecurityManager() {
        URL resourceURL = CarbonBaseUtilsTest.class.getClassLoader().getResource("");
        if (resourceURL != null) {
            String resourcePath = resourceURL.getPath();
            resourcePath = resourcePath + "policy-test.policy";
            System.setProperty("java.security.policy", resourcePath);
            System.setSecurityManager(new SecurityManager());
        }
    }

    @AfterClass
    public void clearSecurityManager() {
        System.clearProperty("java.security.policy");
    }

    @Test(groups = {"org.wso2.carbon.base"})
    public void testGetCarbonConfigDirPath() throws ClassNotFoundException, IllegalAccessException,
            NoSuchFieldException {
        //setting env variables for testing getCarbonConfigDirPath() in CarbonBaseUtils
        Map<String, String> newEnv = new HashMap<>();
        newEnv.put(CarbonBaseConstants.CARBON_CONFIG_DIR_PATH_ENV, "../wso2am-2.1.0/repository/conf");
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newEnv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField
                    ("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newEnv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = null;
                    field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newEnv);
                }
            }
        }
        //setting env variables done for testing getCarbonConfigDirPath() in CarbonBaseUtils
        assertEquals(CarbonBaseUtils.getCarbonConfigDirPath(),
                "../wso2am-2.1.0/repository/conf", "Must provide the location where carbon.xml resides");
    }

    @Test(groups = {"org.wso2.carbon.base"})
    public void testGetServerXML() {
        System.setProperty(CarbonBaseConstants.CARBON_CONFIG_DIR_PATH, "../wso2am-2.1.0/repository/conf");
        String absoluteLocation = CarbonBaseUtils.getServerXml();
        assertEquals(absoluteLocation, "../wso2am-2.1.0/repository/conf/carbon" + ".xml", "Must match the formed " +
                "absoluteLocation of carbon.xml");
    }

    @Test(groups = {"org.wso2.carbon.base"})
    public void testGetCarbonHome() {
        System.setProperty(CarbonBaseConstants.CARBON_HOME, "../wso2am-2.1.0");
        assertEquals(CarbonBaseUtils.getCarbonHome(), "../wso2am-2.1.0", "Must provide the carbon home ");
    }

    @Test(groups = {"org.wso2.carbon.base"})
    public void testCheckSecurityWithClasses() {
        List<String> allowedClasses = Arrays.asList("org.junit.runners.model.FrameworkMethod$1.runReflectiveCall",
                "org.wso2.carbon.base.CarbonBaseUtils.checkSecurity", "org.junit.internal.runners.statements" +
                        ".InvokeMethod.evaluate", "org.junit.runner.JUnitCore.run", "org.junit.runners.ParentRunner.run");
        CarbonBaseUtils.checkSecurity(allowedClasses);
    }

    @Test(groups = {"org.wso2.carbon.base"})
    public void testCheckSecurityWithMethods() {
        Map<String, String> allowedMethods = new HashMap<>();
        allowedMethods.put("sun.reflect.NativeMethodAccessorImpl", "invoke");
        allowedMethods.put("sun.reflect.DelegatingMethodAccessorImpl", "invoke");
        allowedMethods.put("java.lang.reflect.Method", "invoke");
        allowedMethods.put("org.junit.internal.runners.statements.InvokeMethod", "evaluate");
        CarbonBaseUtils.checkSecurity(allowedMethods);
    }
}
