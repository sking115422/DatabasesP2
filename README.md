# CSCI X370 Databases Project 2

This is a front end UI to help manage the sample SQL employees data base. The goal is to find 1st and 2nd degrees of seperation between coworkers that worked in the same department at the same time.

## Getting Started

1. Make sure that Java SE Development Kit (JDK) is installed on the computer where this program will be run
2. Make sure that your enironment variable are properly set up
    * Here is a link that explains 1 & 2 well: [link](https://www.codejava.net/java-core/how-to-write-compile-and-run-a-hello-world-java-program-for-beginners#:~:text=In%20order%20to%20write%20and,Java%20programs%20on%20your%20computer)
3. Make sure that SQL and MySQL workbench are installed on your computer
    * Here is a link that explains 3 well: [link](https://www.youtube.com/watch?v=WuBcTJnIuzo&t=582s)
4. Put the sample database "employees" into SQL. This is database is included in the "extra" folder of this repository under "test_db", and the database file is called "employees.sql".
    * Here is a link that explains 4 well: [link](https://www.youtube.com/watch?v=qHjvGAMPzEw)
5. Make sure the CLASSPATH is properly set for the "mysql-connector-java-8.0.23" java file. The connectorJ jar file is located in the "lib" folder.
6. Run code and properly enter your SQL server infromation when promted to connector to the local server on your machine
    * The url should look something like: jdbc:mysql://localhost:3306/employees
    * The user should look somthing like: root
    * The password is unique and may or may not be required
    * These may change depending on how local server is set up on used machine
7. At this point, the code should be running and you can follow the prompts to test


## Dependencies
- JDK
- SQL
- MySQL
- Connector J (Type 4 JDBC driver for connecting to SQL databases)
- Possibly IDE but not required (Eclipse or IntelliJ)

## Authors

- Spencer King
- Daniel Abramow
- Shreenal Patel
