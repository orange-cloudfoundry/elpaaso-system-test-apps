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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wooj7232 on 12/01/2015.
 */
public class NetworkInfo {

    private static final String PING_SUCCEED_MESSAGE = "### Ping SUCCEED ###";
    private static final String HTTP_PREFIX = "http://";

    private String urlToPing = "http://";
    private String urlPingResult = null;

    private String ipToPing = "http://";
    private String ipPingResult = null;
    private int pingTimeoutInSecond;

    public String getUrlPingResult() {
        return urlPingResult;
    }

    NetworkInfo(String pingIp, String urlPing, int pingTimeoutInSecond) {
        this.ipToPing = pingIp;
        this.urlToPing = urlPing;
        this.pingTimeoutInSecond = pingTimeoutInSecond;
    }


    public String getUrlToPing() {
        return urlToPing;
    }

    public String getIpToPing() {
        return ipToPing;
    }

    public int getPingTimeoutInSecond() {
        return pingTimeoutInSecond;
    }

    String ping(String url, int timeoutInMilliSecond) {
        // Otherwise an exception may be thrown on invalid SSL certificates.
        if (url == null) {
            return "Trying to ping a null url.\t### Ping FAILED ###";
        }
        url = url.replaceFirst("https", "http");

        if (!url.startsWith(HTTP_PREFIX)) {
            url= HTTP_PREFIX +url;
        }

        StringBuilder result = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeoutInMilliSecond);
            connection.setReadTimeout(timeoutInMilliSecond);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            result.append("ping ").append(connection.getURL().getHost());
            result.append(" - response Code: ");
            result.append(responseCode);
            if (200 <= responseCode && responseCode <= 399) {
                result.append("\t" + PING_SUCCEED_MESSAGE);
            } else {
                result.append("\t### Ping FAILED ###");
            }

        } catch (IOException exception) {
            result.append("ping ").append(url);
            result.append(" - Exception : ");
            result.append("\t");
            result.append(exception.toString());
            result.append("\t### Ping FAILED ###\n");
        }
        return result.toString();
    }

    public NetworkInfo status() {
        if (urlToPing != null && urlToPing.length() > 0) {
            urlPingResult = ping(urlToPing, pingTimeoutInSecond * 1000);
        }
        if (ipToPing != null && ipToPing.length() > 0) {
            ipPingResult = ping(ipToPing, pingTimeoutInSecond * 1000);
        }
        return this;
    }

    public boolean arePingSuccessful() {
        if (ipPingResult == null && urlPingResult == null) {
            return false;
        }

        boolean result = true;
        if (urlPingResult != null) {
            result = urlPingResult.endsWith(NetworkInfo.PING_SUCCEED_MESSAGE);
        }

        if (ipPingResult != null) {
            result = result && ipPingResult.endsWith(NetworkInfo.PING_SUCCEED_MESSAGE);
        }
        return result;
    }

    public String getIpPingResult() {
        return ipPingResult;
    }
}
