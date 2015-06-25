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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class SystemInfoTest {

    private static final Logger logger = LoggerFactory.getLogger(SystemInfoTest.class);


    SystemInfo systemInfo;

    @Before
    public void setup() {
        systemInfo = spy(new SystemInfo());
    }

    @Test
    public void testInstalledPackagesOnUbuntu() {
        when(systemInfo.getOsNameAndVersion()).thenReturn("xxxUbuntuxxx");

        String installedPackages = systemInfo.getInstalledPackages();
        logger.debug(installedPackages);
    }

    @Test
    public void testInstalledPackagesCommandOnUbuntu() {
        when(systemInfo.getOsNameAndVersion()).thenReturn("xxxUbuntuxxx");

        String installedPackages = systemInfo.getInstalledPackageCommand();
        Assert.assertEquals("dpkg --get-selections", installedPackages);
    }

    @Test
    public void testInstalledPackagesCommandOnCentos() {
        when(systemInfo.getOsNameAndVersion()).thenReturn("xxxCentOSxxx");

        String installedPackages = systemInfo.getInstalledPackageCommand();
        Assert.assertEquals("sudo rpm -qa", installedPackages);
    }

    @Test
    public void testGetHostIp() throws UnknownHostException {
        String expectedIp = java.net.InetAddress.getLocalHost().getHostAddress();

        String actualIp = systemInfo.getHostIp();

        Assert.assertEquals(expectedIp, actualIp);
    }

}