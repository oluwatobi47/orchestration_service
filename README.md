# Orchestration Service

In order to start up this service, ensure the order service is running.

# Running On local PC
Set the environment variables for the dependent services defined in the application.properties file.
Fallback defaults to local running instance based on other docker containers

`mvn clean package`
`java -jar orchestrator-service-0.0.1-SNAPSHOT.jar`

Environment override example:

`export ORDER_SERVICE_URL='http://localhost:4000'`
`export INV_SERVICE_URL='http://localhost:4000'`
`export AUTH_SERVICE_URL='http://localhost:4000'`
`export NOTIFICATION_SERVICE_URL='http://localhost:4000'`
`export SUB_SERVICE_URL='http://localhost:4000'`

then

`mvn clean package`

then

`java -jar orchestrator-service-0.0.1-SNAPSHOT.jar`



# Running with docker compose (Recommended)
To start all services run:

`docker compose up`

Once started:

Access the running instance of the orchestrator on port 3000 on the host machine

`http://localhost:3000`

For documentation for the orchestration of the business process endpoints implemented in this service. Click on link below

http://localhost:3000/swagger-ui/index.html


