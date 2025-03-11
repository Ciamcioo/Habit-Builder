# Habit-Builder

Welcome to the habit builder code base! I hope you will like it.

## What is Habit Builder?

Habit Builder is a RESTful Web Service providing API for external users to manage their habits. The main goal of the project is to create an environment for creating, managing, progressing, and developing new habits. The service's goal is to provide you with tools and operations to develop in any field of your interest. The most crucial thing is that you choose how you are going to do it thanks to the open API you have access to. You may want to build your application around it, plug it into the existing application, or get the application to manage your habit, it's your choice! No matter which way you are going to choose the outcome is going to be the same, you are going to develop new habits that will keep your life organized and the way you want it.

## Table of contests

1. [What is Habit Builder?](#what-is-habit-builder)
2. [Used Technologies](#used-technologies)
3. [Requirements](#requirements)
4. [Installation](#installation)
5. [API Usage](#api-usage)
    - [Example of Request](#examples-of-requests)
    - [Example of Response](#examples-of-responses)
6. [Source Code](#source-code)
    - [Controller layer](#controller-layer)
    - [Service layer](#service-layer)
    - [Data layer](#data-layer)
7. [Contribution](#contribution)
8. [Licences](#licences)

## Used Technologies

Service uses many different technologies to achieve its goal. You can get familiar with them by looking at the list below:
- Java 21
- Spring Boot version 3.4.2
- Spring Web
- Spring Data
- Spring Doc
- Slf4j Logger
- H2 (in-memory database)
- Swagger UI

## Requirements

To get started working with the project, the only thing you are going to need is Java version 21 installed on your machine. You can get it from the [Oracle side](https://www.oracle.com/pl/java/technologies/downloads/#java21). In case you are a Linux user you can get it using your favorite package manager.

## Installation
1. Get the project repository
    - using Git
   ``` shell
        git clone https://github.com/Ciamcioo/Habit-Builder.git
   ```
    - using zip archive
   ``` shell
        cd <path/to/your/development/directory>
        curl -L -o Habit-Builder.zip https://github.com/Ciamcioo/Habit-Builder/archive/refs/heads/main.zip
        unzip  Habit-Builder.zip -d Habit-Builder 
        cd Habit-Builder
   ```
2. Build the habit builder project
    - using Maven installed locally
   ``` shell
      mvn clean install
      mvn compile 
   ```
    - using Maven Wrapper included in the project
   ``` shell
     ./mvnw clean install
     ./mvnw compile   
   ```
3. Run the project
    - using Maven installed locally
   ``` shell
    mvn spring-boot:run
   ```
    - using Maven Wrapper included in the project
   ``` shell
    ./mvnw spring-boot:run
   ```
4. Get to the documentation side to get to know the project API
   Open your favorite browser and go to the documentation side which is located under <http://localhost:8080/swagger-ui/index.html>

After successfully completing this setup you can freely explore the environment of habit builder service. Application by default runs on port 8080. Application's database does not require any additional setup before using it. 

## API usage

Communication with Habit Builder API undergoes via HTTP requests. Service supports four of the most popular HTTP methods which are: GET, POST, PUT, and DELETE. Every resource is uniquely identified by the URL which is an endpoint for different kinds of operations. Requests and responses differ from each other due to the requirements of the operation served on a specific endpoint. Here I can advise reaching out to the [documentation](http://localhost:8080/swagger-ui/index.html) of the API in Swagger-UI to get all the necessary information. API does not need authentication for usage.
The requests to an API might be sent using different tools like Postman, browser, or regular bash using the curl command. In the examples below I will use the curl command to present you client request and service responses.

### Examples of requests

Here I'm providing you with three different examples of API requests with varying methods of HTTP. The method's requirements are presented below in the table.

| Method | Endpoint             | Required Params     | Request Body (fields)                                   | Response Codes | Response Body      |
|--------|----------------------|---------------------|---------------------------------------------------------|----------------|--------------------|
| GET    | `/api/habits`        | None                | None                                                    | 200 Ok         | List of all habits |
| POST   | `/api/habit`         | None                | `name`, `frequency`, `startDate`, `endDate`, `reminder` | 201 Created    | Created habit name |
| DELETE | `/api/habit/{name}`  | Habit name (string) | None                                                    | 204 No Content | None               |

1. GET
   ``` shell
     curl -X 'GET' \
        'http://localhost:8080/api/habits' \
        -H 'accept: */*'
   ``` 
2. POST
    ``` shell
   curl -X 'POST' \
   'http://localhost:8080/api/habit' \
   -H 'accept: */*' \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Coding",
   "frequency": "DAILY",
   "startDate": "2025-03-10",
   "endDate": "2025-03-10",
   "reminder": true
   }'
   ```
3. DELETE
   ``` shell
   curl -X 'DELETE' \
   'http://localhost:8080/api/habit/Coding' \
   -H 'accept: */*'
   ```

### Examples of responses

For shown above request service answered to client with a response. The response also differs from one another based on the request that they have received. For example, they have different status codes and messages.

1. GET Response
   ``` json
   [
      {
      "name": "Programing",
      "frequency": "DAILY",
      "startDate": "2025-03-10",
      "endDate": "2025-08-10",
      "reminder": true
      },
      {
      "name": "Finances",
      "frequency": "DAILY",
      "startDate": "2025-03-10",
      "endDate": "2025-04-10",
      "reminder": true
      },
      {
      "name": "Running",
      "frequency": "WEEKLY",
      "startDate": "2025-03-10",
      "endDate": "2026-03-10",
      "reminder": false
      }
   ]
   ```
2. POST Response
   ```json
      "Coding"
   ```

3. DELETE Response
   For DELETE we do not have a response body, just the HTTP Code.

## Source Code

The source code of a service is built around a three-layer architecture. We can detail the controller, the service layer, and the data layer. Each of those layers has its own purpose. They interact with each other, but they do not know others' logic behind their operations. Layers can interact with each other thanks to the IoC (Inversion of Control) which is provided by Spring framework. Instances of classes that are used in this interaction are called Beans and they are initialized by the Spring.
Let's walk through a simple example of program flow initialized by the client sending the request to the endpoint. Our example is going to be retrieving data about a specific habit by its name.

> **_NOTE:_** Habit name is unique in the database. It makes sure that we are going to retrieve exactly one habit from the database.

### Controller layer
The main purpose of the controller layer is to receive requests from clients, pass them to the service layer, and based on the results provided, create a response to the client. Service object is injected into this layer using Dependency Injection provided by the Spring framework.

``` Java 

@RestController
@RequestMapping("api")
public class HabitController {
    private HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }
    
    ... 
    
    @GetMapping("/habit/{name}")
    public ResponseEntity<HabitDto> getHabitByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(
                habitService.getHabitByName(name),
                HttpStatus.OK);
    }
    
    ...
 }
 ```

Based on the provided sniped of code, we can assure that if everything goes well the client is going to receive the Habit based on the habit name provided as the path variable. In case of any exceptions, the control is going to be redirected to the class annotated as the @RestControllerAdvice which is responding to the client with information about the error in the service. An example of such a class is provided below.

``` Java 
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
        HabitAlreadyExistsException.class
    })
    protected ResponseEntity<Error> badRequestExceptionHandler(RuntimeException exception, WebRequest request) {
        return new ResponseEntity<>(
                new Error(exception.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
    
    ...
 }
 ``` 





> **_NOTE:_** This `badRequestExceptionHandler()` method might not be related to the example of the controller above. This is just a showcase of @RestControllerAdvice class`

### Service layer

The purpose of the service layer is to serve the main logic of the program and communication with the database via Repositories. Based on the specification and invoked operation it might perform different actions related or not to the database. Repository objects are injected into the service class to allow them to work with persisted data.
Going on with our example controller layer invoked the `getHabitByName()` method passing as the argument the name of the habit that we want to retrieve.

``` Java

@Service
public class HabitManagementService implements HabitService{
       ...
       
    private final HabitRepository habitRepository;
      
      ... 
      
    public HabitManagementService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }
      
      ...
      
    @Override
    public HabitDto getHabitByName(String name) {
        Habit habit = habitRepository.findHabitByName(name)
                                     .orElseThrow(
                                             () -> new HabitNotFoundException(String.format(HABIT_NOT_FOUND, name))
                                     );

        return convertHabitToHabitDto(habit);
    }
```

As you can see the logic standing behind the method `getHabitByName()` is fairly simple. It realizes only the communication with the database and retrieving an object. If none of the habits match the passed name,  the method is going to throw and custom exception `HabitNotFoundException` which is going to be handled in controller exception by @RestControllerAdvice class.

### Data layer

The data layer stores the classes whose purpose is to map the data from and to the database. Besides the model classes data layer contains also repository interfaces defining methods which are later parsed to the SQL statements. Repository interfaces are implementing the JpaRepository interface. Java Persist API allows the program to communicate with the database. Habit Builder project as an implementation of JPA uses Hibernate.

``` Java
@Repository
public interface HabitRepository extends JpaRepository<Habit, Integer> {

    Optional<Habit> findHabitByName(String name);

    Boolean existsByName(String name);
}
```

New method definitions for repositories can be created using specific JPA definition syntax or writing JPQL statements within the @Query annotation\
The data layer besides storing the entities definition stores the Data Transfer Objects (DTO) which are used to transport data around the program and for client usage.
``` Java 
public record HabitDto(

    @NotNull(message = "Habit name cannot be null")
    @Size(min = 1, max = 255, message = "Habit name must have 1 to 255 characters")
    String name,

    @NotNull(message = "Habit frequency cannot be null")
    HabitFrequency frequency,


    @FutureOrPresent(message = "Start date of habit cannot be placed in the past")
    LocalDate startDate,

    @Future(message = "End date of habit must be placed in the future")
    LocalDate endDate,
    Boolean reminder
) {

... 

}
```

## Contribution

Pull requests and contributions to the project are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Licences
[MIT](https://choosealicense.com/licenses/mit)

