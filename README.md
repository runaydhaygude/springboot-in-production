

### Installing SDKMan
to control java versioning

### Install Docker

### Install VirtualBox
was not able to install on Mac M2

### gradle commands
1. ./gradlew clean build
2. java -jar build/libs/<jar-name>.jar - click TAB after '/build/libs/' to automatically load jar name
3. java -jar -Dspring.profiles.active=dev build/libs/<jar-name>.jar - to externalize and set active spring profile

### Environment profiles using Spring Profiles
@Profile("production") to limit the files - externalized and environment specific
Maintainability-
application.properties or application.yml
adding suffix creates profile- application-{profile}.properties
application-dev.properties or application-test.properties or application-prod.properties

One more properties files with all profiles
spring.config.activate.on-profile=dev


### Environment Object provided by springboot
@Autowired the Environment variable
environment.getActiveProfiles()


### Reference config from application.properties
you can reference config using @Value()
adding config in common application.properties will make an affect on all profiles

### Enhancing Spring Profiles with beans
We need beans to configure DBs
1. annotate the class with @ConfigurationProperties("spring.datasource")
2. in application-.properties - match the property names after spring.datasource with private member variables in the class
3. Create configuration file wit @EnableConfigurationProperties(DBConfiguration.class) tell spring to load DBConfiguration class
4. now we write method to get db connection for different profile with @Profile()
5. The method will execute as per the profile set active when runing the jar file

### Spring Boot Actuator
1. add sprinboot-stater-actuator dependency
2. now hit localhost:8080/actuator/health on browser
3. we can have actuator on different port for better isolation using
   management.server.port=9001
   management.endpoint.health.show-details=always
4. other actuator features: https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints
5. use management.endpoints.web.exposure.include=health,info,metrics,loggers
   to bypass security for these actuator endpoints

### Custom Health Check
when a service A dependent on another service B. If service B is down then we have to alert the health of the Service A
1. create a class with @Component implementing HealthIndicator and implement health() abstract method