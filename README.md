# README

Assignment 2A: API Automation
1. Go to this web app: https://todoist.com/. Create an account, get access tokens.
2. Access to todoist API documentation:  https://developer.todoist.com/rest/v1
3. Create all the needed test case for Create, Get, Update, Get All Project
4. Using any library on Java (Rest Assured,..) to automate those test cases


Assignment 2B: API/Web Automation
1. Same setup steps with Assignment 2A.
2. Create A project through API
3. Create a Task belonging to the above project.
4. Using browser Go to and login to https://todoist.com/  
5. Go to the created Project > Verify the task was created through Web UI
6. Click on the Task Checkbox > Verify Task was not displayed any more
7. Call the Reopen API.
8. Verify the task is opened again on Web UI

### Test Automation with Java
#### Project using IntelliJ IDEA Community, Java, Maven, TestNG, Selenium and Page Object Model (POM)

### Libraries and Frameworks
#### Selenium - Web automation
#### Maven 3.8.5 - Build and package management: https://maven.apache.org/install.html
#### TestNG - Test execution and Reporting

### Tools
#### Using IntelliJ IDEA Community: https://www.jetbrains.com/idea/download/#section=windows

### Reporting
#### Allure Framework is used as a reporting tool. Report data will be placed in folder target/allure-results/  (can change it in file allure.properties)

### Programming Language
#### Using Java JDK: https://www.oracle.com/java/technologies/downloads/

### Run
#### mvn test