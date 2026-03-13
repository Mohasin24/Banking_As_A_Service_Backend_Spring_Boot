echo "Starting... Discovery-Server"
mvn -pl discovery_server spring-boot:run > logs/discovery_server.log 2>&1 &
echo $! >> service_pids.txt
sleep 5

echo "Starting... Config-Server"
mvn -pl config_server spring-boot:run > logs/config_server.log 2>&1 &
echo $! >> service_pids.txt
sleep 5

echo "Starting... Api-Gateway"
mvn -pl api_gateway spring-boot:run > logs/api_gateway.log 2>&1 &
echo $! >> service_pids.txt
sleep 5

echo "Starting... Authentication Service"
mvn -pl authentication_service spring-boot:run > logs/authentication_service.log 2>&1 &
echo $! >> service_pids.txt
sleep 5


echo "✅ All Servers started successfully!"
echo "📜 Logs available inside the logs/ folder."