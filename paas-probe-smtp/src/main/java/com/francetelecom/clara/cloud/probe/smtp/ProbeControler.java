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

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controler for smtp probe. send a mail based on rest request
 * uses orange smtp specific managed service
 * 
 * @author apog7416
 *
 */
@RestController
public class ProbeControler {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProbeControler.class.getName());

	@Autowired
	@Qualifier("springCloudJavaMailSender")
	private MailSender javaMailSender; 


	// write something
	// ----------------------------------------------------------------------------------------

	@RequestMapping("/send")
	public void sendmail(	@RequestParam String from,
							@RequestParam String to,
							@RequestParam String subject,
							@RequestParam String text							
							) {
		logger.info("send mail, \nfrom {} \nto {} \nsubject{} \ntext {} ", from,to,subject,text);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setFrom(from);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        
        javaMailSender.send(mailMessage);
        logger.info("mail sent!");

	}


}
