#Base Image
FROM openjdk:11.0-jre-slim
#copying orderservice jar to the image
ADD target/orchestrator-service*.jar /home/OchestratorService.jar
#serving the jar as an entrypoint
CMD ["java", "-jar", "/home/OchestratorService.jar"]
