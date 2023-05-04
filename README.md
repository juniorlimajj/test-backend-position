# Cocus Challenge

This project is a Spring Boot application that requires Docker and Maven to be installed in order to
be run.

### Running the Project

To start the Redis and Postgres databases, run the following command in your terminal:

```docker-compose up -d```

### To run the project, run the following command:

```mvn spring-boot:run```

### Running Unit Tests

To run the unit tests, run the following command:

```mvn test```

### Using the Postman Collection

To perform requests to the API, you can import the Postman Collection located in
the ```/data/Cocus-Test.postman_collection.json```
To do this, follow these steps:

- Open the Postman app

- Click on the Import button in the top left corner

- Select the Import Folder option

- Browse to the client folder in the project directory and select it

- Click the Open button to import the collection
