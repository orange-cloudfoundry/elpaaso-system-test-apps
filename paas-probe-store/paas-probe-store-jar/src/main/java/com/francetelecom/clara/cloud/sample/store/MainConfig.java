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
import static org.jclouds.s3.reference.S3Constants.PROPERTY_S3_VIRTUAL_HOST_BUCKETS;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import net.sf.webdav.WebdavServlet;

import org.cloudfoundry.community.service.storage.RiakcsServiceInfo;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;


@Configuration
public class MainConfig extends AbstractCloudConfig {

	
	private static Logger logger=LoggerFactory.getLogger(MainConfig.class.getName());

	
	
	@Bean
	public RiakcsServiceInfo riakService(){
		return (RiakcsServiceInfo) this.cloud().getServiceInfo("s3-myriak");
		
	}
	
	@Autowired
	ProcessBuilder mountFuse;

	
	@Bean
	public BlobStoreContext context() throws MalformedURLException {

		RiakcsServiceInfo riakInfo=riakService();
		
		//jclouds uri translation from cf credentials
		URL url=new URL (riakInfo.getUri());
		String protocol=url.getProtocol();
		String host=url.getHost();

		String baseUri=protocol+"://"+host;
		
		// connect jclouds client to the credentials
		// -------------------------------------------------
		Properties storeProviderInitProperties = new Properties();
		Iterable<Module> modules = ImmutableSet.<Module> of(new SLF4JLoggingModule());

		// added for RiakCS compat
		storeProviderInitProperties.put(PROPERTY_TRUST_ALL_CERTS, true);
		storeProviderInitProperties.put(PROPERTY_RELAX_HOSTNAME, true);
		// for cf-riakcs-provider service, single host, bucketname is not in
		// dns
		storeProviderInitProperties.put(PROPERTY_S3_VIRTUAL_HOST_BUCKETS, false);

		BlobStoreContext context = ContextBuilder.newBuilder("s3")
				// not "aws-s3"
				.overrides(storeProviderInitProperties).endpoint(baseUri).credentials(riakInfo.getAccessKeyId(), riakInfo.getSecretAccessKey()).modules(modules)
				.buildView(BlobStoreContext.class);

		return context;
	}
	
	
	
	
	//add a fuse s3fs mount command. uses credentials. see https://github.com/s3fs-fuse/s3fs-fuse
	public ProcessBuilder mount() throws IOException {
	
		RiakcsServiceInfo riakInfo=riakService();
		
		//locate the binary
		logger.debug("check binary");
		File bin=new File ("/home/vcap/app/s3fs");
		Assert.isTrue( bin.exists());
		Assert.isTrue(bin.isFile());
		Assert.isTrue(bin.canExecute());

		
		//create bucket directory
		logger.debug("check directory");		
		String bucketLocalDir = "/home/vcap/bucket";
		File bucketDir=new File(bucketLocalDir);
		Assert.isTrue(bucketDir.mkdir());
		bucketDir.setReadable(true, true);
		bucketDir.setWritable(true);
		Assert.isTrue(bucketDir.exists());
		 
		
		//prepare parameters
		URL url=new URL (riakInfo.getUri());
		
		
		String protocol=url.getProtocol();
		
		//FIXME patch to use http
		protocol="http";
		
		String host=url.getHost();

		String baseUri=protocol+"://"+host;
		String s3bucketName=riakInfo.getBucket();

		
		ProcessBuilder pb =new ProcessBuilder("./s3fs",
				"-f",
				"-d",
				"-d", //verbose
				s3bucketName,
				"-ourl="+baseUri,
				"-ouse_path_request_style",
				"-ossl_verify_hostname=0",
				"-ouse_cache=/home/vcap/tmp",
				bucketLocalDir);

		Map<String, String> env = pb.environment();
		env.put("AWSACCESSKEYID", riakInfo.getAccessKeyId());
		env.put("AWSSECRETACCESSKEY", riakInfo.getSecretAccessKey());		

		pb.directory(new File("/home/vcap/app"));
		File log = new File("/home/vcap/fuse.log");
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(log));
		logger.debug("processbuilder : "+pb.toString());		
		
		Process p = pb.start();
		Assert.isTrue( pb.redirectInput() == Redirect.PIPE);
		Assert.isTrue(pb.redirectOutput().file() == log);
		Assert.isTrue(p.getInputStream().read() == -1);				 
		
		return pb;
	}
	
	
	
	//add a WebDavServlet for testing purpose (webdav exposes a local filesystem in http protocol. 
	//known client : windows / sharepoint, bitkinex, linux cadaver, konqueror
	@Bean
	public ServletRegistrationBean servletRegistrationBean(){
		WebdavServlet webdav=new WebdavServlet();
		ServletRegistrationBean srb=new ServletRegistrationBean(webdav,"/webdav/*");

		//name of the class that implements net.sf.webdav.WebdavStore
		srb.addInitParameter("ResourceHandlerImplementation", "net.sf.webdav.LocalFileSystemStore");
		
		//local fs exposition
		srb.addInitParameter("rootpath", "/home/vcap");
		
		//Overriding RFC 2518, the folders of resources being created, can be created too if they do not exist.
		srb.addInitParameter("lazyFolderCreationOnPut", "0");
		
		
		srb.addInitParameter("default-index-file", "");
		srb.addInitParameter("instead-of-404", "");
		//set to 2G
		srb.addInitParameter("maxUploadSize", "2000000000");
		
		
	    return srb ;
	}
	
}