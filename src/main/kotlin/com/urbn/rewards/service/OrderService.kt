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

    final var maxRewardPoints: Int

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
        // Consider doing a ternary conditional to update the variables
        // Current approach isn't as clean as I'd like. Research more
        // Update existing customer info
        var updatedRewardPoints = Math.floor(orderRequest.purchaseTotal.toDouble()).toInt()
        var updatedRewardsTier = ""
        var updatedNextRewardsTier = ""
        var updatedNextRewardsTierName = ""
        var updatedNextRewardsTierProgress = 0.0F

        if (customers[orderRequest.email] != null) {
            var existingCustomer = customers[orderRequest.email]

            updatedRewardPoints += (existingCustomer?.rewardPoints ?: 0)
        }

        if (updatedRewardPoints < maxRewardPoints) {
            var rewardsIndex = (updatedRewardPoints/100) - 1

            // Error handling for surpassing maximum rewards offered
            // Error handling for before the customer reaches first tier
            if (rewardsIndex > rewards.size - 1) {
                rewardsIndex = rewards.size - 1
            } else if (rewardsIndex < 0) {
                rewardsIndex = 0
            }

            updatedRewardsTier = rewards[rewardsIndex].tier
            updatedNextRewardsTier = rewards[rewardsIndex + 1].tier
            updatedNextRewardsTierName = rewards[rewardsIndex + 1].rewardName
            updatedNextRewardsTierProgress = ((updatedRewardPoints%100).toFloat())/100
        }

        val currentCustomer = Customer(
            email = orderRequest.email,
            rewardPoints = updatedRewardPoints,
            nextRewardsTier = updatedNextRewardsTier,
            rewardsTier = updatedRewardsTier,
            nextRewardsTierName = updatedNextRewardsTierName,
            nextRewardsTierProgress = updatedNextRewardsTierProgress.toFloat()
        )

        customers[orderRequest.email] = currentCustomer

        return currentCustomer
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
