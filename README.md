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

1. Create an AWS account and log in to the management console.
2. Create an AWS-managed IAM role for the EC2 instance.
3. In Elastic Beanstalk Console, click on “Create Application.”
4. Configure the environment by setting a specific environment name.
5. Upload the Spring Boot JAR file.
6. Keep networking, database, and traffic configuration as default.
7. In step 5 configuration, set “SPRING_PROFILES_ACTIVE” to “dev” and “SERVER_PORT” to 5000.
8. Click “Submit” and wait for the application to launch.
9. Once launched, use the provided domain URL to access the application.
10. To switch from Dev to Prod, go to Configuration,
    update the “SPRING_PROFILES_ACTIVE” variable, click Apply, and Refresh.
11. To undeploy the application, click on the Environments tab, then select your environment,
    then click on the Actions button, and then click on Terminate Environment.
12. And then delete the application by going to the Applications tab.
13. When deploying SpringBoot War file, make sure to select Tomcat as a platform when creating an environment in Elastic
    Beanstalk.
14. Also, when deploying war file on Tomcat platform, make sure to add the spring.profiles.active environment variables
    in JVM options in platform software configurations for the application to work.

### Installing Elastic Beanstalk CLI

1. Deploying a Docker image on AWS Elastic Beanstalk involves additional steps compared to previous videos.
2. Use AWS Elastic Beanstalk Command Line Interface (EB CLI) for deployment.
3. On macOS, using the EB CLI can present challenges, so a Vagrant instance is recommended for consistency.
4. Create a docker-compose.yml file to set up parameters for AWS EB CLI.
5. Edit the application.properties file to comment out server.port and management port.
6. make a directory using - ~/vagrant_amzn2
   And create a Vagrant instance using `vagrant init gbailey/amzn2`
   (Few vagrant commands-
   1. `vagrant box list` - list of vagrant instances
   2. `vagrant status` - to check if a instance is running
   3. `vagrant ssh` - to ssh into the running instance
   )
   Zip the project using `zip -r ~/vagrant_amzn2/SpringBootProduction.zip SpringBootProduction`.
7. Start the Vagrant instance with `vagrant up` (make sure Virtual Box is installed - the beta version is required).
   make sure `vagrant-scp` plugin is installed to copy your zip from your directory to vagrant instance
   use this command to install the plugin `vagrant plugin install vagrant-scp`
   copy the zip to the Vagrant instance using `vagrant scp SpringBootProduction.zip SpringBootProduction.zip`
8. SSH into the instance using ` vagrant ssh` command,
   unzip the project using `unzip SpringBootProduction.zip`,
   and update the system using `sudo yum update -y`.
   (yum is the package management utility used on RPM-based Linux distributions)
9. Install dependencies for AWS EB CLI
   using `sudo yum install -y zlib-devel openssl-devel ncurses-devel libffi-devel sqlite-devel.x86_64 readline-devel.x86_64 bzip2-devel.x86_64 unzip git`.
10. Clone AWS EB CLI setup scripts from GitHub
    using `git clone https://github.com/aws/aws-elastic-beanstalk-cli-setup.git`.
    install necessary dependency- `sudo yum install python3-pip`
    `sudo pip3 install virtualenv`
