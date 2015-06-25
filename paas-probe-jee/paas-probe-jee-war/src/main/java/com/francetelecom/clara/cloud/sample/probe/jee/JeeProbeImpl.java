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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

public class JeeProbeImpl implements JeeProbe, ServletContextAware {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(JeeProbeImpl.class);

	private String dateCreationString;
	private String lastBuilder;
	private String projectVersion;

	private String urlToPing = "http://";
	private String ipToPing = "http://";
	private int pingTimeoutInSecond = 2;

	private ServletContext servletContext;

	public String getDateCreationString() {
		return dateCreationString;
	}

	public void setDateCreationString(String dateCreationString) {
		this.dateCreationString = dateCreationString;
	}

	public String getLastBuilder() {
		return lastBuilder;
	}

	public void setLastBuilder(String lastBuilder) {
		this.lastBuilder = lastBuilder;
	}

	public String getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}

	public int getPingTimeoutInSecond() {
		return pingTimeoutInSecond;
	}

	public void setPingTimeoutInSecond(int pingTimeoutInSecond) {
		this.pingTimeoutInSecond = pingTimeoutInSecond;
	}

	public JeeMemoryUsage getMemoryUsage() {
		logger.debug("getMemoryUsage is called");

		JeeMemoryUsage memoryUsage = new JeeMemoryUsage();

		MemoryUsage heap = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

		memoryUsage.setMaxHeapSize(heap.getMax());
		memoryUsage.setUsedHeapSize(heap.getUsed());

		return memoryUsage;
	}

	public String getDateCreation() {
		return getDateCreationString();
	}

	public String getHostIp() {
		logger.debug("getHostIp is called");
		String ip = null;
		try {
			ip = java.net.InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error("getHostIp() exception: " + e, e);
		}
		return ip;
	}

	public String getServerInfo() {
		return servletContext != null ? servletContext.getServerInfo() : "Unknown -> no servlet context";
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String getInstalledPackages() {
		String installedPackageCommand = getInstalledPackageCommand();

		return executeCommandAndGetOutputAsString(installedPackageCommand);
	}

	public String getInstalledPackageCommand() {
		final String redHatLikeCommand = "sudo rpm -qa";
		final String debianLikeCommand = "dpkg --get-selections";

		String rawOsInfo = getOsNameAndVersion();
		boolean isDebianLikeOs = rawOsInfo.contains("Ubuntu") || rawOsInfo.contains("Debian");
		if (isDebianLikeOs) {
			return debianLikeCommand;
		} else {
			return redHatLikeCommand;
		}
	}

	public String getUrlToPing() {
		return urlToPing;
	}

	public void setUrlToPing(String urlToPing) {
		this.urlToPing = urlToPing;
	}

	public String getIpToPing() {
		return ipToPing;
	}

	public void setIpToPing(String ipToPing) {
		this.ipToPing = ipToPing;
	}

	@Override
	public String getPingWithDnsResult() {
		return ping(urlToPing, pingTimeoutInSecond * 1000);
	}

	@Override
	public String getPingWithIpResult() {
		return ping(ipToPing, pingTimeoutInSecond * 1000);
	}

	private String executeCommandAndGetOutputAsString(String rpmCommand) {
		StringBuffer buffer = new StringBuffer();
		try {
			Process p = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", rpmCommand });
			p.waitFor();
			if (p.exitValue() != 0) {
				buffer.append("Command returned ");
				buffer.append(p.exitValue());
				buffer.append("\n\nOUTPUT:\n");
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					buffer.append("\n");
				}
				buffer.append("\n\nERROR:\n");
				reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					buffer.append("\n");
				}
			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					buffer.append("\n");
				}
			}
		} catch (IOException e) {
			return "A IOException occured: " + e.getMessage();
		} catch (InterruptedException e) {
			return "A InterruptedException occured: " + e.getMessage();
		}
		return buffer.toString();
	}

	public String ping(String url, int timeoutInMilliSecond) {
		// Otherwise an exception may be thrown on invalid SSL certificates.
		if (url == null) {
			return "Trying to ping a null url.\t### Ping FAILED ###";
		}
		url = url.replaceFirst("https", "http");

		StringBuilder result = new StringBuilder();
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setConnectTimeout(timeoutInMilliSecond);
			connection.setReadTimeout(timeoutInMilliSecond);
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();
			result.append("ping " + connection.getURL().getHost());
			result.append(" - response Code: ");
			result.append(responseCode);
			if (200 <= responseCode && responseCode <= 399) {
				result.append("\t" + PING_SUCCEED_MESSAGE);
			} else {
				result.append("\t### Ping FAILED ###");
			}

		} catch (IOException exception) {
			result.append("ping " + url);
			result.append(" - Exception : ");
			result.append("\t");
			result.append(exception.toString());
			result.append("\t### Ping FAILED ###\n");
		}
		return result.toString();
	}

    @Override
    public String getJavaSpecificationVersion() {
        return System.getProperty("java.specification.version");
    }

	@Override
	public String getWebGuiUrl() {

		try {
			InitialContext context = new InitialContext();
			return (String) context.lookup("defaultWebGuiPrefix.url");
		} catch (NamingException e) {
			logger.error("Error retrieving web GUI URL in the JNDI.", e);
			return e.getMessage();
		}
	}

	@Override
	public String getOsNameAndVersion() {
		String file = executeCommandAndGetOutputAsString("ls /etc/system-release | wc -l");
		if (file.startsWith("1")) {
			return executeCommandAndGetOutputAsString("cat /etc/system-release");
		} else {
			return executeCommandAndGetOutputAsString("lsb_release -a");
		}
	}

}
