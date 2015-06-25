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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.*;

/**
 * Created by wooj7232 on 12/01/2015.
 */
@XmlRootElement(name = "ProbeInfo")
@XmlType(name = "", propOrder = {"build", "system"})
@Component
public class Info {
    private BuildInfo build;
    private SystemInfo system;


    public SystemInfo getSystem() {
        return system;
    }

    @Autowired
    public void setSystem(SystemInfo system) {
        this.system = system;
    }

    public BuildInfo getBuild() {
        return build;
    }

    @Autowired
    public void setBuild(BuildInfo build) {
        this.build = build;
    }
}
