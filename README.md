# Rank's List

## Overview


## Installation
### Database Setup
Before running the application, a database must be created. The following details the necessary steps to accomplish this:
1. Install MySQL on your machine
   Navigate to MySQL Community Downloads (https://dev.mysql.com/downloads/mysql/) and choose your operating system from the dropdown menu. Download the desired installer/ZIP/TAR,etc.

2. Log in with root user
   Once MySQL has been installed on your machine, open up a command-line prompt and type 'mysql -u root'.

3. Create the database
   Once logged in to the root user, create the database for the application with the following command:

   *CREATE DATABASE ranks_list;*
   
4. Create new user for the database
   Now that the database has been created, a user needs to be set up to access the database while the application is running. Run the following commands:

   *CREATE USER 'rl_user'@'localhost' IDENTIFIED BY 'RanksList';  
   CREATE USER 'rl_user'@'%' IDENTIFIED BY 'RanksList';  
   GRANT ALL PRIVILEGES ON ranks_list.* TO 'rl_user'@'localhost' WITH GRANT OPTION;  
   GRANT ALL PRIVILEGES ON ranks_list.* TO 'rl_user'@'%' WITH GRANT OPTION;*

5. Exit the database
   The database for the application has been created, as well as a user to access the database. To exit the database, type 'exit' into the command prompt.
   
### Application Startup
Now that the database has been set up with MySQL, the application can be started successfully.

1. Clone the GitHub repository
   Copy the application project onto your local machine with the following command:

   *git clone https://github.com/rjohnson05/RanksList.git*

2. Navigate to the root directory of the project if not there already

3. Test the application (Optional)
   To run all tests created to ensure the correct oepration of the application, use the following command:

   *Windows: ./gradlew test  
   MacOS: ./grade test*

3. Start the application with Gradle
   Start the application with the following command:
   
   *Windows: ./gradlew bootRun  
   MacOS: ./grade bootRun*

4. Navigate to http://localhost:8080 in your Internet browser to use the application

## Usage
