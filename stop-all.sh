# Check if service_pids.txt exists
if [ -f service_pids.txt ]; then
    echo "Stopping all microservices..."
    cat service_pids.txt | xargs kill
    rm service_pids.txt
    echo "✅ All services stopped and service_pids.txt removed!"
else
    echo "⚠️ No service_pids.txt found. Maybe services are not running?"
fi
