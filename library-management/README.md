**Library Management System** is a project that aims to practice
Spring modules and its specific features as many as possible. In addition,
it contains some useful non-Spring libraries. Below, you can find
the main titles of these modules, in which features of these are used
and in which classes they are used.

1. **LOMBOK**
2. **WEB MVC WITH REST**
3. **WEB MVC WITH THYMELEAF**
4. **DATA JPA**
5. **MAPSTRUCT**
6. **JACKSON**
7. **VALIDATION**
8. **SECURITY**
9. **EXCEPTION HANDLING**
10. **LOGGING**
11. **ASPECT ORIENTED PROGRAMMING**
12. **TESTING**
13. **AUDITING**
14. **SWAGGER**
15. **DOCKER**
16. **AMQP WITH RABBITMQ**
17. **DOCKER COMPOSE**
18. **MONGO DB**
19. **REDIS**
20. **MAIL**
21. **MAVEN**
22. **SCHEDULING**
23. **JMS WITH ACTIVEMQ**
24. **BATCH**

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
- Swagger UI: http://localhost:7001/swagger-ui/index.html
- H2-Console: http://localhost:7001/h2-console/login 
  (The credentials are `root` for user name and `password` for password)
- RabbitMQ UI: http://localhost:15672
  (The credentials are `guest` for username and password)
- ActiveMQ UI: http://localhost:8161/admin
  (The credentials are `root` for user name and `password` for password)