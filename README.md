# Objective
Create RESTful endpoint(s) to calculate, store, and retrieve customer rewards.
No need to have a database, we'll store everything in memory.

# Background
* You will be using [Spring Boot](https://spring.io/projects/spring-boot).
We will be implementing a rewards service. Here is the list of rewards. You'll find the same list in the current code base 
    ```
    [
        { "tier": "A", "rewardName": "5% off purchase", "points": 100 },
        { "tier": "B", "rewardName": "10% off purchase", "points": 200 },
        { "tier": "C", "rewardName": "15% off purchase", "points": 300 },
        { "tier": "D", "rewardName": "20% off purchase", "points": 400 },
        { "tier": "E", "rewardName": "25% off purchase", "points": 500 },
        { "tier": "F", "rewardName": "30% off purchase", "points": 600 },
        { "tier": "G", "rewardName": "35% off purchase", "points": 700 },
        { "tier": "H", "rewardName": "40% off purchase", "points": 800 },
        { "tier": "I", "rewardName": "45% off purchase", "points": 900 },
        { "tier": "J", "rewardName": "50% off purchase", "points": 1000 }
    ]
    ``` 

# Implementation Instructions
* [Fork this repo.](https://help.github.com/articles/fork-a-repo/)
* Use incremental commits on a story branch to show how you arrived at your solutions.
* Push your story branch to your fork.
* [Issue a pull request](https://help.github.com/articles/using-pull-requests/) against the master branch of this repo.
* Design and implement the following endpoints.
    * **Endpoint 1:**
        * Accept a customer's order data: **email adress**  (ex. "customer01@gmail.com") and **order total** (ex. 100.80).
        * Calculate and store the following customer rewards data. For each dollar a customer spends, the customer will earn 1 reward point. For example, a customer whose order total is $100.80 earns 100 points and belongs to Rewards Tier A. Note: a customer can only belong to one rewards tier. For example, a customer with 205 reward points belongs to Rewards Tier B and cannot use the reward from Tier A. Once a customer has reached the top rewards tier, there are no additional rewards the customer can earn.
            * **Email Address:** the customer's email address (ex. "customer01@gmail.com")
            * **Reward Points:** the customer's rewards points (ex. 100)
            * **Rewards Tier:** the rewards tier the customer has reached (ex. "A")
            * **Reward Tier Name:** the name of the rewards tier (ex. "5% off purchase")
            * **Next Rewards Tier:** the next rewards tier the customer can reach (ex. "B")
            * **Next Rewards Tier Name:** the name of next rewards tier (ex. "10% off purchase")
            * **Next Rewards Tier Progress:** the percentage the customer is away from reaching the next rewards tier (ex. 0.5)
    * **Endpoint 2:** Accept a customer's email address, and return the customer's rewards data that was stored in Endpoint 1.
    * **Endpoint 3:** Return the same rewards data as Endpoint 2 but for all customers.
* For bonus points, add error handling and unit tests.

# Given:
Currently the project looks like the tree below.

* All of the main logic should go into the RewardsService
* The RewardsController the entrypoint of the web application. That is where you'll find the endpoints, e.g. `/purchase`
* Some of the endpoints have been stubbed out for convenience.

## How to run: 
 * Import the project into Intellij
 * File
 * New Project From Existing Sources 
 * Choose the directory
 * Press Ok
 * Right click the RewardsApplication.kt and click `Run RewardsApplication`

## Stubbed methods

Example cURL requests tested: 

`/purchase`, `/rewards/{email}`, `/rewards`

```
curl -X POST http://localhost:8080/purchase -H 'Content-Type: application/json' -d '{"email": "customer@example.com", "purchaseTotal": 40 }'
curl -X POST http://localhost:8080/purchase -H 'Content-Type: application/json' -d '{"email": "barnwaldo@hotmail.com", "purchaseTotal": 240 }'

curl -X GET http://localhost:8080/rewards/customer@example.com -H 'Content-Type: application/json'
curl -X GET http://localhost:8080/rewards/barnwaldo@hotmail.com -H 'Content-Type: application/json'

curl -X GET http://localhost:8080/rewards -H 'Content-Type: application/json'
```


Folder structure: (Test Class Added)


```bash
src
├── main
│   ├── kotlin
│   │   └── com
│   │       └── urbn
│   │           └── rewards
│   │               ├── RewardsApplication.kt
│   │               ├── controller
│   │               │   └── RewardsController.kt
│   │               ├── models
│   │               │   ├── Customer.kt
│   │               │   ├── OrderRequest.kt
│   │               │   └── Rewards.kt
│   │               └── service
│   │                   └── OrderService.kt
│   └── resources
│       ├── application.properties
│  
|──test
│   ├── kotlin
│   │   └── com
│   │       └── urbn
│   │           └── rewards
│   │               ├── RewardsApplicationTest.kt
```
