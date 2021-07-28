**Library Management System** is a project that aims to practice
Spring modules and its specific features as many as possible. In addition,
it contains some useful non-Spring libraries. Below, you can find
the main titles of these modules, in which features of these are used
and in which classes they are used.


#
#
# **PROJECT MODULES AND CONTENT OF BRANCHES**

**MODULE-1: SPRING BASICS**

*1-Spring Boot*

*2-Lombok*

*3-Web MVC*

*4-Thymeleaf*

*5-Data JPA*

*6-Mapstruct & Jackson*

*7-Validation*

*8-Security*

*9-Exception Handling & Logging*

*10-AOP*

*11-Testing*

**MODULE-2: SPRING INTERMEDIATE

*1-Data JPA*

*2-Auditing*

*3-Mapstruct & Jackson*

*4-Validation*

*5-Security*

*6-Configuration*

*7-Caching*

*8-Testing*

*9-Swagger*

**MODULE-3: SPRING ADVANCED**

*1-AMQP*

*2-Docker*

*3-Scheduling*

*4-Mongo DB*

*5-Data JPA*

*6-Redis*

*7-JMS*

*8-Mail*

**MODULE-4: MICROSERVICES WITH SPRING CLOUD**

*1-Infrastructure*

*2-First Microservice*

*3-Service Discovery*

*4-API Gateway*

*5-Multiple Databases*

*6-OpenFeign & API Composition*

*7-Other Service*


#
#
# **SPRING MODULES**

**WEB MVC WITH REST**
- Web MVC with Rest started to be used in `restcontroller` package.
- `WebMvcConfig` is the configuration of it.
- `PostMapping`, `PutMapping`, `GetMapping`, `DeleteMapping` annotations are used
  together with `RequestMapping` annotation.
- `PathVariable` and `RequestBody` annotations are also used when passing parameters.
- `Service` and `Repository` classes are the remaining components of MVC.
- `RestTemplate` which is configured in `WebMvcConfig` is used while calling
  facebook endpoint to get auth token.

**WEB MVC WITH THYMELEAF**
- Web MVC with Rest started to be used in `controller` package.
- As a view resolver, thymeleaf is used.
- Only some controllers are created to exemplify this.
- `Model` is used in general whereas `ModelAndView` and `ModelMap` are used
  in a few methods.
- The files under `resources/templates` directory are html files used in
  the controllers.

**DATA JPA**
- The JPA repositories are defined in `repository/jpa` package.
- Spring Data JPA uses Hibernate as ORM in the background.
- CRUD operations are done by using these repositories.
- Entities are defined in `infra/entity/jpa` package.
- One to one, one to many, many to one and many to many relations
  are used while connecting entities to each other.
- Paging and sorting is done for books.
- n+1 query problem is solved by using `EntityGraph` annotation.
- A custom query can also be generated using `Query` annotation. `join fetch` is
  used to prevent n+1 query problem.
- Specifications are used while searching books with some custom filters.
- Projections are used for authors and categories while mapping the entity into a custom
  object.
- `bootstrap` package fills in the in-memory database.
- This database loader is used basically for integration tests and when the app is up
  with `dev` profile.
- Composite key is used in `LogAggregationEntity`.

**VALIDATION**
- `Valid` annotation is used in rest controllers to validate the entity passing through the controller.
- This annotation used a `validate` method of a `Validator` class.
- Validators are in the `infra/validator` package.
- `UUIDValidator`, however, is a constraint validator to validate uuid is null or not and it gets a parameter
of `NullableUUIDFormat` annotation.

**SECURITY**
- Three different security configs are implemented; basic, jwt and social.
- `BasicSecurityConfig` is enabled if `security.authentication.basic` has a value of `true` in the yml config file.
- It has two users defined in memory, one for librarian role and one for member role.
- It uses `MyBasicAuthenticationEntryPoint` for the auth exceptions.
- `JwtSecurityConfig` is enabled if there are no basic security beans.
- Three different inner classes are implemented; for password authentication, for social authentication and
for authorization.
- These inner classes are called in order and the order is set by `Order` annotation. If the url matches with 
the allowed ones in the configure method, that method is only called.
- `LibraryCardService` is used for the user details service while authenticating. Also, caching is enabled for 
  its instance by `CachingUserDetailsService` is set.
