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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.UnknownHostException;

/**
 * Created by wooj7232 on 09/01/2015.
 */
public class SystemInfo implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(SystemInfo.class);
    final static String UNSUPPORTED_WINDOWS_OS = "Warning !!! Unsupported command on Windows system";

    private String hostIp = null;
    private String installedPackages = "Undefined installed package list";


    public SystemInfo(){
        this.hostIp=getHostIp();

    }

    public String getHostIp() {
        logger.debug("getHostIp is called");
        try {
            hostIp = java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("getHostIp() exception: " + e, e);
        }
        return hostIp;
    }

    public String getServerInfo() {
        //return servletContext != null ? servletContext.getServerInfo() : "Unknown -> no servlet context";
        return "dummy";
    }

    public String getInstalledPackages() {
        if (isWindowsOs()){
            installedPackages = UNSUPPORTED_WINDOWS_OS;
        }   else {
            String installedPackageCommand = getInstalledPackageCommand();
            installedPackages = executeCommandAndGetOutputAsString(installedPackageCommand);
        }

        return installedPackages;
    }

    private boolean isWindowsOs() {
        return System.lineSeparator().equals("\r\n");
    }

    public String getOsNameAndVersion() {
        if (isWindowsOs()){
            return UNSUPPORTED_WINDOWS_OS;
        }

        String file = executeCommandAndGetOutputAsString("ls /etc/system-release | wc -l");
        if (file.startsWith("1")) {
            return executeCommandAndGetOutputAsString("cat /etc/system-release");
        } else {
            return executeCommandAndGetOutputAsString("lsb_release -a");
        }
    }

    public String getInstalledPackageCommand() {

        String rawOsInfo = getOsNameAndVersion();
        boolean isDebianLikeOs = rawOsInfo.contains("Ubuntu") || rawOsInfo.contains("Debian");
        if (isDebianLikeOs) {
            final String debianLikeCommand = "dpkg --get-selections";
            return debianLikeCommand;
        } else {
            final String redHatLikeCommand = "sudo rpm -qa";
            return redHatLikeCommand;
        }
    }


    private String executeCommandAndGetOutputAsString(String rpmCommand) {
        StringBuilder builder = new StringBuilder();
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", rpmCommand});
            p.waitFor();
            if (p.exitValue() != 0) {
                builder.append("Command returned ");
                builder.append(p.exitValue());
                builder.append("\n\nOUTPUT:\n");
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }
                builder.append("\n\nERROR:\n");
                reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }
            }
        } catch (IOException e) {
            return "A IOException occured: " + e.getMessage();
        } catch (InterruptedException e) {
            return "A InterruptedException occured: " + e.getMessage();
        }
        return builder.toString();
    }


    public String getJavaSpecificationVersion() {
        return System.getProperty("java.specification.version");
    }
}
