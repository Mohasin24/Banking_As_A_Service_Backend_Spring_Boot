
for service in notification_service transaction_service; do

  echo "Starting... $service"
  mvn -pl $service spring-boot:run > logs/$service.log 2>&1 &
  echo $! >> service_pids.txt
  sleep 2

done

sleep 5

echo "✅ All Services started successfully!"
echo "📜 Logs available inside the logs/ folder."