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

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

public class JeeProbeIT {

    static String SERVER_URL = "http://localhost:9090/jeeprobe";

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(JeeProbeIT.class);

    JeeProbe probe;
    
    @Before
    public void setup() {
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceInterface(JeeProbe.class);
        httpInvokerProxyFactoryBean.setServiceUrl(SERVER_URL + "/Probes/jeeProbe");
        httpInvokerProxyFactoryBean.afterPropertiesSet();
        probe = (JeeProbe) httpInvokerProxyFactoryBean.getObject();
    }
    
    @Test
    public void testGetConfigProperties_SimpleCall() {
        JeeMemoryUsage memoryUsage = probe.getMemoryUsage();

        // log results
        logger.info("max  heap size is: "+memoryUsage.getMaxHeapSize());
        logger.info("used heap size is: "+memoryUsage.getUsedHeapSize());

        // assertions
        assertTrue("maxHeapSize should be > 0",memoryUsage.getMaxHeapSize()>0);
        assertTrue("usedHeapSize should be > 0", memoryUsage.getUsedHeapSize() > 0);

        assertNotNull("dateCreation should not be null", probe.getDateCreation());
        logger.info("build date is: "+probe.getDateCreation());

        assertNotNull("projectVersion should not be null", probe.getProjectVersion());
        logger.info("build version is: "+probe.getProjectVersion());
    }
    
	@Test
	@Ignore("Intranoo is not accessible from Faas, neither from Prod network architecture")
	public void should_be_abble_to_ping_intranoo_using_dns_name() throws Exception {
		String result = probe.getPingWithDnsResult();
		logger.info("Java DNS ping result: " + result);

		assertNotNull(result);
		if (!result.endsWith(JeeProbe.PING_SUCCEED_MESSAGE)) {
			fail(result);
		}
	}

	@Test
	@Ignore("Intranoo is not accessible from Faas, neither from Prod network architecture")
	public void should_be_abble_to_ping_intranoo_using_ip_name() throws Exception {
		String result = probe.getPingWithIpResult();
		logger.info("Java IP ping result: " + result);

		assertNotNull(result);
		if (!result.endsWith(JeeProbe.PING_SUCCEED_MESSAGE)) {
			fail(result);
		}

	}

    @Test 
    public void testGetHostIp() throws UnknownHostException {
		String expectedIp = java.net.InetAddress.getLocalHost().getHostAddress();
    	
    	String actualIp = probe.getHostIp();
    	
    	assertEquals(expectedIp , actualIp);
    }
}
