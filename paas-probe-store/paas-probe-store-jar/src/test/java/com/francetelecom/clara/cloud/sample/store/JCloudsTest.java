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


import static org.jclouds.Constants.PROPERTY_RELAX_HOSTNAME;
import static org.jclouds.Constants.PROPERTY_TRUST_ALL_CERTS;
import static org.jclouds.s3.options.PutObjectOptions.Builder.withAcl;
import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_VIRTUAL_HOST_BUCKETS;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.io.Payload;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.s3.S3Client;
import org.jclouds.s3.domain.BucketMetadata;
import org.jclouds.s3.domain.CannedAccessPolicy;
import org.jclouds.s3.domain.ListBucketResponse;
import org.jclouds.s3.domain.S3Object;
import org.jclouds.s3.options.ListBucketOptions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

@Ignore
@RunWith(JUnit4.class)
public class JCloudsTest {

	private static Logger logger=LoggerFactory.getLogger(JCloudsTest.class.getName());
	
	
	@Test
	public void testConnect() throws IOException {
		
		String uri="https://undefined";
		
		//binding credentials
		String accessKeyId="";
		String accessKey="";

        //ie bucket ?
        String bucketName="";
		
		
        //connect jclouds client to the credentials -------------------------------------------------
        {
        Properties storeProviderInitProperties = new Properties();
        Iterable<Module> modules = ImmutableSet.<Module> of(new SLF4JLoggingModule());
        
        //added for RiakCS compat
        storeProviderInitProperties.put(PROPERTY_TRUST_ALL_CERTS,true);
		storeProviderInitProperties.put(PROPERTY_RELAX_HOSTNAME,true);
		//for cf-riakcs-provider service, single host, bucketname is not in dns
		storeProviderInitProperties.put(PROPERTY_S3_VIRTUAL_HOST_BUCKETS,false);		
		
		
		BlobStoreContext context = ContextBuilder.newBuilder("s3") //not "aws-s3"
				.overrides(storeProviderInitProperties)
				.endpoint(uri)
				.credentials(accessKeyId, accessKey)
				.modules(modules)
				.buildView(BlobStoreContext.class);

		
		//access underlying native AWS API
		S3Client s3Client=context.unwrapApi(S3Client.class);
		
		boolean headBucket=s3Client.bucketExists(bucketName);
		
		
		//FIXME : acl list is KO with binding access keys (ok with admin keys)
		   /**
		    * 
		    * A GET request operation directed at an object or bucket URI with the "acl" parameter retrieves
		    * the Access Control List (ACL) settings for that S3 item.
		    * <p />
		    * To list a bucket's ACL, you must have READ_ACP access to the item.
		    * 
		    * @return access permissions of the bucket
		    */
			// RiakCS doc : http://docs.basho.com/riakcs/1.5.2/references/apis/storage/s3/RiakCS-GET-Bucket-ACL/
		
		
//		logger.debug("acl");
//		AccessControlList acls=s3Client.getBucketACL(bucketName);
		
		logger.debug("bucket options");
		ListBucketOptions options=new ListBucketOptions();
		
		
		
		
		logger.debug("list owned buckets");		
		Set<BucketMetadata> ownedBuckets=s3Client.listOwnedBuckets();
		
		S3Object object=s3Client.newS3Object();
		object.setPayload("my blob content");
		object.getMetadata().setKey("undefined");
		s3Client.putObject(bucketName, object,withAcl(CannedAccessPolicy.PUBLIC_READ_WRITE));
		
		logger.debug("list bucket content");
		ListBucketResponse bucketList=s3Client.listBucket(bucketName, options);
		
		S3Object getObject =s3Client.getObject(bucketName, "undefined");
		Payload getPayload=getObject.getPayload();
		

		context.close();
		
		
        }

		
	}
	
}
