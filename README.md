# m2m-api
This project provides API documentation and code samples for PeopleNet's M2M platform.

## API Documentation
See the [m2m-api wiki](https://github.com/PeopleNet/m2m-api/wiki) for API documentation.

## Sample Code
The m2m-api project contains code samples for interacting with Amazon SQS.

### Download the Samples
Before running the samples, clone the m2m-api repo from github.
```
git clone https://github.com/PeopleNet/m2m-api.git
```
### AWS Credentials
In order to run the samples, you'll need to configure your AWS credentials locally. The samples use the [DefaultAWSCredentialsProviderChain](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/DefaultAWSCredentialsProviderChain.html) which allows configuration via environment variables, Java System Properties, a local file or an AWS instance profile. See [DefaultAWSCredentialsProviderChain](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/DefaultAWSCredentialsProviderChain.html) for more details.

### SQS Java Sample
The Java sample code is located at [samples/sqs/java](samples/sqs/java).

To build the project, from the [samples/sqs/java](samples/sqs/java) directory execute the following.
```
gradlew shadowJar
```
Once the project is built, the run the sample app as follows.
```
java -jar  build/libs/java-all.jar ${consumptionMode} ${queueName}
```
Where `${consumptionMode}` is either `sync` or `async`.
And, `${queueName}` is the name of an existing Amazon SQS Queue.

To stop the application simply execute `ctl-c` from the terminal window running the sample.

After running for 10 seconds, the sample application will output various metrics indicating the rate at which the application is sending and receiving sample messages. These numbers may vary based on the machine running the sample and network latency to AWS. For example:
```
-- Meters ----------------------------------------------------------------------
received
             count = 1500
         mean rate = 149.68 events/second
     1-minute rate = 136.40 events/second
     5-minute rate = 134.50 events/second
    15-minute rate = 134.17 events/second
sent
             count = 1250
         mean rate = 124.71 events/second
     1-minute rate = 112.24 events/second
     5-minute rate = 110.46 events/second
    15-minute rate = 110.16 events/second
```
To increase the throughput of the sample application increase the number of `senderThreads` and `consumerThreads` in the java class [App.java](samples/sqs/java/src/main/java/com/peoplenet/m2m/sample/sqs/App.java). The default for each is `1`. For example, the following change increases the number of sender and consumer threads to `10`.
```
public class App {

	private static final int senderThreads = 10;
	private static final int consumerThreads = 10;
```
After making the change, re-build the sample via `gradlew shadowJar` and re-run the sample to observe the increased throughput.
