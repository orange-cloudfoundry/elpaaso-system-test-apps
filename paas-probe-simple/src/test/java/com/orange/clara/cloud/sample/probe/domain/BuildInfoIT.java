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
package com.orange.clara.cloud.sample.probe.domain;

import com.orange.clara.cloud.sample.probe.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertNotNull;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class BuildInfoIT {
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(BuildInfoIT.class);

    @Autowired
    Info info;


    @Before
    public void setup() {
    }

    @Test
    public void should_get_build_info_without_null_values() {
        BuildInfo build= info.getBuild();

        assertNotNull("dateCreation should not be null", build.getDateCreation());
        logger.info("build date is: " + build.getDateCreation());

        assertNotNull("projectVersion should not be null", build.getProjectVersion());
        logger.info("build version is: " + build.getProjectVersion());

        assertNotNull("lastBuilder should not be null", build.getLastBuilder());
        logger.info("Last builder is: " + build.getLastBuilder());
    }


}
