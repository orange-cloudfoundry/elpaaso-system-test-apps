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
package com.francetelecom.clara.cloud.sample.rabbit.client;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * spring cloud + spring boot rabbbit connection see
 * http://www.javacodegeeks.com
 * /2014/10/spring-configuration-rabbitmq-connectivity.html
 * 
 * @author pierre
 *
 */
@Configuration
public class MainConfig extends AbstractCloudConfig {

	@Bean
	public ConnectionFactory rabbitRequestCF() {
		
		ConnectionFactory cf = connectionFactory().rabbitConnectionFactory("request");
		return cf;				
	}


	@Bean
	public PlatformTransactionManager transactionManager(){
		return new RabbitTransactionManager(rabbitRequestCF());
	}
	
	
	
	@Bean
	public RabbitTemplate requestExchangeTemplate() {
		RabbitTemplate r = new RabbitTemplate(rabbitRequestCF());
//		r.setExchange("rmq.request.exchange");
		r.setRoutingKey("rmq.request.queue");
		r.setConnectionFactory(rabbitRequestCF());
		r.setChannelTransacted(true);
		return r;
	}

}
