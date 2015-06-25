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
package com.francetelecom.clara.cloud.sample.probe;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class ConfigProperties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2829278890645771690L;

	Map<String, String> entries;
	
	public Map<String, String> getEntries() {
		return entries;
	}

	public void setEntries(Map<String, String> entries) {
		this.entries = entries;
	}

	public ConfigProperties() {
		this.entries = new Hashtable<String,String>();
	}
	
	public ConfigProperties(Map<String,String> entries) {
		this.entries = entries;
	}

	public void put(String name, String value) {
		// TODO Auto-generated method stub
		entries.put(name, value);
	}
}
