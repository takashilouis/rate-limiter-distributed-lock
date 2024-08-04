#!/bin/bash

URL="http://localhost:8080/category/1/articles"  # Update with your actual endpoint

for i in {1..33}
do
  echo "Request $i"
  response=$(curl -s -o /dev/null -w "%{http_code}" $URL)
  echo "Response: $response"
  sleep 1  # Add a 1-second delay between requests
done