11. Run the AWS EB CLI setup script, which may take some time
    using `python ./aws-elastic-beanstalk-cli-setup/scripts/ebcli_installer.py`.
    [ ABOVE DIDN'T WORK ]
    install necessary dependency- `sudo yum install python3-pip`
    To install AWS EB CLI - `sudo pip3 install --upgrade --user --no-cache-dir awsebcli`
12. Verify EB CLI installation with eb --version.
13. Now ready to use AWS EB CLI for deploying Docker images.

### Deploying docker image on elastic beanstalk using eb cli

1. Make sure AWS Elastic Beanstalk Command Line Interface (EBCLI) is installed.
2. Ensure Vagrant is started. Run `sudo yum install ntp ntpdate -y` and then `sudo ntpdate pool.ntp.org` to sync system
   time.
   Time synchronization in virtual machines (VMs) is vital for ensuring accurate timestamps on logs, files, and system
   activities. It plays a key role in security protocols, preventing replay attacks, and facilitating proper
   authentication
   and authorization mechanisms that rely on time-based tokens. In distributed systems, synchronized time is essential
   for coordinating actions among different servers and services. It also aids in troubleshooting by providing
   consistent
   logging and event correlation across multiple systems. Overall, time synchronization is crucial for maintaining a
   secure and well-coordinated computing environment.
3. Verify that the "SpringBootProduction" zip is unzipped. Navigate to the directory using `cd SpringBootProduction`.
4. Run `eb init` to configure the project for Elastic Beanstalk.
5. Select default region (e.g., US east 2). Provide AWS access ID and secret access key obtained from AWS console.
6. Choose application or create a new one. Confirm Docker usage.
7. Run `eb create` for default deployment. Accept DNS and CNAME prefix. Choose load balancer and optional spot fleet
   request.
   Observe EBCLI output for source and Docker image upload progress (make sure you navigate to the correct region to see
   the progress).
8. Monitor progress in AWS Elastic Beanstalk on the AWS Console. Review logging statements for insights into Elastic
   Beanstalk actions.
9. Address health issues by setting environment variables. Use `eb setenv SPRING_PROFILES_ACTIVE=dev,beanstalk`.
10. Navigate to AWS Console, go to configuration, and edit. Add server port (e.g., 5000) and click apply.
11. Check the AWS console for updated health status and environment details. Ensure the application is visible in the
    AWS environment.

### Continuous Deployment (Intro)

1. Continuous Deployment (CD) Process: Continuous deployment is a software release process that utilizes automated
   testing to validate changes in a code base for immediate autonomous deployment to a production environment.
2. GitHub Actions for Workflow Automation: GitHub actions enable the automation of workflows, allowing the creation of
   custom workflows tailored to team needs.
3. Published Actions in GitHub: GitHub provides a wide selection of published actions that users can consume in their
   projects, including professionally prewritten actions for various tasks.
4. Event Types Triggering Workflows: GitHub actions depend on events occurring in a code repository, such as commits,
   merges, deletes, forks, pushes, issue creation, etc.
5. Workflow in GitHub: In GitHub, a workflow is a set of specified GitHub actions that are executed based on predefined
   events in the code repository.
6. GitHub and GitLab for DevOps Automation: Both GitHub and GitLab offer developer tools to automate the DevOps process,
   eliminating the need for a dedicated DevOps engineer.
7. Freedom from Third-Party Server Tools: GitHub and GitLab allow users to automate the DevOps process without relying
   on third-party server tools like Jenkins, Travis-CI, Bamboo, and others.
8. Creating a GitHub Action: To create a GitHub action, users can access the "Actions" link, click on "New Workflow" button, where they can find and
   consume pre-built professional actions for their projects.
9. Setting Up a Java with Gradle Action: Users can set up specific workflows, such as the "Java with Gradle" action, by
   selecting and configuring pre-built actions available in GitHub.
10. Executing the Workflow: The workflow can be triggered by specific events, such as pushes or pulls to the main
    branch, leading to the execution of build jobs, tests, and other defined tasks.
11. Monitoring Workflow Progress: Users can monitor the progress of a workflow in the GitHub Actions section, observing
    the success or failure of each step in the process.
12. Alerts for Workflow Failures: GitHub sends email alerts in case of workflow failures, providing timely notifications
    to address any issues in the deployment process.
13. First GitHub Action Completed: By following the steps, users can create their first GitHub action, demonstrating the
    successful automation of a testing and building process in the workflow.

### Configuring GitHub Actions to build springboot artifact
1. Create a new branch named "jar_workflow_aws" using Git. Push this new branch to your GitHub repository. This
   establishes version control and collaboration.
2. Open the GitHub workflows gradle file and rename it to "build-package-deploy.aws" for clarity. Commit this change to
   version control.
3. Add GitHub actions for creating a timestamp and making the artifact available for download. The timestamp aids
   versioning and tracking JARs in AWS. Use pre-built GitHub actions for these steps.
4. Test the YAML file against the "jar_workflow_aws" branch by temporarily modifying the branch name in the workflow
   file. Ensure proper formatting and set a retention period for artifacts.
5. Push the changes to the "jar_workflow_aws" branch on GitHub. Monitor the GitHub Actions workflow execution to verify
   the successful creation of artifacts with timestamps.

### Explaining gradle.yml in /github/workflow
1. name: Specifies the name of the workflow, which is "Java CI with Gradle."
2. on: Defines the events that trigger the workflow.
3. Triggered on push events to the branch "jar_workflow_aws."
4. Triggered on pull requests to the branch "one."
5. permissions: Specifies the level of access to repository contents needed for the workflow. In this case, it requires
   read access.
6. jobs: Defines the list of jobs to be executed.
7. build: Describes the build job.
8. runs-on: Specifies the operating system for the job, in this case, "ubuntu-latest."
9. steps: Lists the individual steps to be executed in the job.
10. actions/checkout@v3: Checks out the repository code.
11. actions/setup-java@v3: Sets up JDK 11 using the Temurin distribution.
12. run: ./gradlew build: Builds the project using Gradle.
13. nanzm/get-time-action@v1.1: Creates a timestamp for version information.
14. actions/upload-artifact@v2: Uploads the built JAR artifact to GitHub.
15. name: Specifies the name of the artifact using a timestamp.
16. path: Specifies the path to the JAR file.

### Deploying jar file to elastic beanstalk with GitHub Actions
1. Deploying a JAR file to Elastic Beanstalk using GitHub Actions involves several steps for setup and configuration.
2. Begin by navigating to the AWS console and accessing security credentials to obtain the AWS access key ID and secret
   access key.
3. To ensure a clean slate, terminate existing environments and delete applications in Elastic Beanstalk.
4. Create a new application named "SpringBootProdApp" with the Java platform and Corretto 11.
5. Configure environment properties by setting server_port to 5000 and SPRING_PROFILES_ACTIVE to "prod."
6. Create GitHub secrets for AWS access key ID and secret access key in the repository settings.
7. Load the GitHub workflow file named "beanstalk-deploy" in IntelliJ and make necessary adjustments.
8. Configure the workflow with AWS access key ID, secret access key, application name, environment name, version label,
   region, and deployment package details.
9. Change the action trigger back to the main branch.
10. Save and push the changes to the GitHub repository, create a pull request, and merge it.
11. Monitor the GitHub Actions workflows to ensure successful execution.
12. Check the Elastic Beanstalk environment to view updates and verify the application deployment.
