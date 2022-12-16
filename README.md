# README #

This README would normally document whatever steps are necessary to get your automated scratch tests running on your local machine.

### What is this repository for? ###

This repository is used for writing down & maintaining Automation Scratch Tests for Magento.

### Clone git project: ###

* git clone <git@url>

### How do I get set up? ###

In order to setup we need to first clone this repository on your local machine, once the repository is setup then we have to ensure we have following configuration done.

### Configuration ###
* Java 1.8+
* Maven

Just to ensure we have both properly configured on our local machine, then open the Terminal or cmd and run the following commands to check we have Java and maven configured.

### Commands To Execute ###

* java --version
* mvn -v

### How to run tests ###

In cmd or terminal(macos) then go to your project by executing the following commands in order to start the test executions -

* cd magento-qa
* mvn clean test

### Run a specific test ###

To run a specific test

* mvn test -Dtest=\#<testcasename>

### Contribution guidelines ###

### Writing tests ###
In order to start writing down the test lets just understand the structure, we have differentiated each module to a class which is defined as follows -

* GenerateRandomUserTests
* SignUpAndPurchaseProductTests

All tests can be found in the following path i.e. src/test/java/tests/web/<module-name> and page objects are also defined in respective <feature> page class i.e. each test have a respective page class defined in this path i.e.src/main/java/pages/<module-name>.

### Who do I talk to? ###

* You can contact to `sharmavarun2014@gmail.com` for any repo related concerns.

