#!/bin/bash
echo "Starting Philosofit MySQL Application..."
echo

# Check if compiled
if [ ! -d "bin" ]; then
    echo "Application not compiled yet. Running compile.sh first..."
    ./compile.sh
fi

# Run the application
java -cp "bin:lib/mysql-connector-j-8.0.33.jar" Main
