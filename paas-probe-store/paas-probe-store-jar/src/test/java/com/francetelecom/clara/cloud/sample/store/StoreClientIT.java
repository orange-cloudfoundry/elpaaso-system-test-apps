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
package com.francetelecom.clara.cloud.sample.store;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

@Ignore
@RunWith(JUnit4.class)

public class StoreClientIT {

	private static final Logger log = LoggerFactory.getLogger(StoreClientIT.class);

	private String url = "http://undefined";
	private RestTemplate restTemplate = new RestTemplate();

	@Test
	public void should_list_keys() {
		// list Bucket
		Object keys = restTemplate.getForObject(url + "/storeprobe/keys", String.class);
		assertNotNull("keys should not be null", keys);
	}

	@Test
	public void can_write_and_read_file_in_bucket() {

		// write an object
		restTemplate.getForObject(url + "/storeprobe/create?key={key}&value={value}", Void.class, "myKey","myContent");

		// read back the object
		String myContentRead = restTemplate.getForObject(url + "/storeprobe/read?key={key}", String.class,"myKey");
		assertEquals("toto", myContentRead);

		
		// delete object
		restTemplate.getForObject(url+"/storeprobe/detete?key={}", Void.class,"myKey");

	}

}
