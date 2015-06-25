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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


public class NetworkInfoIT {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(NetworkInfoIT.class);

    // SUT
    NetworkInfo info;

    @Before
    public void setup() {
    }

    @Test
    public void should_be_able_to_ping_dbaas_vdr_qa_using_ip() throws Exception {
        info = new NetworkInfoBuilder().setPingIp("http://").build();
        NetworkInfo result = info.status();
        logger.info("Java DNS ping result: " + result);

        assertNotNull(result);
        if (!info.arePingSuccessful()) {
            fail(result.getIpPingResult() + "@@@@@@@@@" + result.getUrlPingResult());
        }
    }

    @Test
    public void should_be_able_to_ping_orange_forge_using_dns_name() throws Exception {
         info = new NetworkInfoBuilder().setUrlPing("http://undfined").build();
        NetworkInfo result = info.status();
        logger.info("Java DNS ping result: " + result);

        assertNotNull(result);
        if (!info.arePingSuccessful()) {
            fail(result.getIpPingResult() + "@@@@@@@@@" + result.getUrlPingResult());
        }
    }

    @Test
    public void should_not_be_able_to_ping_foreman_using_internal_qa_dns_name() throws Exception {
        info = new NetworkInfoBuilder().setPingIp("").build();

        NetworkInfo result = info.status();
        logger.info("Java DNS ping result: " + result.getIpPingResult());

        Assert.assertNotNull(result);
        if (info.arePingSuccessful() || (!containsUnknownHostException(result.getIpPingResult()) && !containsSocketTimeoutException(result.getIpPingResult()))) {
            Assert.fail(result.getIpPingResult());
        }
    }

    @Test
    @Ignore("This test fail on PC behind firewall but succeed on Faas")
    public void should_not_be_able_to_ping_ya_dot_ru_using_its_ip() throws Exception {
        info = new NetworkInfoBuilder().setPingIp("localhost").setPingTimeoutInSecond(3).build();
        NetworkInfo result = info.status();
        logger.info("Java IP ping result: " + result);

        Assert.assertNotNull(result);
        if (info.arePingSuccessful() || (!containsUnknownHostException(result.getIpPingResult()) &&! containsSocketTimeoutException(result.getIpPingResult()))) {
            Assert.fail(result.getIpPingResult());
        }
    }

    private boolean containsUnknownHostException(String errorMessage){
        return errorMessage.contains("java.net.UnknownHostException");
    }

    private boolean containsSocketTimeoutException(String errorMessage){
        return errorMessage.contains("java.net.SocketTimeoutException");
    }

    private boolean containsConnectException(String errorMessage){
        return errorMessage.contains("java.net.ConnectException");
    }


}
