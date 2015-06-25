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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.francetelecom.clara.cloud.sample.rabbit.exchange.Message;

@RestController
public class MessageController {
	
	@Autowired
	private InMemoryMessageRepository repository;
	
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class.getName());
	
	@Transactional
	@RequestMapping("/messages/reset")
	public void reset() {
		logger.info("reseting received message backup");
		repository.deleteAll();
	}
	
	@Transactional
	@RequestMapping("/messages")
	public List<Message> list() {
		logger.info("list all received messages");
		return repository.findAll();	
	}
	
	@Transactional
	@RequestMapping("/messages/count")
	public long count() {
		logger.info("count all received messages");
		return repository.count();	
	}
	
}