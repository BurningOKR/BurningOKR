# Introduction 
TODO: Give a short introduction of your project. Let this section explain the objectives or the motivation behind this project. <br> 
The okr project is an application in which you can define objectives and their key results in a company.
Objectives are aims for companies & their departments to evolve. The motivation behind this ist 
the java academy of the Brockhaus AG. Its own objective is to teach new employes the basics
and the knowledge for real, customer orientated projects.

# Getting Started
TODO: Guide users through getting your code up and running on their own system. In this section you can talk about:
1.	Installation process
2.	Software dependencies
3.	Latest releases
4.	API references

# Build and Test
TODO: Describe and show how to build your code and run the tests. <br>

# Standalone version
To run standalone .jar use:

`java --add-modules java.xml.bind -jar .\okr-tool-app-0.0.1-SNAPSHOT.jar`

see for more details:
https://stackoverflow.com/questions/43574426/how-to-resolve-java-lang-noclassdeffounderror-javax-xml-bind-jaxbexception-in-j

#Postgres
The actual application uses PostgreSQL (or Postgres) as database. The actual used version ist PostgreSQL version 9.5.
After installing the database software, use the software tool pgAdmin III and create a new database with the name 
'okr'. <br>
To use the default database access create a new login-role with name admin and password admin.
For a specified login, create a new login-role and pass the username (db.user) and the password (db.password) as run arguments.
<br>
<br>
Just start the application, the tables for the database will be created automatically by PostgreSQL. 

##Postgres Backup
On the Hostserver (javatrainingvm.westeurope.cloudapp.azure.com) you can find scripts (batch.files) to create a backup of the current state from the database.
The scripts to create and restore the backup are located in C:\DB-Backup, the actual backup files are uncompressed. 
The Backup automatically starts every night at 00:00 AM and stores the Backup in the "Backup" folder in C:\DB-Backup.
To restore a backup uppon the database, run the db_install_backup.bat in the command prompt with the wanted backup.sql file as first parameter.


# Contribute
TODO: Explain how other users and developers can contribute to make your code better. 

If you want to learn more about creating good readme files then refer the following [guidelines](https://www.visualstudio.com/en-us/docs/git/create-a-readme). You can also seek inspiration from the below readme files:
- [ASP.NET Core](https://github.com/aspnet/Home)
- [Visual Studio Code](https://github.com/Microsoft/vscode)
- [Chakra Core](https://github.com/Microsoft/ChakraCore)
- [Chakra Core](https://github.com/Microsoft/ChakraCore)