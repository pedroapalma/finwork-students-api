# Api Students

![Coverage](.github/badges/jacoco.svg)

This project implements a RESTful API for managing student data. It provides basic CRUD (Create, Read, Update, Delete) operations for student resources. The data storage backend is DynamoDB, a fully managed NoSQL database service provided by AWS.

In addition to the CRUD operations, the API includes a method to calculate the average grade of a student. The notes are then sent to a Kafka broker for processing by a consumer, calculating the average grade, the result is stored back in the database.

# Technologies Used

- Java
- Spring Boot
- Amazon DynamoDB
- Apache Kafka
- Vault

# Getting Started

## Prerequisites

### Local

- Docker
- Java
- Maven

### AWS

- AWS Account
- AWS CLI

## Installing

### Local

- Deploy the following containers using Docker [docker-compose.yml](src/main/resources/local/docker-compose.yml) (**NOTE:** Replace the IP address `192.168.1.8` with your local one)
- Clone the repository.
- Build the project using Maven: `mvn clean package`
- Run the application: `java -jar target/studentsapi-0.0.1-SNAPSHOT.jar`
- Access the API endpoints using your preferred HTTP client.

### AWS

# Endpoints

- `GET /api/students`: Retrieve all students
- `GET /api/students/{id}`: Retrieve a student by ID
- `POST /api/students`: Save a student
- `DELETE /api/students/{id}`: Delete a student
- `POST /api/students/average-notes`: Save student with average notes

