# Api Students

![Coverage](.github/badges/jacoco.svg)

This project implements a RESTful API for managing student data. It provides basic CRUD (Create, Read, Update, Delete) operations for student resources. The data storage backend is DynamoDB, a fully managed NoSQL database service provided by AWS.

In addition to the CRUD operations, the API includes a method to calculate the average grade of a student. The notes are then sent to a Kafka broker for processing by a consumer, calculating the average grade, the result is stored back in the database.

## Average Notes flow

![diagram-general.png](src/main/resources/aws/diagram-general.png)

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

- Deploy the following containers using Docker [docker-compose.yml](src/main/resources/local/docker/docker-compose.yml) (**NOTE:** Replace the IP address `192.168.1.8` with your local one)
- Clone the repository.
- Build the project using Maven: `mvn clean package`
- Run the application: `java -jar target/studentsapi-0.0.1-SNAPSHOT.jar`
- Access the API endpoints using your preferred HTTP client.

### AWS

- Run the following cloudformation files
  - [0-dynamodb.yml](src/main/resources/aws/cloud-formation/0-dynamodb.yml)
  - [1-vpc.yml](src/main/resources/aws/cloud-formation/1-vpc.yml)
  - [2-sg.yml](src/main/resources/aws/cloud-formation/2-sg.yml)
  - [3-asg-alb.yml](src/main/resources/aws/cloud-formation/3-asg-alb.yml)
  - [4-vpclink-apigw.yml](src/main/resources/aws/cloud-formation/4-vpclink-apigw.yml)

- Deploy the following docker-compose within the generated instances
  - Vault: [docker-compose.yml](src/main/resources/aws/docker/vault/docker-compose.yml) (**NOTE:** Vault is running on a different EC2)
  - Kafka and Services: [docker-compose.yml](src/main/resources/aws/docker/docker-compose.yml) (**NOTE:** Replace the IP address `10.0.30.106` with your vault EC2)

#### Resulting:

![diagram-aws.png](src/main/resources/aws/diagram-aws.png)

# API

## Endpoints

- `GET /api/v1/students`: Retrieve all students
- `GET /api/v1/students/{id}`: Retrieve a student by ID
- `POST /api/v1/students`: Save a student
- `DELETE /api/v1/students/{id}`: Delete a student
- `POST /api/v1/students/average-notes`: Save student with average notes

## Body 

````
Student{
dni*	      string
name*	      string
notes*	      [number($double)]
averageNotes  number($double)
}
````
## Documentation

http://localhost:8080/swagger-ui/index.html

## Examples

### Request Get Token

#### Curl

```bash
curl --location 'http://localhost:8001/realms/studentsapi-realm/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Cookie: JSESSIONID=B147DB395D50F6FCF45DCFE87C99D725' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=apistudents-client' \
--data-urlencode 'client_secret=CckzSbOLBhbuhL4jcjSe5j99xZFVDnmD' \
--data-urlencode 'username=admin' \
--data-urlencode 'password=admin' \
--data-urlencode 'scope=students/basic students/average-notes profile email'
```

