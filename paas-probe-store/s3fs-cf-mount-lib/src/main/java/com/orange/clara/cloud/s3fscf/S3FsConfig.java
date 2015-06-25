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
package com.orange.clara.cloud.s3fscf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.cloudfoundry.community.service.storage.RiakcsServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
public class S3FsConfig extends AbstractCloudConfig {
	
	private static Logger logger=LoggerFactory.getLogger(S3FsConfig.class.getName());
	
	@Autowired
	public RiakcsServiceInfo riakService;
	
	
	
	//add a fuse s3fs mount command. uses credentials. see https://github.com/s3fs-fuse/s3fs-fuse
	@Bean
	public ProcessBuilder mount() throws IOException {
		
		RiakcsServiceInfo riakInfo=this.riakService;
		logger.info("start configure fuse for riakcs service {}",riakInfo);
		
		String homeDir="/home/vcap/";
		

		logger.debug("create bucket directory");

		
		String bucketLocalDir = homeDir+"buckets/"+riakInfo.getId();
		
		{
		File buckets=new File(homeDir+"buckets");
		Assert.isTrue(buckets.mkdir(),"Can not create buckets dir");
		

		File bucketDir=new File(bucketLocalDir);
		Assert.isTrue(bucketDir.mkdir());
		bucketDir.setReadable(true, true);
		bucketDir.setWritable(true);
		Assert.isTrue(bucketDir.exists(),"bucket dir must be present in "+bucketDir);
		}
		
		logger.debug("create bucket cache directory");
		String bucketCacheDir = homeDir+"bucketcaches/"+riakInfo.getId();		
		{
		File bucketcaches=new File(homeDir+"bucketscaches");
		Assert.isTrue(bucketcaches.mkdir(),"Can not create bucket caches dir");

		File bucketCacheLocalDir=new File(bucketcaches,riakInfo.getId());
		Assert.isTrue(bucketCacheLocalDir.mkdir(),"Can not create bucket cache dir");
		bucketCacheLocalDir.setReadable(true, true);
		bucketCacheLocalDir.setWritable(true);		
		}
		
		//copy the binary s3fs from jar to local fs
		InputStream binIs=this.getClass().getClassLoader().getResourceAsStream("s3fs");
		Assert.notNull(binIs,"Unable to find s3fs in jar");
		Assert.isTrue(binIs.available()>0,"s3fs binary in jar is empty");
		Path target = FileSystems.getDefault().getPath(homeDir+"s3fs");
		Files.copy(binIs,target);
		
		logger.info("s3fs binary set in {}",target);		

		//locate the binary
		logger.debug("check binary");
		File bin=new File (homeDir+"s3fs");
		bin.setExecutable(true);
		
		
		Assert.isTrue( bin.exists(),"binary file must exist");
		Assert.isTrue(bin.isFile(),"binary file must exist (file)");
		Assert.isTrue(bin.canExecute(),"binary must be executable");
		
		//exec mount command line
		//example command line :
		logger.info("launching fuse s3fs on url {} for bucket {}",riakInfo.getBaseUri(),riakInfo.getBucket() );
		
		ProcessBuilder pb =new ProcessBuilder("./s3fs",
				"-f",
				"-d",
				"-d", //verbose
				riakInfo.getBucket(),
				"-ourl="+riakInfo.getBaseUri(),
				"-ouse_path_request_style",
				"-ossl_verify_hostname=0",
				//"-oallow_other",  -o allow_other,uid=0,gid=2301
				// -o default_acl=public-read  -o uid=0 -o gid=2300 -o umask=007
				"-odefault_acl=public-read",
				"-oumask=000",
//				"-ouid=0",
//				"-ogid=2300",				
				"-ouse_cache="+bucketCacheDir,
				bucketLocalDir);

		Map<String, String> env = pb.environment();
		env.put("AWSACCESSKEYID", riakInfo.getAccessKeyId());
		env.put("AWSSECRETACCESSKEY", riakInfo.getSecretAccessKey());		

		pb.directory(new File(homeDir));
		File log = new File(homeDir+"fuse.log");
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(log));
		logger.debug("processbuilder : "+pb.toString());		
		
		Process p = pb.start();
		Assert.isTrue( pb.redirectInput() == Redirect.PIPE,"redirect input KO");
		Assert.isTrue(pb.redirectOutput().file() == log,"redirect ouput KO");
		Assert.isTrue(p.getInputStream().read() == -1);				 

		logger.info("DONE configuring fuse s3 mount for riakcs service {}",riakInfo);
		
		return pb;
	}
	
	
	
	
}