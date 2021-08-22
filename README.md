# A java program to make safe CRUD operations in a mysqlite DB

1. Install sqlite (sudo apt-get install sqlite)
2. Used sqlite methods from https://gist.github.com/mchirico/4751124#file-sqlite-java
3. Compile (Will work with any JDK)From root directory (Dir with /src /bin /lib)
4. SQL DB will be generated in root dir
5. THE REGEX NEEDS WORK

TO COMPILE
javac -d bin -sourcepath src -cp lib/sqlite-jdbc-3.20.0.jar src/inputvalidation/InputValidator.java

TO RUN
1. java -cp bin:lib/sqlite-jdbc-3.20.0.jar inputvalidation.InputValidator ADD "BalajiB" "123123123123"
2. java -cp bin:lib/sqlite-jdbc-3.20.0.jar inputvalidation.InputValidator DEL "BalajiB"
3. java -cp bin:lib/sqlite-jdbc-3.20.0.jar inputvalidation.InputValidator DEL "123123123123"
4. java -cp bin:lib/sqlite-jdbc-3.20.0.jar inputvalidation.InputValidator LIST

To see system error code run "echo $?" from terminal after invalid command detected,
