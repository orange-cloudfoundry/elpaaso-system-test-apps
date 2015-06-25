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
package com.francetelecom.clara.cloud.sample.probe.jee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeProbeImplTest {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(JeeProbeImplTest.class);

	// SUT
	JeeProbe jeeProbe;

	@Before
	public void setup() {
		jeeProbe = new JeeProbeImpl();
	}


	@Test
	public void testMemoryUsage() {

		// run test
		JeeMemoryUsage memoryUsage = jeeProbe.getMemoryUsage();

		// log result
		logger.info("max  heap size is: " + memoryUsage.getMaxHeapSize());
		logger.info("used heap size is: " + memoryUsage.getUsedHeapSize());
		// expected MaxHeapSize
		long expectedMaxHeapSize = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();

		// assertions
		assertEquals("maxHeapSize", expectedMaxHeapSize, memoryUsage.getMaxHeapSize());
		assertTrue("usedHeapSize should be > 0", memoryUsage.getUsedHeapSize() > 0);
	}

	@Test
	public void testInstalledPackagesOnUbuntu() {
		JeeProbeImpl jeeProbeSpy = spy(new JeeProbeImpl());
		when(jeeProbeSpy.getOsNameAndVersion()).thenReturn("xxxUbuntuxxx");

		String installedPackages = jeeProbeSpy.getInstalledPackages();
		logger.debug(installedPackages);
	}

	@Test
	public void testInstalledPackagesCommandOnUbuntu() {
		JeeProbeImpl jeeProbeSpy = spy(new JeeProbeImpl());
		when(jeeProbeSpy.getOsNameAndVersion()).thenReturn("xxxUbuntuxxx");

		String installedPackages = jeeProbeSpy.getInstalledPackageCommand();
		assertEquals("dpkg --get-selections", installedPackages);
	}

	@Test
	public void testInstalledPackagesCommandOnCentos() {
		JeeProbeImpl jeeProbeSpy = spy(new JeeProbeImpl());
		when(jeeProbeSpy.getOsNameAndVersion()).thenReturn("xxxCentOSxxx");

		String installedPackages = jeeProbeSpy.getInstalledPackageCommand();
		assertEquals("sudo rpm -qa", installedPackages);
	}

	@Test
	public void testGetHostIp() throws UnknownHostException {
		String expectedIp = java.net.InetAddress.getLocalHost().getHostAddress();

		String actualIp = jeeProbe.getHostIp();

		assertEquals(expectedIp, actualIp);
	}

	@Test
	@Ignore("Intranoo is not accessible from Faas, neither from Prod network architecture")
	public void should_be_abble_to_ping_intranoo_using_dns_name() throws Exception {
		String result = jeeProbe.getPingWithDnsResult();
		logger.info("Java DNS ping result: " + result);

		assertNotNull(result);
		if (!result.endsWith(JeeProbe.PING_SUCCEED_MESSAGE)) {
			fail(result);
		}
	}

	@Test
	@Ignore("Intranoo is not accessible from Faas, neither from Prod network architecture")
	public void should_be_abble_to_ping_intranoo_using_ip_name() throws Exception {
		String result = jeeProbe.getPingWithIpResult();
		logger.info("Java IP ping result: " + result);

		assertNotNull(result);
		if (!result.endsWith(JeeProbe.PING_SUCCEED_MESSAGE)) {
			fail(result);
		}

	}

}
