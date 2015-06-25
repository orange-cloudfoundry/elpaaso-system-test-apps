# Spring Cloud Service Connector for RiakCS S3 Services

This project provides a [Spring Cloud](https://github.com/spring-projects/spring-cloud) service connector for RiakCS S3 services brokered by the Cloud Foundry [p-riakcs] (adapted from https://github.com/davidehringer/s3-cf-service-broker).

## Example Usage

Add the library to your project:

```
<dependency>
	<groupId>org.cloudfoundry.community</groupId>
	<artifactId>spring-cloud-riakcs-service-connector</artifactId>
	<version>1.0.0</version>
</dependency>
```

Bind an S3 RiakCs service to your Cloud Foundry application.

Use Spring Cloud to get an `RiakcsServiceInfo` instance.

```
CloudFactory cloudFactory = new CloudFactory();
Cloud cloud = cloudFactory.getCloud();

RiakcsServiceInfo serviceInfo = (RiakcsServiceInfo) cloud.getServiceInfo("my-riak-service");
...  serviceInfo.getAccessKeyId();
...  serviceInfo.getSecretAccessKey();
...  serviceInfo.getBucket();
...  serviceInfo.getURI();

```
