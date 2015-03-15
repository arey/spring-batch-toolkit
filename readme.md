# Spring Batch Toolkit #

This toolkit provides additional features to the Spring Batch framework.

## Features ##

* Tasklet used to **delete data from Spring Batch Metadata tables** that are N months old.
* **Elasticsearch** tasklets, processor and writer to create/setup/delete indices and write documents.
* Logging Spring Batch listener
* **Spring Integration Endpoints** that launch jobs, wait the end of a job and send email with job information.
* Tasklet that uncompress a zip file to a working directory.
* Tasklet that deletes a directory recursively.

## Quick Start ##

Download the jar though Maven:

```xml
<dependency>
  <groupId>com.javaetmoi.core</groupId>
  <artifactId>spring-batch-toolkit</artifactId>
  <version>0.2.0</version>
</dependency> 
```

The Spring Batch Toolkit artefacts are available from [Maven Central](http://repo1.maven.org/maven2/com/javaetmoi/core/spring-batch-toolkit/)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.javaetmoi.core/spring-batch-toolkit/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.javaetmoi.core/spring-batch-toolkit)

## Contributing to Spring Batch Tookit ##

* Github is for social coding platform: if you want to write code, we encourage contributions through pull requests from [forks of this repository](http://help.github.com/forking/). If you want to contribute code this way, please reference a GitHub ticket as well covering the specific issue you are addressing.

### Development environment installation ###

Download the code with git:
git clone git://github.com/arey/spring-batch-toolkit.git

Compile the code with maven:
mvn clean install

If you're using an IDE that supports Maven-based projects (IntelliJ Idea, Netbeans or m2Eclipse), you can import the project directly from its POM.
Otherwise, generate IDE metadata with the related IDE maven plugin:
mvn eclipse:clean eclipse:eclipse

## Documentation ##

French articles on the [javaetmoi.com](http://javaetmoi.com) blog:

* [Parallélisation de traitements batchs](http://javaetmoi.com/2012/12/parallelisation-de-traitements-batchs/)
* [Spring Batch s’auto-nettoie](http://javaetmoi.com/2012/06/sprint-batch-sauto-nettoie/)


## Release Note ##

<table>
  <tr>
    <th>Version</th><th>Release date</th><th>Features</th>
  </tr>
<tr>
    <td>1.0.0-SNAPSHOT</td><td>next version</td>
    <td>Spring Batch 3.0 upgrade
    <br>Spring Batch Integration and Admin Endpoints
    <br>Add UncompressTasklet and DeleteDirectoryTasklet</td>
  </tr>
  <tr>
    <td>0.2.0</td><td>05/11/2013</td><td>Spring Batch 2.2 upgrade<br>Elasticsearch tasklets and writer<br>Logging Spring Batch listener</td>
  </tr>
  <tr>
    <td>0.1.0</td><td>10/07/2013</td><td>RemoveSpringBatchHistoryTasklet, partitioning and parallel steps</td>
  </tr>
</table>

## Credits ##

* Uses [Maven](http://maven.apache.org/) as a build tool
* Uses [Cloudbees](http://www.cloudbees.com/foss) and [Travis CI](www.travis-ci.org) for continuous integration builds whenever code is pushed into GitHub

## Build Status ##

Travis: [![Build
Status](https://travis-ci.org/arey/spring-batch-toolkit.png?branch=master)](https://travis-ci.org/arey/spring-batch-toolkit)

Cloudbees Jenkins: [![Build
Status](https://javaetmoi.ci.cloudbees.com/job/spring-batch-toolkit/badge/icon)](https://javaetmoi.ci.cloudbees.com/job/spring-batch-toolkit/)

Coverage Status: [![Coverage Status](https://img.shields.io/coveralls/arey/spring-batch-toolkit.svg)](https://coveralls.io/r/arey/spring-batch-toolkit?branch=master)