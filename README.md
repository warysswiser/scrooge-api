# scrooge-api

[![Build Status](https://travis-ci.org/waryss/scrooge-api.svg?branch=master)](https://travis-ci.org/waryss/scrooge-api)
[![Coverage Status](https://coveralls.io/repos/github/waryss/scrooge-api/badge.svg?branch=master)](https://coveralls.io/github/waryss/scrooge-api?branch=master)
[![tag](https://img.shields.io/github/v/tag/waryss/scrooge-api.svg)](https://github.com/waryss/scrooge-api/releases)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Commit activity](https://img.shields.io/github/commit-activity/m/waryss/scrooge-api.svg)](https://github.com/waryss/scrooge-api/pulse)

## About

Defining a budget, following it and sticking to it is not always easy. The scrooge API exposes some great services to help us do it a little easier.

Exposed services ([swagger](https://github.com/waryss/scrooge-api#documentation "more")):
* _**[domain]/users/***_ : This end point exposes services to manage API users. It allows registration, authentication, profile modifications and parameters account management.

* _**[domain]/budgets/***_ : This end point is the main part of the API. It exposes services that allow users to manage their budgets. It allows, among other things, to define the resources and to record the expenses

* _**[domain]/attachments/***_ : This end point is used to upload expenses receipts. These receipts must me linked to an expense recorded in the "budget" service.


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Openjdk](https://openjdk.java.net/projects/jdk/11/) - The free and open-source implementation of the Java™ Platform 
* [Spring Boot](https://spring.io/projects/spring-boot) - Framework to ease the bootstrapping and development of new Spring Applications
* [MongoDB](https://www.mongodb.com/fr) - Open-Source Relational Database Management System
* [git](https://git-scm.com/) - Free and Open-Source distributed version control system 
* [Swagger](https://swagger.io/) - Open-Source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful Web services.

## Getting Started

All you have to do is cloning the project, and open it with your favorite IDE :fa-smile-o:
```shell
cd ~/[your_workspace]
git clone https://github.com/waryss/scrooge-api.git
```
or
```shell
cd ~/[your_workspace]
git@github.com:waryss/scrooge-api.git
```

### Installing
```shell
cd ~/[workspace]
mvn clean install
```

### Running the tests
```shell
cd ~/[workspace]
mvn test
```

### Running the application locally
```shell
cd ~/[workspace]
mvn spring-boot:run
```
## Documentation
* [Swagger](https://scroogeapi.herokuapp.com/swagger-ui.html#) - Documentation & Testing (from staging)
