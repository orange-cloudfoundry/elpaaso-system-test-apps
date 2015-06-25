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
import com.orange.clara.cloud.sample.probe.domain.AppInfo;
import com.orange.clara.cloud.sample.probe.domain.BuildInfo;
import com.orange.clara.cloud.sample.probe.domain.SystemInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

    @Before
    public void setup() {
        this.serverBaseUrl = SERVER_URL + ":" + port;
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
        assertTrue("Content should be in json: " + info, info.contains("{\"build\":{\""));
    }


}