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

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo;

/**
 * {@link ServiceInfo} for RiakCS S3 services.
 * 
 * @author Pierre Oblin
 */
public class RiakcsServiceInfo extends BaseServiceInfo {

	private final String uri;
	private final String accessKeyId;
	private final String secretAccessKey;
	private final String bucket;

	public RiakcsServiceInfo(String id, String uri, String accessKeyId, String secretAccessKey, String bucket) {
		super(id);
		this.uri = uri;
		this.accessKeyId = accessKeyId;
		this.secretAccessKey = secretAccessKey;
		this.bucket = bucket;
	}

	public String getUri() {
		return uri;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public String getBucket() {
		return bucket;
	}

	/**
	 * specific getter, only gets base host/port for s3 service
	 * 
	 * @return
	 */
	public String getBaseUri() {

		URL url;
		try {
			url = new URL(this.uri);
			String protocol = url.getProtocol();
			String host = url.getHost();
			
			//FIXME: issue on LocateBundle(565): s3fs: /etc/pki/tls/certs/ca-bundle.crt is not readable
			protocol="http";
			return protocol + "://" + host;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

	}

}
