#!/bin/bash

# This is just a note taking commit to test several things 1- forked storied branch commit 2- POST to the service
#
# Base passed
# TODO: Data type issue truncates float to integer for purchaseTotal
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
	"purchaseTotal": 300
}
' | jq


# Failed - might totally be unnecessary, it is just a matter of quickly understanding the baseline before adding anything in
# Just a starting point in testing with other attributes in the body of the JSON request
# TODO: Update code based on requirements in endpoint1 to revalidate
curl -X POST \
  http://localhost:8080/purchase \
  -H 'Content-Type: application/json' \
  -d '{
	"email": "cevherd@gmail.com",
	"purchaseTotal": 100.80
	"rewardPoints": 40,
	"rewardsTier": "3",
	"nextRewardsTier": "4",
	"nextRewardsTierName": "Silver",
	"nextRewardsTierProgress": 1
}
' | jq


