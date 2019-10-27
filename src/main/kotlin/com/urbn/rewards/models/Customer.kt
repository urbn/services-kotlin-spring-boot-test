package com.urbn.rewards.models

data class Customer(
        // changed all parameters to mutable except email (which can only be set upon object creation)
        val email: String,
        var rewardPoints: Int,
        var rewardsTier: String,
        var nextRewardsTier: String,
        var nextRewardsTierName: String,
        var nextRewardsTierProgress: Float
)