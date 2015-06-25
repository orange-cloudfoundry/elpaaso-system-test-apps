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

import com.orange.clara.cloud.sample.probe.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wooj7232 on 09/01/2015.
 */
@RestController
public class ApiProbeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiProbeController.class);

    @Autowired
    private Info info;


    @RequestMapping(value = "/json", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Info getJsonInfo() {
        return info;
    }

    @RequestMapping(value = "/xml", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
    public Info getXmlInfo() {
        return info;
    }

    @RequestMapping(value = "/system", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public SystemInfo getSystemInfo() {
        return info.getSystem();
    }

    @RequestMapping(value = "/system/java", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getJavaVersion() {
        return info.getSystem().getJavaSpecificationVersion();
    }

    @RequestMapping(value = "/system/packages", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getInstalledPackages() {
        return info.getSystem().getInstalledPackages();
    }

    @RequestMapping(value = "/system/os", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getOsInfo() {
        return info.getSystem().getOsNameAndVersion();
    }


    @RequestMapping(value = "/network", produces = {MediaType.APPLICATION_JSON_VALUE})
    public NetworkInfo checkNetworkAccess(@RequestParam(value = "targetIp") String pingIp, @RequestParam(value = "targetUrl") String pingUrl) {
        LOGGER.info("pingIp: {} - pingUrl: {}", pingIp, pingUrl);
        NetworkInfo networkInfo = new NetworkInfoBuilder().setPingIp(pingIp).setUrlPing(pingUrl).build();

        return networkInfo.status();
    }

    @RequestMapping(value = "/build", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public BuildInfo getBuildInfo() {
        return info.getBuild();
    }


    @RequestMapping(value = "/build/projectVersion", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getBuildInfoProjectVersion() {
        return info.getBuild().getProjectVersion();
    }
}
