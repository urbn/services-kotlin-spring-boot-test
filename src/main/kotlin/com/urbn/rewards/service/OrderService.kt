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

    // A list of in memory customers keyed off of the e-mail
    private val customers = HashMap<String, Customer>()

    private val gson = Gson()

    // Stores reward data into rewards array
    init {
        val rewardString = getRewards()
        rewards = gson.fromJson(rewardString, Array<Rewards>::class.java)
    }

    fun purchase(orderRequest: OrderRequest): Map<String, Customer> {
        // Implement purchase endpoint logic here
        // Right now, we're only storing the customer into a hash map

        // Store the reward points in a read-only local variable
        val totalRewardPoints = Math.floor(orderRequest.purchaseTotal.toDouble()).toInt()
        val rewardTierIndex = rewards.indexOfLast { it.points <= totalRewardPoints }
        val rewardsTier = rewards[rewardTierIndex]
        val nextRewardsTier = if (rewardTierIndex < rewards.size - 1) rewards[rewardTierIndex + 1] else null

        customers[orderRequest.email] = Customer(
            email = orderRequest.email,
            rewardPoints = totalRewardPoints,
            rewardsTier = if (rewardsTier != null ) rewardsTier.tier else "",
            rewardsTierName = if (rewardsTier != null ) rewardsTier.rewardName else "",
            nextRewardsTier = if (nextRewardsTier != null ) nextRewardsTier.tier else "",
            nextRewardsTierName = if (nextRewardsTier != null ) nextRewardsTier.rewardName else "",
            nextRewardsTierProgress = getNextRewardsTierProgress(totalRewardPoints, rewardsTier, nextRewardsTier)
        )

        return customers
    }

    private fun getNextRewardsTierProgress(totalRewardPoints: Int, currentTier: Rewards?, nextTier: Rewards?): Float {
        if (currentTier == null || nextTier == null) {
            return 0.0.toFloat()
        }
        val numerator = (totalRewardPoints - currentTier.points).toFloat()
        // Don't assume tiers will only increase in 100 point increments
        // Compute the difference between tiers and use that to calculate percentage
        val denominator = (nextTier.points - currentTier.points).toFloat()
        return numerator * 100.0.toFloat() / denominator
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
