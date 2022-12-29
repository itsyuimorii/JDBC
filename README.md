# JDBC

# 1.JDBC concept

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



# 2. Fetching database connections

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

# 3. Using PreparedStatement to implement CRUD operations

#### 3.1 Operating and accessing the database

- Database connections are used to send commands and SQL statements to the database server and to *receive results back from the database server*. In fact, a database connection is a **socket connection.**

- There are 3 interfaces in the java.sql package that define different ways to call the database.
  - **Statement:** An object used to execute a static SQL statement and return the result it generates. 
  - **PrepatedStatement**: SQL statement is pre-compiled and stored in this object, which can be used to execute the statement many times in an efficient way.
  - **CallableStatement**: Used to execute SQL stored procedures.
  
  ![1566573842140](/Users/yuimorii/Documents/GitHub/JDBC/img/1566573842140.png)

#### 3.2 Disadvantages of using Statement to manipulate data tables

- This object is created by calling the **createStatement() method** of the **Connection object.** This object is used to **execute a static SQL statement and return the result of the execution.**

- The following methods are defined in the Statement interface for executing SQL statements.

  ```java
  int excuteUpdate(String sql): executes the update operation INSERT, UPDATE, DELETE
  
  ResultSet executeQuery(String sql): executes the query operation SELECT
  ```

- But there are disadvantages of using Statement to manipulate data tables.

  - **Problem 1: There is a spelling operation, tedious **
  - **Problem 2: There is a SQL injection problem**

- SQL injection is the practice of taking advantage of the fact that some systems do not check the user input data sufficiently and inject illegal SQL statement segments or commands into the user input data (e.g., SELECT user, password FROM user_table WHERE user='a' OR 1 = ' AND password = ' OR '1' = '1'), thus using the system's SQL engine to accomplish malicious behavior.

- For Java, to prevent **SQL injection,** simply replace Statement with PreparedStatement (which extends from Statement).

- ```java
  public class StatementTest {
  
  	// 使用Statement的弊端：需要拼写sql语句，并且存在SQL注入的问题
  	@Test
  	public void testLogin() {
  		Scanner scan = new Scanner(System.in);
  
  		System.out.print("用户名：");
  		String userName = scan.nextLine();
  		System.out.print("密   码：");
  		String password = scan.nextLine();
  
  		// SELECT user,password FROM user_table WHERE USER = '1' or ' AND PASSWORD = '='1' or '1' = '1';
  		String sql = "SELECT user,password FROM user_table WHERE USER = '" + userName + "' AND PASSWORD = '" + password
  				+ "'";
  		User user = get(sql, User.class);
  		if (user != null) {
  			System.out.println("登陆成功!");
  		} else {
  			System.out.println("用户名或密码错误！");
  		}
  	}
  
  	// 使用Statement实现对数据表的查询操作
  	public <T> T get(String sql, Class<T> clazz) {
  		T t = null;
  
  		Connection conn = null;
  		Statement st = null;
  		ResultSet rs = null;
  		try {
  			// 1.加载配置文件
  			InputStream is = StatementTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
  			Properties pros = new Properties();
  			pros.load(is);
  
  			// 2.读取配置信息
  			String user = pros.getProperty("user");
  			String password = pros.getProperty("password");
  			String url = pros.getProperty("url");
  			String driverClass = pros.getProperty("driverClass");
  
  			// 3.加载驱动
  			Class.forName(driverClass);
  
  			// 4.获取连接
  			conn = DriverManager.getConnection(url, user, password);
  
  			st = conn.createStatement();
  
  			rs = st.executeQuery(sql);
  
  			// 获取结果集的元数据
  			ResultSetMetaData rsmd = rs.getMetaData();
  
  			// 获取结果集的列数
  			int columnCount = rsmd.getColumnCount();
  
  			if (rs.next()) {
  
  				t = clazz.newInstance();
  
  				for (int i = 0; i < columnCount; i++) {
  					// //1. 获取列的名称
  					// String columnName = rsmd.getColumnName(i+1);
  
  					// 1. 获取列的别名
  					String columnName = rsmd.getColumnLabel(i + 1);
  
  					// 2. 根据列名获取对应数据表中的数据
  					Object columnVal = rs.getObject(columnName);
  
  					// 3. 将数据表中得到的数据，封装进对象
  					Field field = clazz.getDeclaredField(columnName);
  					field.setAccessible(true);
  					field.set(t, columnVal);
  				}
  				return t;
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		} finally {
  			// 关闭资源
  			if (rs != null) {
  				try {
  					rs.close();
  				} catch (SQLException e) {
  					e.printStackTrace();
  				}
  			}
  			if (st != null) {
  				try {
  					st.close();
  				} catch (SQLException e) {
  					e.printStackTrace();
  				}
  			}
  
  			if (conn != null) {
  				try {
  					conn.close();
  				} catch (SQLException e) {
  					e.printStackTrace();
  				}
  			}
  		}
  
  		return null;
  	}
  }
  ```

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

##### 3.3.4 Use PreparedStatement to add, delete and change operations

