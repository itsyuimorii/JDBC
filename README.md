# JDBC

## 1.JDBC concept

JDBC (Java Database Connectivity) is an **independent of a specific database management system, common SQL database access and manipulation of the public interface** (a set of API), defines the standard Java class library used to access the database, (**java.sql,javax.sql**) using these libraries can be a **standard ** method, easy access to database resources.

#### **Structure of JDBC** 



<img src="https://github.com/itsyuimorii/JDBC/blob/main/img/main-qimg-721b04a64b3da53bd4662bda5358015f.webp" alt="main-qimg-721b04a64b3da53bd4662bda5358015f" style="zoom:70%;" />

####  JDBC program writing steps

```
Step 1: Register the driver (role: tell the Java program which brand of database it is about to connect to)

Step 2: Get the connection (indicates that the channel between the JVM process and the database process is open, which belongs to the communication between processes, heavyweight, and must close the channel after use.)

Step 3: Get the database operation object (the object dedicated to the execution of sql statements)

Step 4: Execute SQL statements (DQL DML ....)

Step 5: process the query result set (only if the fourth step is executed when the select statement, there is this fifth step to process the query result set.)

Step 6: Release the resources (after using the resources must be closed. Java and the database belongs to the inter-process communication, after opening must be closed.)
```



## 2. Fetching database connections

#### Element I: Driver interface implementation class

- The java.sql.Driver interface is an interface that all JDBC drivers need to implement. This interface is provided to the database vendor for use and different database vendors provide different implementations.

- Instead of accessing the classes that implement the Driver interface directly in the program, the Driver Manager class (java.sql.DriverManager) will call these Driver implementations.
  - Oracle driver: **oracle.jdbc.driver.OracleDriver**
  - Driver for mySql: **com.mysql.jdbc.Driver**

##### Loading and Registering JDBC Drivers

- Load the driver: To load the JDBC driver, call the static method forName() of the Class class and pass it the class name of the JDBC driver to be loaded

  - **Class.forName("com.mysql.jdbc.Driver");**

- Register the driver: DriverManager class is the driver manager class, responsible for managing the driver
  -  use **DriverManager.registerDriver**(com.mysql.jdbc.Driver) to register the driver 

  - It is usually not necessary to explicitly call the registerDriver() method of the DriverManager class to register an instance of the driver class, because the Driver interface driver classes **all** contain a static code block in which the DriverManager.registerDriver() method is called to register an instance of itself. The following is the source code of MySQL's Driver implementation class.

#### Element 2: URL

- The JDBC URL is used to identify a registered driver, and the driver manager uses this URL to select the correct driver to establish a connection to the database.

- The standard JDBC URL consists of three parts, separated by a colon between each part. 
  - **jdbc:subprotocol:subname**
  - **protocol**: the protocol in the JDBC URL is always jdbc 
  - **subprotocol**: subprotocols are used to identify a database driver
  - **subname**: a way to identify a database. Subnames can vary depending on the subprotocol. The purpose of using subnames is to provide enough information to **locate the database**. Contains **host name** (corresponding to the ip address of the server)**, port number, database name**

#### Element 3: user name and password

- user,password can be told to the database by "property name=property value"
- You can call the getConnection() method of the DriverManager class to establish a connection to the database

#### Examples of database connection methods

```
connectionTest.java
```

## 3. Using PreparedStatement to implement CRUD operations

#### 3.1 Operating and accessing the database

- Database connections are used to send commands and SQL statements to the database server and to *receive results back from the database server*. In fact, a database connection is a **socket connection.**

- There are 3 interfaces in the java.sql package that define different ways to call the database.
  - **Statement:** An object used to execute a static SQL statement and return the result it generates. 
  - **PrepatedStatement**: SQL statement is pre-compiled and stored in this object, which can be used to execute the statement many times in an efficient way.
  - **CallableStatement**: Used to execute SQL stored procedures.

#### 3.2 Disadvantages of using Statement to manipulate data tables

- This object is created by calling the createStatement() method of the Connection object. This object is used to execute a static SQL statement and return the result of the execution.

- The following methods are defined in the Statement interface for executing SQL statements.

  ```sql
  int excuteUpdate(String sql): executes the update operation INSERT, UPDATE, DELETE
  ResultSet executeQuery(String sql): executes the query operation SELECT
  ```

- But there are disadvantages of using Statement to manipulate data tables.

  - **Problem 1: There is a spelling operation, tedious **
  - **Problem 2: There is a SQL injection problem**

- SQL injection is the practice of taking advantage of the fact that some systems do not check the user input data sufficiently and inject illegal SQL statement segments or commands into the user input data (e.g., SELECT user, password FROM user_table WHERE user='a' OR 1 = ' AND password = ' OR '1' = '1'), thus using the system's SQL engine to accomplish malicious behavior.

- For Java, to prevent SQL injection, simply replace Statement with PreparedStatement (which extends from Statement).

#### 3.3 Use of PreparedStatement

##### 3.3.1 Introduction to PreparedStatement

- A PreparedStatement object can be obtained by calling the **preparedStatement(String sql)** method of the Connection object

- **PreparedStatement interface is a sub-interface of Statement, which represents a pre-compiled SQL statement**.

- The parameters in the SQL statement represented by the PreparedStatement object are represented by question marks (?) The parameters in the SQL statement represented by the PreparedStatement object are represented by question marks (?), and the setXxx() method of the PreparedStatement object is called to set these parameters. The setXxx() method has two parameters, the first one is the index of the parameter in the SQL statement to be set (starting from 1) and the second one is the value of the parameter in the SQL statement to be set

##### 3.3.2  Java and SQL corresponding data type conversion table

| Java type          | SQL type                 |
| ------------------ | ------------------------ |
| boolean            | BIT                      |
| byte               | TINYINT                  |
| short              | SMALLINT                 |
| int                | INTEGER                  |
| long               | BIGINT                   |
| String             | CHAR,VARCHAR,LONGVARCHAR |
| byte   array       | BINARY  ,    VAR BINARY  |
| java.sql.Date      | DATE                     |
| java.sql.Time      | TIME                     |
| java.sql.Timestamp | TIMESTAMP                |


