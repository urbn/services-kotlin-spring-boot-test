package com.urbn.rewards.service

import com.google.gson.Gson
import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.CustomerRewards
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

        val rewardPoints = Math.floor(orderRequest.purchaseTotal.toDouble()).toInt()
        val curRewards = gson.fromJson(setRewardPoints(rewardPoints), Array<Rewards>::class.java)

        customers[orderRequest.email] = Customer(
                email = orderRequest.email,
                rewardPoints = rewardPoints,
                rewardsTier = curRewards[0].tier,
                rewardsTierName = curRewards[0].rewardName,
                nextRewardsTier = curRewards[1].tier,
                nextRewardsTierName = curRewards[1].rewardName,
                nextRewardsTierProgress = (Math.round(curRewards[0].points.toFloat() / curRewards[1].points.toFloat() * 100.0) / 100.0).toFloat()
        )

        return customers
    }


    fun getCustomerRewards(CustomerRewards: CustomerRewards): Customer? {

        return getAllCustomersRewards()[CustomerRewards.email]
    }

    fun getAllCustomersRewards(): Map<String, Customer> {

        return customers
    }

    fun setRewardPoints(rewardPoints: Int): String {

        val minPoints = rewards[0].points
        val maxPoints = rewards[rewards.size - 1].points
        val returnData: String

        returnData =
                when {
                    (rewardPoints < minPoints) -> """
            [
                { "tier": "", "rewardName": "", "points": $rewardPoints },
                { "tier": "A", "rewardName": "5% off purchase", "points": 100 },
            ]
            """
                    (rewardPoints >= maxPoints) -> """
            [
                { "tier": "J", "rewardName": "50% off purchase", "points": 1000 },
                { "tier": "J", "rewardName": "50% off purchase", "points": 1000 }
            ]
            """
                    else -> (getRewardPoints(rewardPoints))

                }


        return returnData.trimIndent()
    }

    fun getRewardPoints(rewardPoints: Int): String {
        var returnData = ""

        for (i in 0 until (rewards.size)) {
            if (returnData == "") {
                returnData = """{ "tier": "${rewards[i].tier}", "rewardName": "${rewards[i].rewardName}", "points": ${rewards[i].points} }"""
            } else {
                if (rewards[i].points > rewardPoints) {
                    returnData = """$returnData, { "tier": "${rewards[i].tier}", "rewardName": "${rewards[i].rewardName}", "points": ${rewards[i].points} }"""
                    break
                } else {
                    returnData = """{ "tier": "${rewards[i].tier}", "rewardName": "${rewards[i].rewardName}", "points": ${rewards[i].points} }"""
                }
            }

        }

        return """[ $returnData  ]"""

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
