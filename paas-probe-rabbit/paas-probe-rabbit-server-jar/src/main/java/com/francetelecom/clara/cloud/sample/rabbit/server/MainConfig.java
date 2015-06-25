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
package com.francetelecom.clara.cloud.sample.rabbit.server;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;



@Configuration
public class MainConfig extends AbstractCloudConfig {

	@Bean
	public ConnectionFactory rabbitRequestCF() {
		return connectionFactory().rabbitConnectionFactory("request");
	}
	
	@Bean
	PlatformTransactionManager transactionManager(){
		return new RabbitTransactionManager(rabbitRequestCF());
	}
	
	@Bean
	public Queue requestQueue() {
		return new Queue("rmq.request.queue", true);
	}
	
	
	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setTransactionManager(transactionManager());
		container.setQueueNames("rmq.request.queue");
		//container.setQueues(requestQueue());
		container.setMessageListener(listenerAdapter);
//		container.setConcurrentConsumers(10);
//		container.setMaxConcurrentConsumers(10);
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		
		return container;
	}
	
	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

    @Bean
    Receiver receiver() {
        return new Receiver();
    }

	
	

}
