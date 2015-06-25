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
package com.francetelecom.clara.cloud.sample.probe;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.naming.NamingException;

import junit.framework.Assert;

public class ConfigProbeImplTest {

	ConfigProbe jndiProbe;
	
	@Before
	public void setup() throws NamingException {
		
		// Initialize a jndi mock
		
		 String configCtx = ConfigProbeImpl.CONFIG_JNDI_CONTEXT;
		 // add a slash if configCtx is not empty
		 if(!configCtx.equals("")) configCtx += "/";
		 
		 SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
	 
		 builder.bind(configCtx+"paas.prop1", "value1");
		 builder.bind(configCtx+"paas.prop2", "value2");
		 builder.activate();

		// Create SUT
				
		jndiProbe = new ConfigProbeImpl();
	}
	
	@Test
	public void testListJndiEntries() {

		Map<String,String> entries = jndiProbe.getConfigProperties().getEntries();
		
		Assert.assertEquals("value1", entries.get("paas.prop1"));
		Assert.assertEquals("value2", entries.get("paas.prop2"));
		
		/*
		 * config/paas/prop1
		 * config/paas/prop2
		 * 
		 * config/paas.prop
		 * 
		 */
		
	}
}
