#!/bin/bash
echo "Compiling Philosofit MySQL Application..."
echo

# Create output directory
mkdir -p bin

# Compile Java files
javac -d bin src/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Run the application with: ./run.sh"
else
    echo "Compilation failed! Please check for errors."
fi
