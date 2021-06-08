**Library Management System** is a project that aims to practice
Spring modules and its specific features as many as possible. In addition,
it contains some useful non-Spring libraries. Below, you can find
the main titles of these modules, in which features of these are used
and in which classes they are used.

**LOMBOK**
- Lombok is used in the project
- The basic annotations used are;
    - Slf4j
    - Getter, Setter
    - NoArgsConstructor, AllArgsConstructor
    - ToString
    - Builder

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

**MAPSTRUCT**

**JACKSON**

**VALIDATION**

**SECURITY**

**EXCEPTION HANDLING**

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
    

**SWAGGER**

**DOCKER**

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

**DOCKER COMPOSE**

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

**MAVEN**

**SCHEDULING**

**JMS WITH ACTIVEMQ**
- Notifications whose time to send attribute is before the current time are processed
in `NotificationService`.
- These notifications are sent into active mq queue.
- Then they are deleted from redis cache.
- `MailService` listens that queue and it simply send mails to the members.

**BATCH**

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