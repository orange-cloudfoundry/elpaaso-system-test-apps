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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.orange.clara.cloud.sample.probe.Application;
import com.orange.clara.cloud.sample.probe.domain.AppInfo;
import com.orange.clara.cloud.sample.probe.domain.BuildInfo;
import com.orange.clara.cloud.sample.probe.domain.SystemInfo;
import jdk.nashorn.internal.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class ApiProbeControllerIT {
    static final String SERVER_URL = "http://localhost";
    private static Logger LOGGER = LoggerFactory.getLogger(ApiProbeControllerIT.class);


    @Value("${local.server.port}")
    int port;

    RestTemplate restTemplate = new TestRestTemplate();
    private String serverBaseUrl;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        this.serverBaseUrl = SERVER_URL + ":" + port;
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetHostIp() throws UnknownHostException {
        String expectedIp = java.net.InetAddress.getLocalHost().getHostAddress();

        SystemInfo info = restTemplate.getForEntity(serverBaseUrl + "/system", SystemInfo.class).getBody();

        assertEquals(expectedIp, info.getHostIp());
    }

    @Test
    public void should_be_java8_runtime() throws UnknownHostException {
        String expectedJavaVersion = "1.8";

        String remoteJavaVersionFromSystem = restTemplate.getForEntity(serverBaseUrl + "/system/java", String.class).getBody();
        String remoteJavaVersionFromEnv = restTemplate.getForEntity(serverBaseUrl + "/env/java.specification.version", String.class).getBody();

        assertEquals(expectedJavaVersion, remoteJavaVersionFromSystem);
        assertEquals(remoteJavaVersionFromSystem, remoteJavaVersionFromEnv);

    }

    @Test
    public void installedPackages_and_os_name_should_contain_a_value() throws UnknownHostException {

        SystemInfo info = restTemplate.getForEntity(serverBaseUrl + "/system", SystemInfo.class).getBody();

        assertNotNull(info.getInstalledPackages());
        assertNotNull(info.getOsNameAndVersion());
    }

    @Test
    public void should_get_app_info() {

        AppInfo app = restTemplate.getForEntity(serverBaseUrl + "/app", AppInfo.class).getBody();

        assertNotNull(app);
        assertNotNull(app.getMaxHeapSize());
        assertNotNull(app.getUsedHeapSize());
        assertNotNull(app.getVarEnvironment());
        assertNotNull(app.getSystemProperties());
    }

    @Test
    public void should_get_system_properties_as_properties() {

        Properties appProperties = getEnvSpecificProperties("systemProperties");
        final Set<String> entries = appProperties.stringPropertyNames();
        assertNotNull(entries);
        assertTrue("System properties should not be empty", entries.size() > 0);
        for (String key : entries) {
            final String value = appProperties.getProperty(key);
            LOGGER.info("Property <{}>= <{}>", key, value);

        }
    }

    private Properties getEnvSpecificProperties(String key) {
        Properties appProperties = new Properties();
        Properties metaProperties = restTemplate.getForEntity(serverBaseUrl + "/env/", Properties.class).getBody();
        assertNotNull(metaProperties);
        HashMap<String, String> systemProperties = (HashMap<String, String>) metaProperties.get(key);
        appProperties.putAll(systemProperties);
        return appProperties;
    }

    @Test
    public void should_get_system_environment_as_properties() {
        Properties systemEnvironment = getEnvSpecificProperties("systemEnvironment");
        assertNotNull(systemEnvironment);
        final Set<String> entries = systemEnvironment.stringPropertyNames();
        assertNotNull(entries);
        assertTrue("System enviroment should not be empty", entries.size() > 0);
        for (String key : entries) {
            final String value = systemEnvironment.getProperty(key);
            LOGGER.info("Property <{}>= <{}>", key, value);

        }
    }


    @Test
    public void should_get_build_info_without_null_values() {
        BuildInfo build = restTemplate.getForEntity(serverBaseUrl + "/build", BuildInfo.class).getBody();

        LOGGER.info("build: {}", build);
        //    assertNotNull("dateCreation should not be null", build.getDateCreation());

        assertNotNull("projectVersion should not be null", build.getProjectVersion());

        assertNotNull("lastBuilder should not be null", build.getLastBuilder());
    }

    @Test
    public void should_expose_info_as_xml() {
        final ResponseEntity<String> response = restTemplate.getForEntity(serverBaseUrl + "/xml", String.class);

        assertEquals("", HttpStatus.OK, response.getStatusCode());
        String info = response.getBody();

        assertNotNull(info);
        assertTrue("Content should be in Xml: " + info, info.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"));
    }

    @Test
    public void should_expose_info_as_html_on_base_url_endpoint() {
        final ResponseEntity<String> response = restTemplate.getForEntity(serverBaseUrl + "/", String.class);
        assertEquals("", HttpStatus.OK, response.getStatusCode());

        String info = response.getBody();

        assertNotNull(info);
        assertTrue("Content should be in json: " + info, info.contains("<!DOCTYPE html PUBLIC "));
    }

    @Test
    public void should_expose_info_as_json() {
        final ResponseEntity<String> response = restTemplate.getForEntity(serverBaseUrl + "/json", String.class);
        assertEquals("", HttpStatus.OK, response.getStatusCode());

        String info = response.getBody();

        assertNotNull(info);
        Map<String, Object> parsedInfo = convertJsonToMap(info);

        assertTrue("Should contain 'build' in" + info, parsedInfo.containsKey("build"));
    }

    @Test
    public void should_retrieve_a_specific_request_header() {
        HttpHeaders customHeaders = new HttpHeaders();
        customHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        customHeaders.add("sm_universalId", "aa11____");
        customHeaders.add("sm-universalId", "aa11----");

        HttpEntity<String> entity = new HttpEntity<>("parameters", customHeaders);
        ResponseEntity<String> response = restTemplate.exchange(serverBaseUrl + "/headers/sm_universalId".toLowerCase(),
                HttpMethod.GET,
                entity,
                String.class);

        assertEquals("Http 200 expected while requesting /headers", HttpStatus.OK, response.getStatusCode());
        String requestHeadersValue = response.getBody();

        assertEquals("Request header value should match", "aa11____",requestHeadersValue);
    }

    @Test
    public void should_expose_request_headers() {
        HttpHeaders customHeaders = new HttpHeaders();
        customHeaders.setAccept(Collections.singletonList(MediaType.ALL));
        customHeaders.add("sm_universalId", "aa11____");
        customHeaders.add("sm-universalId", "aa11----");

        HttpEntity<String> entity = new HttpEntity<>("parameters", customHeaders);
        ResponseEntity<String> response = restTemplate.exchange(serverBaseUrl + "/headers",
                HttpMethod.GET,
                entity,
                String.class);

        assertEquals("Http 200 expected while requesting /headers", HttpStatus.OK, response.getStatusCode());
        String requestHeadersAsReponse = response.getBody();

        Map<String, Object> requestHeaders = convertJsonToMap(requestHeadersAsReponse);
        assertEquals(MediaType.ALL_VALUE, requestHeaders.get("accept"));
        assertEquals("aa11____", requestHeaders.get("sm_universalid"));
        assertEquals("aa11----", requestHeaders.get("sm-universalid"));
    }


    public Map<String, Object> convertJsonToMap(String json) {
        Map<String, Object> retMap = new HashMap<>();
        if (json != null) {
            try {
                retMap = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            } catch (IOException e) {
                throw  new RuntimeException("Error while reading Java Map from JSON response: " + json, e);
            }
        }
        return retMap;
    }

}