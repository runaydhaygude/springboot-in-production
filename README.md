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
2. in application-.properties - match the property names after spring.datasource with private member variables in the
   class
3. Create configuration file wit @EnableConfigurationProperties(DBConfiguration.class) tell spring to load
   DBConfiguration class
4. now we write method to get db connection for different profile with @Profile()
5. The method will execute as per the profile set active when runing the jar file

### Spring Boot Actuator

1. add sprinboot-stater-actuator dependency
2. now hit localhost:8080/actuator/health on browser
3. we can have actuator on different port for better isolation using
   management.server.port=9001
   management.endpoint.health.show-details=always
4. other actuator
   features: https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints
5. use management.endpoints.web.exposure.include=health,info,metrics,loggers
   to bypass security for these actuator endpoints

### Custom Health Check

when a service A dependent on another service B. If service B is down then we have to alert the health of the Service A

1. create a class with @Component implementing HealthIndicator and implement health() abstract method

### Packaging Strategy

1. Uber-Jar Packaging Strategy
2. War Packaging Strategy - can execute like jar. Deploy to traditional web application servers, problems with
   shared class path, Multiple deployment within same tomcat.
3. RPM distro - Encapsulates uber jar. Control start and stop of Linux service like using
   systemctl (command-line utility in Linux-based operating systems).
   Assign roles and restarted by listener agents
   You have to match the linux version where the RPM is build and where it is deployed. This can be handled through
   CI/CD and build agents
4. Docker Packaging strategy -
   Embed a jar or war file with app server
   Application and configuration are containerized
5. Native Image packaging strategy-
   Under development with Spring Native
   Quickly create and deploy

### Install Tomcat

Faced issue deploying war with tomcat 10+. It seems higher spring-boot version is needed

1. brew install tomcat@9
2. cd /opt/homebrew/opt/tomcat@9/libexec
3. vi conf/catalina.properties : to edit the file
4. The purpose of catalina.properties is to configure various system-wide properties for the Apache Tomcat servlet
   container.
5. click 'i' to insert - spring.profiles.active=prod
6. click 'esc', then ':w' to save and then :qa to quit the file
7. execute bin/startup.sh (like wise execute bin/shutdown.sh when done)

### Vim Editors

vim - To enter the Vim text editor, open a terminal and type vim, followed by the name of the file you want to edit. For
example: vim filename.
vi - same as vim
i - Enter Insert mode before the cursor position.
a - Enter Insert mode after the cursor position.
Esc - Exit Insert mode and return to Normal mode.
:w - Save changes made to the file.
:q - Quit Vim.
:wq - Save changes and quit Vim.
:q! - Quit Vim without saving changes (force quit).
yy - Copy (yank) the current line.
p - Paste the copied/yanked text after the cursor.
dd - Delete (cut) the current line.
u - Undo the last change.
Ctrl-R - Redo (opposite of undo).
/pattern - Search for a pattern in the file.
n - Move to the next occurrence of the search pattern.
N - Move to the previous occurrence of the search pattern.
:set number - Display line numbers in the left margin.
:set nonumber - Hide line numbers.
:e filename - Open a new file (discard changes to the current file).
:sp filename - Split the window horizontally to edit a new file.
:vsp filename - Split the window vertically to edit a new file.
Ctrl-W Ctrl-W - Switch between split windows.
:q - Close the current split window.
:help - Open the Vim help documentation.
:q - Close the help documentation.

### deploying war using tomcat

1. in 'build.gradle' mention id 'war' to apply war plugin
2. 'bootWar': This is a Gradle task provided by the Spring Boot plugin. It's used to package your Spring Boot
   application as
   a WAR file (Web Application Archive) that can be deployed to a servlet container.
   'archiveFileName': This line specifies the name of the WAR file that will be generated when you run the bootWar task.
   In this case, it sets the WAR file's name to 'SpringBootProdApplication.war'.
3. add tomcat in provided scope-
4. Scope: providedRuntime: The providedRuntime scope is typically used in Java projects, especially in web applications.
   When you declare a dependency with this scope, it means that you expect the dependency to be provided by the runtime
   environment where your application is deployed, and it should not be packaged with your application. In the context
   of a Spring Boot web application, this often means that you're expecting the Tomcat server to be provided by the
   servlet container, such as when deploying to a servlet container like Apache Tomcat.
5. extend the main class to SpringBootServletInitializer, s used to configure how your Spring Boot application should be
   deployed within a servlet container.
