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
package com.francetelecom.clara.cloud.sample.store;

import static org.jclouds.s3.options.PutObjectOptions.Builder.withAcl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cloudfoundry.community.service.storage.RiakcsServiceInfo;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.options.PutOptions;
import org.jclouds.io.Payload;
import org.jclouds.s3.S3Client;
import org.jclouds.s3.domain.CannedAccessPolicy;
import org.jclouds.s3.domain.ListBucketResponse;
import org.jclouds.s3.domain.ObjectMetadata;
import org.jclouds.s3.domain.S3Object;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

/**
 * REST controler for store probe. exposes rest verb to manipulate the bucket
 * provided by CloudFoundry RIAKCS contrib provider
 * 
 * @author pierre
 *
 */
@RestController
public class ProbeControler {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProbeControler.class.getName());

	@Autowired
	private BlobStoreContext blobStoreCtx;

	@Autowired
	public RiakcsServiceInfo riakService;

	// ----------------------------------------------------------------------------------------

	@RequestMapping("/storeprobe/create")
	public void create(@RequestParam String key, @RequestParam String value) {
		logger.info("create key {} value {} ", key, value);

		// access underlying native AWS API
		S3Client s3Client = this.blobStoreCtx.unwrapApi(S3Client.class);
		boolean headBucket = s3Client.bucketExists(this.riakService.getBucket());

		S3Object object = s3Client.newS3Object();
		object.setPayload(value);
		object.getMetadata().setKey(key);
		s3Client.putObject(this.riakService.getBucket(), object, withAcl(CannedAccessPolicy.PUBLIC_READ_WRITE));
	}

	@RequestMapping("/storeprobe/read")
	public String read(@RequestParam String key) {
		logger.info("read ");
		S3Client s3Client = this.blobStoreCtx.unwrapApi(S3Client.class);
		S3Object getObject = s3Client.getObject(this.riakService.getBucket(), key);
		Payload getPayload = getObject.getPayload();
		return getPayload.toString();
	}

	@RequestMapping("/storeprobe/delete")
	public void delete(@RequestParam String key) {
		logger.info("delete ");
		S3Client s3Client = this.blobStoreCtx.unwrapApi(S3Client.class);
		s3Client.deleteObject(this.riakService.getBucket(), key);
	}

	@RequestMapping("/storeprobe/keys")
	public List<String> listKeys() {
		logger.debug("list owned buckets");
		S3Client s3Client = this.blobStoreCtx.unwrapApi(S3Client.class);

		List<String> keys = new ArrayList<String>();

		ListBucketResponse files = s3Client.listBucket(this.riakService.getBucket());
		Iterator<ObjectMetadata> it = files.iterator();
		while (it.hasNext()) {
			ObjectMetadata metadata = it.next();
			keys.add(metadata.getKey());
		}
		return keys;
	}

	
	@RequestMapping("/storeprobe/jclouds-create")
	public void jcloudsCreate(@RequestParam String key, @RequestParam String value) throws IOException {
		logger.info("create key {} value {} ", key, value);
		
		// Create a Blob
		ByteSource payload = ByteSource.wrap(value.getBytes(Charsets.UTF_8));
		BlobStore blobStore=this.blobStoreCtx.getBlobStore();
		Blob blob = blobStore.blobBuilder(key) // you can use folders via blobBuilder(folderName + "/sushi.jpg")
			.payload(payload)
		    .contentLength(payload.size())
		    .build();
//		// Upload the Blob
		PutOptions putOptions=new PutOptions(false);
		String etag=blobStore.putBlob(this.riakService.getBucket(),blob,putOptions);
		logger.info("blob created with etag {}",etag);
	}

	@RequestMapping("/storeprobe/jclouds-read")
	public String jcloudsRead(@RequestParam String key) throws IOException {
		logger.info("read ");
		
		BlobStore blobStore=this.blobStoreCtx.getBlobStore();
		Blob blob=blobStore.getBlob(this.riakService.getBucket(),key);
		if (blob==null) throw new IllegalArgumentException("key not found: "+key);
		
		InputStream is=blob.getPayload().openStream();
		String value=streamToString(is);
		logger.info("read value {} ",value);
		return value;
	}
	
	/**
	 * utility to convert inputstream to String
	 * see http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String streamToString(InputStream in) throws IOException {
		  StringBuilder out = new StringBuilder();
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  for(String line = br.readLine(); line != null; line = br.readLine()) 
		    out.append(line);
		  br.close();
		  return out.toString();
		}
	

	@RequestMapping("/storeprobe/jclouds-delete")
	public void jcloudsDelete(@RequestParam String key) {
		logger.info("delete ");
		BlobStore blobStore=this.blobStoreCtx.getBlobStore();
		blobStore.removeBlob(this.riakService.getBucket(), key);
	}

	@RequestMapping("/storeprobe/jclouds-keys")
	public List<String> jcloudsListKeys() {
		logger.debug("list owned buckets");
		
		BlobStore blobStore=this.blobStoreCtx.getBlobStore();
		PageSet<? extends StorageMetadata> list=blobStore.list(this.riakService.getBucket());
		
		List<String> lists=new ArrayList<String>();
		for (StorageMetadata metadata: list){
			lists.add(metadata.toString());
		}
	
		return lists;
	}		

	
	
}
