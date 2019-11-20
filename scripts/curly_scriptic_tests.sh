#!/bin/bash

curl -X POST \
  http://localhost:8080/purchase \
  -H 'Content-Type: application/json' \
  -d '{
	"email": "cevherd@gmail.com",
	"purchaseTotal": 100.80
}
' | jq

#xsizxenjins:scripts cevherdogan$ curl -X POST   http://localhost:8080/purchase   -H 'Content-Type: application/json'   -d '{
#"email": "cevherd@gmail.com",
#"purchaseTotal": 100.80
#}
#' | jq
#  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
#                                 Dload  Upload   Total   Spent    Left  Speed
#100   231    0   173  100    58  21625   7250 --:--:-- --:--:-- --:--:-- 28875
#{
#  "cevherd@gmail.com": {
#    "email": "cevherd@gmail.com",
#    "rewardPoints": 100.8,
#    "rewardsTier": "???",
#    "nextRewardsTier": "??",
#    "nextRewardsTierName": "???",
#    "nextRewardsTierProgress": 0
#  }
#}

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
	"purchaseTotal": 400.80
	"rewardPoints": 40,
	"rewardsTier": "3",
	"nextRewardsTier": "4",
	"nextRewardsTierName": "Silver",
	"nextRewardsTierProgress": 1
}
' | jq


