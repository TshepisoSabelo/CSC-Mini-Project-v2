#!/bin/bash

echo "Compiling Java sources..."

# Create bin if it doesn't exist
mkdir -p bin

# Exclude UI folders/files (JavaFX)
find src -name "*.java" \
    | grep -v "src/main/UI" \
    | grep -v "src/main/UserInterface" \
    > sources.txt

# Compile
javac -d bin @sources.txt

# Check if compilation succeeded
if [ $? -eq 0 ]; then
    echo "Compilation successful."
    echo "Running Test..."

    java -Xmx12G -cp bin Test

else
    echo "Compilation failed."
    exit 1
fi