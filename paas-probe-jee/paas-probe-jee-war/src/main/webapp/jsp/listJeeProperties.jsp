<%--

    Copyright (C) 2015 Orange
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" %>
<html>
 <head><title>JEE Probe Web application : properties</title></head>
<body>
	<h2>Jee Probe</h2>
	<h4>List of JEE properties</h4>
	<table rules="all" cellspacing="0" cellpadding="4"
	    style="border: 2px solid #cfc4b8; border-right: 2px solid #cfc4b8; border-bottom:2px solid #cfc4b8">
		<tr>
			<td><b>Property</b></td>
			<td><b>Value</b></td>
		</tr>
		<tr>
			<td>Host IP</td>
			<td>${hostIp}</td>
	    </tr>
		<tr>
			<td>MaxHeapSize</td>
			<td>${ maxHeapSize }</td>
		</tr>
		<tr>
			<td>UsedHeapSize</td>
			<td>${ usedHeapSize }</td>
		</tr>
		<tr>
			<td>Probe build date</td>
			<td>${dateCreation}</td>
	    </tr>
		<tr>
			<td>Probe build author</td>
			<td>${lastBuilder}</td>
	    </tr>
		<tr>
			<td>Probe build version</td>
			<td>${projectVersion}</td>
	    </tr>
		<tr>
			<td>Ping test using DNS</td>
			<td>${pingWithDns}</td>
	    </tr>
		<tr>
			<td>Ping test using IP</td>
			<td>${pingWithIp}</td>
	    </tr>
		<tr>
			<td>Server info</td>
			<td>${serverInfo}</td>
	    </tr>
		<tr>
			<td>Installed packages</td>
			<td>${installedPackages}</td>
	    </tr>
	    <tr>
			<td>Java version</td>
			<td>${javaVersion}</td>
	    </tr>
	    <tr>
			<td>Web Gui URL bound in JNDI</td>
			<td>${webGuiUrl}</td>
	    </tr>
	    <tr>
			<td>OS name and version</td>
			<td>${osInfo}</td>
	    </tr>
	</table>
</body>
</html>