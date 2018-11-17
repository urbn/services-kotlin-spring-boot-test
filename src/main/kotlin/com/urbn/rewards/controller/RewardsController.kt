package com.urbn.rewards.controller

import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.OrderRequest
import com.urbn.rewards.models.Rewards
import com.urbn.rewards.service.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

// RestController denotes that this class has web functionality
// The constructor value (orderService) is initialized through dependency injection (Spring magic)
@RestController
class RewardsController(private val orderService: OrderService) {

    @PostMapping("/purchase")
    fun purchase(@RequestBody orderRequest: OrderRequest): Customer {
        // right now the orderService returns a map of all customers once created.
        // TASK: we should return only the customer that made the request
        return orderService.purchase(orderRequest)
    }

    @GetMapping("/rewards")
    fun getRewardsTiers(): Array<Rewards> {
        return orderService.rewards
    }

    @GetMapping("/customer-reward/email/{email}")
    fun getCustomerRewards(@PathVariable email: String ): Rewards {
        return orderService.getCustomerReward(email)
    }

    @GetMapping("/all-customer-rewards")
    fun getAllCustomerRewards(): HashMap<String, Rewards> {
        return orderService.getAllCustomerRewards()
    }
}

