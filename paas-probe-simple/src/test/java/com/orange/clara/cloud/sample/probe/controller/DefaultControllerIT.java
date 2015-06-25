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
package com.orange.clara.cloud.sample.probe.controller;

import com.orange.clara.cloud.sample.probe.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class DefaultControllerIT {
    private static Logger LOGGER = LoggerFactory.getLogger(DefaultControllerIT.class);

    static final String SERVER_URL = "http://localhost";

    @Value("${local.server.port}")
    int port;

    RestTemplate restTemplate = new TestRestTemplate();


    @Test
    public void should_be_available_on_slash(){
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(SERVER_URL + ":" + port, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        LOGGER.debug("Header: {}",responseEntity.getHeaders());
    }

    @Test
    public void should_be_available_on_slash_index(){
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(SERVER_URL + ":" + port+"/index.html", String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        LOGGER.debug("Header: {}",responseEntity.getHeaders());
    }


}