- `MyAuthenticationProvider` is used while authenticating and this method encodes the password using `PasswordEncoder` bean.
- For social login, a custom encoder is defined in `EncryptionConfig` to enable passing the social login raw password
through the authentication.
- `infra/auth` package contains the files for auth filtering, entry points, token generation, validation and decoder services,
auth providing and authority deserialization.
- The filters construct the login model with username and password. After checking the model is ok, authentication is attempted
with `UsernamePasswordAuthenticationToken`.
- If it is successful, a jwt token is generated using `JwtTokenGenerationService`.
- The generated tokens are written into Redis cache with a ttl to decrease the load in the auth service.
- While authorizing, `TokenValidationFilter` is used to set the authentication.
- `PreAuthorize` annotation in the rest controllers checks if the user has the right to authorize the method or not.

**EXCEPTION HANDLING**
- Exceptions are handled using `RestResponseEntityExceptionHandler` which is annotated by `RestControllerAdvice`.
- It catches all the exceptions thrown in the MVC flow.
- `JWTAuthenticationEntryPoint` and `MyBasicAuthenticationEntryPoint` catch the exceptions while attempting to authenticate.

**LOGGING**
- Logging is handled using Lombok's `@Slf4j`
- For `NotificationMapper`, it's necessary to define a concrete logger implementation
  since it's an interface and its implementation is generated in the compile-time.
- The implementation package is `apache.logging.log4j`
- The logging levels are defined in yml file.

**ASPECT ORIENTED PROGRAMMING**
- `LogExecutionTime` is an annotation to use `LogExecutionAspect`.
- This aspect logs the execution time with the security context holder for
  the classes with that annotation.
- It depends on the expression of `aspect.enabled.log-execution-time`
- `NullableUUIDFormat` is another annotation which is configured by `UuidValidator`
  as a constraint.
- This validator implements `ConstraintValidator` having a method of `isValid`
- `LogNullParameterAspect` is logging null parameters of all methods.
- It depends on the expression of `aspect.enabled.log-null-parameters`

**TESTING**
- JUnit5 and Mockito are used as testing frameworks.
- `auth` and `restcontroller` packages contain integration tests.
- `test` profile is used for all the integration tests.
- Bootstrap loader loads all the data into the in-memory database.
- Mock user with a specific role is used for integration tests.
- Integration tests must be run in order to be successful because each can change the in-memory database.
- `service` package tests caching of the service layer.
- `util` package contains some unit tests.

**AUDITING**
- `AuditConfig` is the configuration for auditing. It provides an
  auditor aware.
- This aware is necessar to get the current auditor for filling in the
  `lastModifiedBy` field.
- `Auditable` interface provides columns for auditing such as createdBy,
  lastModifiedDate etc.
- The concrete entity classes implementing this interface have automatically
  these fields.

**CACHING**
- `CacheConfig` is the configuration of the in-app caching.
- In this config file, caches are defined in order to be used in the
  service layer such as authors, books etc.
- In the service layer, while getting entities, they are put into the cache.
- While deleting them, they are evicted from the cache.
- While updating an entity, some deletes the item from the cache and
  some forces to put the item into the cache.
- While getting multiple entities, caching is done one by one. So, it's
  necessary to use `@Resource` annotation to call a private method with a
  caching annotation.

**AMQP WITH RABBITMQ**
- `RabbitMqConfig` is the configuration of the rabbit mq. It defines the queues
  and the topic exchange.
- The configuration depends on the `amqp.enabled` expression on the yml file.
- `ConcreteMemberLogPublisherService` is sending member logs to the member events topic exchange with a routing key of the related log topic.
  (Book, BookItem, BookLoan...)
- `MemberEventsHandler` listens all the queues in the topic exchange and put them into an in-memory queue.
- In the `WebMvcConfig`, a concrete `MemberLogPublisherService` bean is created depending on
  `RabbitMqConfig` bean.
- Otherwise, `VoidLogService` bean is created.

**MONGO DB**
- `MemberLogEntity` is a document entity used in mongo db.
- `MemberLogService` is used for the operations of this type of entity.
- `MemberLogRepository` is used for the simple operations.
- `MongoTemplate` is used for the complex queries such as aggregations.
- `MemberEventsHandler` consumes the in-memory queue and put the member logs into the
  mongo db using `MemberLogService`
