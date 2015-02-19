# m2m-api
This project provides API documentation and code samples for PeopleNet's M2M platform.

## API Documentation
See the [m2m-api wiki](https://github.com/PeopleNet/m2m-api/wiki) for API documentation.

## Sample Code
The m2m-api project contains sample MQTT publish and subscribe code.

### Java Sample
The Java sample code is located at [samples/java](samples/java).

The sample consists of an MQTT publisher and an MQTT subscriber application.

By default, the publisher application publishes five separate MQTT messages containing the current time to the topic *peoplenet/samples*.

The subscriber application subscribes to the same topic and prints the message contents to the console.

#### Running the Java Sample
To run the samples, first clone the project from github.
```
git clone https://github.com/PeopleNet/m2m-api.git
```
*Note, this requires git to be installed locally. If git is not installed on your system, it can be downloaded [here](http://git-scm.com/downloads).*


#### Sample Configuration
The applications use the freely available mosquito broker located at *test.mosquito.org*. Unlike the PeopleNet M2M broker, the mosquito broker does not provide credentials.
