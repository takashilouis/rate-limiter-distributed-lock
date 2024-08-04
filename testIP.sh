#!/bin/bash

URL="http://localhost:8080/category/1/articles"  # Update with your actual endpoint
IP1="192.168.1.1"
IP2="192.168.1.2"

# Function to send requests
send_requests() {
  local ip=$1
  local count=$2
  for ((i=1; i<=count; i++))
  do
    echo "Request $i from IP $ip"
    response=$(curl -s -o /dev/null -w "%{http_code}" -H "X-Forwarded-For: $ip" $URL)
    echo "Response: $response"
    sleep 1  # Add a 1-second delay between requests
  done
}

# Send 29 requests from IP 1
send_requests $IP1 29

# Send 31 requests from IP 2
send_requests $IP2 31