- `MemberLogController` provides an api for getting the member logs and their aggregations.

**REDIS**

**MAIL**
- `JavaMailSender` in the `MailService` is used to send mails about notifications.
- `MailService` listens an active mq queue about notifications and then send mails to
  the related members.

**SCHEDULING**
- `TaskSchedulerConfig` is the configuration file of the scheduling.
- It sets the number of threads that runs for scheduling tasks.
- `MemberEventsHandler` uses scheduling jobs for saving logs and aggregations.
- `NotificationService` uses scheduling job for processing notifications.

**JMS WITH ACTIVEMQ**
- Notifications whose time to send attribute is before the current time are processed
  in `NotificationService`.
- These notifications are sent into active mq queue.
- Then they are deleted from redis cache.
- `MailService` listens that queue and it simply send mails to the members.

**BATCH**
- spring.jpa.properties.hibernate.jdbc.batch_size is used 
- saveAll method of LogAggregationService uses that property while inserting multiple entities into the database
at one go.

#
#  
# **OTHERS**

**LOMBOK**
- Lombok is used in the project
- The basic annotations used are;
    - Slf4j
    - Getter, Setter
    - NoArgsConstructor, AllArgsConstructor
    - ToString
    - Builder

**MAPSTRUCT**
- `mapper` package contains all the mapping files.
- `CyclePreventiveContext` is used for preventing mapping cycles from one class into another
  if mapping in both sides exist.
- `AfterMapping`, `BeforeMapping` annotations and `default` methods are used.
- If we need another component in a mapper file, we can use decorators with `DecoratedWith` annotation.
- In book reserving and book loaning, decorators are used.
- While mapping notifications, since the value getting from redis is a map, `AfterMapping` annotation and reflection
api are used to map it into the entity.

**JACKSON**

**SWAGGER**
- Swagger 2 is used in the project and its config is defined in `SwaggerConfig`.
- `Docket` beans are created using bean factory according to the methods with `PreAuthorize` annotation.
- The roles used in this annotation is the group of the docket while the paths of the methods are the paths of the docket.
- `swagger/model` package contains the api model files for swagger.
- `swagger/controller` package contains the api interfaces for swagger.
- The rest controllers implementing swagger api interfaces are added to the swagger documentation.
- `AuthenticationSwagerApi` is used for adding auth to swagger documentation. Since the login methods are called by Spring
Security, the methods here are not actually called.

**DOCKER**
- `Dockerfile` is the main file for dockerizing the entire app.
- It uses a base library for java and it takes some environment variables.

**DOCKER COMPOSE**

**MAVEN**

#
#
# **DESIGN PATTERNS**

**BUILDER**

**FACTORY**

**DECORATOR**

**STATE**

**TEMPLATE**

**CHAIN OF RESPONSIBILITY**

#
#
# **PROFILES**
Multiple profiles exist in the project depending on the intended purpose.

### **TEST**
- This profile is used for all tests under `com.sahin.library_management` package.
- It uses an H2 database.
- Bootstrap loader runs before the tests.
- According to the data that bootstrap classes loads into in memory H2 database, tests run.
- You need to run the containers inside docker-compose file in order to run tests successfully. (except the Spring app itself)
- AMQP is not enabled by default so that member logs are not save into mongo db.

### **SWAGGER**
- This profile is used for swagger api definitions.
- It uses `data-sim.sql` while loading data into in memory H2 database.

### **DEV**
- This profile is used for development environment.
- It can use either bootstrap loaders or `data-sim.sql` while loading data into in memory H2 database
depending on whether `spring.datasource.platform = sim` line exists or not in `application-dev.yml` file.
- It can use either basic or jwt authentication depending on the value of `security.authentication.basic` in yml file.

### **PROD**
- This profile is for production environment.
- It uses jwt authentication by default.
- It uses MySQL database.

# **LINKS**
- Website with Thymeleaf: http://localhost:7001/model
- Swagger UI: http://localhost:7001/swagger-ui/index.html
- H2-Console: http://localhost:7001/h2-console/login 
  (The credentials are `root` for user name and `password` for password)
- RabbitMQ UI: http://localhost:15672
  (The credentials are `guest` for username and password)
- ActiveMQ UI: http://localhost:8161/admin
  (The credentials are `root` for user name and `password` for password)
