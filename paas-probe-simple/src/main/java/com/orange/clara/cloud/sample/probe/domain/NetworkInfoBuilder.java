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

public class NetworkInfoBuilder {
    private String pingIp = null;
    private String urlPing = null;
    private int pingTimeoutInSecond = 2;

    public NetworkInfoBuilder setPingTimeoutInSecond(int pingTimeoutInSecond) {
        this.pingTimeoutInSecond = pingTimeoutInSecond;
        return this;
    }

    public NetworkInfoBuilder setPingIp(String pingIp) {
        this.pingIp = pingIp;
        return this;
    }

    public NetworkInfoBuilder setUrlPing(String urlPing) {
        this.urlPing = urlPing;
        return this;
    }

    public NetworkInfo build() {
        return new NetworkInfo(pingIp, urlPing, pingTimeoutInSecond);
    }
}