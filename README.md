

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

### Packaging Strategy
1. Uber-Jar Packaging Strategy
2. War Packaging Strategy - can execute like jar. Deploy to traditional web application servers, problems with
   shared class path, Multiple deployment within same tomcat. 
3. RPM distro - Encapsulates uber jar. Control start and stop of Linux service like using
   systemctl (command-line utility in Linux-based operating systems).
   Assign roles and restarted by listener agents
   You have to match the linux version where the RPM is build and where it is deployed. This can be handled through CI/CD and build agents
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
4. The purpose of catalina.properties is to configure various system-wide properties for the Apache Tomcat servlet container.
5. click 'i' to insert - spring.profiles.active=prod
6. click 'esc', then ':w' to save and then :qa to quit the file
7. execute bin/startup.sh (like wise execute bin/shutdown.sh when done)


### Vim Editors
vim - To enter the Vim text editor, open a terminal and type vim, followed by the name of the file you want to edit. For example: vim filename.
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
2. 'bootWar': This is a Gradle task provided by the Spring Boot plugin. It's used to package your Spring Boot application as
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