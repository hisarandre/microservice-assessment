<h1 align="center">Welcome to the Assessment Microservice 👋</h1>
<p>
</p>

> The microservice is a part of the Project Mediscreen. It calculates the risk of a patient from their informations and consultations notes.

## Versions
- Spring Boot: 3.1.2
- Maven: 3.1.2
- JDK: 17

## Run the app

To launch the app, you can run it locally or use docker.

### Run with docker
Go to [the webapp](https://github.com/hisarandre/Mediscreen-webapp) to launch the app with docker

### Run local

0. First, clone the Assessment repository

   To launch the application, you can follow these steps:

1. Make sure you have the required versions of Java and dependencies installed.
2. Open a terminal or command prompt and navigate to the project directory.
3. Run the following command to build the project and create an executable JAR file:
   ` mvn package `
4. Once the build is successful, you can launch the app using the following command:
   ` java -jar target/assessment-0.0.1-SNAPSHOT.jar `
   This will start the app on the configured server address : http://localhost:8080

## Testing

Run the command for testing:
- `mvn test`
The jacoco report will be generated in target/site/index.html


## Endpoints
Note:
Make sure you have at least the microservice Patient and History running on the correct port
You can change the port in the WebClient package

- /assess/risk/{patientId} - GET REQUEST
- /assess/id  - POST REQUEST with "Integer patId" as param
- /assess/name  - POST REQUEST with "String family" and "String given" as param

## Curls
Note:
Make sure you have at least the microservice Patient and History running on the correct port
Make sure you did the previous curl to implement the patient and the history in each microservice

`curl -d "patId=11" -X POST http://localhost:8080/assess/id`
Patient: Test TestNone (age 52) diabetes assessment is: None
`curl -d "patId=12" -X POST http://localhost:8080/assess/id`
Patient: Test TestBorderline (age 73) diabetes assessment is: Borderline
`curl -d "patId=13" -X POST http://localhost:8080/assess/id`
Patient: Test TestInDanger (age 14) diabetes assessment is: In danger
`curl -d "patId=14" -X POST http://localhost:8080/assess/id`
Patient: Test TestEarlyOnset (age 16) diabetes assessment is: Early onset
`curl -d "family=TestBorderline&given=test" -X POST http://localhost:8080/assess/name`
Patient: Test TestBorderline (age 73) diabetes assessment is: Borderline
`curl -d "family=TestNone&given=test" -X POST http://localhost:8080/assess/name`
Patient: Test TestNone (age 52) diabetes assessment is: None
`curl -d "family=TestInDanger&given=test" -X POST http://localhost:8080/assess/name`
Patient: Test TestInDanger (age 14) diabetes assessment is: In danger
`curl -d "family=TestEarlyOnset&given=test" -X POST http://localhost:8080/assess/name`
Patient: Test TestEarlyOnset (age 16) diabetes assessment is: Early onset