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

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

public class WebProbeIT {

	private static Logger logger=LoggerFactory.getLogger(WebProbeIT.class);
	
	static String SERVER_URL = "http://localhost:9090/";

	
	@Test
	public void testGetConfigProperties_SimpleCall() {
		
		int serverWaitTimeSeconds = 3;		
		
		boolean result = callServer(serverWaitTimeSeconds);
		
		Assert.assertTrue(result);
	}
	
	@Test
	public void testHttpTimeout() {
		boolean timeout = false;
		int serverWaitTimeSeconds = 5;
		do {

			long start = System.currentTimeMillis();
			long end = System.currentTimeMillis();
			try {
				this.callServer(serverWaitTimeSeconds);
			} catch (RemoteAccessException e) {
				logger.error("failur : " + e.toString());
				timeout = true;
			} finally {
				end = System.currentTimeMillis();
			}
			serverWaitTimeSeconds += 40;
			logger.debug("ok with server time " + serverWaitTimeSeconds);
			logger.debug("(client time ms :{} )", (end - start));

		} while (timeout == false);

		logger.info("timeout occurs with server time " + serverWaitTimeSeconds);

		// must not timeout below 30 seconds
		Assert.assertTrue(serverWaitTimeSeconds > 240);		
		
	}
	
	/**
	 * utility method to call with Spring RPC the waiting server
	 * @param serverWaitTimeSeconds
	 * @return
	 */
	private boolean callServer(int serverWaitTimeSeconds) {
		HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
		httpInvokerProxyFactoryBean.setServiceInterface(WebProbe.class);
		httpInvokerProxyFactoryBean.setServiceUrl(SERVER_URL + "/timeoutProbe");
		httpInvokerProxyFactoryBean.afterPropertiesSet();
		WebProbe configProbe = (WebProbe) httpInvokerProxyFactoryBean.getObject();


		boolean result=configProbe.pingRequest(serverWaitTimeSeconds);
		return result;
	}
	
	
}
