package com.urbn.rewards.models

data class Customer(
    val email: String,
    val rewardPoints: Float,
    val rewardsTier: String,
    val nextRewardsTier: String,
    val nextRewardsTierName: String,
    val nextRewardsTierProgress: Float
)