
for service in account_service authentication_service loan_service notification_service payment_service transaction_service user_service; do

  echo "Starting... $service"
  mvn -pl $service spring-boot:run > logs/$service.log 2>&1 &
  echo $! >> service_pids.txt
  sleep 2

done

echo "✅ All services started successfully!"
echo "📜 Logs available inside the logs/ folder."