package com.urbn.rewards.service

import com.google.gson.Gson
import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.OrderRequest
import com.urbn.rewards.models.Rewards
import org.springframework.stereotype.Service

import java.util.HashMap

@Service
class OrderService {

    // Hard coded list of rewards. See the getRewards function below
    final var rewards: Array<Rewards>

    // The reward point correlating to the highest tier achievable
    private val maxRewardPoints: Int

    // A list of in memory customers keyed off of the e-mail
    private val customers = HashMap<String, Customer>()

    private val gson = Gson()

    // Stores reward data into rewards array
    init {
        val rewardString = getRewards()
        rewards = gson.fromJson(rewardString, Array<Rewards>::class.java)
        maxRewardPoints = rewards[rewards.size-1].points
    }

    fun purchase(orderRequest: OrderRequest): Customer {
        // Update existing customer info
        // Below is the base case
        var updatedRewardPoints = Math.floor(orderRequest.purchaseTotal.toDouble()).toInt()
        var updatedRewardsTier = ""
        var updatedRewardsName = ""
        var updatedNextRewardsTier = ""
        var updatedNextRewardsTierName = ""
        var updatedNextRewardsTierProgress: Float

        if (customers[orderRequest.email] != null) {
            var existingCustomer = customers[orderRequest.email]

            updatedRewardPoints += (existingCustomer?.rewardPoints ?: 0)
        }

        updatedNextRewardsTierProgress = ((updatedRewardPoints%100).toFloat())/100


        // Used to handle cases where customer exceeds the maximum reward
        var maxRewardsIndex = rewards.size - 1

        // Handles earliest case where customer doesn't qualify for
        // any rewards yet
        if (updatedRewardPoints < 100) {
            updatedNextRewardsTier = rewards[0].tier
            updatedNextRewardsTierName = rewards[0].rewardName
        }
        // Handles the case where customer qualifies for reward but hasn't
        // reached the final tier
        else if (updatedRewardPoints in 100 until maxRewardPoints) {
            var rewardsIndex = (updatedRewardPoints/100) - 1

            // Error handling for surpassing maximum rewards offered
            // Error handling for before the customer reaches first tier
            if (rewardsIndex > maxRewardsIndex) {
                rewardsIndex = maxRewardsIndex
            } else if (rewardsIndex < 0) {
                rewardsIndex = 0
            }

            updatedRewardsTier = rewards[rewardsIndex].tier
            updatedRewardsName = rewards[rewardsIndex].rewardName
            updatedNextRewardsTier = rewards[rewardsIndex + 1].tier
            updatedNextRewardsTierName = rewards[rewardsIndex + 1].rewardName
            updatedNextRewardsTierProgress = ((updatedRewardPoints%100).toFloat())/100
        }
        // Handles the case where customer reaches the final tier
        else if (updatedRewardPoints >= maxRewardPoints) {
            updatedRewardsTier = rewards[maxRewardsIndex].tier
            updatedRewardsName = rewards[maxRewardsIndex].rewardName
            updatedNextRewardsTierProgress = 0F
        }


        // Updates and return the customer's info
        val currentCustomer = Customer(
            email = orderRequest.email,
            rewardPoints = updatedRewardPoints,
            rewardsTier = updatedRewardsTier,
            rewardsName = updatedRewardsName,
            nextRewardsTier = updatedNextRewardsTier,
            nextRewardsTierName = updatedNextRewardsTierName,
            nextRewardsTierProgress = updatedNextRewardsTierProgress
        )

        customers[orderRequest.email] = currentCustomer

        return currentCustomer
    }

    // Returns the corresponding customer's reward info
    fun getCustomerReward(email: String): Rewards {
        // Non-existent customers will simply return no reward
        // values for each field as described in the default
        // initialization of Customer data class
        var customerReward = Rewards()
        val customer = customers[email]

        if (customer != null) {
            customerReward = Rewards(
                rewardName = customer.rewardsName,
                tier = customer.rewardsTier,
                points = customer.rewardPoints
            )
        }

        return customerReward
    }

    // Return all customer's rewards
    fun getAllCustomerRewards(): HashMap<String, Rewards> {
        var customersRewards = HashMap<String, Rewards>()

        for ((key, value) in customers) {

            val rewards = Rewards(
                    rewardName = value.rewardsName,
                    tier = value.rewardsTier,
                    points = value.rewardPoints
            )
            customersRewards[key] = rewards
        }

        return customersRewards
    }

    private fun getRewards(): String {
        return """
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
            """.trimIndent()
    }

}
