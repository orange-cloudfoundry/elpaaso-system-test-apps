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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * server side probe implementation.
 * Loops, waiting for the specified time, then return
 * @author apog7416
 *
 */
public class WebProbeImpl implements WebProbe {

	private static final Logger logger = LoggerFactory.getLogger(WebProbeImpl.class);

	public WebProbeImpl() {
		
	}
	
	
	@Override
	public boolean pingRequest(int serverWaitTimeSeconds) {
		logger.info("receive ping request, now waiting");
		for (int i=0;i<serverWaitTimeSeconds;i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("Unexpected Interrupted Exception "+ e.toString());
			}
			logger.info("ping Request held on server for {} seconds ",i);
		}
		return true;
	}
	
	

}
