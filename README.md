# Rank's List

## Overview


## Installation
### Database Setup
Before running the application, a database must be created. The following details the necessary steps to accomplish this:
1. **Install MySQL on your machine**  
   Navigate to MySQL Community Downloads (https://dev.mysql.com/downloads/mysql/) and choose your operating system from the dropdown menu. Download the desired installer/ZIP/TAR,etc.

2. **Log in with root user**  
   Once MySQL has been installed on your machine, open up a command-line prompt and type 'mysql -u root'.

3. **Create the database**  
   Once logged in to the root user, create the database for the application with the following command:

   *CREATE DATABASE ranks_list;*
   
4. **Create new user for the database**  
   Now that the database has been created, a user needs to be set up to access the database while the application is running. Run the following commands:

   *CREATE USER 'rl_user'@'localhost' IDENTIFIED BY 'RanksList';  
   CREATE USER 'rl_user'@'%' IDENTIFIED BY 'RanksList';  
   GRANT ALL PRIVILEGES ON ranks_list.* TO 'rl_user'@'localhost' WITH GRANT OPTION;  
   GRANT ALL PRIVILEGES ON ranks_list.* TO 'rl_user'@'%' WITH GRANT OPTION;*

5. **Exit the database**  
   The database for the application has been created, as well as a user to access the database. To exit the database, type 'exit' into the command prompt.
   
### Application Startup
Now that the database has been set up with MySQL, the application can be started successfully.

1. **Clone the GitHub repository**  
   Copy the application project onto your local machine with the following command:

   *git clone https://github.com/rjohnson05/RanksList.git*

2. **Navigate to the root directory of the project if not there already**  

3. **Test the application** (Optional)  
   To run all tests created to ensure the correct operation of the application, use the following command:

   *Windows: ./gradlew test  
   MacOS: ./grade test*

3. **Start the application with Gradle**  
   Start the application with the following command:
   
   *Windows: ./gradlew bootRun  
   MacOS: ./grade bootRun*

4. **Navigate to http://localhost:8080 in your Internet browser to use the application**

## Usage
This app is meant to help users achieve goals in order to set them on the right path to buy the advertised items.

### Login

This app requires all users to login in order to use the app.
You must first register by clicking the register button which will
allow you to create an account. Your username must be between 6 - 32 characters and your
password must be between 8 - 16 characters, contain one uppercase 
letter, and contain a special character.

Once you have successfully registered, you are able to login to 
the app by entering your credentials on the login page and
clicking submit.

### Advertisements
Once logged in, you are taken to the home page. This page will
show you available advertisements. These were posted by other
users and are available for you to create goals for. You also 
have the ability to save ads by clicking the star button. This
adds the saved ad to your "starred ads" which you can access
through the navigation bar at the top.

Along with viewing ads, you can also create them. To do this, 
click on the "Create Ad" tab on the navigation bar. This will
bring you do a page where you have the ability to post an ad
to the app. You must add a name for the ad, and have the option
to add a price and description as well. Click submit when all
the information is accurate. 

This will bring you back to the home page where you created
ad will be displayed. This also adds the ad to your "Created Ads"
tab on the navigation bar. Clicking the "Created Ads" tab on the 
navigation bar will bring up all your created ads. Here you have 
the ability to edit and delete your ad.

### Goals

Goals can be added to each ad and are meant to help you
get ahead financially or otherwise and help you buy whatever
is being advertised. To add a goal to an add, click on the ad
and then click on create a new goal. This will bring you to the
"Add New Goal" page where you will add the description of your
goal and click submit. Your goal will now show up underneath 
the ad it is attached to. You can add more goals or delete existing
goals by clicking the garbage can icon on each goal.

### Profile

The "Profile" tab in the navigation bar allows for two options:
Change Password and Logout. To change your password, click on the 
change password dropdown item. This will bring you to a page
that allows you to change your password. First, you must verify
your old password, then choose a new password. The same requirements
for the password apply for the new password.

The second option is logout. You should always logout of your
account so your account and information is secure.