```java
	// common add, delete, change operations (embodiment of a: add, delete, change; embodiment of the second: for different tables)
	public void update(String sql, Object ... args){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 1. Get the database connection
			conn = JDBCUtils.getConnection();
			
			//2. Get an instance of PreparedStatement (or: pre-compiled sql statement)
			ps = conn.prepareStatement(sql);
			//3. Fill placeholders
			for(int i = 0;i < args.length;i++){
				ps.setObject(i + 1, args[i]);
			}
			
			//4. Execute sql statement
			ps.execute();
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			//5. Close the resource
			JDBCUtils.closeResource(conn, ps);
			
		}
	}
```

##### 3.3.5 Using PreparedStatement to implement query operations

```java
	// generic query for different tables: return an object (version 1.0)
	public <T> T getInstance(Class<T> clazz, String sql, Object... args) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 1. Get the database connection
			conn = JDBCUtils.getConnection();

			// 2. pre-compile sql statement to get PreparedStatement object
			ps = conn.prepareStatement(sql);

			// 3. Fill the placeholder
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			// 4. ExecuteQuery(), get the result set: ResultSet
			rs = ps.executeQuery();

			// 5. Get the metadata of the result set: ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();

			// 6.1 Get columnCount,columnLabel by ResultSetMetaData; get column value by ResultSet
			int columnCount = rsmd.getColumnCount();
			if (rs.next()) {
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) { // iterate through each column

					// Get the column value
					Object columnVal = rs.getObject(i + 1);
					// Get the alias of the column: the alias of the column is filled with the property name of the class
					String columnLabel = rsmd.getColumnLabel(i + 1);
					// 6.2 Use reflection to assign values to the corresponding properties of the object
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnVal);

				}

				return t;

			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			// 7. Close the resource
			JDBCUtils.closeResource(conn, ps, rs);
		}

		return null;

	}
```

#### 3.4 ResultSet & ResultSetMetaData

##### 3.4.1 ResultSet

- The query requires a call to the **executeQuery() method** of the PreparedStatement, and the result of the query is a **ResultSet object**.

- The **ResultSet object** encapsulates the result set for performing database operations in the form of a logical table, and the ResultSet interface is implemented by the database vendor.
- The ResultSet returns what is actually a **data table**. There is a pointer to the first record of the data table.

- The ResultSet object maintains a **cursor** to the current row of data, which initially precedes the first row and can be moved to the next row by the next() method of the ResultSet object. The next() method is called to check if the next row is valid. If it is, the method returns true and the pointer is moved down. It is equivalent to the combination of the hasNext() and next() methods of the Iterator object.
- When the pointer points to a row, you can get the value of each column by calling getXxx(int index) or getXxx(int columnName).

  - For example: getInt(1), getString("name")
  - **Note: The indexes in the relevant Java APIs involved in the interaction between Java and the database start from 1. **

- Common methods of the ResultSet interface.
  
  boolean next()
  
  getString()
  
  ![1555580152530](https://github.com/itsyuimorii/JDBC/blob/main/img/1555580152530.png) 

##### 3.4.2 ResultSetMetaData

An object that can be used to get information about the type and properties of the columns in the ResultSet object

```
ResultSetMetaData meta = rs.getMetaData();
```

- **getColumnName**(int column): Get the name of the specified column
- **getColumnLabel**(int column): get the alias of the specified column
- **getColumnCount**(): returns the number of columns in the current ResultSet object. 

- **getColumnTypeName**(int column): retrieves the database specific type name of the specified column. 
- **getColumnDisplaySize**(int column): indicates the maximum standard width of the specified column in characters. 
- **isNullable**(int column): Indicates whether the value in the specified column can be null. 

- **isAutoIncrement**(int column): indicates whether the specified columns are automatically numbered so that they remain read-only. 

> **Question 1: After getting the result set, how to know which columns are in the result set ? What are the column names?   The resultSetMetaData is an object that describes the ResultSet.**

Need to use an object describing the ResultSet, that is, ResultSetMetaData

> **Question 2: About ResultSetMetaData** 1.

1. **How to get ResultSetMetaData**: Just call the getMetaData() method of ResultSet
2. **Get the number of columns in the ResultSet**: Call the getColumnCount() method of ResultSetMetaData
3. **Get the alias of each column of the ResultSet**: Call the getColumnLabel() method of the ResultSetMetaData

![Screen Shot 2022-12-22 at 9.11.09 AM](https://github.com/itsyuimorii/JDBC/blob/main/img/Screen%20Shot%202022-12-22%20at%209.11.09%20AM.png)

#### 3.5 Release of resources

- Release ResultSet, Statement, Connection.
- Database connection (Connection) is a very rare resource and **must be released immediately after use**, if Connection is not closed properly in time it will lead to system downtime.The principle of using Connection is ** create it as late as possible and release it as early as possible. **
- It can be closed in finally to ensure that the resource will be closed in case of exceptions in other code.

#### 3.6 JDBC API Summary

- Two kinds of ideas

  - Interface-oriented programming idea

  - **ORM idea (object relational mapping)**
    - A data table corresponds to a java class
    - A record in the table corresponds to an object of the java class
    - A field in a table corresponds to a property of a java class

  > sql is required to combine the column names and table attribute names to write. Note the aliasing.

- Two techniques

  - JDBC result set metadata: ResultSetMetaData
    - Get the number of columns: getColumnCount()
    - Get the alias of the column: getColumnLabel()
  - Create the object of the specified class, get the specified properties and assign values through reflection
