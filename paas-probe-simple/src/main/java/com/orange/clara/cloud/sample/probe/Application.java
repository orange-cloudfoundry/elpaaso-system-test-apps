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
package com.orange.clara.cloud.sample.probe;

import com.orange.clara.cloud.sample.probe.domain.AppInfo;
import com.orange.clara.cloud.sample.probe.domain.BuildInfo;
import com.orange.clara.cloud.sample.probe.domain.Info;
import com.orange.clara.cloud.sample.probe.domain.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wooj7232 on 09/01/2015.
 */
@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Value("${info.build.lastBuilder}")
    private String lastBuilder;

    @Value("${info.build.dateCreationString}")
    private String creationDate;

    @Value("${info.build.projectVersion}")
    private String projectVersion;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public BuildInfo getBuildInfo() {
        BuildInfo buildInfo = new BuildInfo();
        buildInfo.setCreationDate(creationDate);
        buildInfo.setLastBuilder(lastBuilder);
        buildInfo.setProjectVersion(projectVersion);

        return buildInfo;
    }

    @Bean
    public AppInfo getAppInfo() {
        return new AppInfo();
    }

    @Bean
    public SystemInfo getSystemInfo() {
        return new SystemInfo();
    }

    @Bean
    public Info getProbeInfo() {
        Info info = new Info();
        return info;

    }

}