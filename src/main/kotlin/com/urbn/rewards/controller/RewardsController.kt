package com.urbn.rewards.controller

import com.google.gson.Gson
import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.OrderRequest
import com.urbn.rewards.models.Rewards
import com.urbn.rewards.service.OrderService
import org.springframework.web.bind.annotation.*

// RestController denotes that this class has web functionality
// The constructor value (orderService) is initialized through dependency injection (Spring magic)
@RestController
class RewardsController(private val orderService: OrderService) {

    private val customers = HashMap<String, Customer>()

    private val gson = Gson()

    @PostMapping("/purchase")
    fun purchase(@RequestBody orderRequest: OrderRequest): Map<String, Customer> {
        // right now the orderService returns a map of all customers once created.
        // TASK: we should return only the customer that made the request
        return orderService.purchase(orderRequest)
    }

    @GetMapping("/rewards")
    fun getRewardsTiers(): Array<Rewards> {
        return orderService.rewards
    }

    // http://localhost:8080/customer?email=emailid
    @GetMapping("/customer") // value: email, defaultValue: customer@gmail.com
    fun getCustomerRewards(@RequestParam email: String): Customer? {
        return getCustomerRewardStatus(email)
    }

    private fun getCustomerRewardStatus(email: String): Customer? = customers[email]
}

