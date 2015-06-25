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
package com.francetelecom.clara.cloud.sample.probe.jee;

/**
 * JeeProbe interface
 * Sample usage : cf JeeProbeImpl
 * Last update  : $LastChangedDate$
 * Last author  : $Author$
 * @version     : $Revision$
 */
public interface JeeProbe {

    public static final String PING_SUCCEED_MESSAGE = "### Ping SUCCEED ###";

	/**
     * get the execution node memory usage
     * @return JeeMemoryUsage
     */
    JeeMemoryUsage getMemoryUsage();

    /**
     * get the last execution node ear builder name
     * @return string ear build author
     */
    String getLastBuilder();

    /**
     * get the last execution node ear build version
     * @return string (ie. "1.0.9-SNAPSHOT")
     */
    String getProjectVersion();

    /**
     * get the last execution node ear creation date
     * @return string date (ie. "08/03/2012-10:15" )
     */
    String getDateCreation();
    
    /**
     * get the ip of the host on which the EAR is deployed
     * @return ip such as aaa.bbb.ccc.ddd
     */
    String getHostIp();
    
    /**
     * Return application server version as returned by ServletContext.getServerInfo()
     * @return Application server version as returned by ServletContext.getServerInfo()
     */
    String getServerInfo();
    
    /**
     * List all installed packages (RPM)
     * @return All installed packages (RPM), one per line
     */
    String getInstalledPackages();

	/**
	 * Execute a java ping on intranoo homepage (by default) to ensure dns name
	 * resolution
	 * 
	 * @return
	 */
	String getPingWithDnsResult();

	/**
	 * Execute a java ping on intranoo homepage (by default) with IP to ensure
	 * dns name resolution
	 * 
	 * @return
	 */
	String getPingWithIpResult();
	
	/**
     * Get Java Runtime Environment specification version
     * @return the content of "java.specification.version" property
     */
    String getJavaSpecificationVersion();

    /**
	 * Get the URL of the web GUI as it is registered in the JNDI
	 * @return the URL of the web GUI
	 */
	String getWebGuiUrl();

	/**
	 * Get the name and version of most linux distributions
	 * @return the distribution name and version
	 */
	String getOsNameAndVersion();

}
