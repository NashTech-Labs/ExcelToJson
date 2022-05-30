## Excel To Json

This project is developed in Scala Programming Language to convert Excel to Json.

## Commands

### clean

This command cleans the sbt project by deleting the target directory. The command output relevant messages.
````
sbt clean
````

### compile

This command compiles the scala source classes of the sbt project.
````
sbt compile
````
### run

Enter the project folder and enter sbt run command:
````
sbt run
````
More details about project (e.g. version etc..) can be found in file build.sbt



## Route

##### Convert Excel to json
###### Request: PUT <- localhost:7001/template/excel-to-json
###### Body: form-data
````
excelFile: File
````

###### Response
````
excelFile.json will be stored in project directory.
````


## END
