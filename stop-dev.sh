# Check if dev_pids.txt exists
if [ -f dev_pids.txt ]; then
    echo "Stopping service..."
    cat dev_pids.txt | xargs kill
    rm dev_pids.txt
    echo "✅ Service stopped and dev_pids.txt removed!"
else
    echo "⚠️ No dev_pids.txt found. Maybe service is not running?"
fi