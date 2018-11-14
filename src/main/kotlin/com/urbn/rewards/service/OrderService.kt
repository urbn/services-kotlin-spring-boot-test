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

    // Returns all customers as a read-only list
    fun getAllCustomers(): List<Customer> {
        return customers.values.toList()
    }

    // Returns specific customer as a nullable type
    fun getCustomer(email: String): Customer? {
        return customers[email]
    }

	fun deleteCustomer(email: String) {
        if (customers.contains(email)) customers.remove(email)
	}

	fun purchase(orderRequest: OrderRequest): Map<String, Customer> {
        // Implement purchase endpoint logic here
        // Right now, we're only storing the customer into a hash map

        // Store the reward points in a read-only local variable
        val (totalRewardPoints, rewardsTier, nextRewardsTier, progress) = getRewardsData(orderRequest.purchaseTotal.toDouble())

        customers[orderRequest.email] = Customer(
            email = orderRequest.email,
            rewardPoints = totalRewardPoints,
            rewardsTier = if (rewardsTier != null ) rewardsTier.tier else "",
            rewardsTierName = if (rewardsTier != null ) rewardsTier.rewardName else "",
            nextRewardsTier = if (nextRewardsTier != null ) nextRewardsTier.tier else "",
            nextRewardsTierName = if (nextRewardsTier != null ) nextRewardsTier.rewardName else "",
            nextRewardsTierProgress = progress
        )

        return customers
    }

    data class RewardData(val totalPoints: Int, val rewardsTier: Rewards?, val nextTier: Rewards?, val progress: Float)
    private fun getRewardsData(purchaseTotal: Double): RewardData {

        val totalRewardPoints = Math.floor(purchaseTotal).toInt()

        // Check if rewards tier exists for current total points
        val rewardTierIndex = rewards.indexOfLast { it.points <= totalRewardPoints }

        // Grab the reward tier info for the index that was found
        val rewardsTier = if (rewardTierIndex >= 0) rewards[rewardTierIndex] else null

        // Compute the next rewards tier
        var nextRewardsTier: Rewards? = null
        if (rewardTierIndex == -1) {
            nextRewardsTier = rewards[0]
        }
        else if (rewardTierIndex < rewards.size - 1) {
            nextRewardsTier = rewards[rewardTierIndex + 1]
        }
		val progress = getNextRewardsTierProgress(totalRewardPoints, rewardsTier, nextRewardsTier)
        return RewardData(totalRewardPoints, rewardsTier, nextRewardsTier, progress)
	}

    private fun getNextRewardsTierProgress(totalRewardPoints: Int, currentTier: Rewards?, nextTier: Rewards?): Float {
        if (nextTier == null) {
            return 0.0.toFloat()
        }
		// If total points are less than first tier, we need to use the total points as numerator
        val numerator = if (currentTier != null) (totalRewardPoints - currentTier.points).toFloat() else totalRewardPoints.toFloat()
        // Don't assume tiers will only increase in 100 point increments
        // Compute the difference between tiers and use that to calculate percentage
        // If total points are less than first tier, we need to use the next tier points as denominator
        val denominator = if (currentTier != null) (nextTier.points - currentTier.points).toFloat() else nextTier.points.toFloat()
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
