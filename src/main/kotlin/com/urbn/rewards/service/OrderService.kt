package com.urbn.rewards.service

import com.google.gson.Gson
import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.OrderRequest
import com.urbn.rewards.models.Rewards
import org.springframework.stereotype.Service

@Service
class OrderService {

    // Hard coded list of rewards. See the getRewards function below
    final var rewards: Array<Rewards>

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
        // Implement purchase endpoint logic here
        // Right now, we're only storing the customer into a hash map

        var totalRewardPoints = Math.floor(orderRequest.purchaseTotal.toDouble()).toInt()
        var totalRewardsTier = ""
        var totalRewardsTierName = ""
        var totalNextRewardsTier = ""
        var totalNextRewardsTierName = ""
        var totalNextRewardsTierProgress: Float

        if (customers[orderRequest.email] != null) {
            val existingCustomer = customers[orderRequest.email]

            totalRewardPoints += (existingCustomer?.rewardPoints ?: 0)
        }

        totalNextRewardsTierProgress = ((totalRewardPoints%100).toFloat())/100

        val maxRewardsIndex = rewards.size - 1

        when {
            // no rewards
            totalRewardPoints < 100 -> {
                totalNextRewardsTier = rewards[0].tier
                totalNextRewardsTierName = rewards[0].rewardName
            }
            // 100 - 1000
            totalRewardPoints in 100 until maxRewardPoints -> {
                var rewardsIndex = (totalRewardPoints/100) - 1

                if (rewardsIndex > maxRewardsIndex) {
                    rewardsIndex = maxRewardsIndex
                } else if (rewardsIndex < 0) {
                    rewardsIndex = 0
                }

                totalRewardsTier = rewards[rewardsIndex].tier
                totalRewardsTierName = rewards[rewardsIndex].rewardName
                totalNextRewardsTier = rewards[rewardsIndex + 1].tier
                totalNextRewardsTierName = rewards[rewardsIndex + 1].rewardName
                totalNextRewardsTierProgress = ((totalRewardPoints%100).toFloat())/100
            }
            // > 1000
            totalRewardPoints >= maxRewardPoints -> {
                totalRewardsTier = rewards[maxRewardsIndex].tier
                totalRewardsTierName = rewards[maxRewardsIndex].rewardName
                totalNextRewardsTier = "N/A"
                totalNextRewardsTierName = "N/A"
                totalNextRewardsTierProgress = 0F
            }
        }

        val currentCustomer = Customer(
            email = orderRequest.email,
            rewardPoints = totalRewardPoints,
            rewardsTier = totalRewardsTier,
            rewardsTierName = totalRewardsTierName,
            nextRewardsTier = totalNextRewardsTier,
            nextRewardsTierName = totalNextRewardsTierName,
            nextRewardsTierProgress = totalNextRewardsTierProgress
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

    // Implement reward endpoint logic here
    fun reward(email: String): Customer? {
        return customers[email]
    }

    // Implement allRewards endpoint logic here
    fun allRewards(): List<Customer> {
        return customers.values.toList()
    }

}
