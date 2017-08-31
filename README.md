Description
===========

This is an example of an OpenDOF requestor and provider that implement the Hello World interface. Both the requestor and provider are written as client applications that are designed to connect to an OpenDOF router.

----------

Running the Hello World Provider and Requestor
===========

The configuration for the hello world provider and requestor can be found at the top of the java file fore each application. The host must be set to point to the OpenDOF router. The credentials must also be set to use a a domain, identity, and key that the OpenDOF router supports.

After building the applications, run the both the helloWorldProvider and helloWorldRequestor applications. Either application can be started first, however the requestor only waits for the provider for the configured timeout (default 60 seconds).

	bin/iot-service start

Build Instructions
===========

This project uses [Apache Ant](http://ant.apache.org/) and [Apache
Ivy](http://ant.apache.org/ivy/) to build and resolve dependencies. In
addition, a Java Development Kit (JDK) of 1.7 or greater is required.

To build, make sure any dependencies are available in a configured Ivy
repository, and execute `ant dist` in the root of the project (this is
the default ant target).

##Runtime Prerequisites

A Java Runtime Environment (JRE) of 1.7 or greater is required.