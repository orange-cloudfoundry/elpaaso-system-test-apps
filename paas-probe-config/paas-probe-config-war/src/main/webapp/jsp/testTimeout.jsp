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
<%@page language="java" import="com.francetelecom.clara.cloud.sample.probe.WebProbeImpl" %>
<%@page language="java" import="java.util.Map" %>
<html>
<head>
	<title>Timout test JSP</title>
</head>
<body>
	<h2>Timout test JSP</h2>
	<h4>List of config properties</h4>
	<table border="1" width="300">
		<tr>
			<td width="150"><b>Property</b></td>
			<td width="100"><b>Value</b></td>
		</tr>
		<%
			WebProbeImpl probe = new WebProbeImpl();
		
		%>
		<tr>
			<td width="150"><%=name%></td>
			<td width="100"><%=value%></td>
		</tr>
		<%}%>
	</table>
</body>
</html>