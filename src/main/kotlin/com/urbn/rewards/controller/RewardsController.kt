package com.urbn.rewards.controller

import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.OrderRequest
import com.urbn.rewards.service.OrderService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

// RestController denotes that this class has web functionality
// The constructor value (orderService) is initialized through dependency injection (Spring magic)
@RestController
class RewardsController(private val orderService: OrderService) {

    // Endpoint 1 has no return value - it creates customer if not in map and tabulates reward status/info
    @PostMapping("/purchase")
    fun purchase(@Valid @RequestBody orderRequest: OrderRequest): Unit {
        orderService.purchase(orderRequest)
    }

    // Endpoint 2 returns customer corresponding to email address
    @GetMapping("/rewards/{emailAddress}")
    fun customerRewardTiers(@PathVariable emailAddress: String): Customer {
        return orderService.getCustomer(emailAddress)
    }

    // Endpoint 3 returns a list of all customers with reward tier info
    @GetMapping("/rewards")
    fun allCustomerRewardsTiers(): ArrayList<Customer> {
        return orderService.getCustomers()
    }
}

