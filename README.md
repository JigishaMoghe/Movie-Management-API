# Movie-Management-API
This project is a small, but fully‑featured REST API for storing and managing a collection of movies. It was  written as part of a coding exercise and deliberately keeps the scope clear and simple: you can add, list, edit  and delete movies, all while enjoying validation, pagination, Swagger documentation and unit tests. 

 What it does

-Entity: A Movie consists of an id (auto‑generated UUID), a required title , an optional director and genre , a releaseYear (with sensible lower and upper bounds), and a rating from 1 to 10.

-Endpoints:

 GET /movies returns all movies; page and size query parameters let you paginate through the list.
 
 GET /movies/{id} fetches a single movie by its ID.
 
 POST /movies creates a new record (title and rating are validated).
 
 PUT /movies/{id} updates an existing record; you can send only the fields you wish to change.
 
 DELETE /movies/{id} removes a movie permanently.

-Validation: Spring’s bean validation is used to ensure that required fields are present and that numbers like year and rating fall within reasonable limits. Invalid input returns HTTP 400 with helpful error messages.

-Documentation: The API is annotated and wired to springdoc, so visiting /swagger-ui.html in your browser gives you a full interactive OpenAPI UI.

-Layering: Controllers delegate to services, which in turn talk to a simple in‑memory repository. This keeps your HTTP layer, business logic and persistence concerns nicely separated.

-Testing: A handful of JUnit tests exercise the CRUD endpoints, pagination and validation logic. They clear the in‑memory store before each run to avoid test interference.

-Docker: A multi‑stage Dockerfile is provided to build a small, self‑contained image. Use it if you want to run the service in a container.
 
Tech stack:

-Java 17

-Spring Boot 3 (Web, Validation and Test starters)

-springdoc-openapi for Swagger/OpenAPI support

-Maven for building and dependency management
 
 Getting started

 Cloning and building 
   1. Clone this repository or copy the project files into your own GitHub repo.
   2. Make sure you have Java 17 and Maven 3.x installed on your machine.
   3. From the project root, run: mvn clean package
 
 This will compile the code, run the tests and produce a JAR under the  target/ directory. If you want to skip tests during development you can add -DskipTests .

 
 Running the application
 You have two easy options:
 • From the command line:
 mvn spring-boot:run

 or, if you prefer to run the built JAR:
 java-jar target/movie-api-0.0.1-SNAPSHOT.jar

 By default the API will start on port 8080. You can change the port by editing src/main/resources/application.properties or by passing --server.port=9090 on the command line.
 
Inside an IDE:
 
 -Import the Maven project into Eclipse, IntelliJ or another Java IDE.
 
 -Locate Movie_api.java and run it as a Java application. Spring Boot’s dev tools will hot‑reload your changes.
 
 -Alternatively, in Eclipse you can right‑click the project and select Run As ▸ Spring Boot App or Run As ▸ Maven build… if you need more control.
 
 Once it’s running, open your browser at  http://localhost:8080/swagger-ui.html to explore and test the API via Swagger UI. 
 
 Running the tests
 To run the unit tests, execute:
 mvn test

 Inside an IDE you can also right‑click the MovieControllerTest class and select Run As ▸ JUnit Test.
 The tests cover the basic CRUD flow, pagination logic, and validation error handling. You will see some
 warning logs when invalid payloads are sent; this is expected, as the tests deliberately trigger validation
 errors to ensure the API responds correctly.
 
Using Docker
 If you’d like to run the service in a container, build the image like this: 
 
 docker build-t movie-api .
 
 docker run-p 8080:8080 movie-api
 
 The published JAR is copied into a minimal JRE image during the build, keeping the runtime footprint small.
 
 
 Running via POM in an IDE
 If you’re using Eclipse or another IDE that integrates with Maven, you can also run or test the project by
 right‑clicking the pom.xml :
 
Choose Run As ▸ Maven Build…, set goals to clean spring-boot:run to start the application.

Choose Run As ▸ Maven Test to execute the unit tests.

This approach is handy if you have multiple modules or want to customise profiles.
 
 Final notes : 
 This API keeps its persistence layer in memory for simplicity. In a real‑world application you would replace MovieRepository with a database implementation (JPA, JDBC, etc.). The service and controller layers have been designed so you can swap the repository out without changing the HTTP layer. If you’d like to add a real database, environment‑based configuration with .env files, or additional integration tests, the structure here should make those additions straightforward. 