#### Response

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI3eHY1ME5OTlVyMEhCNmRXWWg5dWxvSjRJMi1pX3hBUlNqQkFrVmszMkJFIn0.eyJleHAiOjE3MTYyNTU4MDQsImlhdCI6MTcxNjI1NTUwNCwianRpIjoiODE5MzU1MjYtMzEyNi00NjQxLWIzNmQtYWVkNWNhOGVkZTg2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDAxL3JlYWxtcy9zdHVkZW50c2FwaS1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiMDA4MDYxYS05YjI5LTQ0OGEtYjU2Ni1lY2NmNzFjNzQzYWIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhcGlzdHVkZW50cy1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiNWEyZjYyNDUtZjk2Zi00N2E3LWJiYjctNTFkZDZkZTlkNWY4IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwMDEiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImFkbWluX3JvbGUiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1zdHVkZW50c2FwaS1yZWFsbSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoic3R1ZGVudHMvYXZlcmFnZS1ub3RlcyBzdHVkZW50cy9iYXNpYyBwcm9maWxlIGVtYWlsIiwic2lkIjoiNWEyZjYyNDUtZjk2Zi00N2E3LWJiYjctNTFkZDZkZTlkNWY4IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJhZG1pbiBhZG1pbiIsInByZWZlcnJlZF91c2VybmFtZSI6ImFkbWluIiwiZ2l2ZW5fbmFtZSI6ImFkbWluIiwiZmFtaWx5X25hbWUiOiJhZG1pbiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIn0.Nzw5x9jpnciUKLdlhGRh2oxR3jnK77oNxAU1xzOAn7qS2VO_euztIGMydyMELdo_Y8nEaSylujrhlhOdlWA5GP4kWE28ruTM9KeuZiuB4CY0CSdMBi15n3THxoJk_wyihHrB2S855OjmPhOYhbMhYc0OQPmvj1mf38jr9LhVS9OMNhH2nftKUPatgLblrEpMVdW3TLVdghkYmheA-gm7Ij8fm92A9NGoQ0mkON5fqClJFLTtETzdHbIAXXz-QjdJY8ji8eWdxFHlN5EZtUKYqSEFuvOMAcQvQJbnSnYZuoZB2neVhIlaTz0ZXSKoxYgOdXwr3wNRYk7Btgfbs3Pqnw",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI1NmI2ZjY2MC0wOGI4LTQwNWItODJlZC03NjY0NmI4NzUzYWEifQ.eyJleHAiOjE3MTYyNTczMDQsImlhdCI6MTcxNjI1NTUwNCwianRpIjoiZjY0NzcwODUtMmUyMS00ZGM3LWFlNzQtMWIzYmZlZWYyMTA4IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDAxL3JlYWxtcy9zdHVkZW50c2FwaS1yZWFsbSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAwMS9yZWFsbXMvc3R1ZGVudHNhcGktcmVhbG0iLCJzdWIiOiJiMDA4MDYxYS05YjI5LTQ0OGEtYjU2Ni1lY2NmNzFjNzQzYWIiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoiYXBpc3R1ZGVudHMtY2xpZW50Iiwic2Vzc2lvbl9zdGF0ZSI6IjVhMmY2MjQ1LWY5NmYtNDdhNy1iYmI3LTUxZGQ2ZGU5ZDVmOCIsInNjb3BlIjoic3R1ZGVudHMvYXZlcmFnZS1ub3RlcyBzdHVkZW50cy9iYXNpYyBwcm9maWxlIGVtYWlsIiwic2lkIjoiNWEyZjYyNDUtZjk2Zi00N2E3LWJiYjctNTFkZDZkZTlkNWY4In0.ZU9fYEHKvnr0jyrRN3O8Gj14k4iXPrIYiG7dtSAUvt8",
  "token_type": "Bearer",
  "not-before-policy": 0,
  "session_state": "5a2f6245-f96f-47a7-bbb7-51dd6de9d5f8",
  "scope": "students/average-notes students/basic profile email"
}
```

### Request POST

`POST` http://localhost:8080/api/v1/students/average-notes

#### Curl

```bash
curl --location 'http://localhost:8080/api/v1/students/average-notes' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI3eHY1ME5OTlVyMEhCNmRXWWg5dWxvSjRJMi1pX3hBUlNqQkFrVmszMkJFIn0.eyJleHAiOjE3MTYyNTU2NzgsImlhdCI6MTcxNjI1NTM3OCwianRpIjoiZTA2NTY5M2EtYzJkYy00MTg3LThjZWQtOWQ4ZDAwNzI1MjgzIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDAxL3JlYWxtcy9zdHVkZW50c2FwaS1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiMDA4MDYxYS05YjI5LTQ0OGEtYjU2Ni1lY2NmNzFjNzQzYWIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhcGlzdHVkZW50cy1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiNzQ1NDdjODUtNWEyYy00MjQ0LTk1MTUtNmJjMjBhYWVhYmIzIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwMDEiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImFkbWluX3JvbGUiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1zdHVkZW50c2FwaS1yZWFsbSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoic3R1ZGVudHMvYXZlcmFnZS1ub3RlcyBzdHVkZW50cy9iYXNpYyBwcm9maWxlIGVtYWlsIiwic2lkIjoiNzQ1NDdjODUtNWEyYy00MjQ0LTk1MTUtNmJjMjBhYWVhYmIzIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJhZG1pbiBhZG1pbiIsInByZWZlcnJlZF91c2VybmFtZSI6ImFkbWluIiwiZ2l2ZW5fbmFtZSI6ImFkbWluIiwiZmFtaWx5X25hbWUiOiJhZG1pbiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIn0.FwqHg5lXgDF-kjJhS1XLinf229MWpJxR_8XSFwu2dDU1xeGyqTrO1NKyhoOeH4hKuKrnY0t4cjwYLG1T4CHfY9VHaDW-Ok3Dpsv4ybr9WLwOXA9Hgy7fTi-zwma4Y1l6QPQUmXqUVK3g-fDL0rFFos-N3uOWUvdUwJhTXhXd_pCTGUHk8xxhesciDyovvSYvGb2HDBa71sQ4nKFDpIl2VIg7XvpdQOaiyMU6GMNGj6fCw1NxiHNoRIPoRATEURRS5wNOAybtj_gZs6W--qO7DmIgN1g9LIvViywKOx-z6U42tuFBucbvE3vLyRRCV3itbL4D4EpvX56OVGuVBOZeyg' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=B147DB395D50F6FCF45DCFE87C99D725' \
--data '{
    "dni": "1",
    "name": "Pedro2",
    "notes": [
        1.0,
        2.0,
        9.0
    ]
}'
```

#### Response

`OK`

### Request GET

`GET` http://localhost:8080/api/v1/students/1

#### Curl

```bash
curl --location 'http://localhost:8080/api/v1/students/1' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI3eHY1ME5OTlVyMEhCNmRXWWg5dWxvSjRJMi1pX3hBUlNqQkFrVmszMkJFIn0.eyJleHAiOjE3MTYyNTU2NzgsImlhdCI6MTcxNjI1NTM3OCwianRpIjoiZTA2NTY5M2EtYzJkYy00MTg3LThjZWQtOWQ4ZDAwNzI1MjgzIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDAxL3JlYWxtcy9zdHVkZW50c2FwaS1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiMDA4MDYxYS05YjI5LTQ0OGEtYjU2Ni1lY2NmNzFjNzQzYWIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhcGlzdHVkZW50cy1jbGllbnQiLCJzZXNzaW9uX3N0YXRlIjoiNzQ1NDdjODUtNWEyYy00MjQ0LTk1MTUtNmJjMjBhYWVhYmIzIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwMDEiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImFkbWluX3JvbGUiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1zdHVkZW50c2FwaS1yZWFsbSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoic3R1ZGVudHMvYXZlcmFnZS1ub3RlcyBzdHVkZW50cy9iYXNpYyBwcm9maWxlIGVtYWlsIiwic2lkIjoiNzQ1NDdjODUtNWEyYy00MjQ0LTk1MTUtNmJjMjBhYWVhYmIzIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJhZG1pbiBhZG1pbiIsInByZWZlcnJlZF91c2VybmFtZSI6ImFkbWluIiwiZ2l2ZW5fbmFtZSI6ImFkbWluIiwiZmFtaWx5X25hbWUiOiJhZG1pbiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIn0.FwqHg5lXgDF-kjJhS1XLinf229MWpJxR_8XSFwu2dDU1xeGyqTrO1NKyhoOeH4hKuKrnY0t4cjwYLG1T4CHfY9VHaDW-Ok3Dpsv4ybr9WLwOXA9Hgy7fTi-zwma4Y1l6QPQUmXqUVK3g-fDL0rFFos-N3uOWUvdUwJhTXhXd_pCTGUHk8xxhesciDyovvSYvGb2HDBa71sQ4nKFDpIl2VIg7XvpdQOaiyMU6GMNGj6fCw1NxiHNoRIPoRATEURRS5wNOAybtj_gZs6W--qO7DmIgN1g9LIvViywKOx-z6U42tuFBucbvE3vLyRRCV3itbL4D4EpvX56OVGuVBOZeyg' \
--header 'Cookie: JSESSIONID=B147DB395D50F6FCF45DCFE87C99D725'
```

#### Response

````
{
  "dni": "1",
  "name": "Pedro",
  "notes": [
    1.0,
    2.0,
    3.0
  ],
  "averageNotes": 2.0
}
````

### OAuth2 getting Issuer and Token endpoints

- Keycloak: `http://localhost:8001/realms/studentsapi-realm/.well-known/openid-configuration`
- Cognito: `https://cognito-idp.'Region'.amazonaws.com/'your user pool ID'/.well-known/openid-configuration`

### Links

- https://docs.aws.amazon.com/cognito/latest/developerguide/federation-endpoints.html
- https://www.baeldung.com/spring-boot-keycloak