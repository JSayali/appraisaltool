# Introduction #

This bundle comes with tomcat 6 and all required libraries (Spring 3 + Hibernate) on which CMMi tool depends. This page contains a few steps which you have to do.


# Requirements #

  * Java SE 5.0 JRE or higher configured on path JRE\_HOME
  * Hibernate supported database. Preconfigured for PostreSQL.
  * About 50 MB place on disk and free 8080 port

# Installation steps #

  1. Unzip bundle (all following paths are relative to your unizipped bundle/tomcat directory)
  1. If you are using other database then PosteSql, you have to copy JDBC driver to /lib.
  1. Navigate to webapps/cmmi-tool/WEB-INF/classes/META-INF/persistence.xml and open it in and editor
  1. Fill in connection properties to your database. If you dont want what anything meas, google "persistence.xml". You have to set following properties
```
!--  database setup   --> 
  <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect" /> 
 <!--  connection to database  --> 
  <property name="hibernate.connection.url" value="jdbc:postgresql://localhost:5432/cmmi-tool" /> 
  <property name="hibernate.connection.password" value="cmmi" /> 
  <property name="hibernate.connection.username" value="cmmi" /> 

```
  1. navigate to /bin, make sure you have specifies JRE\_HOME path and type ./startup or startup.bat (depending on system)

Now its done!

see log logs/castalina.out if any problems.

CMMI Tool is deployed on URL
http://localhost:8080/cmmi-tool/

Default admin username/password is **admin/admin**.