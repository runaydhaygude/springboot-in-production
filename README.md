

### Installing SDKMan
to control java versioning

### Install Docker

### Install VirtualBox
was not able to install on Mac M2

### gradle commands
1. ./gradlew clean build
2. java -jar build/libs/<jar-name>.jar - click TAB after '/build/libs/' to automatically load jar name
3. java -jar -Dspring.profiles.active=dev build/libs/<jar-name>.jar - to run with a profile

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

