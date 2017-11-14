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

package org.wso2.carbon.sample.bundle10;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kasun on 11/13/17.
 */
public class Activator implements BundleActivator {
    private static final Logger logger = LoggerFactory.getLogger("Activator.class");

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        logger.info("===== Activated updated the sample bundle 10");

    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        logger.info("==== Deactivated updated sample bundle 10");
    }
}
