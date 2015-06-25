/**
 * Copyright (C) 2015 Orange
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orange.clara.cloud.sample.probe.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Map;
import java.util.Properties;

import static java.lang.System.*;

public class AppInfo implements Serializable {
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(AppInfo.class);

    private static final long serialVersionUID = 9004825875791737675L;

    Map<String, String> varEnvironment = getenv();

    /**
     * maxHeapSize in bytes
     */
    long maxHeapSize;

    /**
     * usedHeapSize in bytes
     */
    long usedHeapSize;

    private Properties systemProps;


    public AppInfo() {
        MemoryUsage heap = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

        maxHeapSize = heap.getMax();
        usedHeapSize = heap.getUsed();
    }

    /**
     * @return maxHeapSize in bytes
     */
    public long getMaxHeapSize() {
        return maxHeapSize;
    }

    public void setMaxHeapSize(long maxHeapSize) {
        this.maxHeapSize = maxHeapSize;
    }

    /**
     * @return usedHeapSize in bytes
     */
    public long getUsedHeapSize() {
        MemoryUsage heap = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        usedHeapSize = heap.getUsed();

        return usedHeapSize;
    }

    public void setUsedHeapSize(long usedHeapSize) {
        this.usedHeapSize = usedHeapSize;
    }


    public Properties getSystemProperties() {
        systemProps= getProperties();
        return systemProps;
    }

    public Map<String, String> getVarEnvironment() {
        varEnvironment = getenv();
        return varEnvironment;
    }
}
