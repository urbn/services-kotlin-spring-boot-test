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

    // Next Tier Increment
    final var MODULO_PCT = 100;

    // A list of in memory customers keyed off of the e-mail
    private val customers = HashMap<String, Customer>()

    private val gson = Gson()

    // Stores reward data into rewards array
    init {
        val rewardString = getRewards()
        rewards = gson.fromJson(rewardString, Array<Rewards>::class.java)
    }

    fun getRewardsTierAndNameByPoints(
            points: Int): Rewards? {

        for (reward in rewards) {
            if (reward.points == points) {
                return reward
            }
        }
        return null
    }

    fun purchase(orderRequest: OrderRequest): Map<String, Customer> {
        // Implement purchase endpoint logic here
        // Right now, we're only storing the customer into a hash map

        customers[orderRequest.email] = Customer(
            email = orderRequest.email,
            rewardPoints = Math.floor(orderRequest.purchaseTotal.toDouble()).toInt(),
            rewardsTier = getRewardsTierAndNameByPoints(Math.floor(orderRequest.purchaseTotal.toDouble()).toInt() - Math.floor(orderRequest.purchaseTotal.toDouble()).toInt()%MODULO_PCT)?.tier.toString(),
            nextRewardsTier = getRewardsTierAndNameByPoints((MODULO_PCT + Math.floor(orderRequest.purchaseTotal.toDouble()).toInt() - Math.floor(orderRequest.purchaseTotal.toDouble()).toInt()%MODULO_PCT))?.tier.toString(),
            nextRewardsTierName = getRewardsTierAndNameByPoints((MODULO_PCT + Math.floor(orderRequest.purchaseTotal.toDouble()).toInt() - Math.floor(orderRequest.purchaseTotal.toDouble()).toInt()%MODULO_PCT))?.rewardName.toString(),
            nextRewardsTierProgress = 100*((Math.floor(orderRequest.purchaseTotal.toDouble()) - ((Math.floor(orderRequest.purchaseTotal.toDouble())-Math.floor(orderRequest.purchaseTotal.toDouble())%MODULO_PCT)))/100).toFloat()
        )

        return customers
    }

    fun getCustomerRewardStatus(customerEmail: String): Customer? {
        return customers[customerEmail]
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
