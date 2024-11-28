#!/bin/bash

# Exit immediately if any command fails
set -e

# Run Gradle to generate the package
echo "Running gradle packageDistributionForCurrentOS..."
./gradlew packageDistributionForCurrentOS

# Define the directory where the .deb file is generated
DEB_DIR="composeApp/build/compose/binaries/main/deb"

# Check if the directory exists
if [ ! -d "$DEB_DIR" ]; then
  echo "Error: Directory $DEB_DIR not found."
  exit 1
fi

# Find the .deb file inside the directory
DEB_FILE=$(find "$DEB_DIR" -name "*.deb" | head -n 1)

# Check if a .deb file was found
if [ -z "$DEB_FILE" ]; then
  echo "Error: No .deb file found in $DEB_DIR."
  exit 1
fi

# Install the .deb file
echo "Installing package $DEB_FILE..."
sudo dpkg -i "$DEB_FILE"

echo "Installation completed!"
