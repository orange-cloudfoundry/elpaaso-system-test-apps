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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by wooj7232 on 09/01/2015.
 */
@SpringBootApplication
public class Application {

    @Value("${info.fail-on-startup:true}")
    boolean failOnStartup;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public String throwException() {
        if (failOnStartup) {
            throw new RuntimeException("This app should never start !!!");
        }
        System.out.println("Failure on startup disabled !");
        return "Started";
    }
}