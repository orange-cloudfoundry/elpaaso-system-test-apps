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
package com.francetelecom.clara.cloud.sample.probe.jee.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.francetelecom.clara.cloud.sample.probe.jee.JeeMemoryUsage;
import com.francetelecom.clara.cloud.sample.probe.jee.JeeProbe;

/**
 * JeeProbeController status page controller
 * view : cf listJeeProperties
 * Last update  : $LastChangedDate$
 * Last author  : $Author$
 * @version     : $Revision$
 */
@Controller
public class JeeProbeController {

    private static final String stateJspPage = "jsp/listJeeProperties.jsp";
    @Autowired
    JeeProbe jeeProbe;

    @RequestMapping("/status")
    protected ModelAndView status(HttpServletRequest request, HttpServletResponse response) {
        JeeMemoryUsage memoryUsage = jeeProbe.getMemoryUsage();
        String maxHeapSize = String.valueOf(memoryUsage.getMaxHeapSize());
        String usedHeapSize = String.valueOf(memoryUsage.getUsedHeapSize());
        String dateCreation = jeeProbe.getDateCreation();
        String lastBuilder = jeeProbe.getLastBuilder();
        String projectVersion = jeeProbe.getProjectVersion();
        String hostIp = jeeProbe.getHostIp();
        String serverInfo = jeeProbe.getServerInfo();
		String pingWithUrl = "Disabled " + JeeProbe.PING_SUCCEED_MESSAGE;// jeeProbe.getPingWithDnsResult();
		String pingWithIp = "Disabled " + JeeProbe.PING_SUCCEED_MESSAGE;// jeeProbe.getPingWithIpResult();
        String installedPackages = jeeProbe.getInstalledPackages().replaceAll("\n", "<br/>");
        String javaVersion = jeeProbe.getJavaSpecificationVersion();
		String webGuiUrl = jeeProbe.getWebGuiUrl();
		String osInfo = jeeProbe.getOsNameAndVersion();


        ModelAndView modelAndView = new ModelAndView(stateJspPage);
        modelAndView.addObject("maxHeapSize", maxHeapSize);
        modelAndView.addObject("usedHeapSize", usedHeapSize);
        modelAndView.addObject("lastBuilder", lastBuilder);
        modelAndView.addObject("dateCreation", dateCreation);
        modelAndView.addObject("projectVersion", projectVersion);
        modelAndView.addObject("hostIp", hostIp);
        modelAndView.addObject("serverInfo", serverInfo);
		modelAndView.addObject("pingWithDns", pingWithUrl);
		modelAndView.addObject("pingWithIp", pingWithIp);
        modelAndView.addObject("installedPackages", installedPackages);
        modelAndView.addObject("javaVersion", javaVersion);
		modelAndView.addObject("webGuiUrl", webGuiUrl);
		modelAndView.addObject("osInfo", osInfo);

        return modelAndView;
    }

}