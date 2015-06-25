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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.ow2.carol.jndi.wrapping.UnicastJNDIReferenceWrapper;

public class ConfigProbeImpl implements ConfigProbe {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(ConfigProbeImpl.class);

	// Currently config properties are bound in root context
	// This constant might be useful if they are bound in a dedicated context
	static final String CONFIG_JNDI_CONTEXT = "";
	
	public ConfigProperties getConfigProperties() {

		logger.debug("getConfigProperties is called" );
		ConfigProperties configProperties = new ConfigProperties();

		Context context;
		NamingEnumeration<NameClassPair> names;
		
		try {
			context = new InitialContext();
			names = context.list(CONFIG_JNDI_CONTEXT);
		
			while(names.hasMore()) {
				NameClassPair binding = names.next();
				String name = binding.getName();
				try {
					Object value = context.lookup(name);
					logger.debug(String.format("jndi name: %-25s object: %s",name,value.toString()));
					if(value instanceof String) {
						logger.info( String.format(" property: %-25s  value: %s",name, value));
						configProperties.put(name, (String)value);				
					} else {
						// object type is not string: this can't be config property
					}
					
				} catch(NamingException e) {
					logger.debug(String.format("jndi name: %-25s lookup failed: %s",name, e));
				}
		}
		} catch (NamingException e) {
			logger.error("Exception "+e);
			// return an empty collection
		}

		return configProperties;
	}
	

}
