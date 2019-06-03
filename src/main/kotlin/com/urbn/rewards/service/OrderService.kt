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

    fun purchase(orderRequest: OrderRequest): Customer? {
        // Implement purchase endpoint logic here
        // Right now, we're only storing the customer into a hash map

        // declare the variables and set to base case
        var newRewardPoints = Math.floor(orderRequest.purchaseTotal.toDouble()).toInt()
        var newRewardsTier = ""
        var newNextRewardsTier = "A"
        var newNextRewardsTierName = "5% off purchase"
        var newNextRewardsTierProgress = (newRewardPoints / 100.0).toFloat()

        // check if the customer exists and if so, add existing reward points
        if (customers[orderRequest.email] != null) {
            val existingCustomer = customers[orderRequest.email]
            // this took some googling to figure out null case
            newRewardPoints += existingCustomer?.rewardPoints ?:0
        }

        // with the new total, round down to nearest tier point level
        val truncPointsToTier = newRewardPoints - (newRewardPoints % 100)

        // check if max level is already reached
        if (truncPointsToTier in 100..999) {
            for ( reward in rewards ) {
                if ( reward.points == truncPointsToTier ) {
                    newRewardsTier = reward.tier
                    newNextRewardsTierProgress = (( newRewardPoints - truncPointsToTier ) / 100.0).toFloat()

                } else if ( reward.points == truncPointsToTier + 100 ) {
                    newNextRewardsTier = reward.tier
                    newNextRewardsTierName = reward.rewardName
                }
            }
        } else if (truncPointsToTier >= 1000) {
            newRewardsTier = "J"
            newNextRewardsTierProgress = 0.0F
            newNextRewardsTier = ""
            newNextRewardsTierName = ""
        }

        // set the customer key to the new values
        customers[orderRequest.email] = Customer(
                email = orderRequest.email,
                rewardPoints = newRewardPoints,
                nextRewardsTier = newNextRewardsTier,
                rewardsTier = newRewardsTier,
                nextRewardsTierName = newNextRewardsTierName,
                nextRewardsTierProgress = newNextRewardsTierProgress
        )

        // return the customer
        return getCustomer(orderRequest.email)

//        // get the current customer
//        val currentCustomer = customers[orderRequest.email]
//
//        // just realized I can't'
////        currentCustomer.rewardPoints += Math.floor(orderRequest.purchaseTotal.toDouble()).toInt(),
////        return currentCustomer
//
//        customers[orderRequest.email] = Customer(
//                email = orderRequest.email,
//                rewardPoints = 0,
//                nextRewardsTier = "",
//                rewardsTier = "",
//                nextRewardsTierName = "",
//                nextRewardsTierProgress = 0.0.toFloat()
//        )
//
//        return getCustomer(orderRequest.email)

    }
//
//        totalRewardPoints += (existingCustomer?.rewardPoints ?: 0)
//
//        var rewardPoints = ""
//        var nextRewardsTier = ""
//        var rewardsTier = ""
//        var nextRewardsTierName = ""
//        var nextRewardsTierProgress: Float
//
//        fun rewardPoints(): Int {
//
//        }
//
//        customers[orderRequest.email] = Customer(
//            email = orderRequest.email,
//            rewardPoints = Math.floor(orderRequest.purchaseTotal.toDouble()).toInt(),
//            nextRewardsTier = "??",
//            rewardsTier = "???",
//            nextRewardsTierName = "???",
//            nextRewardsTierProgress = 0.0.toFloat()
//        )
//
//        return customers
//    }

    // adding functions for new endpoints

    fun getCustomers(): List<Customer>? {
        return customers.values.toList()
    }

    fun getCustomer(email: String): Customer? {
        return customers[email]
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
