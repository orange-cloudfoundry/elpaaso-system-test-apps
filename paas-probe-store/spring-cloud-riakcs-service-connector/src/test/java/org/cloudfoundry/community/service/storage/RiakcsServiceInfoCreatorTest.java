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
package org.cloudfoundry.community.service.storage;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.cloudfoundry.TestCloudFoundryConnector;
import org.springframework.cloud.util.EnvironmentAccessor;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * @author David Ehringer
 */
public class RiakcsServiceInfoCreatorTest {

    private TestCloudFoundryConnector cloudConnector = new TestCloudFoundryConnector();

    private CloudFactory cloudFactory;
    @Mock
    private EnvironmentAccessor environment;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        cloudConnector.setCloudEnvironment(environment);

        cloudFactory = new CloudFactory();
        cloudFactory.registerCloudConnector(cloudConnector);

        when(environment.getEnvValue("VCAP_APPLICATION")).thenReturn(fileAsString("vcap_application.json"));
    }

    @Test
    public void riakcsServicesAreAvailableThroughServiceInfo() throws IOException {
        when(environment.getEnvValue("VCAP_SERVICES")).thenReturn(fileAsString("riakcs-service-info.json"));

        Cloud cloud = cloudFactory.getCloud();

        assertThat(cloud.getServiceInfos().size(), is(1));
        RiakcsServiceInfo serviceInfo = (RiakcsServiceInfo) cloud.getServiceInfo("riakcs-s3-bucket");
        assertThat(serviceInfo.getId(), is("riakcs-s3-bucket"));
        assertThat(serviceInfo.getAccessKeyId(), is("YOOG5RBFUWRLX8IPVRF9"));
        assertThat(serviceInfo.getSecretAccessKey(), is("iY3Uwxxp-qa9wIsbpED3UeLkMDTGIqUqtMlNWw=="));
        assertThat(serviceInfo.getUri(), is("https://YOOG5RBFUWRLX8IPVRF9:iY3Uwxxp-qa9wIsbpED3UeLkMDTGIqUqtMlNWw%3D%3D@p-riakcs.redacted-domain/service-instance-936c8641-2611-4371-bd71-e1ce9d0cde5b"));
        
//      assertThat(serviceInfo.getBaseUri(), is("https://p-riakcs.redacted-domain"));
        assertThat(serviceInfo.getBaseUri(), is("http://p-riakcs.redacted-domain"));
        
        
        assertThat(serviceInfo.getBucket(), is("service-instance-936c8641-2611-4371-bd71-e1ce9d0cde5b"));
        
        //assertThat(serviceInfo.getUsername(), is("cloud-foundry-s3-c5271ba4-6d2f-4163-843c-6a5fdceb7a1a"));
    }

    private String fileAsString(String fileName) throws IOException {
        URL url = Resources.getResource(fileName);
        return Resources.toString(url, Charsets.UTF_8);
    }

}
