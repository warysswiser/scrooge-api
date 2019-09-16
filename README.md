# scrooge-api

[![Build Status](https://travis-ci.org/waryss/scrooge-api.svg?branch=master)](https://travis-ci.org/waryss/scrooge-api)
[![Coverage Status](https://coveralls.io/repos/github/waryss/scrooge-api/badge.svg?branch=master)](https://coveralls.io/github/waryss/scrooge-api?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

This API expose services that help expenses management. 
It exposes simple CRUD services to manage
* [domain]/users/* : This end point exposes services to manage API users
* [domain]/expenses/* : This end point is the main part of the API. It exposes services that allow users to manage their budgets. It allows, among other things, to define the resources and to record the expenses
* [domain]/attachments/* : This end point is used to upload expenses receipts. These receipts must me linked to an expense recorded in the "budget" service.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Openjdk](https://openjdk.java.net/projects/jdk/11/) - The free and open-source implementation of the Java™ Platform 
* [Spring Boot](https://spring.io/projects/spring-boot) - Framework to ease the bootstrapping and development of new Spring Applications
* [MongoDB](https://www.mongodb.com/fr) - Open-Source Relational Database Management System
* [git](https://git-scm.com/) - Free and Open-Source distributed version control system 
* [Swagger](https://swagger.io/) - Open-Source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful Web services.

## Getting Started

All you have to do is cloning the project, and open it to your favorite IDE :)
```
cd ~/[workspace]
git clone https://github.com/waryss/scrooge-api.git
or
git@github.com:waryss/scrooge-api.git
```

### Installing
```
cd ~/[workspace]
mvn clean install
```

## Running the tests
```
cd ~/[workspace]
mvn test
```

## Running the application locally
```
cd ~/[workspace]
mvn spring-boot:run
```
## Documentation
* [Swagger](http://localhost:8089/swagger-ui.html#) - Documentation & Testing
