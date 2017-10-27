# Carne-IOT Server [![Build Status](https://travis-ci.org/carne-iot/server.svg?branch=master)](https://travis-ci.org/carne-iot/server)

The platform server

## Getting started

1. Install postgres, and set the corresponding user, password and database, or set application properties to use a remote postgres database.

2. Change working directory to ```<project-root>```

3. Install project modules

	``` 
	$ mvn clean install
	```

4. Build the project
	
	``` 
	$ mvn clean package
	```

5. Run the application
	
	``` 
	$ java -jar <-Dproperties> <project-root>/from-arch-webapp/target/from-arch-webapp-1.0.0-SNAPSHOT.jar
	```


## Acknowledgement
Project created using 
[com.bellotapps.archetypes.spring-boot-archetype](https://github.com/juanmbellini/spring-boot-archetype) archetype
