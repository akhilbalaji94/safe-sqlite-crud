A java program to make safe CRUD operations in a mysqlite DB

Install sqlite (sudo apt-get install sqlite)
Used sqlite methods from https://gist.github.com/mchirico/4751124#file-sqlite-java
Compiled (Will work with any JDK)From root directory (Dir with /src /bin /lib)
SQL DB will be generated in root dir

THE REGEX NEEDS TO BE worked on.

TO COMPILE
javac -d bin -sourcepath src -cp lib/sqlite-jdbc-3.20.0.jar src/inputvalidation/InputValidator.java

TO RUN
java -cp bin:lib/sqlite-jdbc-3.20.0.jar inputvalidation.InputValidator ADD "BalajiB" "123123123123"
java -cp bin:lib/sqlite-jdbc-3.20.0.jar inputvalidation.InputValidator DEL "BalajiB"
java -cp bin:lib/sqlite-jdbc-3.20.0.jar inputvalidation.InputValidator DEL "123123123123"
java -cp bin:lib/sqlite-jdbc-3.20.0.jar inputvalidation.InputValidator LIST

To see system error code run "echo $?" from terminal after invalid command detected,
