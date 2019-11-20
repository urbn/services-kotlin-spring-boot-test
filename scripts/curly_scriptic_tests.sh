#!/bin/bash

# Reverted Float back to Int as the intent is about to determine values of the other attributes based on
# purchaseTotal in Int
curl -X POST \
  http://localhost:8080/purchase \
  -H 'Content-Type: application/json' \
  -d '{
	"email": "cevherd@gmail.com",
	"purchaseTotal": 100.80
}
' | jq

curl -X POST \
  http://localhost:8080/purchase \
  -H 'Content-Type: application/json' \
  -d '{
	"email": "cevherd@gmail.com",
	"purchaseTotal": 200
}
' | jq

# Add curl in loop to validate reward level range A-J when purchaseTotal from 100 to 1000
