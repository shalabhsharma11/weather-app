Introduction
---

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database.

### Dependencies
- Spring Boot 2
- Spring boot web
- Spring boot JPA
- Postgre DB
- Lombok

### Build Tool
- Maven

### How to Run 
If you want to run application in IDE, execute the following command from the project root directory:

```
mvn clean package
```

#### Run Inside Docker

`Dockerfile` is present. Execute the following command from the project root directory to create a image:

```
docker build --tag weather-app:0.0.1 .
```

Start the container using `docker-compose.yaml` file.

```
docker-compose up
``` 
This will start the `Postgresql` database and `weather` application in docker container.

### Application Endpoint

```
http://localhost:8080/weather?city=amsterdam
```

### Response

```
{
"id": 1,
"city": "Amsterdam",
"country": "NL",
"temperature": 288.14,
"dateTime": 1621178217
}
```

### Decision Made
- On first call for a city, app will persist the information to DB and in inmemory cache. 
- In subsequent calls, if temperature of a city is same then it will return information from cache. 
- If there are multiple call within `WEATHER_REQUEST_WAIT_MS` for same city, then also it will return information from cache.
- In all other scenario it will save information to DB and update cache too.
 
This is done to prevent unnecessary calls to DB and OpenWeather API.

**Decision To Make**: We can also implement to only store the highest and the lowest temperature for a day for a particular city. Need to make decision about the cleanup/archive of the old DB data.


### Configuration

|  Config |  Description | Default Value |
|---|---|---|
| `SPRING_DATASOURCE_URL` | database URL  | `jdbc:postgresql://database:5432/weather_db` |
| `SPRING_DATASOURCE_USERNAME` |  Database user name | `user`|
| `SPRING_DATASOURCE_PASSWORD` | Database password  | `password` |
| `WEATHER_API_KEY` | OpenWeather Api key. | `N/A` |
| `WEATHER_REQUEST_WAIT_MS` | This will prevent making frequent call to OpenWeather API for particular city. | `120000 ms` |