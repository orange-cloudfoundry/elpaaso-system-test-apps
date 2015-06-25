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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

/**
 * An internal service provider for creating RiakcsServiceInfo instances.
 * 
 * @author Pierre Oblin
 */
public class RiakcsServiceInfoCreator extends CloudFoundryServiceInfoCreator<RiakcsServiceInfo> {

    public RiakcsServiceInfoCreator() {
        super(new Tags("riak-cs","s3"));
    }

    public RiakcsServiceInfo createServiceInfo(Map<String, Object> serviceData) {
        String id = (String) serviceData.get("name");

        @SuppressWarnings("unchecked")
        Map<String, Object> credentials = (Map<String, Object>) serviceData.get("credentials");

        String uri = (String) credentials.get("uri");
        String accessKeyId = (String) credentials.get("access_key_id");
        String secretAccessKey = (String) credentials.get("secret_access_key");
        
		URL url;
		try {
			url = new URL (uri);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
			
		String path=url.getPath();
		String bucket=path.substring(1);
        
        return new RiakcsServiceInfo(id, uri, accessKeyId, secretAccessKey, bucket);
    }

}
