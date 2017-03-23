To deploy to tomcat do the following:

- Add ojdbc6.jar to %TOMCAT_HOME%/lib directory

- Update context.xml in %TOMCAT_HOME%/conf directory to add a resource

<Resource name="jdbc/pipedemo"
     type="javax.sql.DataSource"
     auth="Container"
     username = "your user"
     password = "your pwd"
     driverClassName = "oracle.jdbc.driver.OracleDriver"
     maxActive = "8"
     maxIdle = "4"/>