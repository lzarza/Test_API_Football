# Football team manager API 

This is the API required for a coding game in a technical interview
The aim is to be able to manage a football club team

## Prerequisites

- Java JDK 23 minimum
- Maven for loading dependancies

## Compile

- Get source code and import as Maven Project in your IDE
- Modify application.properties to use your own database, the projects contains an embedded H2 database for test purposes.
- modify data.sql to set up you own settings. 
- Build application using maven clean install

## Run and Test

- Run API using maven spring-boot:run
- You can see your springboot api is running using this link : http://localhost:8080/test-api/swagger-ui/index.html
- You may find some postman requests for test your application

## Note from developper

- The base topic was only on team management but it feels incomplete so i added the possibility to manage players too
- delete methods don't delete since this is not something to put in all hands. The teams and players are not deleted but disabled. Still available through their id but lists won't bring them.
- Both controllers follow the REST directives to be able to perform CRUD operations.
