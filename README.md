# JDBC

## 1.JDBC concept

JDBC (Java Database Connectivity) is an **independent of a specific database management system, common SQL database access and manipulation of the public interface** (a set of API), defines the standard Java class library used to access the database, (**java.sql,javax.sql**) using these libraries can be a **standard ** method, easy access to database resources.

#### **Structure of JDBC** 

<img src="/Users/yuimorii/Documents/GitHub/JDBC/img/main-qimg-721b04a64b3da53bd4662bda5358015f.webp" alt="main-qimg-721b04a64b3da53bd4662bda5358015f" style="zoom:70%;" />

####  JDBC program writing steps

### 

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
