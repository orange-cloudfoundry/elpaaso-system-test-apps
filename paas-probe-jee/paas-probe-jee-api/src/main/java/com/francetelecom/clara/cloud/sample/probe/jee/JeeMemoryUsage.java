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
package com.francetelecom.clara.cloud.sample.probe.jee;

import java.io.Serializable;

public class JeeMemoryUsage implements Serializable {

	private static final long serialVersionUID = 9004825875791737675L;

	/**
	 * maxHeapSize in bytes
	 */
	long maxHeapSize;
	/**
	 * usedHeapSize in bytes
	 */
	long usedHeapSize;
	
	/**
	 * @return maxHeapSize in bytes
	 */
	public long getMaxHeapSize() {
		return maxHeapSize;
	}
	
	public void setMaxHeapSize(long maxHeapSize) {
		this.maxHeapSize = maxHeapSize;
	}
	
	/**
	 * @return usedHeapSize in bytes
	 */
	public long getUsedHeapSize() {
		return usedHeapSize;
	}
	public void setUsedHeapSize(long usedHeapSize) {
		this.usedHeapSize = usedHeapSize;
	}

	
}
