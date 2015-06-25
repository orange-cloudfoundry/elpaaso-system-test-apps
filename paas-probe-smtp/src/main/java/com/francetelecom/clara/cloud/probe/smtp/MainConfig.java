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
package com.francetelecom.clara.cloud.probe.smtp;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MainConfig extends AbstractCloudConfig {

	private static Logger logger = LoggerFactory.getLogger(MainConfig.class
			.getName());

	// binding credentials
	 @Value("${cloud.services.myfed.connection.host}")
	String smtpHost;

	 @Value("${cloud.services.myfed.connection.port}")
	int smtpPort;

	 @Value("${cloud.services.myfed.connection.username}")
	String smtpUserName;

	 @Value("${cloud.services.myfed.connection.password}")
	String smtpPassword;
	
	 
	 
	 
//	@Autowired
//	SmtpServiceInfo serviceInfo;
	
	@Bean
	public JavaMailSender springCloudJavaMailSender(){
		return connectionFactory().service(JavaMailSender.class);
	}

	@Bean
	public JavaMailSender customJavaMailSender() {

		logger.debug("creating spring JavaMailSender");
		JavaMailSenderImpl sender = new JavaMailSenderImpl();

		Properties mailProperties = new Properties();
		mailProperties.put("mail.smtp.auth", true);
		mailProperties.put("mail.smtp.starttls.enable", false);
		
		sender.setJavaMailProperties(mailProperties);
		sender.setHost(this.smtpHost);
		sender.setPort(this.smtpPort);
		sender.setProtocol("smtp");
		sender.setUsername(this.smtpUserName);
		sender.setPassword(this.smtpPassword);

		return sender;
	}

}