6. Overide the method -
   return builder.sources(SpringBootProdApplication.class);: In this line, you are configuring the Spring
   SpringApplicationBuilder to use your SpringBootProdApplication class as the source of your Spring Boot application.
   This tells Spring Boot which class contains the configuration and entry point for your application.
7. add servlet context path in application properties
8. build the war using gradle command
9. copy the war file to tomcat's webapps -
   cp build/libs/SpringBootProdApplication.war /opt/homebrew/opt/tomcat@9/libexec/webapps
10. start tomcat if not started already and then print logs using following command-
    tail -f /opt/homebrew/opt/tomcat@9/libexec/logs/catalina.out

### packaging application in docker

1. Ensure Docker is running before proceeding.
2. Create a Dockerfile with instructions:
    1. `FROM openjdk:8-jdk-alpine`: Specifies the base image as OpenJDK 8 with Alpine Linux, which is a lightweight
       distribution.
    2. `MAINTAINER runaysolutions.com`: Identifies the maintainer of the Dockerfile or image. In this case, it is set
       to "runaysolutions.com."
    3. `RUN addgroup -S mygroup && adduser -S myuser -G mygroup`: Creates a non-root user (`myuser`) and a corresponding
       group (`mygroup`). The `-S` flag indicates that the group and user should be system accounts.
    4. `USER myuser:mygroup`: Sets the user and group for subsequent commands to `myuser` and `mygroup`.
    5. `ARG JAR_FILE=build/libs/SpringBootProduction-0.0.1-SNAPSHOT.jar`: Defines an argument (`JAR_FILE`) for the
       location of the Spring Boot application JAR file. The default value is set to the specified path.
    6. `COPY ${JAR_FILE} app.jar`: Copies the JAR file specified by the `JAR_FILE` argument to the Docker image with the
       name "app.jar."
    7. `ENTRYPOINT ["java", "-jar", "app.jar"]`: Specifies the command that will be executed when the Docker container
       starts. It runs the Java application using the specified JAR file ("app.jar"). The `ENTRYPOINT` is an array,
       allowing for additional arguments to be passed when the container is run.
3. Save the application and run `gradle clean build` in the terminal.
4. Build the Docker image using `Docker build -t springboot prod image .`
5. Run the Docker container with `Docker run -d -p 9090:9000 -e SPRING_PROFILES_ACTIVE=prod springboot prod image`.
6. Stop the current Docker instance if needed with `Docker container stop [container ID]`.
7. Alternatively, use the Spring Boot Gradle plugin to build the Docker image (incompatible with Gradle 9.0)
   with `gradle w bootBuildImage --imageName=MDR solutions/springboot-prod-image`.
8. The Spring Boot Gradle plugin can build a Docker image without a separate Dockerfile
9. Run the Docker image built with the plugin
   using `Docker run -p 9090:9000 -e SPRING_PROFILES_ACTIVE=prod -t [image name]`.

### GraalVM
GraalVM is a tooling platform for converting java applications into native applications.
It is a high-performance runtime that provides support for multiple languages and can run applications written in
languages such as Java, JavaScript, Python, Ruby, and others. It includes the GraalVM Compiler, which offers
ahead-of-time compilation for improved performance, and supports the execution of polyglot applications, allowing
different languages to be used together in a single application.

### Deploying the application AWS ElasticBeanStalk
1.	Create an AWS account and log in to the management console.
2.	Create an AWS-managed IAM role for the EC2 instance.
3.	In Elastic Beanstalk Console, click on “Create Application.”
4.	Configure the environment by setting a specific environment name.
5.	Upload the Spring Boot JAR file.
6.	Keep networking, database, and traffic configuration as default.
7.	In step 5 configuration, set “SPRING_PROFILES_ACTIVE” to “dev” and “SERVER_PORT” to 5000.
8.	Click “Submit” and wait for the application to launch.
9.	Once launched, use the provided domain URL to access the application.
10.	To switch from Dev to Prod, go to Configuration,
     update the “SPRING_PROFILES_ACTIVE” variable, click Apply, and Refresh.
11. To undeploy the application, click on the Environments tab, then select your environment, 
    then click on the Actions button, and then click on Terminate Environment.
12. And then delete the application by going to the Applications tab.
13. When deploying SpringBoot War file, make sure to select Tomcat as a platform when creating an environment in Elastic Beanstalk.
14. Also, when deploying war file on Tomcat platform, make sure to add the spring.profiles.active environment variables
    in JVM options in platform software configurations for the application to work.