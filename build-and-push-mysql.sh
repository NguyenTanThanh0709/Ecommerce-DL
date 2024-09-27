#!/bin/bash

# Docker Hub username
USERNAME="nguyentanthanh0709"

# MySQL images and their tags
declare -A MYSQL_IMAGES=(
    ["userdb"]="3301"
    ["productdb"]="3302"
    ["cartdb"]="3303"
    ["purchasedb"]="3304"
)

# Loop through each MySQL image and push it to Docker Hub
for DB in "${!MYSQL_IMAGES[@]}"; do
    echo "Building MySQL image for $DB..."
    
    # Build the Docker image
    docker build -t "$USERNAME/mysql:$DB" -f Dockerfile-mysql . || { echo "Failed to build $DB"; exit 1; }
    
    echo "Pushing MySQL image for $DB to Docker Hub..."
    
    # Push the Docker image
    docker push "$USERNAME/mysql:$DB" || { echo "Failed to push $DB"; exit 1; }
    
    echo "$DB image built and pushed successfully."
done

echo "All MySQL images built and pushed successfully."
