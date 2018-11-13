package com.urbn.rewards.controller

import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.OrderRequest
import com.urbn.rewards.models.Rewards
import com.urbn.rewards.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RewardsController {

    @Autowired
    lateinit var orderService: OrderService

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
}

