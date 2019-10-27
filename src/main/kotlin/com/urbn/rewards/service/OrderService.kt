package com.urbn.rewards.service

import com.google.gson.Gson
import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.OrderRequest
import com.urbn.rewards.models.Rewards
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.regex.Pattern.compile
import kotlin.math.min

@Service
class OrderService {

    // Hard coded list of rewards. See the getRewards function below
    final var rewards: Array<Rewards>

    // A list of in memory customers keyed off of the e-mail
    private val customers = HashMap<String, Customer>()

    private val gson = Gson()

    // email REGEX matcher
    private val emailRegex = compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    )

    // Stores reward data into rewards array
    init {
        val rewardString = getRewards()
        rewards = gson.fromJson(rewardString, Array<Rewards>::class.java)
    }

    fun getCustomer(emailAddress: String): Customer {
        if (emailAddress.isEmail() && customers.containsKey(emailAddress)) {
            val customer = customers.get(emailAddress)
            if (customer != null) {
                return customer
            }
            else {
                toConsole("$emailAddress has null customer in map...")
                return Customer(
                        email = emailAddress,
                        rewardPoints = 0,
                        nextRewardsTier = "n/a",
                        rewardsTier = "n/a",
                        nextRewardsTierName = "n/a",
                        nextRewardsTierProgress = 0.0.toFloat()
                )
            }
        } else {
            toConsole("$emailAddress is invalid or does not exist in map...")
            return Customer(
                    email = emailAddress,
                    rewardPoints = 0,
                    nextRewardsTier = "n/a",
                    rewardsTier = "n/a",
                    nextRewardsTierName = "n/a",
                    nextRewardsTierProgress = 0.0.toFloat()
            )
        }

    }

    fun getCustomers(): ArrayList<Customer> {
        return ArrayList(customers.values)
    }

    fun purchase(orderRequest: OrderRequest): Unit {

        // (redundant) check if email address is valid (log to console since no logger is setup in this test app)
        if (!orderRequest.email.isEmail()) {
            toConsole("${orderRequest.email} is invalid email address. No order record processed")
            return
        }

        val dollars = orderRequest.purchaseTotal.toDouble()

        // update reward points if customer exists else add customer with reward points
        if (customers.containsKey(orderRequest.email)) {
            val customer = customers.get(orderRequest.email)
            if(customer != null) {
                customer.rewardPoints += dollars.toInt()
                toConsole("${orderRequest.email} updated with purchase: ${orderRequest.purchaseTotal}")
            }
        } else {
            customers.put(orderRequest.email, Customer(
                    email = orderRequest.email,
                    rewardPoints = dollars.toInt(),
                    nextRewardsTier = "n/a",
                    rewardsTier = "n/a",
                    nextRewardsTierName = "n/a",
                    nextRewardsTierProgress = 0.0.toFloat()
            ))
            toConsole("${orderRequest.email} added to map with purchase: ${orderRequest.purchaseTotal}")
        }

        // process rewards points - update customer reward parameters based on current reward points level
        val customer = customers.get(orderRequest.email)
        if(customer != null) {
            val rewardIdx: Int = min(customer.rewardPoints / 100, rewards.size) - 1
            if(rewardIdx > -1) {
                customer.rewardsTier = rewards[rewardIdx].tier
                if (rewardIdx < rewards.size - 1) {
                    customer.nextRewardsTier = rewards[rewardIdx + 1].tier
                    customer.nextRewardsTierName = rewards[rewardIdx + 1].rewardName
                    customer.nextRewardsTierProgress = (customer.rewardPoints.toFloat() - (rewardIdx + 1) * 100) / 100.0F
                } else {
                    customer.nextRewardsTier = "None"
                    customer.nextRewardsTierName = "None"
                    customer.nextRewardsTierProgress = 0.0F
                }
            } else {
                customer.rewardsTier = "None"
                customer.nextRewardsTier = rewards[0].tier
                customer.nextRewardsTierName = rewards[0].rewardName
                customer.nextRewardsTierProgress = customer.rewardPoints.toFloat() / 100.0F
            }
        } else {
            // if customer is null, delete key/value? from map and return new customer with no rewards
            toConsole("${orderRequest.email} has null customer in map. Record deleted and no order processed...")
            customers.remove(orderRequest.email)
        }

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

    // email REGEX matcher function
    private fun String.isEmail(): Boolean {
        return emailRegex.matcher(this).matches()
    }

    private fun toConsole(strLine: String): Unit {
        println("${LocalDateTime.now()}  INFO -------- " + strLine)
    }

}
