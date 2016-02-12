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
Once the project is built, run the sample app as follows.
```
java -jar  build/libs/java-all.jar ${queueName}
```
Where `${queueName}` is the name of an existing Amazon SQS Queue.
