echo "Building project docker build"
# Ensure java 11 is setup as default PC JDK

export ORDER_SERVICE_URL='http://localhost:3003'

mvn clean package -DskipTests
docker build -t alaooluwatobi/soa_orchestration_service:latest .
docker push alaooluwatobi/soa_orchestration_service:latest
